package dev.seijou.com.seijoutodo.Helpers

import android.content.Context
import dev.seijou.com.seijoutodo.Helpers.Constants.Companion.accountImage
import dev.seijou.com.seijoutodo.Helpers.Constants.Companion.sharedPreferencesName
import dev.seijou.com.seijoutodo.Helpers.Constants.Companion.userIdPrefs

/**
 * Created by frontend on 11/12/17.
 */
class PreferencesManager {
    companion object {

        fun saveUserId(c: Context, userId: String){
            var editor = c.getSharedPreferences(sharedPreferencesName, 0).edit()
            editor.putString(userIdPrefs, userId)
            editor.commit()
        }

        fun getUserId(c: Context) : String? = c.getSharedPreferences(sharedPreferencesName, 0).getString(userIdPrefs, null)


        fun saveAccountImage(c: Context, image: String) {
            var editor = c.getSharedPreferences(sharedPreferencesName, 0).edit()
            editor.putString(accountImage, image)
            editor.commit()
        }

        fun getAccountImage(c: Context) : String {
            val p = c.getSharedPreferences(sharedPreferencesName, 0)
            return p.getString(accountImage, "empty")
        }

        fun clean(c: Context) =  c.getSharedPreferences(sharedPreferencesName, 0).edit().clear()


    }
}