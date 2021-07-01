package com.iamdilipkumar.randomiser.ui.activities.cam

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.view.SurfaceView
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.iamdilipkumar.randomiser.R
import com.iamdilipkumar.randomiser.ui.activities.results.LuckyResultActivity
import com.iamdilipkumar.randomiser.ui.base.BaseActivityMVP
import com.iamdilipkumar.randomiser.utilities.AppConstants
import kotlinx.android.synthetic.main.activity_cam.*
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.Utils
import org.opencv.core.Mat

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class CamActivity : BaseActivityMVP<CamPresenter>(), CamView,
    CameraBridgeViewBase.CvCameraViewListener2, View.OnClickListener {

    private var mRgba: Mat? = null
    private var mGray: Mat? = null
    private var mDetectedBitmap: Bitmap? = null
    private var mOpenCvCameraView: CameraBridgeViewBase? = null

    private val mLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                SUCCESS -> {
                    presenter.initFacialDetection()

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
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // Initialise the camera view
        mOpenCvCameraView = custom_cam_view as CameraBridgeViewBase
        mOpenCvCameraView?.visibility = SurfaceView.VISIBLE
        mOpenCvCameraView?.setCvCameraViewListener(this)
        iv_place.setOnClickListener(this)
    }

    override fun getLayoutId(): Int {
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

    /**
     * Function to handle once permission has been granted
     */
    override fun onCameraPermissionGranted() {
        val cameraViews = getCameraViewList()
        for (cameraBridgeViewBase in cameraViews) {
            cameraBridgeViewBase.setCameraPermissionGranted()
        }
    }

    override fun setBitmapResultOnThreshold(rgba: Mat) {
        mDetectedBitmap = Bitmap.createBitmap(rgba.cols(), rgba.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(rgba, mDetectedBitmap)
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
        mGray = Mat()
        mRgba = Mat()
    }

    override fun onCameraViewStopped() {
        mGray?.release()
        mRgba?.release()
    }

    /**
     * Handle frame operations here based on the Mat received from Cameraview
     */
    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        mRgba = inputFrame!!.rgba()
        mGray = inputFrame.gray()

        presenter.detectFaces(mRgba!!, mGray!!)

        this@CamActivity.runOnUiThread {
            if (mDetectedBitmap != null) {
                cl_result_preview.visibility = View.VISIBLE
                iv_place.setImageBitmap(mDetectedBitmap)
            }
        }

        // Checking orientation issue
        /*val mRgba = inputFrame!!.rgba()
        val mRgbaT: Mat = mRgba.t()
        Core.flip(mRgba.t(), mRgbaT, 1)
        Imgproc.resize(mRgbaT, mRgbaT, mRgba.size())*/
        return mRgba!!
    }

    override fun onClick(v: View?) {
        when (v) {
            iv_place -> {
                if (mDetectedBitmap != null) {
                    // Using static to avoid additional permission request to pass bytes Array in intent
                    LuckyResultActivity.detectedBitmap = mDetectedBitmap
                    val intent = Intent(this, LuckyResultActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}