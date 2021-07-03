package com.iamdilipkumar.randomiser.ui.activities.info

import android.content.Intent
import android.content.pm.PackageManager
import com.iamdilipkumar.randomiser.R
import com.iamdilipkumar.randomiser.dialogs.SingleButtonDialog
import com.iamdilipkumar.randomiser.dialogs.TwoButtonDialog
import com.iamdilipkumar.randomiser.helpers.TwoButtonDialogInterface
import com.iamdilipkumar.randomiser.ui.activities.cam.CamActivity
import com.iamdilipkumar.randomiser.ui.activities.customcam.CustomCamActivity
import com.iamdilipkumar.randomiser.ui.base.BaseActivityMVP
import com.iamdilipkumar.randomiser.utilities.AppConstants
import kotlinx.android.synthetic.main.activity_info.*

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class InfoActivity : BaseActivityMVP<InfoPresenter>(), InfoView {
    override fun instantiatePresenter(): InfoPresenter {
        return InfoPresenter(this)
    }

    override fun afterViews() {
        btn_start_detection.setOnClickListener {
            presenter.requestCameraPermissions(this@InfoActivity)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_info
    }

    override fun onCameraPermissionGranted() {
        val intent = if (AppConstants.SHOW_CAMERAX) {
            Intent(this, CustomCamActivity::class.java)
        } else {
            Intent(this, CamActivity::class.java)
        }

        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == AppConstants.CAMERA_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onCameraPermissionGranted()
        } else {
            // Handle when permission is denied
            TwoButtonDialog(
                this@InfoActivity,
                getString(R.string.camera_permission_title),
                getString(R.string.camera_permission_second_request),
                getString(android.R.string.cancel),
                getString(android.R.string.ok),
                object : TwoButtonDialogInterface {
                    override fun leftButtonClicked() {
                        SingleButtonDialog(
                            this@InfoActivity,
                            getString(R.string.camera_permission_title),
                            getString(R.string.camera_permission_fail)
                        ).show()
                    }

                    override fun rightButtonClicked() {
                        presenter.requestCameraPermissions(this@InfoActivity)
                    }
                }).show()

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}