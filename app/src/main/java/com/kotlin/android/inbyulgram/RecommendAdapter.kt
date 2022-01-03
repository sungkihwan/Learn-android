package com.kotlin.android.inbyulgram

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlin.android.inbyulgram.databinding.ItemSearchRecommendBinding

class RecommendAdapter (private val context: MainActivity, private val dataList: ArrayList<String>) :
    RecyclerView.Adapter<RecommendAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemSearchRecommendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, item: String) {
//          이미지
            Glide.with(context).load(item).into(binding.recommendIv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchRecommendBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}
