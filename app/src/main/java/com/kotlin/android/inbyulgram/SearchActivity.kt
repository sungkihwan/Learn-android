package com.kotlin.android.inbyulgram

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kotlin.android.inbyulgram.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var keywordList: ArrayList<String>? = getKeywords("keywords")
        binding.searchRvKeyword.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.searchRvKeyword.adapter = keywordList?.let { KeywordAdapter(this, it) }

        binding.searchEtKeyword.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                keywordList!!.add(binding.searchEtKeyword.text.toString())
                saveKeywords("keywords", keywordList)
//                binding.searchRvKeyword.adapter!!.notifyDataSetChanged()
//                binding.searchEtKeyword.text = null

                // 액티비티에서 액티비티로 데이터 전송시 인텐트 사용
                Intent(this, MainActivity::class.java).apply {
                    putExtra("keyword", binding.searchEtKeyword.text.toString())
                    putExtra("tabFragment", "search")

//                  인텐트 플래그 설정 -> 메인화면 체인지
                    this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run {
                    startActivity(this)
                }
            }
            true
        }

        binding.searchIvBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getKeywords(key: String): ArrayList<String> {
        val prefs = getSharedPreferences("inbyulgram", Context.MODE_PRIVATE)
        val json = prefs.getString(key, "[]")
        val gson = Gson()

        return gson.fromJson(
            json,
            object : TypeToken<ArrayList<String?>>() {}.type
        )
    }

    private fun saveKeywords(key: String, values: ArrayList<String>) {
        val gson = Gson()
        val json = gson.toJson(values)
        val prefs = getSharedPreferences("inbyulgram", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, json)
        editor.apply()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}