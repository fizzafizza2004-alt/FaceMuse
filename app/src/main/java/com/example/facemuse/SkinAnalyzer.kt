package com.example.facemuse

import com.google.mlkit.vision.face.Face

object SkinAnalyzer {

    fun analyzeFace(face: Face?): String {
        if (face == null) return "Face not clear"

        val smile = face.smilingProbability ?: 0f
        val leftEye = face.leftEyeOpenProbability ?: 0f
        val rightEye = face.rightEyeOpenProbability ?: 0f

        return """
        Face Analysis:
        Smile Probability: $smile
        Eye Openness: ${(leftEye + rightEye) / 2}

        Skin Type: Normal
        Possible Concerns: Mild dryness

        """.trimIndent()
    }
}
