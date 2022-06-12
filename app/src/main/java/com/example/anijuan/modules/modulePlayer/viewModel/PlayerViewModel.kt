package com.example.anijuan.modules.modulePlayer.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.anijuan.modules.modulePlayer.model.PlayerInteractor

class PlayerViewModel: ViewModel() {
    private val interactor = PlayerInteractor()

    private var url:MutableLiveData<String> = MutableLiveData<String>()

    fun setUrl(url:String){
        this.url.value = url
    }

    fun getUrl() = url
}