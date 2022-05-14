package com.example.anijuan.activitys

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.example.anijuan.R
import com.example.anijuan.databinding.ActivityPlayerBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PlayerActivity : AppCompatActivity() {

    private lateinit var mBinding:ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPlayerBinding.inflate(layoutInflater)


        setupActivity()
        setContentView(mBinding.root)

        getUrl()

    }

    private fun getUrl() {
        val url = intent.extras?.getString("url").toString()
        openVideo(url)
    }

    private fun setupActivity() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event?.action == KeyEvent.ACTION_DOWN){
            when(keyCode){
                KeyEvent.KEYCODE_BACK -> {
                    if(mBinding.wvVideo.canGoBack()) mBinding.wvVideo.goBack()
                    else {
                        MaterialAlertDialogBuilder(this)
                            .setTitle(getString(R.string.close_video_confirm))
                            .setPositiveButton(R.string.btn_confirm_dialog){ _, _ ->
                                finish()
                            }.setNegativeButton(R.string.btn_cancel_dialog,null)
                            .show()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun openVideo(url:String) {
        val frameVideo = "<html><body style='margin:0px; padding:0px;'> <iframe style='width:100%; height:100%;' src='$url' frameborder='0' allowfullscreen></iframe></body></html>"

        mBinding.wvVideo.webChromeClient = WebChromeClient()
        mBinding.wvVideo.webViewClient = WebViewClient()
        mBinding.wvVideo.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

        //settings webView
        mBinding.wvVideo.settings.javaScriptEnabled = true
        mBinding.wvVideo.settings.setSupportMultipleWindows(false)
        mBinding.wvVideo.settings.javaScriptCanOpenWindowsAutomatically = false
        mBinding.wvVideo.settings.useWideViewPort = true
        mBinding.wvVideo.settings.loadWithOverviewMode = true
        mBinding.wvVideo.settings.textZoom = 100
        mBinding.wvVideo.clearCache(true)
        mBinding.wvVideo.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

        mBinding.wvVideo.loadData(frameVideo,"text/html","utf-8")
    }
}