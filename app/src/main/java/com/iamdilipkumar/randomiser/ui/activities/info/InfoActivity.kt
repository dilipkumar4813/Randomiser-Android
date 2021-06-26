package com.iamdilipkumar.randomiser.ui.activities.info

import com.iamdilipkumar.randomiser.R
import com.iamdilipkumar.randomiser.ui.base.BaseActivityMVP

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

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_info
    }
}