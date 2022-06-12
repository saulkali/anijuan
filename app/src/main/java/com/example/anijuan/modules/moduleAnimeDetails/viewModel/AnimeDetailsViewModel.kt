package com.example.anijuan.modules.moduleAnimeDetails.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.anijuan.common.entities.Anime
import com.example.anijuan.modules.moduleAnimeDetails.model.AnimeDetailsInteractor

class AnimeDetailsViewModel:ViewModel() {

    lateinit var mAnimeSelected: MutableLiveData<Anime>

    private val interactor = AnimeDetailsInteractor()

    fun setAnimeSelected(anime: Anime){
        mAnimeSelected.value = anime
    }

    fun getAnimeSelected():LiveData<Anime> = mAnimeSelected
}