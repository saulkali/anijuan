package com.example.anijuan.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anijuan.R
import com.example.anijuan.databinding.FragmentAnimeBinding
import com.example.anijuan.databinding.ItemCardAnimeBinding
import com.example.anijuan.entitys.Anime
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase

class AnimeFragment : Fragment() {
    private lateinit var mBinding:FragmentAnimeBinding

    private lateinit var mFirebaseAdapter:FirebaseRecyclerAdapter<Anime,AnimeHolder>
    private lateinit var mLayoutManager:RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentAnimeBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupFireBase()
    }

    override fun onStart() {
        super.onStart()
        mFirebaseAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAdapter.stopListening()
    }
    private fun setupFireBase() {
        val query = FirebaseDatabase.getInstance().reference.child("animes")
        val options = FirebaseRecyclerOptions.Builder<Anime>().setQuery(query,Anime::class.java).build()

        mFirebaseAdapter = object : FirebaseRecyclerAdapter<Anime,AnimeHolder>(options){
            private lateinit var mContext:Context

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeHolder {
                mContext = parent.context
                val view = LayoutInflater.from(mContext).inflate(R.layout.item_card_anime,parent,false)
                return AnimeHolder(view)
            }

            @SuppressLint("NotifyDataSetChanged") // bug interno firebase 8.0.0
            override fun onDataChanged() {
                super.onDataChanged()
                notifyDataSetChanged()
            }
            override fun onBindViewHolder(holder: AnimeHolder, position: Int, model: Anime) {
                val anime = getItem(position)
                with(holder){
                    binding.tvNameAnime.text = anime.name
                    binding.tvDescriptionAnime.text = anime.description
                    binding.tvSeasonAnime.text = anime.season.toString()

                    Glide.with(mContext)
                        .load(anime.photoUrl)
                        .centerCrop()
                        .into(binding.ivPhotoAnime)
                }
            }

        }

        mLayoutManager = GridLayoutManager(context,2)
        mBinding.rvAnime.apply {
            layoutManager = mLayoutManager
            adapter = mFirebaseAdapter
        }
    }

    inner class AnimeHolder(view:View):RecyclerView.ViewHolder(view){
        val binding = ItemCardAnimeBinding.bind(view)
        fun setListener(anime:Anime){

        }
    }

}