package com.iamdilipkumar.randomiser.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
abstract class BaseActivity : AppCompatActivity() {

    protected val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(provideContentViewId())
        initPresenter()
        prepare()
    }

    fun showMessage(id: Int) {
        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }

    abstract fun provideContentViewId(): Int

    abstract fun initPresenter()

    abstract fun prepare()
}