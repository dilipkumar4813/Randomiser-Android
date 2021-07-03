package com.iamdilipkumar.randomiser.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.iamdilipkumar.randomiser.R
import com.iamdilipkumar.randomiser.helpers.SingleButtonDialogInterface
import com.iamdilipkumar.randomiser.helpers.TwoButtonDialogInterface
import kotlinx.android.synthetic.main.dialog_single_button.*

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class SingleButtonDialog(context: Context) : Dialog(context) {

    var mTitle: String? = null
    var mDescription: String? = null
    var mInterface: SingleButtonDialogInterface? = null

    constructor(context: Context, title: String, description: String) : this(context) {
        mTitle = title
        mDescription = description
    }

    constructor(
        context: Context,
        title: String,
        description: String,
        singleButtonDialogInterface: SingleButtonDialogInterface
    ) : this(context) {
        mTitle = title
        mDescription = description
        mInterface = singleButtonDialogInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_single_button)

        tv_title.text = mTitle
        tv_description.text = mDescription

        btn_ok.setOnClickListener {
            dismiss()
            mInterface?.onButtonClicked()
        }
    }
}