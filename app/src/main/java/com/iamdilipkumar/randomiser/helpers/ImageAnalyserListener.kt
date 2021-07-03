package com.iamdilipkumar.randomiser.helpers

import org.opencv.core.Mat

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
interface ImageAnalyserListener {
    // To show analysis data on log
    fun display(data: String)

    // Convert proxy image to Mat and send for bitmap conversion
    fun setMat(rgba: Mat, gray: Mat)
}