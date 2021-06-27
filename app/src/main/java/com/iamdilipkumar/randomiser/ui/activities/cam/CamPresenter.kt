package com.iamdilipkumar.randomiser.ui.activities.cam

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.iamdilipkumar.randomiser.R
import com.iamdilipkumar.randomiser.ui.base.BasePresenter
import com.iamdilipkumar.randomiser.utilities.AppConstants
import com.iamdilipkumar.randomiser.utilities.tracker.DetectionBasedTracker
import org.opencv.android.*
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.roundToInt

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class CamPresenter(private val camView: CamView) : BasePresenter<CamView>(camView) {

    private var mCascadeFile: File? = null
    private var mJavaDetector: CascadeClassifier? = null
    private var mNativeDetector: DetectionBasedTracker? = null

    private var mDetectorType = AppConstants.JAVA_DETECTOR

    private var mAbsoluteFaceSize = 0

    /**
     * Function to request permission for camera and return to activity for further processing
     */
    fun requestCameraPermissions(activity: CamActivity) {
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
            camView.onCameraPermissionGranted()
        }
    }

    /**
     * Initialize the camera view using OpenCV
     */
    fun initOpenCV(mLoaderCallback: BaseLoaderCallback) {
        if (!OpenCVLoader.initDebug()) {
            // Internal OpenCV library not found. Using OpenCV Manager for initialization
            OpenCVLoader.initAsync(
                OpenCVLoader.OPENCV_VERSION_3_0_0,
                camView.getContext(),
                mLoaderCallback
            )
        } else {
            // OpenCV library found inside package. Using it!
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    /**
     * When the activity goes to the background or is terminated to release the resource
     */
    fun disableView(mOpenCvCameraView: CameraBridgeViewBase?) {
        mOpenCvCameraView?.disableView()
    }

    fun initFacialDetection() {
        try {
            val context = camView.getContext()
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

        val facesArray: Array<Rect> = faces.toArray()
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
        }
    }
}