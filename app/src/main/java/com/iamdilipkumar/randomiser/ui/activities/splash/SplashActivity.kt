package com.iamdilipkumar.randomiser.ui.activities.splash

import android.content.Intent
import android.os.Handler
import com.iamdilipkumar.randomiser.R
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
        Handler().postDelayed({
            startActivity(Intent(this, InfoActivity::class.java))
            finishAffinity()
        }, 2000)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }
}