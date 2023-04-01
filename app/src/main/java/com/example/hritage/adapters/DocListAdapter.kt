package com.example.hritage.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hritage.Constant
import com.example.hritage.activities.WebWiewActivity
import com.example.hritage.databinding.ContentOfWebListBinding
import com.example.hritage.model.ListOfDataFromWebModel

class DocListAdapter(
    private val item: ListOfDataFromWebModel?,
    private val normalLink:Int,
    private val context: Context
): RecyclerView.Adapter<DocListAdapter.viewHolder>() {
    inner class viewHolder(val binding: ContentOfWebListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun itemBinding(text: String, drawableId: Int) {
            binding.content.text=text
            binding.webContentImageView.setImageResource(drawableId)
        }
       //TODO Implement latter
        fun itemBinding1(text: String,date:String,drawableId: Int){
            binding.content.text=text
            binding.contentDate.text=date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(binding = ContentOfWebListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return item?.nameOfContent!!.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        if(item?.date!=null && item.date.isNotEmpty()){
            holder.itemBinding1(item.nameOfContent[position],item.date[position], item.drawableId)
        }else {
            item?.let { holder.itemBinding(item.nameOfContent[position], it.drawableId) }
        }
        holder.binding.root.setOnClickListener {

            when (normalLink) {
                0 -> {
                    val intent= Intent(context, WebWiewActivity::class.java)
                    intent.putExtra(Constant.SEND_LINK_TO_WEB_ACTIVITY,"https://www.heritageit.edu/"+item!!.linkOfComponent[position])
                    context.startActivity(intent)
                }
                1 -> {
                    val intent=Intent(Intent.ACTION_VIEW, Uri.parse(item!!.linkOfComponent[position]))
                    context.startActivity(intent)
                }
                else -> {
                    val intent= Intent(context, WebWiewActivity::class.java)
                    intent.putExtra(Constant.SEND_LINK_TO_WEB_ACTIVITY,item!!.linkOfComponent[position])
                    context.startActivity(intent)
                }
            }

            Log.i("itemContent","https://www.heritageit.edu/"+item.linkOfComponent[position])
        }
    }
}