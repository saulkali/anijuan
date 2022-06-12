package com.example.anijuan.modules.moduleSettings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.anijuan.databinding.ActivitySettingsBinding
import com.example.anijuan.modules.moduleSettings.viewModel.SettingsViewModel
import java.util.ArrayList

class SettingsActivity : AppCompatActivity() {

    private lateinit var mBinding:ActivitySettingsBinding
    private lateinit var mSettingsViewModel:SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupAppBar()

        setupViewModel()
    }

    private fun setupViewModel() {
        mSettingsViewModel = ViewModelProvider(this)[
                SettingsViewModel::class.java
        ]
        mSettingsViewModel.getListProviders().observe(this){ listProviders ->
            setSpinnerProviders(listProviders)
        }
    }

    fun setSpinnerProviders(listProviders:ArrayList<String>){
        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,listProviders)
        mBinding.snIdServerVideo.adapter = arrayAdapter
    }

    private fun setupAppBar() {
        setSupportActionBar(mBinding.tbSettings)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        mBinding.tbSettings.setNavigationOnClickListener {
            finish()
        }
    }
}