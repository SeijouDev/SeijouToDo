package dev.seijou.com.seijoutodo.Helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import dev.seijou.com.seijoutodo.Helpers.Helpers
import dev.seijou.com.seijoutodo.Helpers.PreferencesManager
import java.net.URL

/**
 * Created by frontend on 11/12/17.
 */
class Tasks {

    class DownloadImageTask(c: Context) : AsyncTask<String, Void, Bitmap>() {

        val context = c

        override fun doInBackground(vararg urls: String): Bitmap? {

            val url = urls[0]
            var image: Bitmap? = null
            try {

                val i = URL(url).openStream()
                image = BitmapFactory.decodeStream(i)

            } catch (e: Exception) {
                Log.e("Error", e.message)
                e.printStackTrace()
            }

            return image
        }

        override fun onPostExecute(result: Bitmap) {
            PreferencesManager.saveAccountImage(this.context, Helpers.BitMapToString(result))
        }
    }
}