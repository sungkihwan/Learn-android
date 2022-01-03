package com.kotlin.android.inbyulgram

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kotlin.android.inbyulgram.databinding.ItemSearchKeywordBinding

class KeywordAdapter (private val context: SearchActivity, private val dataList: ArrayList<String>) :
    RecyclerView.Adapter<KeywordAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemSearchKeywordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, item: String, dataList: ArrayList<String>, keywordAdapter: KeywordAdapter) {
            binding.keywordTv.text = item

            binding.keywordBtnRemove.setOnClickListener {
                dataList.remove(item)

                // 삭제
                val gson = Gson()
                val json = gson.toJson(dataList)
                val prefs = context.getSharedPreferences("inbyulgram", Context.MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putString("keywords", json)
                editor.apply()

                //리사이클러뷰 갱신
                keywordAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchKeywordBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, dataList[position], dataList, this)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}