package com.example.anijuan.modules.moduleSearchAnime.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.anijuan.common.entities.Anime
import com.example.anijuan.modules.moduleSearchAnime.model.SearchAnimeInteractor

class SearchAnimeViewModel: ViewModel() {

    private val mListAnime:MutableLiveData<MutableList<Anime>> = MutableLiveData<MutableList<Anime>>()
    private val interactor = SearchAnimeInteractor()

    init {
        loadAnime()
    }

    fun getListAnime() = mListAnime

    fun setListAnime(listAnime: MutableList<Anime>){
        mListAnime.value = listAnime
    }

    private fun loadAnime(){
        interactor.getAnimes {
            mListAnime.value = it
        }
    }
}