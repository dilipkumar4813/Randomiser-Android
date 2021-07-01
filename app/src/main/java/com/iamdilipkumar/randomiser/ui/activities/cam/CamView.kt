package com.iamdilipkumar.randomiser.ui.activities.cam

import com.iamdilipkumar.randomiser.ui.base.BaseView
import org.opencv.core.Mat

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
interface CamView: BaseView {
    fun onCameraPermissionGranted()
    fun setBitmapResultOnThreshold(rgba: Mat)
}