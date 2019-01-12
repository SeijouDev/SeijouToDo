package dev.seijou.com.seijoutodo.Views.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_main.drawer_layout
import kotlinx.android.synthetic.main.activity_main.nav_view
import kotlinx.android.synthetic.main.app_bar_main.toolbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import dev.seijou.com.seijoutodo.Helpers.Constants
import dev.seijou.com.seijoutodo.Objects.Account
import dev.seijou.com.seijoutodo.Helpers.Helpers
import dev.seijou.com.seijoutodo.Helpers.PreferencesManager
import dev.seijou.com.seijoutodo.Helpers.SqliteHelper
import dev.seijou.com.seijoutodo.R
import dev.seijou.com.seijoutodo.Views.Fragments.*


class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener {

    private var account : Account? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        account = SqliteHelper(applicationContext).getSessionUser()

        if(account == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }


        configDrawer()
        changeFragment(ExpensesFragment.newInstance() , Constants.expensesFragmentTag)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.nav_todo -> changeFragment(TodoFragment.newInstance(), Constants.todoFragmentTag)

            R.id.nav_places -> changeFragment(PlacesFragment.newInstance(), Constants.placesFragmentTag)

            R.id.nav_expenses -> changeFragment(ExpensesFragment.newInstance(), Constants.expensesFragmentTag)

            R.id.nav_music -> changeFragment(MusicFragment.newInstance(), Constants.musicFragmentTag)

            R.id.nav_settings -> changeFragment(SettingsFragment.newInstance(), Constants.settingsFragmentTag)

            R.id.nav_shopping -> changeFragment(ShoppingFragment.newInstance(), Constants.shoppingFragmentTag)

            R.id.nav_logout -> logout()

        }

        drawer_layout.closeDrawer(GravityCompat.START)

        return true
    }

    private fun configDrawer() {

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        val header = navigationView.getHeaderView(0)
        var name : TextView = header.findViewById(R.id.username)
        var email : TextView = header.findViewById(R.id.mail)
        var imageView : ImageView = header.findViewById(R.id.profile_image)

        name.setText("${account!!.name} ${account!!.lastname}")
        email.setText("${account!!.email}")

        Handler().postDelayed({
            account!!.image = PreferencesManager.getAccountImage(applicationContext)
            var bitmap = Helpers.StringToBitMap(account!!.image!!)
            imageView.setImageBitmap(bitmap)
        }, 2000)
    }

    private fun logout() {
        if(account!!.loginType == 0) {
            LoginManager.getInstance().logOut()
            PreferencesManager.clean(applicationContext)
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
        else{
            var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
            var mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
            mGoogleSignInClient.signOut().addOnCompleteListener(this,  {
                PreferencesManager.clean(applicationContext)
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            })
        }
    }

    fun changeFragment(f:Fragment, tag:String) {
        supportFragmentManager.beginTransaction().replace(R.id.fragments_container, f, tag).commit()
        setTitle(tag)
    }

}
