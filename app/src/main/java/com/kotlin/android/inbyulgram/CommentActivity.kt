package com.kotlin.android.inbyulgram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kotlin.android.inbyulgram.databinding.ActivityCommentBinding

class CommentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}