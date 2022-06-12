package com.example.anijuan.modules.moduleSettings.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.anijuan.modules.moduleSettings.model.SettingsInteractor
import java.util.ArrayList

class SettingsViewModel: ViewModel() {

    var mListProviders:MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    private val interactor = SettingsInteractor()

    init {
        loadProviders()
    }

    fun setListProviders(listProviders:ArrayList<String>){
        mListProviders.value = listProviders
    }

    fun getListProviders() = mListProviders

    fun loadProviders(){
        interactor.getProviders {
            mListProviders.value = it
        }
    }
}