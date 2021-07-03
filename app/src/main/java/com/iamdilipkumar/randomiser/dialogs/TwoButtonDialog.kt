package com.iamdilipkumar.randomiser.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.iamdilipkumar.randomiser.R
import com.iamdilipkumar.randomiser.helpers.TwoButtonDialogInterface
import kotlinx.android.synthetic.main.dialog_two_button.*

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class TwoButtonDialog(
    context: Context,
    val title: String,
    val description: String,
    val buttonLeftText: String,
    val buttonRightText: String,
    private val twoDialogInterface: TwoButtonDialogInterface
) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_two_button)

        tv_title.text = title
        tv_description.text = description

        btn_left.text = buttonLeftText
        btn_right.text = buttonRightText

        btn_left.setOnClickListener {
            dismiss()
            twoDialogInterface.leftButtonClicked()
        }

        btn_right.setOnClickListener {
            dismiss()
            twoDialogInterface.rightButtonClicked()
        }
    }
}