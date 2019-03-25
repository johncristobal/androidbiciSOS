package com.bicisos.i7.bicisos.Api

import android.util.Log
import com.bicisos.i7.bicisos.Model.Taller
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class ApiClient //: AsyncTask<String, String, String>() {
{
    var urlConnection: HttpURLConnection? = null
    var reader: BufferedReader? = null
    //var ip = "http://xatsaautopartes.xyz/Api/"
    var ip = "http://xatsaautopartes.xyz/soscilistaapi/"

    fun callTalleres() : List<Taller>? {
        try {
            val url = URL(ip+"api/talleres")

            urlConnection = url.openConnection() as HttpURLConnection?
            urlConnection!!.setRequestMethod("GET")
            urlConnection!!.connect()

            val inputStream = urlConnection!!.getInputStream()
            val buffer = StringBuffer()
            if (inputStream == null) {
                return null
            }

            reader = BufferedReader(InputStreamReader(inputStream))

            var line = reader!!.readLine()

            while ((line) != null) {
                buffer.append(line + "\n")
                line = reader!!.readLine()
            }

            if (buffer.length == 0) {
                return null
            }

            var result = buffer.toString()
            val gson = Gson()
            val datos= gson.fromJson(result,Array<Taller>::class.java).toList()
            //Log.w("res",datos)
            return datos

        } catch (e: Exception) {
            Log.e("Request", "Error ", e)
            return null
        } finally {
            if (urlConnection != null) {
                urlConnection!!.disconnect()
            }
            if (reader != null) {
                try {
                    reader!!.close()
                } catch (e: IOException) {
                    Log.e("Request", "Error closing stream", e)
                }
            }
        }
    }
}
