package com.example.anijuan.modules.moduleMain.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anijuan.R
import com.example.anijuan.modules.modulePlayer.PlayerActivity
import com.example.anijuan.databinding.FragmentHomeBinding
import com.example.anijuan.databinding.ItemCardEpisodeBinding
import com.example.anijuan.common.entities.Anime
import com.example.anijuan.common.entities.Episode
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class HomeFragment : Fragment() {

    //global vars
    private var mUrlEpisodes = "episodes"
    private val mUrlAnimes = "animes"
    private val mUrlSeeLater = "seelaters"

    //view binding
    private lateinit var mBinding:FragmentHomeBinding

    //firebase
    private lateinit var mFirebaseAdapter:FirebaseRecyclerAdapter<Episode,EpisodeHolder>
    private lateinit var mLinearManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val query = FirebaseDatabase.getInstance().reference.child(mUrlEpisodes)

        val options = FirebaseRecyclerOptions.Builder<Episode>()
            .setQuery(query){
                val episode = it.getValue(Episode::class.java)
                episode!!.id = it.key
                episode
            }.build()

        mFirebaseAdapter = object: FirebaseRecyclerAdapter<Episode,EpisodeHolder>(options){
            private lateinit var mContext:Context

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeHolder {
                mContext = parent.context

                val view = LayoutInflater.from(mContext).inflate(R.layout.item_card_episode,parent,false)
                return EpisodeHolder(view)
            }

            @SuppressLint("NotifyDataSetChanged") // bug interno en firebase 8.0.0
            override fun onDataChanged() {
                super.onDataChanged()
                notifyDataSetChanged()
                mBinding.progressBar.visibility = View.GONE
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onBindViewHolder(holder: EpisodeHolder, position: Int, model: Episode) {
                val episode = getItem(position)
                var anime:Anime? = null
                for (ani in episode.anime){
                    anime = ani.value
                }
                with(holder){
                    binding.tvNameEpisode.text = episode.name
                    binding.tvDescriptionEpisode.text = episode.description
                    binding.tvNumberEpisode.text = episode.episode.toString()
                    binding.tvCounterLikes.text = episode.listLike.keys.size.toString()

                    anime?.let {
                        binding.tvSubNameAnime.text = it.name
                    }

                    binding.cbEpisodeLike.isChecked = episode.listLike.containsKey(
                        FirebaseAuth.getInstance().currentUser!!.uid
                    )

                    val stars:Int? = episode.listStars.get(
                        FirebaseAuth.getInstance().currentUser!!.uid
                    )
                    stars?.let {
                        getStarsEpisode(it)
                    }

                    Glide.with(mContext)
                        .load(episode.photoUrl)
                        .centerCrop()
                        .circleCrop()
                        .into(binding.ivPhotoEpisode)

                    binding.root.setOnClickListener {
                        openVideoListener(episode)
                    }
                    binding.root.setOnLongClickListener {

                        MaterialAlertDialogBuilder(mContext)
                            .setTitle(R.string.add_later_title)
                            .setPositiveButton(R.string.btn_confirm_dialog){_,_ ->
                                saveEpisodeSeeLater(episode)
                            }
                            .setNegativeButton(R.string.btn_cancel_dialog,null)
                            .show()

                        saveEpisodeSeeLater(episode)
                        true
                    }
                    binding.cbEpisodeLike.setOnCheckedChangeListener { _, checked ->
                        setLikeEpisode(episode,checked)
                    }

                    binding.cbStart1.setOnClickListener {
                        val checked = binding.cbStart1.isChecked
                        if (checked) setStarsEpisode(episode,1)
                        else setStarsEpisode(episode,6)
                    }
                    binding.cbStart2.setOnClickListener {
                        val checked = binding.cbStart2.isChecked
                        if (checked) setStarsEpisode(episode,2)
                        else setStarsEpisode(episode,6)
                    }
                    binding.cbStart3.setOnClickListener {
                        val checked = binding.cbStart3.isChecked
                        if (checked) setStarsEpisode(episode,3)
                        else setStarsEpisode(episode,6)
                    }
                    binding.cbStart4.setOnClickListener {
                        val checked = binding.cbStart4.isChecked
                        if (checked) setStarsEpisode(episode,4)
                        else setStarsEpisode(episode,6)
                    }
                    binding.cbStart5.setOnClickListener {
                        val checked = binding.cbStart5.isChecked
                        if (checked) setStarsEpisode(episode,5)
                        else setStarsEpisode(episode,6)
                    }


                }//end with holder
            }//end function
        }//end mFirebaseAdapter
        mLinearManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true)

        mBinding.rvEpisodes.apply {
            setHasFixedSize(true)
            layoutManager = mLinearManager
            adapter = mFirebaseAdapter
        }
    }


    override fun onStart() {
        super.onStart()
        mFirebaseAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAdapter.stopListening()
    }

    private fun startVideo(url:String){
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("url",url)
        startActivity(intent)

    }
    fun saveEpisodeSeeLaterFirebase(episode: Episode){

        val keyUser = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance()
            .reference.child(mUrlSeeLater)
            .child(keyUser)
            .child(mUrlEpisodes)
            .child(episode.id.toString())
            .setValue(episode)
    }
    fun setLikeEpisodes(episode: Episode,checked: Boolean){
        var anime:Anime? = null
        for(ani in episode.anime){
            anime = ani.value
            anime.id = ani.key
        }
        anime?.let {
            if(checked){
                //episodes references
                val databaseReferenceEpisodes = FirebaseDatabase.getInstance().reference.child(mUrlEpisodes)
                    .child(episode.id.toString())
                    .child("listLike")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(checked)
                //anime references
                val databaseReference = FirebaseDatabase.getInstance().reference.child(mUrlAnimes)
                    .child(anime.id.toString())
                    .child(mUrlEpisodes).child(episode.id!!)
                    .child("listLike")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(checked)

            }else {

                //episodes references
                val databaseReferenceEpisodes = FirebaseDatabase.getInstance().reference.child(mUrlEpisodes)
                    .child(episode.id.toString())
                    .child("listLike")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(null)

                //anime reference
                val databaseReference = FirebaseDatabase.getInstance().reference.child(mUrlAnimes)
                    .child(anime.id.toString())
                    .child(mUrlEpisodes)
                    .child(episode.id!!)
                    .child("listLike")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(null)
            }
        }
    }

    fun setStarsEpisodes(episode: Episode,stars: Int){
        var anime:Anime? = null
        for(ani in episode.anime){
            anime = ani.value
            anime.id = ani.key
        }
        anime?.let {
            //episodes references
            val databaseReferenceEpisodes = FirebaseDatabase.getInstance().reference.child(mUrlEpisodes)
                .child(episode.id.toString())
                .child("listStars")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(stars)

            //anime references
            val databaseReference = FirebaseDatabase.getInstance().reference.child(mUrlAnimes)
                .child(anime.id.toString())
                .child(mUrlEpisodes).child(episode.id!!)
                .child("listStars")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(stars)
        }
    }

    inner class EpisodeHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = ItemCardEpisodeBinding.bind(view)

        fun setLikeEpisode(episode: Episode,checked:Boolean){
            setLikeEpisodes(episode,checked)
        }

        fun setStarsEpisode(episode: Episode,stars:Int){
            setStarsEpisodes(episode,stars)
            getStarsEpisode(stars)
        }

        fun saveEpisodeSeeLater(episode: Episode){
            saveEpisodeSeeLaterFirebase(episode)
        }

        fun openVideoListener(episode: Episode){
            startVideo(episode.urlVideo)
        }

        fun getStarsEpisode(stars: Int){
            when(stars){
                1 -> {
                    binding.cbStart1.isChecked = true
                    binding.cbStart2.isChecked = false
                    binding.cbStart3.isChecked = false
                    binding.cbStart4.isChecked = false
                    binding.cbStart5.isChecked = false
                }
                2 -> {
                    binding.cbStart1.isChecked = true
                    binding.cbStart2.isChecked = true
                    binding.cbStart3.isChecked = false
                    binding.cbStart4.isChecked = false
                    binding.cbStart5.isChecked = false
                }
                3 -> {
                    binding.cbStart1.isChecked = true
                    binding.cbStart2.isChecked = true
                    binding.cbStart3.isChecked = true
                    binding.cbStart4.isChecked = false
                    binding.cbStart5.isChecked = false
                }
                4 -> {
                    binding.cbStart1.isChecked = true
                    binding.cbStart2.isChecked = true
                    binding.cbStart3.isChecked = true
                    binding.cbStart4.isChecked = true
                    binding.cbStart5.isChecked = false
                }
                5 -> {
                    binding.cbStart1.isChecked = true
                    binding.cbStart2.isChecked = true
                    binding.cbStart3.isChecked = true
                    binding.cbStart4.isChecked = true
                    binding.cbStart5.isChecked = true
                }
                6 -> {
                    binding.cbStart1.isChecked = false
                    binding.cbStart2.isChecked = false
                    binding.cbStart3.isChecked = false
                    binding.cbStart4.isChecked = false
                    binding.cbStart5.isChecked = false
                }
            }
        }
    }
}