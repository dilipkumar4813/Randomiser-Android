package com.iamdilipkumar.randomiser.di.component

import com.iamdilipkumar.randomiser.di.module.ContextModule
import com.iamdilipkumar.randomiser.ui.activities.SplashPresenter
import com.iamdilipkumar.randomiser.ui.base.BaseView
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
@Singleton
@Component(modules = [(ContextModule::class)])
interface PresenterInjector {

    /**
     * Injects required dependencies into the specified Presenter.
     */

    @Component.Builder
    interface Builder {
        fun build(): PresenterInjector

        fun contextModule(contextModule: ContextModule): Builder

        @BindsInstance
        fun baseView(baseView: BaseView): Builder
    }

    /*activities*/
    fun injectSplash(splashPresenter: SplashPresenter)
}