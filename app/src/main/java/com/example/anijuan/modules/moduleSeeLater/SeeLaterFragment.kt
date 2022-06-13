package com.example.anijuan.modules.moduleSeeLater

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anijuan.modules.moduleAnimeDetails.AnimeDetailsActivity
import com.example.anijuan.modules.modulePlayer.PlayerActivity
import com.example.anijuan.databinding.FragmentSeeLaterBinding
import com.example.anijuan.databinding.ItemCardAnimeBinding
import com.example.anijuan.databinding.ItemCardEpisodeBinding
import com.example.anijuan.common.entities.Anime
import com.example.anijuan.common.entities.Episode
import com.example.anijuan.modules.moduleSeeLater.viewModel.SeeLaterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SeeLaterFragment : Fragment() {


    private lateinit var mBinding:FragmentSeeLaterBinding

    private val mUrlSeeLater = "seelaters"
    private val mUrlAnime = "animes"
    private val mUrlEpisodes = "episodes"

    private lateinit var mSeeLaterViewModel:SeeLaterViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentSeeLaterBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
    }

    private fun setupViewModel() {
        mSeeLaterViewModel = ViewModelProvider(requireActivity())[
                SeeLaterViewModel::class.java
        ]
        mSeeLaterViewModel.getFirebaseAdapterAnime().observe(viewLifecycleOwner){
            setupRecycleViewAnime()
        }
        mSeeLaterViewModel.getFirebaseAdapterEpisode().observe(viewLifecycleOwner){
            setupRecycleViewEpisodes()
        }

    }

    private fun setupRecycleViewEpisodes() {


        val manager = LinearLayoutManager(context)

        mBinding.rvEpisodesSave.apply {
            layoutManager = manager
            adapter = mSeeLaterViewModel.getFirebaseAdapterEpisode().value
        }

    }

    override fun onStart() {
        super.onStart()
        mSeeLaterViewModel.getFirebaseAdapterAnime().value!!.startListening()
        mSeeLaterViewModel.getFirebaseAdapterEpisode().value!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        mSeeLaterViewModel.getFirebaseAdapterAnime().value!!.startListening()
        mSeeLaterViewModel.getFirebaseAdapterEpisode().value!!.stopListening()
    }
    private fun openPlayerActivity(episode: Episode){
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("url",episode.urlVideo)
        startActivity(intent)

    }

    private fun setupRecycleViewAnime() {

        val manager = GridLayoutManager(context,2)

        mBinding.rvAnimeSave.apply {
            layoutManager = manager
            adapter = mSeeLaterViewModel.getFirebaseAdapterAnime().value
        }

    }

    private fun deleteAnimeSeeLater(anime: Anime) {
        val keyUser = FirebaseAuth.getInstance().currentUser!!.uid
        anime.id?.let {
            FirebaseDatabase.getInstance()
                .reference
                .child(mUrlSeeLater)
                .child(keyUser)
                .child(mUrlAnime)
                .child(anime.id.toString())
                .removeValue()
        }
    }

    private fun deleteEpisodeSeeLater(episode: Episode) {
        val keyUser = FirebaseAuth.getInstance().currentUser!!.uid
        episode.id?.let {
            FirebaseDatabase.getInstance()
                .reference
                .child(mUrlSeeLater)
                .child(keyUser)
                .child(mUrlEpisodes)
                .child(episode.id.toString())
                .removeValue()
        }
    }

    private fun openDetailsAnime(anime: Anime){
        val intent = Intent(context, AnimeDetailsActivity::class.java)
        intent.putExtra("anime",anime)
        startActivity(intent)
    }

    inner class AnimeHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = ItemCardAnimeBinding.bind(view)

        fun deleteAnimeSee(anime: Anime){
            deleteAnimeSeeLater(anime)
        }
    }

    inner class EpisodeHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = ItemCardEpisodeBinding.bind(view)
        fun deleteEpisodeSee(episode: Episode){
            deleteEpisodeSeeLater(episode)
        }
        fun openPlayer(episode: Episode){
            openPlayerActivity(episode)
        }
    }
}