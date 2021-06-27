package com.iamdilipkumar.randomiser.ui.activities.cam

import android.content.pm.PackageManager
import android.view.SurfaceView
import android.view.WindowManager
import com.iamdilipkumar.randomiser.R
import com.iamdilipkumar.randomiser.ui.base.BaseActivityMVP
import com.iamdilipkumar.randomiser.utilities.AppConstants
import kotlinx.android.synthetic.main.activity_cam.*
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.Mat

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class CamActivity : BaseActivityMVP<CamPresenter>(), CamView,
    CameraBridgeViewBase.CvCameraViewListener2 {

    var mOpenCvCameraView: CameraBridgeViewBase? = null

    private val mLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                SUCCESS -> {
                    // OpenCV loaded successfully
                    mOpenCvCameraView?.enableView()
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    override fun instantiatePresenter(): CamPresenter {
        return CamPresenter(this)
    }

    override fun afterViews() {
        // Initialise the camera view
        mOpenCvCameraView = custom_cam_view as CameraBridgeViewBase
        mOpenCvCameraView?.visibility = SurfaceView.VISIBLE
        mOpenCvCameraView?.setCvCameraViewListener(this)
    }

    override fun getLayoutId(): Int {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        return R.layout.activity_cam
    }

    override fun onStart() {
        super.onStart()
        presenter.requestCameraPermissions(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.initOpenCV(mLoaderCallback)
    }

    override fun onPause() {
        super.onPause()
        presenter.disableView(mOpenCvCameraView)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.disableView(mOpenCvCameraView)
    }

    private fun getCameraViewList(): MutableList<out CameraBridgeViewBase> {
        return listOf(mOpenCvCameraView) as MutableList<out CameraBridgeViewBase>
    }

    override fun onCameraPermissionGranted() {
        val cameraViews = getCameraViewList() ?: return
        for (cameraBridgeViewBase in cameraViews) {
            cameraBridgeViewBase.setCameraPermissionGranted()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == AppConstants.CAMERA_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onCameraPermissionGranted()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCameraViewStarted(width: Int, height: Int) {

    }

    override fun onCameraViewStopped() {

    }

    /**
     * Handle frame operations here based on the Mat received from Cameraview
     */
    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        return inputFrame!!.rgba()
    }
}