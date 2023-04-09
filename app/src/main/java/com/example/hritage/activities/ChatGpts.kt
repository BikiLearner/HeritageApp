package com.example.hritage.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hritage.ExtraClass.Message
import com.example.hritage.R
import com.example.hritage.adapters.MessageAdapter
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okio.IOException
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit


class ChatGpts : AppCompatActivity() {
    private val apiKey = "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"
    private var recyclerView: RecyclerView? = null
    private var welcomeTextView: TextView? = null
    private var messageEditText: EditText? = null
    private var sendButton: ImageButton? = null
    private var messageList: ArrayList<Message>? = null
    private var messageAdapter: MessageAdapter? = null
    private val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    private var client =OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS) // Set the connection timeout to 60 seconds
        .build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_gpt)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Chat Bot"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
        }


        messageList = ArrayList()

        recyclerView = findViewById(R.id.recycler_view)
        welcomeTextView = findViewById(R.id.welcome_text)
        messageEditText = findViewById(R.id.message_edit_text)
        sendButton = findViewById(R.id.send_btn)

        //setup recycler view

        //setup recycler view
        messageAdapter = MessageAdapter(messageList!!,this@ChatGpts)
        recyclerView?.adapter=messageAdapter
        val llm = LinearLayoutManager(this)
        llm.stackFromEnd = true
        recyclerView?.layoutManager = llm

        sendButton?.setOnClickListener { v: View? ->
            val question: String = messageEditText?.text.toString().trim()
            addToChat(question, Message.SENT_BY_ME)
            messageEditText?.setText("")
            callAPI(question)
            welcomeTextView?.visibility = View.GONE
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun addToChat(message: String?, sentBy: String?) {
        runOnUiThread {
            messageList!!.add(Message(message!!, sentBy!!))
            messageAdapter!!.notifyDataSetChanged()
            recyclerView!!.smoothScrollToPosition(messageAdapter!!.itemCount)
        }
    }

    private fun addResponse(response: String?) {
        messageList!!.removeAt(messageList!!.size - 1)
        addToChat(response, Message.SENT_BY_BOT)
    }
    private fun callAPI(question: String?) {
        messageList!!.add(Message("Typing... ", Message.SENT_BY_BOT))
        //okhttp
        val jsonBody = JSONObject()
        try {
            jsonBody.put("model", "text-davinci-003")
            jsonBody.put("prompt", question)
            jsonBody.put("max_tokens", 4000)
            jsonBody.put("temperature", 0)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body: RequestBody =RequestBody.create(JSON,jsonBody.toString())
        val request = Request.Builder()
            .url("https://api.openai.com/v1/completions")
            .header("Authorization", "Bearer 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")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                addResponse("Failed to load response due to "+e.message);
            }

            @SuppressLint("SetTextI18n")
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonObject: JSONObject?
                    try {
                        jsonObject = response.body?.string()?.let { JSONObject(it) }
                        val jsonArray = jsonObject?.getJSONArray("choices")
                        val result = jsonArray?.getJSONObject(0)?.getString("text")
                        Log.i("HELLO",result.toString().trim())
//                        responseTextView.text=result.toString().trim()
                        addResponse(result!!.trim())
                    } catch (e: JSONException) {
                        Log.i("HELLO",e.printStackTrace().toString())
//                        responseTextView.text=e.printStackTrace().toString()
                        e.printStackTrace()
                    }
                } else {
                    Log.i("HELLO","Failed to load response due to ${response.code}")
//                    responseTextView.text="Failed to load response due to ${response.body?.toString()}"
                    addResponse("Failed to load response due to "+ response.body.toString())
                }
            }
        })

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}