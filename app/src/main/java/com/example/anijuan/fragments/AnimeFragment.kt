package com.example.anijuan.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anijuan.R
import com.example.anijuan.activitys.AnimeDetailsActivity
import com.example.anijuan.databinding.FragmentAnimeBinding
import com.example.anijuan.databinding.ItemCardAnimeBinding
import com.example.anijuan.entitys.Anime
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AnimeFragment : Fragment() {
    private lateinit var mBinding:FragmentAnimeBinding

    private lateinit var mFirebaseAdapter:FirebaseRecyclerAdapter<Anime,AnimeHolder>
    private lateinit var mLayoutManager:RecyclerView.LayoutManager

    private val mUrlSeeLater = "seelaters"
    private val mUrlAnime = "animes"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        val query = FirebaseDatabase.getInstance().reference.child(mUrlAnime)
        val options = FirebaseRecyclerOptions.Builder<Anime>().setQuery(query){
            val anime = it.getValue(Anime::class.java)
            anime!!.id = it.key
            anime
        }.build()

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

                    binding.root.setOnClickListener {
                        openDetailsAnimeActivity(anime)
                    }

                    binding.root.setOnLongClickListener {
                        context?.let { it1 ->
                            MaterialAlertDialogBuilder(it1)
                                .setTitle(getString(R.string.add_later_title))
                                .setPositiveButton(R.string.btn_confirm_dialog){ _,_ ->
                                    saveAnimeSeeLater(anime)
                                }
                                .setNegativeButton(R.string.btn_cancel_dialog,null)
                                .show()
                        }
                        true
                    }

                }
            }

        }

        mLayoutManager = GridLayoutManager(context,2)
        mBinding.rvAnime.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = mFirebaseAdapter
        }
    }

    private fun saveAnimeSeeLater(anime: Anime) {
        // save anime see later
        val key = FirebaseDatabase.getInstance().reference.child(mUrlSeeLater).push().key!!
        val keyUser = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().reference.child(mUrlSeeLater).child(keyUser).child(mUrlAnime).child(key).setValue(anime)
    }

    private fun openDetailsAnime(anime: Anime){
        val intent = Intent(context,AnimeDetailsActivity::class.java)
        intent.putExtra("anime",anime)
        startActivity(intent)
    }

    inner class AnimeHolder(view:View):RecyclerView.ViewHolder(view){
        val binding = ItemCardAnimeBinding.bind(view)


        fun openDetailsAnimeActivity(anime:Anime){
            openDetailsAnime(anime)
        }
    }

}