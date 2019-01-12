package dev.seijou.com.seijoutodo.Views.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.facebook.login.widget.LoginButton
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.GraphRequest
import java.util.Arrays
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dev.seijou.com.seijoutodo.Objects.Account
import dev.seijou.com.seijoutodo.Helpers.PreferencesManager
import dev.seijou.com.seijoutodo.Helpers.SqliteHelper
import dev.seijou.com.seijoutodo.R
import dev.seijou.com.seijoutodo.Helpers.Tasks


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
        configGoogleLogin()
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

    private fun configFacebookLogin() {

        var loginButton : LoginButton = findViewById(R.id.login_button)
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"))
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {

                val request = GraphRequest.newMeRequest(loginResult.accessToken) {

                    obj , user  -> Log.v("LoginActivity", user.toString())

                    val id = obj.getString("id")
                    val firstname = obj.getString("first_name")
                    val lastname = obj.getString("last_name")
                    val email =  obj.getString("email")

                    Tasks.DownloadImageTask(applicationContext).execute("https://graph.facebook.com/${id}/picture?type=large")
                    createSession(firstname, lastname, email, id, 0)

                }

                val parameters = Bundle()
                parameters.putString("fields", "email,id,name,last_name,first_name,gender,birthday,cover,age_range,link,locale,picture,timezone,updated_time,verified")
                request.parameters = parameters
                request.executeAsync()

            }

            override fun onCancel() {}

            override fun onError(e: FacebookException) {
                Toast.makeText(applicationContext, "Algo ha salido mal!",Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun configGoogleLogin() {
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        var account = GoogleSignIn.getLastSignedInAccount(this)

        var signInButton: SignInButton = findViewById(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setOnClickListener {
            loginType = 1
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, 100)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {

            var acct : GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val personGivenName = acct.getGivenName()
            val personFamilyName = acct.getFamilyName()
            val personEmail = acct.getEmail()
            val personId = acct.getId()
            val personPhoto = acct.getPhotoUrl()

            Log.e("Login" , personPhoto.toString())
            if(personPhoto != null)
                Tasks.DownloadImageTask(applicationContext).execute(personPhoto.toString())

            createSession(personGivenName!!, personFamilyName!!, personEmail!!, personId!!,1 )
        }
        catch (e: Exception) {
            Log.e("Exception",e.message)
        }

        loginType = 0

    }

    private fun createSession(name: String, lastName: String, email: String, id: String, loginType: Int){
        PreferencesManager.saveUserId(applicationContext,id)

        if(!SqliteHelper(applicationContext).userExists(id))
            SqliteHelper(applicationContext).insertUser(Account(id, name, lastName, email, loginType, "0"))

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}

