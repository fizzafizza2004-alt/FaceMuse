package com.example.facemuse

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class ChatBotActivity : AppCompatActivity() {

    private val geminiRepository by lazy { GeminiRepository() }
    private val messages = mutableListOf<Message>()
    private val chatAdapter = ChatAdapter(messages)

    private lateinit var quickQuestionsLayout: LinearLayout
    private lateinit var promptEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)

        val rvChat = findViewById<RecyclerView>(R.id.rvChat)
        val sendButton = findViewById<ImageButton>(R.id.btnSend)
        val backButton = findViewById<TextView>(R.id.tvBack)
        quickQuestionsLayout = findViewById(R.id.llQuickQuestions)
        promptEditText = findViewById(R.id.etPrompt)

        rvChat.layoutManager = LinearLayoutManager(this)
        rvChat.adapter = chatAdapter

        // Add initial AI message
        addMessage("Hi! I'm here to help you understand your skin analysis. What would you like to know?", false)

        sendButton.setOnClickListener {
            val prompt = promptEditText.text.toString()
            if (prompt.isNotEmpty()) {
                sendMessage(prompt)
            }
        }

        backButton.setOnClickListener { finish() }

        // Setup quick questions
        setupQuickQuestion(R.id.q1)
        setupQuickQuestion(R.id.q2)
        setupQuickQuestion(R.id.q3)
        setupQuickQuestion(R.id.q4)
    }

    private fun setupQuickQuestion(buttonId: Int) {
        findViewById<TextView>(buttonId).setOnClickListener {
            val question = (it as TextView).text.toString()
            sendMessage(question)
        }
    }

    private fun sendMessage(prompt: String) {
        addMessage(prompt, true)
        promptEditText.text.clear()
        quickQuestionsLayout.visibility = View.GONE // Hide quick questions

        lifecycleScope.launch {
            val response = geminiRepository.getAIResponse(prompt)
            addMessage(response, false)
        }
    }

    private fun addMessage(text: String, isFromUser: Boolean) {
        messages.add(Message(text, isFromUser))
        chatAdapter.notifyItemInserted(messages.size - 1)
        findViewById<RecyclerView>(R.id.rvChat).scrollToPosition(messages.size - 1)
    }
}
