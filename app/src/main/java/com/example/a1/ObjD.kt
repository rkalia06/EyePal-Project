package com.example.a1;

import android.content.Context
import android.graphics.*
import android.util.Log
//import android.view.View
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import kotlin.math.abs

class ObjD(private var ctx: Context) {

    var BoxLeft: Float = 0.0F
    var BoxRight: Float = 0.0F
    var BoxTop: Float = 0.0F
    var BoxBottom: Float = 0.0F
    private val MAX_FONT_SIZE = 96F
    var cupArea = 0F
    var discArea = 0F

    fun runObjDet(bitmap: Bitmap): Bitmap {
        // Step 1: Create TFLite's TensorImage object
        val image = TensorImage.fromBitmap(bitmap)

        // Step 2: Initialize the detector object
        val options = ObjectDetector.ObjectDetectorOptions.builder()
            .setMaxResults(2)
            .build()
        val detector = ObjectDetector.createFromFileAndOptions(ctx, "betterResObjDet.tflite", options)

        // Step 3: Feed given image to the detector
        val results = detector.detect(image)
        debPrint(results)
        // Step 4: Parse the detection result and show it
        val resultToDisplay = results.map {
            // Get the top-1 category and craft the display text
            val category = it.categories.first()
            val text = "${category.label}, ${category.score.times(100).toInt()}%"

            // Create a data object to display the detection result
            DetectionResult(it.boundingBox, text)
        }
        // Draw the detection result on the bitmap and show it.
        val imgWithResult = ddResult(bitmap, resultToDisplay)
//        runOnUiThread {
//            inputImageView.setImageBitmap(imgWithResult)
//        }
        return imgWithResult

//        return bitmap;
    }

    fun debPrint(results : List<Detection>){
        var ticker = 1
        for ((i, obj) in results.withIndex()) {
            val box = obj.boundingBox

            Log.d("OBJD", "Detected object: ${i} ")
            Log.d("OBJD", "  boundingBox: (${box.left}, ${box.top}) - (${box.right},${box.bottom})")
            BoxLeft = box.left
            BoxRight = box.right
            BoxTop = box.top
            BoxBottom = box.bottom

            //calculate statistics
            var width = abs(BoxLeft - BoxRight)
            var height = abs(BoxTop - BoxBottom)
            var area = width * height

            if (ticker == 1){
                discArea = area
                Log.d("SET DISC AREA", area.toString())
            }
            else {
                cupArea = area
                Log.d("SET CUP AREA", area.toString())
            }
            ticker += 1

            for ((j, category) in obj.categories.withIndex()) {
                Log.d("OBJD", "    Label $j: ${category.label}")
                val confidence: Int = category.score.times(100).toInt()
                Log.d("OBJD", "    Confidence: ${confidence}%")
            }
        }
    }

    /**
     * drawDetectionResult(bitmap: Bitmap, detectionResults: List<DetectionResult>
     *      Draw a box around each objects and show the object's name.
     */
    fun ddResult(
        bitmap: Bitmap,
        detectionResults: List<DetectionResult>
    ): Bitmap {
        val outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(outputBitmap)
        val pen = Paint()
        pen.textAlign = Paint.Align.LEFT

        detectionResults.forEach {
            // draw bounding box
            pen.color = Color.BLUE
            pen.strokeWidth = 2F
            pen.style = Paint.Style.STROKE
            val box = it.boundingBox
            canvas.drawRect(box, pen)

            val tagSize = Rect(0, 0, 0, 0)

            // calculate the right font size
            pen.style = Paint.Style.FILL
            pen.color = Color.BLUE
            pen.strokeWidth = 2F

            pen.textSize = 100F
            pen.getTextBounds(it.text, 0, it.text.length, tagSize)
            val fontSize: Float = pen.textSize * box.width() / tagSize.width()

            // adjust the font size so texts are inside the bounding box
            if (fontSize < pen.textSize) pen.textSize = fontSize

            var margin = (box.width() - tagSize.width()) / 2.0F
            if (margin < 0F) margin = 0F
//            canvas.drawText(
//                it.text, box.left + 100000,
//                box.top + tagSize.height().times(1F), pen
//            )

        }
        return outputBitmap
    }
}

data class DetectionResult(val boundingBox: RectF, val text: String)
