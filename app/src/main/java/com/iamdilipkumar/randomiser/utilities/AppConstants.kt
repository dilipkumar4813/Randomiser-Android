package com.iamdilipkumar.randomiser.utilities

import org.opencv.core.Scalar

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class AppConstants {
    companion object {
        const val CAM_TAG = "Cam"

        const val CAMERA_PERMISSION_REQUEST_CODE = 200

        const val RELATIVE_FACE_SIZE = 0.2f

        const val JAVA_DETECTOR = 0
        const val NATIVE_DETECTOR = 1

        // Color for Face Box and Text Color
        val FACE_RECT_COLOR = Scalar(0.0, 255.0, 0.0, 255.0)
        val TEXT_RECT_COLOR = Scalar( 255.0, 0.0, 0.0)

        // Threshold for the application
        val THRESHOLD = 2

        // Whether to show original and processed image
        val SHOW_OPENCV_IMAGE_DIFFERENCE = false
    }
}