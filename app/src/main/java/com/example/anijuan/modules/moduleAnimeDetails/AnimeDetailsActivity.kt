package com.example.anijuan.modules.moduleAnimeDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.anijuan.databinding.ActivityAnimeDetailsBinding
import com.example.anijuan.common.entities.Anime
import com.example.anijuan.databinding.ItemCardEpisodeBinding
import com.example.anijuan.modules.moduleAnimeDetails.viewModel.AnimeDetailsViewModel


class AnimeDetailsActivity : AppCompatActivity() {

    private lateinit var mBinding:ActivityAnimeDetailsBinding

    private lateinit var mAnimeDetailsViewModel:AnimeDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAnimeDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupViewModel()
        getAnimeDetails()
        setupAppBar()

    }

    private fun setupRecycleView() {
        val manager = LinearLayoutManager(this)
        mBinding.rvDetailsAnimeEpisodes.apply {
            layoutManager = manager
            adapter = mAnimeDetailsViewModel.getFirebaseAdapter().value
        }
    }

    private fun setupViewModel() {
        mAnimeDetailsViewModel = ViewModelProvider(this)[AnimeDetailsViewModel::class.java]
        mAnimeDetailsViewModel.getFirebaseAdapter().observe(this){
            setupRecycleView()
        }
    }


    private fun setupAppBar() {
        setSupportActionBar(mBinding.tbDetailsAnime)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)

    }
    private fun getAnimeDetails() {
        val anime: Anime? = intent.getSerializableExtra("anime") as? Anime
        if(anime == null)
            finish()

        Glide.with(this)
            .load(anime!!.photoUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(mBinding.ivDetailsAnimePhoto)

        mBinding.tvNameDetailsAnime.text = anime.name
        mBinding.tvDescriptionDetailsAnime.text = anime.description

        mAnimeDetailsViewModel.setAnimeSelected(anime)
    }


    override fun onStart() {
        super.onStart()
        mAnimeDetailsViewModel.getFirebaseAdapter().value?.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAnimeDetailsViewModel.getFirebaseAdapter().value?.stopListening()

    }

    inner class AnimeDetailsHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = ItemCardEpisodeBinding.bind(view)
        fun getStarsEpisode(stars: Int){
            when(stars){
                1 -> {
                    binding.cbStart1.isChecked = true
                    binding.cbStart2.isChecked = false
                    binding.cbStart3.isChecked = false
                    binding.cbStart4.isChecked = false
                    binding.cbStart5.isChecked = false
                }
                2 -> {
                    binding.cbStart1.isChecked = true
                    binding.cbStart2.isChecked = true
                    binding.cbStart3.isChecked = false
                    binding.cbStart4.isChecked = false
                    binding.cbStart5.isChecked = false
                }
                3 -> {
                    binding.cbStart1.isChecked = true
                    binding.cbStart2.isChecked = true
                    binding.cbStart3.isChecked = true
                    binding.cbStart4.isChecked = false
                    binding.cbStart5.isChecked = false
                }
                4 -> {
                    binding.cbStart1.isChecked = true
                    binding.cbStart2.isChecked = true
                    binding.cbStart3.isChecked = true
                    binding.cbStart4.isChecked = true
                    binding.cbStart5.isChecked = false
                }
                5 -> {
                    binding.cbStart1.isChecked = true
                    binding.cbStart2.isChecked = true
                    binding.cbStart3.isChecked = true
                    binding.cbStart4.isChecked = true
                    binding.cbStart5.isChecked = true
                }
                6 -> {
                    binding.cbStart1.isChecked = false
                    binding.cbStart2.isChecked = false
                    binding.cbStart3.isChecked = false
                    binding.cbStart4.isChecked = false
                    binding.cbStart5.isChecked = false
                }
            }
        }
    }

}