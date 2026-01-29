package com.example.facemuse

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.IOException

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val ivResult = findViewById<ImageView>(R.id.ivResult)
        val analysisItemsLayout = findViewById<LinearLayout>(R.id.llAnalysisItems)
        val routineButton = findViewById<Button>(R.id.btnRoutine)
        val askButton = findViewById<Button>(R.id.btnAskQuestions)
        val historyLink = findViewById<TextView>(R.id.tvViewHistory)

        val imageUri = intent.data

        if (imageUri != null) {
            ivResult.setImageURI(imageUri)
            analyzeImage(imageUri, analysisItemsLayout)
        } else {
            // Handle no image case
        }

        routineButton.setOnClickListener {
            startActivity(Intent(this, RoutineActivity::class.java))
        }

        askButton.setOnClickListener {
            startActivity(Intent(this, ChatBotActivity::class.java))
        }

        historyLink.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    private fun analyzeImage(uri: Uri, layout: LinearLayout) {
        try {
            val inputImage = InputImage.fromFilePath(this, uri)
            val options = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build()

            val detector = FaceDetection.getClient(options)

            detector.process(inputImage)
                .addOnSuccessListener { faces ->
                    if (faces.isNotEmpty()) {
                        val face = faces[0]
                        // This is a placeholder for your real analysis data
                        val analysisData = mapOf(
                            "Hydration" to Pair("Good", 85),
                            "Texture" to Pair("Needs Attention", 72),
                            "Skin Tone" to Pair("Good", 88)
                        )

                        analysisData.forEach { (name, data) ->
                            val itemView = LayoutInflater.from(this).inflate(R.layout.item_analysis_row, layout, false)
                            itemView.findViewById<TextView>(R.id.tvAnalysisName).text = name
                            itemView.findViewById<TextView>(R.id.tvAnalysisStatus).text = data.first
                            itemView.findViewById<TextView>(R.id.tvAnalysisPercentage).text = "${data.second}%"
                            layout.addView(itemView)
                        }
                        
                        // Also save to history
                        val fullAnalysisString = analysisData.entries.joinToString { "${it.key}: ${it.value.first} (${it.value.second}%) " }
                        val savedImagePath = ImageStorage.save(this, uri)
                        val analysisResult = AnalysisResult(System.currentTimeMillis(), savedImagePath, fullAnalysisString)
                        HistoryManager.saveAnalysis(this, analysisResult)

                    } else {
                        // Handle no face detected
                    }
                }
                .addOnFailureListener { e ->
                    // Handle error
                }
        } catch (e: IOException) {
            // Handle error
        }
    }
}
