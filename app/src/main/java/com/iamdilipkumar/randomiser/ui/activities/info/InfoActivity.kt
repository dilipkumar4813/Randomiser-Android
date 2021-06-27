package com.iamdilipkumar.randomiser.ui.activities.info

import android.content.Intent
import com.iamdilipkumar.randomiser.R
import com.iamdilipkumar.randomiser.ui.activities.cam.CamActivity
import com.iamdilipkumar.randomiser.ui.base.BaseActivityMVP
import kotlinx.android.synthetic.main.activity_info.*

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class InfoActivity: BaseActivityMVP<InfoPresenter>(), InfoView {
    override fun instantiatePresenter(): InfoPresenter {
        return InfoPresenter(this)
    }

    override fun afterViews() {
        btn_start_detection.setOnClickListener {
            startActivity(Intent(this, CamActivity::class.java))
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_info
    }
}