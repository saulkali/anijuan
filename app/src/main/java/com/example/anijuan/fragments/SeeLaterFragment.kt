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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anijuan.R
import com.example.anijuan.activitys.AnimeDetailsActivity
import com.example.anijuan.databinding.FragmentSeeLaterBinding
import com.example.anijuan.databinding.ItemCardAnimeBinding
import com.example.anijuan.entitys.Anime
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase

class SeeLaterFragment : Fragment() {


    private lateinit var mBinding:FragmentSeeLaterBinding

    private val mUrlSeeLater = "seelaters"

    private lateinit var mFirebaseAdapter:FirebaseRecyclerAdapter<Anime,AnimeHolder>


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

        setupFirebase()
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

        val query = FirebaseDatabase.getInstance().reference.child(mUrlSeeLater)
        val options = FirebaseRecyclerOptions.Builder<Anime>().setQuery(query) {
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
                        openDetailsAnime(anime)
                    }
                    binding.root.setOnLongClickListener {
                        context?.let { it1 ->
                            MaterialAlertDialogBuilder(it1)
                                .setTitle(getString(R.string.delete_later_title))
                                .setPositiveButton(R.string.btn_confirm_dialog){ _,_ ->
                                    deleteAnimeSee(anime)
                                }
                                .setNegativeButton(R.string.btn_cancel_dialog,null)
                                .show()
                        }
                        true 
                    }
                }

            }

            @SuppressLint("NotifyDataSetChanged") // ugs
            override fun onDataChanged() {
                super.onDataChanged()
                notifyDataSetChanged()
            }

        }

        val manager = GridLayoutManager(context,2)

        mBinding.rvAnimeSave.apply {
            layoutManager = manager
            adapter = mFirebaseAdapter
        }


    }

    private fun deleteAnimeSeeLater(anime: Anime) {
        anime.id?.let {
            FirebaseDatabase.getInstance()
                .reference
                .child(mUrlSeeLater).child(anime.id.toString())
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


}