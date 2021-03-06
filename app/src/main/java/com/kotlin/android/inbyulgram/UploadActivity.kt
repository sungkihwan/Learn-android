package com.kotlin.android.inbyulgram

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.kotlin.android.inbyulgram.databinding.ActivityUploadBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private val PICK_STORAGE = 1001
    private val PICK_CAMERA = 1000
    private var PERMISSIONS_REQUEST = 100
    private var imageUrl: Uri? = null
    private var firebaseStorage: FirebaseStorage? = null
    private lateinit var database: DatabaseReference

    private val Permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

//      db 객체 셋업
        firebaseStorage = Firebase.storage
        val db = Firebase.database
        database = db.getReference("FeedList")

        pickImage()
        checkPermissions(Permissions)

        binding.uploadIvImage.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, PICK_CAMERA)
            }
        }

        binding.uploadBtnBack.setOnClickListener {
            onBackPressed()
        }

        binding.uploadBtnComplete.setOnClickListener {
//          Firebase Storage 이미지 업로드
            uploadImage()
        }
    }

    private fun pickImage() {
        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/* video/*"

//      사진/영상 업로드 디프리케이티드 되서 변경해야함
        startActivityForResult(intent, PICK_STORAGE)
    }

    private fun uploadImage() {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "$timeStamp.jpeg"
        val storageReference = firebaseStorage?.reference?.child(imageFileName)
        storageReference?.putFile(imageUrl!!)?.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            storageReference.downloadUrl
        }?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                database.get().addOnSuccessListener {  it ->
                    var values = it.value as ArrayList<HashMap<String,Any>>?
                    database.child((values?.size?:0 + 1).toString()).setValue(
                        Feed(
                            "KihwanSung",
                            downloadUri.toString(),
                            downloadUri.toString(),
                            0,
                            false,
                            false
                        ))
                }
                finish()
            } else {
                // Handle failures
                // ...
            }
        }
    }

    private fun checkPermissions(permissions: Array<String>): Boolean {
        val permissionList: MutableList<String> = mutableListOf()
        for (permission in permissions) {
            val result = ContextCompat.checkSelfPermission(this, permission)
            if(result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission)
            }
        }
        if (permissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this, permissionList.toTypedArray(), PERMISSIONS_REQUEST
            )
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RESULT_OK) {
            if(requestCode == PICK_STORAGE) {
                val pickedImage: Uri? = data?.data
                if (pickedImage != null) {
                    imageUrl = pickedImage
                }
                // 이미지 뿌려주기
                Glide.with(this).load(imageUrl).into(binding.uploadIvImage)
            }
            if (requestCode == PICK_CAMERA) {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                val pickedImage: Uri? = data?.data
                if (pickedImage != null) {
                    imageUrl = pickedImage
                }
                binding.uploadIvImage.setImageBitmap(imageBitmap)
            }
        }
    }
}