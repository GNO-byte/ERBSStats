package com.gno.erbs.erbs.stats

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screeen)

        (application as MainApplication).isInitialized.observe(this) {
            if (it) {
                val intent = Intent(application, MainActivity::class.java)
                //finish()
                startActivity(intent)
            }
        }
    }
}



