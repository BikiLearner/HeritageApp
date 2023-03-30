package com.example.hritage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hritage.databinding.RvContentBinding

class collegeImageAdapter(
    private val item: Array<Int>
): RecyclerView.Adapter<collegeImageAdapter.viewHolder>() {
    inner class viewHolder(val binding: RvContentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun itemBinding(imageId:Int) {
            binding.imageRv.setImageResource(imageId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(binding = RvContentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return Integer.MAX_VALUE
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val pos=position%(item.size-1)
        holder.itemBinding(item[pos])
//        setAnimation(holder.itemView,pos)
    }
}