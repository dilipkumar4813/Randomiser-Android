package com.iamdilipkumar.randomiser.ui.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.iamdilipkumar.randomiser.R
import com.iamdilipkumar.randomiser.utilities.AppPreference

/**
 * @project:        Randomiser
 * @version:        1.0
 * @author:         Dilip Kumar <dilipkumar4813@gmail.com>
 * @description:    All activity classes must extend. It provides required methods and presenter instantiation and calls.
 */
abstract class BaseActivityMVP<P : BasePresenter<BaseView>> : BaseView, AppCompatActivity() {

    companion object {
        init {
            System.loadLibrary("detection_based_tracker")
        }
    }

    lateinit var presenter: P
    lateinit var progressBar: ProgressBar
    lateinit var constraintLayout: ConstraintLayout
    lateinit var mPreference: AppPreference
    var pd: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = instantiatePresenter()
        if (getLayoutId() != -1) {
            setContentView(getLayoutId())
        }
        mPreference = AppPreference.getInstance(this)
        afterViews()
    }

    /**
     * Instantiates the presenter of the activity
     */
    protected abstract fun instantiatePresenter(): P

    /**
     * After setting up view, do something
     */
    protected abstract fun afterViews()

    /**
     * Initialzie Child Layout
     */
    protected abstract fun getLayoutId(): Int

    override fun getContext(): Context {
        return this
    }

    protected fun showProgressDialog() {
        pd?.dismiss()
        pd = Dialog(this)
        pd?.setContentView(R.layout.dialog_custom_progress)
        pd?.setCancelable(false)
        val window = pd?.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            window.setGravity(Gravity.CENTER)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        pd?.show()
    }

    protected fun hideProgressDialog() {
        pd?.dismiss()
    }
}