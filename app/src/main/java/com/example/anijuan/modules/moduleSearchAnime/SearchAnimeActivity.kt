package com.example.anijuan.modules.moduleSearchAnime

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.example.anijuan.R
import com.example.anijuan.databinding.ActivitySearchAnimeBinding
import com.example.anijuan.common.entities.Anime
import com.example.anijuan.modules.moduleAnimeDetails.AnimeDetailsActivity
import com.example.anijuan.modules.moduleSearchAnime.adapter.AnimeAdapter
import com.example.anijuan.modules.moduleSearchAnime.interfaces.SearchAnimeAux
import com.google.firebase.database.*


class SearchAnimeActivity : AppCompatActivity(), SearchAnimeAux {

    private lateinit var mBinding: ActivitySearchAnimeBinding

    private val mListAnime:MutableList<Anime> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySearchAnimeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        getAnimeLocal()
        setupEditText()
        setupButtons()
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
                filterAnime(mListAnime)
        }
        mBinding.etSearchAnime.addTextChangedListener{
            if(it.toString().length >= 3){
                mBinding.tvHelper.visibility = View.GONE
                filterAnime(mListAnime)
            }else{
                mBinding.tvHelper.visibility = View.VISIBLE
                mBinding.tvHelper.text = getString(R.string.helper_title_search)
                mBinding.rvSearchAnime.adapter = null
            }
        }
    }
    private fun getAnimeLocal() {
        val query = FirebaseDatabase.getInstance()
            .reference
            .child("animes")

        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    for (anime in snapshot.children){
                        val encodeAnime:Anime? = anime.getValue(Anime::class.java)

                        if (encodeAnime != null)
                            mListAnime.add(encodeAnime)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


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

    override fun onStart() {
        super.onStart()
        filterAnime(mListAnime)
    }


    override fun openAnimeDetails(anime: Anime) {
        finish()
        val intent = Intent(applicationContext, AnimeDetailsActivity::class.java)
        intent.putExtra("anime",anime)
        startActivity(intent)
    }


}