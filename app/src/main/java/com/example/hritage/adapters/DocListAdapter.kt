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
import com.example.hritage.model.ListOfDataFromWebModel

class DocListAdapter(
    private val item: ListOfDataFromWebModel?,
    private val normalLink:Boolean,
    private val context: Context
): RecyclerView.Adapter<DocListAdapter.viewHolder>() {
    inner class viewHolder(val binding: ContentOfWebListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun itemBinding(text: String, drawableId: Int) {
            binding.content.text=text
            binding.webContentImageView.setImageResource(drawableId)
        }
       //TODO Implement latter
        fun itemBinding1(text: String,date:String){
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
        holder.itemBinding(item!!.nameOfContent[position], item.drawableId)
        Log.i("ItemLIstGetting",item.nameOfContent[position])
        holder.binding.root.setOnClickListener {
            val intent= Intent(context, WebWiewActivity::class.java)
            if(!normalLink){
                 intent.putExtra(Constant.SEND_LINK_TO_WEB_ACTIVITY,"https://www.heritageit.edu/"+item.linkOfComponent[position])
            }else{
                intent.putExtra(Constant.SEND_LINK_TO_WEB_ACTIVITY,item.linkOfComponent[position])
            }
            context.startActivity(intent)
            Log.i("itemContent","https://www.heritageit.edu/"+item.linkOfComponent[position])
        }
    }
}