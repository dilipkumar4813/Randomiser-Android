package com.iamdilipkumar.randomiser.ui.base

import com.iamdilipkumar.randomiser.di.component.DaggerPresenterInjector
import com.iamdilipkumar.randomiser.di.component.PresenterInjector
import com.iamdilipkumar.randomiser.di.module.ContextModule
import com.iamdilipkumar.randomiser.ui.activities.SplashPresenter

/**
 * @project:        Randomiser
 * @version:        1.0
 * @author:         Dilip Kumar <dilipkumar4813@gmail.com>
 * @description:    Base presenter any presenter of the application must extend. It provides initial injections and required methods.
 */
abstract class BasePresenter<out V : BaseView>(protected val view: V) {
    private val injector: PresenterInjector = DaggerPresenterInjector
        .builder()
        .baseView(view)
        .contextModule(ContextModule)
        .build()

    init {
        inject()
    }

    /**
     * This method may be called when the presenter view is created
     */
    open fun onViewCreated() {}

    /**
     * This method may be called when the presenter view is destroyed
     */
    open fun onViewDestroyed() {}

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            // Activities
            is SplashPresenter -> injector.injectSplash(this)
        }
    }
}