package com.example.anijuan.modules.moduleAnime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anijuan.databinding.FragmentAnimeBinding
import com.example.anijuan.databinding.ItemCardAnimeBinding
import com.example.anijuan.modules.moduleAnime.viewModel.AnimeViewModel

class AnimeFragment : Fragment() {
    private lateinit var mBinding:FragmentAnimeBinding

    private lateinit var mLayoutManager:RecyclerView.LayoutManager

    private lateinit var mAnimeViewModel:AnimeViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentAnimeBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
    }

    private fun setupViewModel() {
        mAnimeViewModel = ViewModelProvider(requireActivity())[AnimeViewModel::class.java]
        mAnimeViewModel.getFirebaseAdapter().observe(viewLifecycleOwner){
            setupRecycleView()
        }
    }

    override fun onStart() {
        super.onStart()
        mAnimeViewModel.getFirebaseAdapter().value!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAnimeViewModel.getFirebaseAdapter().value!!.stopListening()
    }

    private fun setupRecycleView() {
        mLayoutManager = GridLayoutManager(context,2)
        mBinding.rvAnime.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = mAnimeViewModel.getFirebaseAdapter().value
        }
    }

    inner class AnimeHolder(view:View):RecyclerView.ViewHolder(view){
        val binding = ItemCardAnimeBinding.bind(view)
    }

}