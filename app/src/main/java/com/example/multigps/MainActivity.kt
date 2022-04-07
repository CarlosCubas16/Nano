package com.example.multigps

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        session()
    }

    private fun session()
    {
        val handler = Handler()
        handler.postDelayed({
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
            val email = prefs.getString("email", null)
            val provider = prefs.getString("provider", null)

            if (email != null && provider != null)
            {
                showHome(email ?: "", ProviderType.valueOf(provider))
            }else{
                isFirstTime
            }
        }, 1500)
    }

    private val isFirstTime: Unit
        private get() {
            val preferences = application.getSharedPreferences("onBoard", MODE_PRIVATE)
            val isFirstTime = preferences.getBoolean("isFirstTime", true)
            if (isFirstTime) {
                val editor = preferences.edit()
                editor.putBoolean("isFirstTime", false)
                editor.apply()
                val i = Intent(this@MainActivity, OnBoardActivity::class.java)
                startActivity(i)
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
                finish()
            } else {
                val i = Intent(this@MainActivity, AuthActivity::class.java).apply {
                    putExtra("localize", "login")
                }
                startActivity(i)
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
                finish()
            }
        }

    private fun showHome(email: String, provider: ProviderType)
    {
        val homeIntent = Intent(this@MainActivity, HomeActivity::class.java).apply {
            putExtra("email",email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        finish()
    }

    public override fun onResume() {
        super.onResume()
        window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }
}