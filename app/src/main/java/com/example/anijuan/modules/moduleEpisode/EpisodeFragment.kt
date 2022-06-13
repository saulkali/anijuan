package com.example.anijuan.modules.moduleEpisode

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anijuan.R
import com.example.anijuan.modules.modulePlayer.PlayerActivity
import com.example.anijuan.databinding.FragmentHomeBinding
import com.example.anijuan.databinding.ItemCardEpisodeBinding
import com.example.anijuan.common.entities.Anime
import com.example.anijuan.common.entities.Episode
import com.example.anijuan.modules.moduleEpisode.viewModel.EpisodeViewModel
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class EpisodeFragment : Fragment() {

    private lateinit var mBinding:FragmentHomeBinding

    private lateinit var mLinearManager: LinearLayoutManager

    private lateinit var mEpisodeViewModel:EpisodeViewModel

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

        setupViewModel()
    }

    private fun setupViewModel() {
        mEpisodeViewModel = ViewModelProvider(requireActivity())[
                EpisodeViewModel::class.java
        ]
        mEpisodeViewModel.getFirebaseAdapter().observe(viewLifecycleOwner){
            setupRecycleView()
        }
        mEpisodeViewModel.getProgressBarVisibility().observe(viewLifecycleOwner){ visibility ->
            showProgressBar(visibility)
        }
    }

    private fun setupRecycleView() {
        mLinearManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true)

        mBinding.rvEpisodes.apply {
            setHasFixedSize(true)
            layoutManager = mLinearManager
            adapter = mEpisodeViewModel.getFirebaseAdapter().value
        }
    }

    override fun onStart() {
        super.onStart()
        mEpisodeViewModel.getFirebaseAdapter().value!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        mEpisodeViewModel.getFirebaseAdapter().value!!.stopListening()
    }


    fun showProgressBar(isVisibility:Boolean){
        if(isVisibility)
            mBinding.progressBar.visibility = View.VISIBLE
        else
            mBinding.progressBar.visibility = View.GONE

    }

    inner class EpisodeHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = ItemCardEpisodeBinding.bind(view)

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