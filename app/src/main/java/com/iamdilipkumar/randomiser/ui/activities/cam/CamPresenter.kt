package com.iamdilipkumar.randomiser.ui.activities.cam

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import com.iamdilipkumar.randomiser.ui.base.BasePresenter
import com.iamdilipkumar.randomiser.utilities.AppConstants
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class CamPresenter(private val camView: CamView) : BasePresenter<CamView>(camView) {

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
}