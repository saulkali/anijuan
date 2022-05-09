package com.example.anijuan.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.anijuan.R
import com.example.anijuan.databinding.ActivitySearchAnimeBinding

class SearchAnimeActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySearchAnimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySearchAnimeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}