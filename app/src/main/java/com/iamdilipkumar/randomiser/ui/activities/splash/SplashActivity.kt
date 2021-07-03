package com.iamdilipkumar.randomiser.ui.activities.splash

import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.Looper
import android.os.Process
import com.iamdilipkumar.randomiser.R
import com.iamdilipkumar.randomiser.dialogs.SingleButtonDialog
import com.iamdilipkumar.randomiser.helpers.SingleButtonDialogInterface
import com.iamdilipkumar.randomiser.ui.activities.info.InfoActivity
import com.iamdilipkumar.randomiser.ui.base.BaseActivityMVP

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class SplashActivity : BaseActivityMVP<SplashPresenter>(), SplashView {

    override fun instantiatePresenter(): SplashPresenter {
        return SplashPresenter(this)
    }

    override fun afterViews() {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        if (cameraManager.cameraIdList.isEmpty()) {
            SingleButtonDialog(
                this,
                getString(R.string.no_camera_title),
                getString(R.string.no_camera_description),
                object : SingleButtonDialogInterface {
                    override fun onButtonClicked() {
                        val pid = Process.myPid()
                        Process.killProcess(pid)
                    }
                }
            ).show()

            return
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, InfoActivity::class.java))
            finishAffinity()
        }, 2000)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }
}