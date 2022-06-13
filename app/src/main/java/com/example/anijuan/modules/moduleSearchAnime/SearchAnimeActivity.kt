package com.example.anijuan.modules.moduleSearchAnime

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.anijuan.R
import com.example.anijuan.databinding.ActivitySearchAnimeBinding
import com.example.anijuan.common.entities.Anime
import com.example.anijuan.modules.moduleAnimeDetails.AnimeDetailsActivity
import com.example.anijuan.modules.moduleSearchAnime.adapter.AnimeAdapter
import com.example.anijuan.modules.moduleSearchAnime.interfaces.SearchAnimeAux
import com.example.anijuan.modules.moduleSearchAnime.viewModel.SearchAnimeViewModel
import com.google.firebase.database.*


class SearchAnimeActivity : AppCompatActivity(), SearchAnimeAux {

    private lateinit var mBinding: ActivitySearchAnimeBinding

    private lateinit var mSearchAnimeViewModel:SearchAnimeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySearchAnimeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        setupEditText()
        setupButtons()
        setupViewModel()
    }

    private fun setupViewModel() {
        mSearchAnimeViewModel = ViewModelProvider(this)[
                SearchAnimeViewModel::class.java
        ]

    }

    private fun setupButtons() {
        mBinding.ibBackPressed.setOnClickListener {
            finish()
        }
    }

    private fun setupEditText() {
        mBinding.tilSearchAnime.requestFocus()
        mBinding.etSearchAnime.setOnFocusChangeListener { view, focus ->
            if (!focus)
                mSearchAnimeViewModel.getListAnime().value?.let { listAnime ->
                    filterAnime(listAnime)
                }
        }
        mBinding.etSearchAnime.addTextChangedListener{
            if(it.toString().length >= 3){
                mBinding.tvHelper.visibility = View.GONE
                mSearchAnimeViewModel.getListAnime().value?.let { listAnime ->
                    filterAnime(listAnime)
                }
            }else{
                mBinding.tvHelper.visibility = View.VISIBLE
                mBinding.tvHelper.text = getString(R.string.helper_title_search)
                mBinding.rvSearchAnime.adapter = null
            }
        }
    }

    private fun filterAnime(listAnime:MutableList<Anime>){
        listAnime.removeIf { obj:Anime? -> obj == null  }
        val listAnimeFind = mutableListOf<Anime>()

        val filterKey = mBinding.etSearchAnime.text.toString().lowercase()

        for(anime in listAnime){
            val nameAnime = anime.name?.lowercase()

            if (nameAnime != null) {
                if(filterKey in nameAnime){
                    listAnimeFind.add(anime)
                }
            }
        }

        listAnimeFind.removeIf { obj:Anime? -> obj == null  }
        if(listAnimeFind.isEmpty() && mBinding.etSearchAnime.text.toString().length >= 3){
            mBinding.tvHelper.visibility = View.VISIBLE
            mBinding.tvHelper.text = getString(R.string.without_result_title)
            mBinding.rvSearchAnime.adapter = null
        }else{
            val manager = GridLayoutManager(this,2)
            val animeAdapter = AnimeAdapter(listAnimeFind,this)
            mBinding.rvSearchAnime.adapter = null
            mBinding.rvSearchAnime.apply {
                layoutManager = manager
                adapter = animeAdapter
            }
        }
    }

    override fun finishActivity() {
        finish()
    }


}