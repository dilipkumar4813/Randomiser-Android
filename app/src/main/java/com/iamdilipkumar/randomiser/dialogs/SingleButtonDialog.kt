package com.iamdilipkumar.randomiser.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.iamdilipkumar.randomiser.R
import kotlinx.android.synthetic.main.dialog_single_button.*

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class SingleButtonDialog (context: Context, val title: String, val description: String): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_single_button)

        tv_title.text = title
        tv_description.text = description

        btn_ok.setOnClickListener {
            dismiss()
        }
    }
}