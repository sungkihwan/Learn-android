package com.kotlin.android.inbyulgram

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kotlin.android.inbyulgram.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    private var storyList: ArrayList<Story> = arrayListOf(
        Story("Kihwan", "https://file.mk.co.kr/meet/neds/2021/06/image_readtop_2021_535745_16226846584668330.jpg"),
        Story("Kihwan", "https://file.mk.co.kr/meet/neds/2021/06/image_readtop_2021_535745_16226846584668330.jpg"),
        Story("Kihwan", "https://file.mk.co.kr/meet/neds/2021/06/image_readtop_2021_535745_16226846584668330.jpg"),
        Story("Kihwan", "https://file.mk.co.kr/meet/neds/2021/06/image_readtop_2021_535745_16226846584668330.jpg"),
        Story("Kihwan", "https://file.mk.co.kr/meet/neds/2021/06/image_readtop_2021_535745_16226846584668330.jpg"),
        Story("Kihwan", "https://file.mk.co.kr/meet/neds/2021/06/image_readtop_2021_535745_16226846584668330.jpg"),
        Story("Kihwan", "https://file.mk.co.kr/meet/neds/2021/06/image_readtop_2021_535745_16226846584668330.jpg"),
        Story("Kihwan", "https://file.mk.co.kr/meet/neds/2021/06/image_readtop_2021_535745_16226846584668330.jpg"),
        Story("Kihwan", "https://file.mk.co.kr/meet/neds/2021/06/image_readtop_2021_535745_16226846584668330.jpg"),
        Story("Kihwan", "https://file.mk.co.kr/meet/neds/2021/06/image_readtop_2021_535745_16226846584668330.jpg"),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.database
        database = db.getReference("FeedList")

        binding.homeRvStory.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.homeRvStory.adapter = StoryAdapter(activity as MainActivity, storyList)
    }

    override fun onStart() {
        super.onStart()

        var feedList: ArrayList<Feed> = arrayListOf()

        binding.homeRvFeed.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.homeRvFeed.adapter = FeedAdapter(activity as MainActivity, feedList)
        binding.homeRvFeed.isNestedScrollingEnabled = false

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val values = snapshot.value as ArrayList<HashMap<String, Any>>?
                for (i: Int in 1 until (values?.size?: 0)) {
                    val data = values?.get(i)
                    feedList.add(
                        Feed(
                            data?.get("userId") as String,
                            data?.get("imageUrl") as String,
                            data?.get("profileImageUrl") as String,
                            data?.get("likeCount") as Long,
                            false,
                            false
//                            data?.get("isBookmark") as Boolean,
//                            data?.get("isLike") as Boolean
                        )
                    )
                }
                binding.homeRvFeed.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}