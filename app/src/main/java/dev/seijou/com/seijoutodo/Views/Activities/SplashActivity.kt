package dev.seijou.com.seijoutodo.Views.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import dev.seijou.com.seijoutodo.Helpers.PreferencesManager
import dev.seijou.com.seijoutodo.R

/**
 * Created by frontend on 11/12/17.
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var i: Intent

        if(PreferencesManager.getUserId(applicationContext) != null)
            i = Intent(this, MainActivity::class.java)
        else
            i = Intent(this, LoginActivity::class.java)

        Handler().postDelayed({
            startActivity(i)
            finish()
        }, 1500)

    }
}