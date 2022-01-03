package com.kotlin.android.inbyulgram

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.kotlin.android.inbyulgram.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//      Firebase 메시지 클라우드 사용
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("토큰 : ", token.toString())
        })

        if (intent.getStringExtra("tabFragment").equals("search")) {
            val searchFragment = SearchFragment()

            // 액티비티에서 프래그먼트로 데이터 전송시 번들 사용
            val bundle = Bundle()
            bundle.putString("keyword", intent.getStringExtra("keyword"))
            searchFragment.arguments = bundle
            val transition = supportFragmentManager.beginTransaction()
            transition.add(R.id.main_fl, searchFragment)
            transition.commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.main_fl, HomeFragment()).commitAllowingStateLoss()
        }

        binding.mainBottomNav.itemIconTintList = null
        binding.mainBottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_fl, HomeFragment()).commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.tab_search -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_fl, SearchFragment()).commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.tab_upload -> {
                    val intent = Intent(this, UploadActivity::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
                R.id.tab_heart -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_fl, HeartFragment()).commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.tab_mypage -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_fl, MypageFragment()).commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                else -> {
                    false
                }
            }
        }
    }
}