package com.example.facemuse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val messages: List<Message>) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    // Move constants inside a companion object
    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_AI = 2
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tvMessage)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isFromUser) VIEW_TYPE_USER else VIEW_TYPE_AI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutId = if (viewType == VIEW_TYPE_USER) R.layout.item_chat_message_user else R.layout.item_chat_message_ai
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.textView.text = message.text

        holder.itemView.visibility = if (message.isVisible) View.VISIBLE else View.GONE
        holder.itemView.layoutParams = RecyclerView.LayoutParams(if (message.isVisible) ViewGroup.LayoutParams.MATCH_PARENT else 0, if (message.isVisible) ViewGroup.LayoutParams.WRAP_CONTENT else 0)
    }

    override fun getItemCount() = messages.size
}
