package com.example.anijuan.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anijuan.R
import com.example.anijuan.activitys.PlayerActivity
import com.example.anijuan.databinding.FragmentHomeBinding
import com.example.anijuan.databinding.ItemCardEpisodeBinding
import com.example.anijuan.entitys.Episode
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class HomeFragment : Fragment() {

    //global vars
    private var EPISODES = "episodes"

    //view binding
    private lateinit var mBinding:FragmentHomeBinding

    //firebase
    private lateinit var mFirebaseAdapter:FirebaseRecyclerAdapter<Episode,EpisodeHolder>
    private lateinit var mLinearManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val query = FirebaseDatabase.getInstance().reference.child(EPISODES)

        val options = FirebaseRecyclerOptions.Builder<Episode>()
            .setQuery(query,Episode::class.java).build()

        mFirebaseAdapter = object: FirebaseRecyclerAdapter<Episode,EpisodeHolder>(options){
            private lateinit var mContext:Context

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeHolder {
                mContext = parent.context

                val view = LayoutInflater.from(mContext).inflate(R.layout.item_card_episode,parent,false)
                return EpisodeHolder(view)
            }

            @SuppressLint("NotifyDataSetChanged") // bug en firebase 8.0.0
            override fun onDataChanged() {
                super.onDataChanged()
                notifyDataSetChanged()
                Toast.makeText(context, "datos cargados", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onBindViewHolder(holder: EpisodeHolder, position: Int, model: Episode) {
                val episode = getItem(position)
                with(holder){
                    binding.tvNameEpisode.text = episode.name
                    binding.tvDescriptionEpisode.text = episode.description
                    binding.tvNumberEpisode.text = getString(R.string.episode_subtitle) + episode.episode.toString()

                    Glide.with(mContext)
                        .load(episode.photoUrl)
                        .centerCrop()
                        .into(binding.ivPhotoEpisode)

                    binding.root.setOnClickListener {
                        openVideoListener(episode)
                    }
                }
            }
        }
        mLinearManager = LinearLayoutManager(context)

        mBinding.rvEpisodes.apply {
            setHasFixedSize(true)
            layoutManager = mLinearManager
            adapter = mFirebaseAdapter
        }
    }


    override fun onStart() {
        super.onStart()
        mFirebaseAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAdapter.stopListening()
    }

    private fun startVideo(url:String){
        val intent = Intent(context,PlayerActivity::class.java)
        intent.putExtra("url",url)
        startActivity(intent)

    }

    inner class EpisodeHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = ItemCardEpisodeBinding.bind(view)

        fun setListener(episode:Episode){

        }

        fun openVideoListener(episode: Episode){
            startVideo(episode.urlVideo)
        }
    }

}