package com.iamdilipkumar.randomiser.ui.activities.results

import com.iamdilipkumar.randomiser.R
import com.iamdilipkumar.randomiser.ui.base.BaseActivityMVP

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class LuckyResultActivity: BaseActivityMVP<LuckyResultPresenter>(), LuckyResultView {
    override fun instantiatePresenter(): LuckyResultPresenter {
        return LuckyResultPresenter(this)
    }

    override fun afterViews() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_lucky_result
    }
}