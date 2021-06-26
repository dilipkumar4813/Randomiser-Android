package com.iamdilipkumar.randomiser.ui.activities.cam

import com.iamdilipkumar.randomiser.R
import com.iamdilipkumar.randomiser.ui.base.BaseActivityMVP

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class CamActivity: BaseActivityMVP<CamPresenter>(), CamView {
    override fun instantiatePresenter(): CamPresenter {
        return CamPresenter(this)
    }

    override fun afterViews() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_cam
    }
}