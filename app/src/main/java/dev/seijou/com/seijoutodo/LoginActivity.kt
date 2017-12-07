package dev.seijou.com.seijoutodo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import com.facebook.login.widget.LoginButton
import android.widget.TextView
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.GraphRequest
import java.util.Arrays
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


/**
 * Created by frontend on 7/12/17.
 */
class LoginActivity : AppCompatActivity() {

    private var callbackManager: CallbackManager? = null
    private var loginType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        callbackManager = CallbackManager.Factory.create()
        configFacebookLogin()

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        var account = GoogleSignIn.getLastSignedInAccount(this)
        Log.e("adasdasd", account.toString())


        var signInButton: SignInButton = findViewById(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setOnClickListener {
            loginType = 1
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, 100)
        }



    }

    fun configFacebookLogin() {
        var info: TextView = findViewById(R.id.info)
        var loginButton : LoginButton = findViewById(R.id.login_button)
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"))
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {

                val request = GraphRequest.newMeRequest(loginResult.accessToken) {
                    `object`, user  -> Log.v("LoginActivity", user.toString())

                    val id = `object`.getString("id")
                    val name = `object`.getString("name")
                    val firstname = `object`.getString("first_name")
                    val lastname = `object`.getString("last_name")
                    val link = `object`.getString("link")
                    val gender = `object`.getString("gender")
                    val picture = `object`.getString("picture")

                    info!!.setText("- ${name} \n- ${firstname} \n- ${lastname} \n- ${link} \n- ${gender} \n- ${picture}")

                    DownloadImageTask(findViewById<ImageView>(R.id.img)).execute("https://graph.facebook.com/" + id + "/picture?type=large")


                }

                val parameters = Bundle()
                parameters.putString("fields", "email,id,name,last_name,first_name,gender,birthday,cover,age_range,link,locale,picture,timezone,updated_time,verified")
                request.parameters = parameters
                request.executeAsync()


            }

            override fun onCancel() {
                Toast.makeText(applicationContext, "Cancel",Toast.LENGTH_SHORT).show()
                info!!.setText("Login attempt canceled.")

            }

            override fun onError(e: FacebookException) {
                Toast.makeText(applicationContext, "Error",Toast.LENGTH_SHORT).show()
                info!!.setText("Login attempt failed.")
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(loginType == 1){
            if (requestCode == 100) {
                var task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task)
            }
        }
        else
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            var acct : GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val personGivenName = acct.getGivenName()
            val personFamilyName = acct.getFamilyName()
            val personEmail = acct.getEmail()
            val personId = acct.getId()
            val personPhoto = acct.getPhotoUrl()
        } catch (e: Exception) {
        }
        loginType = 0
    }

    private inner class DownloadImageTask(internal var bmImage: ImageView) : AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg urls: String): Bitmap? {
            val urldisplay = urls[0]
            var mIcon11: Bitmap? = null
            try {
                val `in` = java.net.URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                Log.e("Error", e.message)
                e.printStackTrace()
            }

            return mIcon11
        }

        override fun onPostExecute(result: Bitmap) {
            bmImage.setImageBitmap(result)
        }
    }


}

