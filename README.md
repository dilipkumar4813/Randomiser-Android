# Randomiser-Android

[![OpenCV: 4.5.2](https://img.shields.io/badge/OpenCV-27338e)](https://docs.opencv.org/4.5.2/) [![Kotlin: 1.5.20](https://img.shields.io/badge/kotlin-%230095D5.svg)](https://kotlinlang.org/) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) 

Ever had a scenario when your friends/family had to pick a person who had to do a specific task and flipping a coin just did not solve your problem. This project aims on resolving that using the OpenCV library.

## End Result
![Chosen One](/screenshots/chosen.png "Chosen One")

## Requirements
* Android Studio
* Android NDK
* Android CMake
* OpenCV

## Instructions
1. Clone the repository
2. Download OpenCV 4.5.2 ([Click here](https://opencv.org/releases/))
3. Unzip the downloaded library and copy the sdk.
4. Paste the copied sdk folder in the root folder where you have clone the project (Example: If the project is within ```AndroidStudioProjects/Randomiser-Android``` then paste the sdk in ```AndroidStudioProjects```)
5. Open the cloned project using Android Studio and let the IDE configure the project.
6. Open ```local.properties``` and add the ```ndk.dir=<YOUR-NDK-PATH>``` below ```sdk.dir```
7. Open ```build.gradle``` and update the ```ndkVersion``` number as per your downloaded version.
8. Build and run.

## Threshold
* At the moment the result is generated only when 2 or more faces are detected if you would like to change threshold, then open ```AppConstants``` class and update the ```THRESHOLD``` variable.

## CameraX or JavaCamera2View
* At the moment the detection activity uses JavaCamera2View if you would like to change, then open ```AppConstants``` class and update the ```SHOW_CAMERAX``` variable to ```true``` to show CameraX or ```false``` for JavaCamera2View.

## Sharpen Image
* To make the image sharp we use the Gaussian Filter, if you would like to see the difference between the original image and the processed image, then open ```AppConstants``` class and update the ```SHOW_OPENCV_IMAGE_DIFFERENCE``` variable to ```true```. 

![Processing Image](/screenshots/image-difference.png "Difference")

## References
* [OpenCV 4.5.2](https://docs.opencv.org/4.5.2/)
* [PNG Tree for Assets](https://pngtree.com/so/photo)
* [Google Fonts](https://fonts.google.com/specimen/Cairo#standard-styles)
* [Logo from iamdilipkumar](https://iamdilipkumar.com/)
* [Latest Cascade File](https://github.com/opencv/opencv/blob/master/data/haarcascades/haarcascade_frontalface_default.xml)

## Notes
* Color combination is due to the inspiration from Nintendo Switch, it does not go with the color wheel but wanted to try it out.