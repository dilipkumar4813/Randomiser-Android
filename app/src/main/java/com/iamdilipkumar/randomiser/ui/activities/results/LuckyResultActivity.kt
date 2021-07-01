package com.iamdilipkumar.randomiser.ui.activities.results

import android.graphics.Bitmap
import android.view.View
import com.iamdilipkumar.randomiser.R
import com.iamdilipkumar.randomiser.ui.base.BaseActivityMVP
import kotlinx.android.synthetic.main.activity_lucky_result.*

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class LuckyResultActivity : BaseActivityMVP<LuckyResultPresenter>(), LuckyResultView,
    View.OnClickListener {

    companion object {
        var detectedBitmap: Bitmap? = null
    }

    override fun instantiatePresenter(): LuckyResultPresenter {
        return LuckyResultPresenter(this)
    }

    override fun afterViews() {
        if (detectedBitmap != null) {
            iv_results.setImageBitmap(detectedBitmap!!)
            tv_results_error.visibility = View.GONE
        } else {
            tv_results_error.visibility = View.VISIBLE
        }

        btn_try_again.setOnClickListener(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_lucky_result
    }

    override fun onClick(v: View?) {
        when(v) {
            btn_try_again -> finish()
        }
    }

    override fun onDestroy() {
        detectedBitmap = null
        super.onDestroy()
    }
}