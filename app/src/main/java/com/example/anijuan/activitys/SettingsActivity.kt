package com.example.anijuan.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.anijuan.R
import com.example.anijuan.databinding.ActivitySettingsBinding
import com.example.anijuan.entitys.Providers
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class SettingsActivity : AppCompatActivity() {

    private lateinit var mBinding:ActivitySettingsBinding
    private val mUrlProviders = "providers"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        getSpinnerProviders()

        setupAppBar()
    }

    private fun getSpinnerProviders() {
        val providersReference = FirebaseDatabase.getInstance().reference.child(mUrlProviders)
        val listProviders = arrayListOf<String>()

        val eventListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(provider in snapshot.children){
                    provider.getValue(Providers::class.java)?.let {
                        listProviders.add(it.name)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        providersReference.addListenerForSingleValueEvent(eventListener)
        setSpinnerProviders(listProviders)
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