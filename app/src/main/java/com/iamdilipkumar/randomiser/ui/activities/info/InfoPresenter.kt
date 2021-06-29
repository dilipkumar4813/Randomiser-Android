package com.iamdilipkumar.randomiser.ui.activities.info

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import com.iamdilipkumar.randomiser.ui.base.BasePresenter
import com.iamdilipkumar.randomiser.utilities.AppConstants

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class InfoPresenter(private val infoView: InfoView) : BasePresenter<InfoView>(infoView) {

    /**
     * Function to request permission for camera and return to activity for further processing
     */
    fun requestCameraPermissions(activity: InfoActivity) {
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
            infoView.onCameraPermissionGranted()
        }
    }
}