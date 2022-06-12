package com.example.anijuan.modules.moduleAnimeDetails

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.anijuan.R
import com.example.anijuan.databinding.ActivityAnimeDetailsBinding
import com.example.anijuan.common.entities.Anime
import com.example.anijuan.common.entities.Episode
import com.example.anijuan.common.settingsCustom.SettingsCustom
import com.example.anijuan.databinding.ItemCardEpisodeBinding
import com.example.anijuan.modules.moduleAnimeDetails.interfaces.AnimeDetailsAux
import com.example.anijuan.modules.moduleAnimeDetails.viewModel.AnimeDetailsViewModel
import com.example.anijuan.modules.modulePlayer.PlayerActivity
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AnimeDetailsActivity : AppCompatActivity(), AnimeDetailsAux {

    private lateinit var mBinding:ActivityAnimeDetailsBinding

    private lateinit var mAnime:Anime
    private lateinit var mFirebaseAdapter:FirebaseRecyclerAdapter<Episode,AnimeDetailsHolder>

    private lateinit var mAnimeDetailsViewModel:AnimeDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAnimeDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        getAnimeDetails()

        setupAppBar()

        setupFirebase()

        setupViewModel()

    }

    private fun setupViewModel() {
        mAnimeDetailsViewModel = ViewModelProvider(this)[AnimeDetailsViewModel::class.java]
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

        mAnime = anime
    }

    override fun openActivityDetails(episode: Episode) {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra("url",episode.urlVideo)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        mFirebaseAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAdapter.stopListening()
    }

    private fun setupFirebase() {
        val query = FirebaseDatabase.getInstance().reference
            .child(SettingsCustom.referenceAnimes)
            .child(mAnime.id.toString())
            .child(SettingsCustom.referenceEpisodes)
        val options = FirebaseRecyclerOptions.Builder<Episode>().setQuery(query){
            val episode = it.getValue(Episode::class.java)
            episode!!.id = it.key.toString()
            episode
        }.build()

        mFirebaseAdapter = object: FirebaseRecyclerAdapter<Episode,AnimeDetailsHolder>(options){
            private lateinit var mContext:Context

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeDetailsHolder {
                mContext = parent.context
                val view = LayoutInflater.from(mContext).inflate(R.layout.item_card_episode,parent,false)
                return AnimeDetailsHolder(view)
            }

            override fun onBindViewHolder(
                holder: AnimeDetailsHolder,
                position: Int,
                model: Episode
            ) {
                val episode = getItem(position)

                with(holder){
                    Glide.with(mContext)
                        .load(episode.photoUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .circleCrop()
                        .into(binding.ivPhotoEpisode)

                    binding.tvNameEpisode.text = episode.name
                    binding.tvDescriptionEpisode.text = episode.description
                    binding.tvNumberEpisode.text = episode.episode.toString()

                    val stars:Int? = episode.listStars[
                            FirebaseAuth.getInstance().currentUser!!.uid
                    ]
                    stars?.let {
                        getStarsEpisode(it)
                    }

                    binding.cbEpisodeLike.isChecked = episode.listLike.containsKey(
                        FirebaseAuth.getInstance().currentUser!!.uid
                    )

                    binding.root.setOnClickListener {
                        openActivityDetails(episode)
                    }

                    binding.tvCounterLikes.text = episode.listLike.keys.size.toString()
                    binding.cbEpisodeLike.setOnCheckedChangeListener { _ , checked ->
                        setLikeEpisode(episode,checked)
                    }

                    binding.cbStart1.setOnClickListener {
                        val checked = binding.cbStart1.isChecked
                        if (checked) setStarsEpisode(episode,1)
                        else setStarsEpisode(episode,6)
                    }
                    binding.cbStart2.setOnClickListener {
                        val checked = binding.cbStart2.isChecked
                        if (checked) setStarsEpisode(episode,2)
                        else setStarsEpisode(episode,6)
                    }
                    binding.cbStart3.setOnClickListener {
                        val checked = binding.cbStart3.isChecked
                        if (checked) setStarsEpisode(episode,3)
                        else setStarsEpisode(episode,6)
                    }
                    binding.cbStart4.setOnClickListener {
                        val checked = binding.cbStart4.isChecked
                        if (checked) setStarsEpisode(episode,4)
                        else setStarsEpisode(episode,6)
                    }
                    binding.cbStart5.setOnClickListener {
                        val checked = binding.cbStart5.isChecked
                        if (checked) setStarsEpisode(episode,5)
                        else setStarsEpisode(episode,6)
                    }

                }
            }

        }

        val manager = LinearLayoutManager(this)

        mBinding.rvDetailsAnimeEpisodes.apply {
            layoutManager = manager
            adapter = mFirebaseAdapter
        }

    }


    inner class AnimeDetailsHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = ItemCardEpisodeBinding.bind(view)
        fun setLikeEpisode(episode: Episode,checked:Boolean){
            var anime:Anime? = null
            for(ani in episode.anime){
                anime = ani.value
                anime.id = ani.key
            }
            anime?.let{
                if(checked){
                    //anime references
                    FirebaseDatabase.getInstance().reference
                        .child( SettingsCustom.referenceAnimes)
                        .child(it.id.toString())
                        .child(SettingsCustom.referenceEpisodes)
                        .child(episode.id!!)
                        .child("listLike")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(checked)

                    //episodes references
                    FirebaseDatabase.getInstance().reference
                        .child(SettingsCustom.referenceEpisodes)
                        .child(episode.id.toString())
                        .child("listLike")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(checked)
                }else {
                    //anime reference
                    FirebaseDatabase.getInstance().reference
                        .child(SettingsCustom.referenceAnimes)
                        .child(it.id.toString()).child(SettingsCustom.referenceEpisodes)
                        .child(episode.id!!).child("listLike")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(null)
                    //episodes references
                    FirebaseDatabase.getInstance().reference
                        .child(SettingsCustom.referenceEpisodes)
                        .child(episode.id.toString())
                        .child("listLike")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(null)
                }
            }
        }

        fun setStarsEpisode(episode: Episode,stars:Int){
            var anime:Anime? = null
            for(ani in episode.anime){
                anime = ani.value
                anime.id = ani.key
            }
            anime?.let {
                //episodes references
                FirebaseDatabase.getInstance().reference
                    .child(SettingsCustom.referenceEpisodes)
                    .child(episode.id.toString())
                    .child("listStars")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(stars)

                //anime references
                FirebaseDatabase.getInstance().reference
                    .child(SettingsCustom.referenceAnimes)
                    .child(anime.id.toString())
                    .child(SettingsCustom.referenceEpisodes)
                    .child(episode.id!!)
                    .child("listStars")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(stars)
            }
            getStarsEpisode(stars)
        }

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