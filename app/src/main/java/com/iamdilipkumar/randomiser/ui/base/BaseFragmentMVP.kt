package com.iamdilipkumar.randomiser.ui.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import com.iamdilipkumar.randomiser.R
import com.iamdilipkumar.randomiser.utilities.AppPreference

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
abstract class BaseFragmentMVP<P : BasePresenter<BaseView>> : BaseView, androidx.fragment.app.Fragment() {

    protected lateinit var presenter: P
    lateinit var viewFragment: View
    lateinit var coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout
    lateinit var mPreference: AppPreference
    var pd: Dialog? = null

    companion object {
        var progressBar: ProgressBar? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = instantiatePresenter()
        mPreference = AppPreference.getInstance(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(getLayoutId()!=-1)
            viewFragment = inflater.inflate(getLayoutId(), container, false)
        return viewFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
        return requireActivity().applicationContext
    }

    protected fun getFragmentView(): View {
        return viewFragment
    }

    protected fun showProgressDialog() {
        pd?.dismiss()

        pd = Dialog(requireActivity())
        pd?.setContentView(R.layout.dialog_custom_progress)

        pd?.setCancelable(false);
        val window = pd?.window
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        pd?.show()
    }

    protected fun hideProgressDialog() {
        pd?.dismiss()
    }

}