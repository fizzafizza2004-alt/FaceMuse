package com.example.facemuse

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

data class RoutineStep(val number: Int, val title: String, val description: String)

class RoutineActivity : AppCompatActivity() {

    private lateinit var stepsContainer: LinearLayout

    // Placeholder data - this will be replaced by AI suggestions
    private val morningRoutine = listOf(
        RoutineStep(1, "Gentle Cleanser", "Wash face with lukewarm water and a mild cleanser"),
        RoutineStep(2, "Hydrating Toner", "Apply to balance and prep skin"),
        RoutineStep(3, "Moisturizer", "Lightweight, hydrating formula"),
        RoutineStep(4, "Sunscreen SPF 30+", "Broad spectrum protection - essential!")
    )

    private val nightRoutine = listOf(
        RoutineStep(1, "Gentle Cleanser", "Remove makeup and impurities"),
        RoutineStep(2, "Exfoliating Serum", "Use 2-3 times a week to remove dead skin cells"),
        RoutineStep(3, "Night Cream", "Rich, nourishing formula for overnight repair")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine)

        stepsContainer = findViewById(R.id.llRoutineSteps)
        val morningTab = findViewById<TextView>(R.id.tabMorning)
        val nightTab = findViewById<TextView>(R.id.tabNight)
        val backButton = findViewById<TextView>(R.id.tvBack)
        val askChatbotButton = findViewById<Button>(R.id.btnAskChatbot)
        val feedbackLink = findViewById<TextView>(R.id.tvShareFeedback)

        // Set initial routine
        updateRoutine(morningRoutine)

        morningTab.setOnClickListener {
            it.background = getDrawable(R.drawable.tab_selected_background)
            nightTab.background = getDrawable(R.drawable.tab_unselected_background)
            updateRoutine(morningRoutine)
        }

        nightTab.setOnClickListener {
            it.background = getDrawable(R.drawable.tab_selected_background)
            morningTab.background = getDrawable(R.drawable.tab_unselected_background)
            updateRoutine(nightRoutine)
        }

        backButton.setOnClickListener {
            finish() // Go back to the previous screen (Results)
        }

        askChatbotButton.setOnClickListener {
            startActivity(Intent(this, ChatBotActivity::class.java))
        }

        feedbackLink.setOnClickListener {
            startActivity(Intent(this, FeedbackActivity::class.java))
        }
    }

    private fun updateRoutine(steps: List<RoutineStep>) {
        stepsContainer.removeAllViews()
        for (step in steps) {
            val stepView = LayoutInflater.from(this).inflate(R.layout.item_routine_step, stepsContainer, false)
            stepView.findViewById<TextView>(R.id.tvStepNumber).text = step.number.toString()
            stepView.findViewById<TextView>(R.id.tvStepTitle).text = step.title
            stepView.findViewById<TextView>(R.id.tvStepDescription).text = step.description
            stepsContainer.addView(stepView)
        }
    }
}
