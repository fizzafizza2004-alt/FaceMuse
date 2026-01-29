package com.example.facemuse

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class HistoryActivity : AppCompatActivity() {

    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyList: MutableList<AnalysisResult>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val rvHistory = findViewById<RecyclerView>(R.id.rvHistory)
        val backButton = findViewById<TextView>(R.id.tvBack)
        val clearHistoryButton = findViewById<Button>(R.id.btnClearHistory)
        val managePrivacyLink = findViewById<TextView>(R.id.tvManagePrivacy)

        historyList = HistoryManager.getHistory(this).toMutableList()

        historyAdapter = HistoryAdapter(
            historyList,
            onDetailsClick = { result ->
                val intent = Intent(this, ResultActivity::class.java)
                intent.data = Uri.fromFile(File(result.imageUri))
                startActivity(intent)
            },
            onDeleteClick = { result ->
                showDeleteConfirmationDialog(result)
            }
        )

        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.adapter = historyAdapter

        backButton.setOnClickListener { finish() }

        clearHistoryButton.setOnClickListener {
            showClearAllConfirmationDialog()
        }

        managePrivacyLink.setOnClickListener {
            startActivity(Intent(this, PrivacyActivity::class.java))
        }
    }

    private fun showDeleteConfirmationDialog(result: AnalysisResult) {
        AlertDialog.Builder(this)
            .setTitle("Delete Analysis")
            .setMessage("Are you sure you want to delete this analysis record?")
            .setPositiveButton("Delete") { _, _ ->
                HistoryManager.deleteAnalysis(this@HistoryActivity, result)
                historyList.remove(result)
                historyAdapter.notifyDataSetChanged()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showClearAllConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Clear All History")
            .setMessage("Are you sure you want to delete your entire analysis history? This action cannot be undone.")
            .setPositiveButton("Clear All") { _, _ ->
                HistoryManager.clearHistory(this@HistoryActivity)
                historyList.clear()
                historyAdapter.notifyDataSetChanged()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
