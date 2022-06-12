package com.example.anijuan.modules.moduleAnime.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.anijuan.R
import com.example.anijuan.common.entities.Anime
import com.example.anijuan.modules.moduleAnime.AnimeFragment
import com.example.anijuan.modules.moduleAnime.model.AnimeInteractor
import com.example.anijuan.modules.moduleAnimeDetails.AnimeDetailsActivity
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AnimeViewModel: ViewModel() {
    private val interactor = AnimeInteractor()

    var firebaseAdapter:MutableLiveData<FirebaseRecyclerAdapter<Anime,AnimeFragment.AnimeHolder>> =
        MutableLiveData<FirebaseRecyclerAdapter<Anime,AnimeFragment.AnimeHolder>>()

    init {
        loadFirebase()
    }

    fun getFirebaseAdapter():LiveData<FirebaseRecyclerAdapter<Anime,AnimeFragment.AnimeHolder>> = firebaseAdapter

    fun loadFirebase(){
        interactor.getAnimes {  query ->
            val options = FirebaseRecyclerOptions.Builder<Anime>().setQuery(query){
                val anime = it.getValue(Anime::class.java)
                anime!!.id = it.key
                anime
            }.build()

            firebaseAdapter.value = object : FirebaseRecyclerAdapter<Anime, AnimeFragment.AnimeHolder>(options){
                private lateinit var mContext: Context

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeFragment.AnimeHolder {
                    mContext = parent.context
                    val view = LayoutInflater.from(mContext).inflate(R.layout.item_card_anime,parent,false)
                    return AnimeFragment().AnimeHolder(view)
                }

                @SuppressLint("NotifyDataSetChanged") // bug interno firebase 8.0.0
                override fun onDataChanged() {
                    super.onDataChanged()
                    notifyDataSetChanged()
                }
                override fun onBindViewHolder(holder: AnimeFragment.AnimeHolder, position: Int, model: Anime) {
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
                            val intent = Intent(mContext, AnimeDetailsActivity::class.java)
                            intent.putExtra("anime",anime)
                            mContext.startActivity(intent)
                        }

                        binding.root.setOnLongClickListener {
                            mContext.let { it1 ->
                                MaterialAlertDialogBuilder(it1)
                                    .setTitle(mContext.getString(R.string.add_later_title))
                                    .setPositiveButton(R.string.btn_confirm_dialog){ _, _ ->
                                        interactor.saveAnimeSeeLater(anime){}
                                    }
                                    .setNegativeButton(R.string.btn_cancel_dialog,null)
                                    .show()
                            }
                            true
                        }
                    }
                }
            }
        }
    }
}