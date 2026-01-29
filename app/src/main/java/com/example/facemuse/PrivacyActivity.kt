package com.example.facemuse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class PrivacyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)

        val backButton = findViewById<TextView>(R.id.tvBack)
        val manageHistoryButton = findViewById<Button>(R.id.btnManageHistory)
        val deleteAllDataButton = findViewById<Button>(R.id.btnDeleteAllData)
        val logoutButton = findViewById<Button>(R.id.btnLogout)

        backButton.setOnClickListener {
            finish() // Go back to the previous screen
        }

        manageHistoryButton.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        deleteAllDataButton.setOnClickListener {
            showDeleteAllDataConfirmationDialog()
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

        // TODO: Add listeners for the switches to save user preferences
    }

    private fun showDeleteAllDataConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete All Data")
            .setMessage("Are you sure you want to permanently delete all of your data, including your account and analysis history? This action cannot be undone.")
            .setPositiveButton("Delete All") { _, _ ->
                // Perform data deletion and logout
                HistoryManager.clearHistory(this)
                FirebaseAuth.getInstance().currentUser?.delete()?.addOnCompleteListener {
                    Toast.makeText(this, "All data has been deleted.", Toast.LENGTH_SHORT).show()
                    // Restart the app at the login screen
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
