package com.iamdilipkumar.randomiser.di.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.iamdilipkumar.randomiser.utilities.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
@Module
class AppModule (private val application: App) {

    @Provides
    @Singleton
    fun providesApplicationContext(): Context = application

    @Provides
    @Singleton
    fun providesApplication(): App = application

    @Provides
    @Singleton
    fun providesGson(): Gson =
        GsonBuilder()
            .serializeNulls()
            .create()
}