package com.example.anijuan

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.anijuan.databinding.ActivityMainBinding
import com.example.anijuan.fragments.AnimeFragment
import com.example.anijuan.fragments.HomeFragment
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    //viewBinding
    private lateinit var mBinding:ActivityMainBinding

    //firebase
    private lateinit var mAuthListener:FirebaseAuth.AuthStateListener
    private var mFireBaseAuth:FirebaseAuth? = null

    //fragmentManager -> frameLayout
    private lateinit var mActiveFragment: Fragment
    private lateinit var mFragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setupAuth()
        setupBottomNav()
        setupAppBar()
    }

    private fun setupAppBar() {
        mBinding.tbMain.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.btnActionLogOut -> {
                    if (mFireBaseAuth != null) {
                        mFireBaseAuth!!.signOut()
                        finish()
                    }
                    true
                }
                else -> true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_app_bar,menu)
        return super.onCreateOptionsMenu(menu)
    }
    private fun setupBottomNav() {
        val homeFragment = HomeFragment()
        val animeFragment = AnimeFragment()

        mActiveFragment = homeFragment

        mFragmentManager = supportFragmentManager

        mFragmentManager.beginTransaction()
            .add(R.id.frameLayoutManager,mActiveFragment,HomeFragment::class.java.name)
            .show(mActiveFragment)
            .commit()

        mFragmentManager.beginTransaction()
            .add(R.id.frameLayoutManager,animeFragment,AnimeFragment::class.java.name)
            .hide(animeFragment)
            .commit()

        mBinding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.btnActionHome -> selectFragment(mActiveFragment,homeFragment)
                R.id.btnActionAnime -> selectFragment(mActiveFragment,animeFragment)
                else -> true
            }
        }
    }

    private fun selectFragment(oldFragment:Fragment,newFragment:Fragment):Boolean{
        mFragmentManager.beginTransaction().hide(oldFragment).show(newFragment).commit()
        mActiveFragment = newFragment
        return true
    }

    val auth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            Toast.makeText(this, getString(R.string.login_welcome_title), Toast.LENGTH_SHORT).show()
        }
        else{
            finish()
        }
    }

    private fun setupAuth() {
        mFireBaseAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {

            val user = it.currentUser

            if(user == null) {
                val intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setAvailableProviders(
                        listOf(
                            AuthUI.IdpConfig.EmailBuilder().build(),
                            AuthUI.IdpConfig.GoogleBuilder().build()
                        )
                    ).build()
                auth.launch(intent)
            }
        }

        mFireBaseAuth?.addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        mFireBaseAuth?.removeAuthStateListener(mAuthListener)
    }

    override fun onResume() {
        super.onResume()
        mFireBaseAuth?.addAuthStateListener(mAuthListener)
    }

}