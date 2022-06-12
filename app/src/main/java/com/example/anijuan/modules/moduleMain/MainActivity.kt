package com.example.anijuan.modules.moduleMain

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.anijuan.R
import com.example.anijuan.modules.moduleSearchAnime.SearchAnimeActivity
import com.example.anijuan.modules.moduleSettings.SettingsActivity
import com.example.anijuan.databinding.ActivityMainBinding
import com.example.anijuan.modules.moduleAnime.AnimeFragment
import com.example.anijuan.modules.moduleEpisode.EpisodeFragment
import com.example.anijuan.modules.moduleIssue.IssueFragment
import com.example.anijuan.modules.moduleSeeLater.SeeLaterFragment
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

        setupAppBar()
        setupAuth()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null)
            setupBottomNav()

    }

    private fun setupAppBar() {
        mBinding.tbMain.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.btnActionLogOut -> {

                    mFireBaseAuth?.signOut()
                    finish()
                    true
                }
                R.id.btnActionSearch -> {
                    openSearchActivity()
                    true
                }
                R.id.btnActionSettings -> {
                    openSettingsActivity()
                    true
                }
                else -> true
            }
        }
    }

    private fun openSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun openSearchActivity(){
        val intent = Intent(this, SearchAnimeActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_app_bar,menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupBottomNav() {
        val homeFragment = EpisodeFragment()
        val animeFragment = AnimeFragment()
        val seeLaterFragment = SeeLaterFragment()
        val issueFragment = IssueFragment()

        mActiveFragment = homeFragment

        mFragmentManager = supportFragmentManager

        //home fragment add
        mFragmentManager.beginTransaction()
            .add(R.id.frameLayoutManager,mActiveFragment, EpisodeFragment::class.java.name)
            .show(mActiveFragment)
            .commit()

        //anime fragment add
        mFragmentManager.beginTransaction()
            .add(R.id.frameLayoutManager,animeFragment, AnimeFragment::class.java.name)
            .hide(animeFragment)
            .commit()

        //see later fragment add
        mFragmentManager.beginTransaction()
            .add(R.id.frameLayoutManager,seeLaterFragment, SeeLaterFragment::class.java.name)
            .hide(seeLaterFragment)
            .commit()

        // issue fragment add
        mFragmentManager.beginTransaction()
            .add(R.id.frameLayoutManager,issueFragment, IssueFragment::class.java.name)
            .hide(issueFragment)
            .commit()

        mBinding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.btnActionHome -> selectFragment(mActiveFragment,homeFragment)
                R.id.btnActionAnime -> selectFragment(mActiveFragment,animeFragment)
                R.id.btnActionSeeLatter -> selectFragment(mActiveFragment,seeLaterFragment)
                R.id.btnActionIssue -> selectFragment(mActiveFragment,issueFragment)
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
        if(it.resultCode != Activity.RESULT_OK){
            finish()
        }else{
            Toast.makeText(this, getString(R.string.login_welcome_title), Toast.LENGTH_SHORT).show()
            setupBottomNav()
        }
    }


    private fun setupAuth() {
        mFireBaseAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if (user == null) {
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