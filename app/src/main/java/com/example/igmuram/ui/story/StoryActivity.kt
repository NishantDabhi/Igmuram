package com.example.igmuram.ui.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewPropertyAnimator
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.igmuram.R
import com.example.igmuram.databinding.ActivityStoryBinding

class StoryActivity : AppCompatActivity() {

    private val storyViewModel by viewModels<StoryViewModel>()
    private lateinit var _binding : ActivityStoryBinding
    private val storyPagerAdapter = StoryPagerAdapter()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val tagName = intent.getStringExtra("tag")

        tagName?.let { storyViewModel.fetchTagGallery(it) }

        _binding.storyViewPager.adapter = storyPagerAdapter
        _binding.storyViewPager.registerOnPageChangeCallback(pageChangeCallBack)

        _binding.progressView.animate()
            .scaleX(1F)
            .setDuration(3000)
            .start()
    }

    private val nextPageRunnable = Runnable {
        _binding.storyViewPager.currentItem++
    }

    private val pageChangeCallBack = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            _binding.progressView.scaleX = 0F
            _binding.progressView.animate().cancel()
            _binding.progressView.animate().scaleX(1F).setDuration(5000).setStartDelay(10).start()

            handler.removeCallbacks(nextPageRunnable)
            handler.postDelayed(nextPageRunnable, 5000)
        }
    }

    override fun onResume() {
        super.onResume()

        storyViewModel.images.observe(this) {
            storyPagerAdapter.submitList(it)
        }
    }
}