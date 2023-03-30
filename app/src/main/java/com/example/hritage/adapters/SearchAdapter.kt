package com.example.hritage.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hritage.Constant
import com.example.hritage.activities.WebWiewActivity
import com.example.hritage.databinding.ContentOfWebListBinding

class SearchAdapter(
    private val item1:java.util.ArrayList<String>,
    private val item2:java.util.ArrayList<String>,
    private val context: Context
): RecyclerView.Adapter<SearchAdapter.viewHolder>() {
    inner class viewHolder(val binding: ContentOfWebListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun itemBinding(text: String) {
            binding.content.text=text
        }
        //TODO Implement latter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        return viewHolder(ContentOfWebListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return item1.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.itemBinding(item1[position])
//        Log.i("ItemLIstGetting",item.nameOfContent[position])
        holder.binding.root.setOnClickListener {
            val intent= Intent(context, WebWiewActivity::class.java)
            intent.putExtra(Constant.SEND_LINK_TO_WEB_ACTIVITY,"https://www.heritageit.edu/"+item2[position])
            context.startActivity(intent)
            Log.i("itemContent","https://www.heritageit.edu/"+item2[position])
        }
    }
}