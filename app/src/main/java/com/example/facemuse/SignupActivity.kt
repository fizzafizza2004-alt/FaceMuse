package com.example.facemuse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val nameField = findViewById<EditText>(R.id.etName)
        val emailField = findViewById<EditText>(R.id.etEmail)
        val passwordField = findViewById<EditText>(R.id.etPass)
        val confirmPasswordField = findViewById<EditText>(R.id.etConfirmPass)
        val createAccountButton = findViewById<Button>(R.id.btnCreateAccount)
        val loginLink = findViewById<TextView>(R.id.tvLoginLink)

        createAccountButton.setOnClickListener {
            val name = nameField.text.toString()
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { 
                    // On success, navigate to the instructions screen
                    startActivity(Intent(this, InstructionsActivity::class.java))
                    finishAffinity() // Clear all previous activities
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Signup failed: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        }

        loginLink.setOnClickListener {
            // Navigate back to the Login screen
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
