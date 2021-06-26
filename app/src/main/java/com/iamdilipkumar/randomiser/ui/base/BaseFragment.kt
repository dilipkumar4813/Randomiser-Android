package com.iamdilipkumar.randomiser.ui.base

import android.R
import android.app.ProgressDialog
import android.os.Bundle

/**
 * @project:        Randomiser
 * @version:        1.0
 * @author:         Dilip Kumar <dilipkumar4813@gmail.com>
 * @description:    All fragment classes must extend. It provides required methods and presenter instantiation and calls.
 */
abstract class BaseFragment: androidx.fragment.app.Fragment() {

    protected lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun showProgressDialog() {
        progressDialog = ProgressDialog.show(activity, null, "Please wait..")
        progressDialog.setCancelable(false)
        progressDialog.setIndeterminateDrawable(resources.getDrawable(R.drawable.progress_horizontal))
        if (!requireActivity().isFinishing) {
            progressDialog.show()
        }
    }

    protected fun hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            if (!requireActivity().isFinishing) {
                progressDialog.dismiss()
            }
        }
    }
}