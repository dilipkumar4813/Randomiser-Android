package com.iamdilipkumar.randomiser.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iamdilipkumar.randomiser.R
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

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }
}