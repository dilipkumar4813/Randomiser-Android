package com.iamdilipkumar.randomiser.ui.activities.customcam

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.view.ScaleGestureDetector
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.iamdilipkumar.randomiser.R
import com.iamdilipkumar.randomiser.helpers.ImageAnalyserListener
import com.iamdilipkumar.randomiser.ui.activities.results.LuckyResultActivity
import com.iamdilipkumar.randomiser.ui.base.BaseActivityMVP
import com.iamdilipkumar.randomiser.utilities.LuminosityAnalyser
import kotlinx.android.synthetic.main.activity_cam.*
import kotlinx.android.synthetic.main.activity_custom_cam.*
import kotlinx.android.synthetic.main.activity_custom_cam.cl_result_preview
import kotlinx.android.synthetic.main.activity_custom_cam.iv_place
import org.opencv.android.Utils
import org.opencv.core.Mat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class CustomCamActivity : BaseActivityMVP<CustomCamPresenter>(), CustomCamView,
    View.OnClickListener {

    private var mDetectedBitmap: Bitmap? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun instantiatePresenter(): CustomCamPresenter {
        return CustomCamPresenter(this)
    }

    override fun afterViews() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
        iv_place.setOnClickListener(this)

        // Use this to capture original image, add additional permission checking for storage
        btn_capture.setOnClickListener(this)
        btn_capture.visibility = View.GONE
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_custom_cam
    }

    override fun onCameraPermissionGranted() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(sv_custom_cam.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(
                        cameraExecutor,
                        LuminosityAnalyser(object : ImageAnalyserListener {
                            override fun display(data: String) {
                                Log.d("Custom Cam", "Average luminosity: $data")
                            }

                            override fun setMat(rgba: Mat, gray: Mat) {
                                presenter.detectFaces(rgba, gray)
                            }

                        })
                    )
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            presenter.initFacialDetection()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                val camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
                )

                enablePinchToZoom(camera)
            } catch (exc: Exception) {
                Log.e("Custom Cam", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    override fun setBitmapResultOnThreshold(rgba: Mat) {
        mDetectedBitmap = Bitmap.createBitmap(rgba.cols(), rgba.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(rgba, mDetectedBitmap)

        this@CustomCamActivity.runOnUiThread {
            if (mDetectedBitmap != null) {
                cl_result_preview.visibility = View.VISIBLE
                iv_place.setImageBitmap(mDetectedBitmap)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.requestCameraPermissions(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    /**
     * Function to enable pinch to zoom
     */
    private fun enablePinchToZoom(camera: Camera) {
        val scaleGestureDetector = ScaleGestureDetector(this,
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    val scale = camera.cameraInfo.zoomState.value!!.zoomRatio * detector.scaleFactor
                    camera.cameraControl.setZoomRatio(scale)
                    return true
                }
            })

        sv_custom_cam.setOnTouchListener { view, event ->
            view.performClick()
            scaleGestureDetector.onTouchEvent(event)
            return@setOnTouchListener true
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            btn_capture -> {
                if (imageCapture != null) {
                    presenter.captureImage(imageCapture!!)
                }
            }
            iv_place -> {
                if (mDetectedBitmap != null) {
                    LuckyResultActivity.originalBitmap = mDetectedBitmap!!
                    LuckyResultActivity.detectedBitmap = presenter.sharpenImage(mDetectedBitmap!!)

                    val intent = Intent(this, LuckyResultActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}