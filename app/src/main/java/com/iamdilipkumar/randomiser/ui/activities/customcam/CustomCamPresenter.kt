package com.iamdilipkumar.randomiser.ui.activities.customcam

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import com.iamdilipkumar.randomiser.R
import com.iamdilipkumar.randomiser.ui.base.BasePresenter
import com.iamdilipkumar.randomiser.utilities.AppConstants
import com.iamdilipkumar.randomiser.utilities.tracker.DetectionBasedTracker
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class CustomCamPresenter(private val customCamView: CustomCamView) : BasePresenter<CustomCamView>(customCamView) {

    private lateinit var outputDirectory: File
    private var mCascadeFile: File? = null
    private var mJavaDetector: CascadeClassifier? = null
    private var mNativeDetector: DetectionBasedTracker? = null
    private var mDetectorType = AppConstants.JAVA_DETECTOR
    private var mAbsoluteFaceSize = 0

    /**
     * Function to request permission for camera and return to activity for further processing
     */
    fun requestCameraPermissions(activity: CustomCamActivity) {
        var havePermission = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    AppConstants.CAMERA_PERMISSION_REQUEST_CODE
                )
                havePermission = false
            }
        }
        if (havePermission) {
            customCamView.onCameraPermissionGranted()
        }
    }

    fun getOutputDirectory(): File {
        val context = customCamView.getContext()
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else context.filesDir
    }

    fun captureImage(imageCapture: ImageCapture) {
        val context = customCamView.getContext()

        outputDirectory = getOutputDirectory()

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            "take.jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("Custom Cam", "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    Log.d("Custom Cam", msg)
                }
            })
    }

    /**
     * Initialise Facial Detection by getting the lbpcascade_frontface xml
     * The lbpcascade_frontface xml is the dots that is used to determine facial features
     * such as eyes, cheeks etc
     */
    fun initFacialDetection() {
        try {
            val context = customCamView.getContext()
            // load cascade file from application resources
            val inputStream = context.resources.openRawResource(R.raw.lbpcascade_frontalface)
            val cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE)
            mCascadeFile = File(cascadeDir, "lbpcascade_frontalface.xml")
            val os = FileOutputStream(mCascadeFile)
            val buffer = ByteArray(4096)
            var bytesRead = 0

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                os.write(buffer, 0, bytesRead)
            }
            inputStream.close()

            os.close()
            mJavaDetector = CascadeClassifier(mCascadeFile!!.absolutePath)
            if (mJavaDetector!!.empty()) {
                Log.e(AppConstants.CAM_TAG, "Failed to load cascade classifier")
                mJavaDetector = null
            } else Log.i(
                AppConstants.CAM_TAG,
                "Loaded cascade classifier from " + mCascadeFile!!.absolutePath
            )
            mNativeDetector =
                DetectionBasedTracker(
                    mCascadeFile!!.absolutePath,
                    0
                )
            cascadeDir.delete()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(AppConstants.CAM_TAG, "Failed to load cascade. Exception thrown: $e")
        }
    }

    /**
     * Function to detect the faces during the frame update of the camera
     * to get live preview of the detections
     */
    fun detectFaces(rgba: Mat, gray: Mat) {
        if (mAbsoluteFaceSize == 0) {
            val height = gray.rows()
            if ((height * AppConstants.RELATIVE_FACE_SIZE).roundToInt() > 0) {
                mAbsoluteFaceSize = (height * AppConstants.RELATIVE_FACE_SIZE).roundToInt()
            }
            mNativeDetector!!.setMinFaceSize(mAbsoluteFaceSize)
        }

        val faces = MatOfRect()

        if (mDetectorType == AppConstants.JAVA_DETECTOR) {
            if (mJavaDetector != null) mJavaDetector!!.detectMultiScale(
                gray, faces, 1.1, 2, 2,  // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                Size(mAbsoluteFaceSize.toDouble(), mAbsoluteFaceSize.toDouble()), Size()
            )
        } else if (mDetectorType == AppConstants.NATIVE_DETECTOR) {
            if (mNativeDetector != null) mNativeDetector!!.detect(gray, faces)
        } else {
            Log.e(AppConstants.CAM_TAG, "Detection method is not selected!")
        }

        drawTheOddOneOut(faces.toArray(), rgba)
    }

    /**
     * Function to manage the threshold logic to draw a text when detected the odd one out
     * Store the Mat in activity global variable for capture
     *
     * Note: We are storing in global variable since OpenCV capture works on milleseconds
     */
    private fun drawTheOddOneOut(facesArray: Array<Rect>, rgba: Mat) {
        var hitPersonal = 0
        if (facesArray.size > 1) {
            hitPersonal = (facesArray.indices).random()
        }

        for (i in facesArray.indices) {
            val rt = facesArray[i]
            Imgproc.rectangle(
                rgba,
                rt.tl(),
                rt.br(),
                AppConstants.FACE_RECT_COLOR,
                2
            )

            // Imgproc.putText(mRgba, "Hello")
            Log.d("Image", "Image count $i")

            if (facesArray.size >= AppConstants.THRESHOLD && hitPersonal == i) {
                // Draw the text above the box
                Imgproc.putText(
                    rgba,
                    "Chosen one!", Point(
                        max(rt.tl().x - 10, 0.0), max(rt.tl().y - 10, 0.0)
                    ), Imgproc.FONT_HERSHEY_TRIPLEX,
                    1.0, AppConstants.TEXT_RECT_COLOR
                )
            }
        }

        if (facesArray.size >= AppConstants.THRESHOLD) {
            customCamView.setBitmapResultOnThreshold(rgba)
        }
    }

    /**
     * Get mat from bitmap and process the image
     * Blur it using the Gaussian filter and subtract the smoothed version from the original image.
     */
    fun sharpenImage(detectedBitmap: Bitmap): Bitmap {
        val rgba = Mat()
        val bmp32 = detectedBitmap.copy(Bitmap.Config.ARGB_8888, true)
        Utils.bitmapToMat(bmp32, rgba)

        val dest = Mat(rgba.rows(), rgba.cols(), rgba.type())
        Imgproc.GaussianBlur(rgba, dest, Size(0.0, 0.0), 10.0)
        Core.addWeighted(rgba, 1.5, dest, -0.5, 0.0, dest)

        val processedBitmap = Bitmap.createBitmap(
            dest.cols(),
            dest.rows(),
            Bitmap.Config.ARGB_8888
        )
        Utils.matToBitmap(dest, processedBitmap)

        return processedBitmap
    }
}