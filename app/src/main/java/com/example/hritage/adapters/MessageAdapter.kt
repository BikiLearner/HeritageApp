package com.example.hritage.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.example.hritage.ExtraClass.Message
import com.example.hritage.R


class MessageAdapter(messageList: List<Message>, private val context:Context) :
    RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {
    var messageList: List<Message>


    init {
        this.messageList = messageList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val chatView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_item, null)
        return MyViewHolder(chatView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message: Message = messageList[position]
        if (message.sentBy == Message.SENT_BY_ME) {
            holder.leftChatView.visibility = View.GONE
            holder.rightChatView.visibility = View.VISIBLE
            holder.rightTextView.text = message.message
        } else {
            holder.rightChatView.visibility = View.GONE
            holder.leftChatView.visibility = View.VISIBLE
            holder.leftTextView.text = message.message
        }
        holder.imageClipboardBtn.setOnClickListener {
            val text=holder.leftTextView.text.toString()
            toSaveCode(text)
        }
    }
    private fun toSaveCode(text:String){
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label",text)
        clipboardManager.setPrimaryClip(clip)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var leftChatView: LinearLayout
        var rightChatView: LinearLayout
        var leftTextView: TextView
        var rightTextView: TextView
        lateinit var imageClipboardBtn:ImageButton

        init {
            leftChatView = itemView.findViewById(R.id.left_chat_view)
            rightChatView = itemView.findViewById(R.id.right_chat_view)
            leftTextView = itemView.findViewById(R.id.left_chat_text_view)
            rightTextView = itemView.findViewById(R.id.right_chat_text_view)
            imageClipboardBtn=itemView.findViewById(R.id.clipboard_of_textview_chat_bot)
        }
    }
}