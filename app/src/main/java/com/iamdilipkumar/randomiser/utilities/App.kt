package com.iamdilipkumar.randomiser.utilities

import android.app.Application

/**
 * @project:        Randomiser
 * @version:        1.0
 * @author:         Dilip Kumar <dilipkumar4813@gmail.com>
 * @description:    Application class
 */
class App: Application() {

    companion object {
        var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}