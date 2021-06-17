package com.bicisos.i7.bicisos.Api

import android.util.Log
import com.bicisos.i7.bicisos.model.Taller
import com.google.gson.Gson
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject



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

    fun sendmail(message: String) :  String? {
        try {
            val url = URL(ip+"api/mail")

            urlConnection = url.openConnection() as HttpURLConnection?
            urlConnection!!.setRequestMethod("POST")
            urlConnection!!.connectTimeout = 5000
            urlConnection!!.setRequestProperty("Content-Type", "application/json; utf-8")
            urlConnection!!.setRequestProperty("Accept", "application/json")
            urlConnection!!.doOutput = true

            val cred = JSONObject()
            cred.put("correo","cristobaljohn00@gmail.com")
            cred.put("message",message)
            val params = JSONObject()
            params.put("params",cred)

            val output = (urlConnection!!.outputStream)
            output.write(params.toString().toByteArray())
            output.close()

            /*urlConnection!!.out .getOutputStreamWriter().use({ os ->
                //val input = params.toByteArray() //.toByteArray("utf-8")// ..getBytes("utf-8")
                os.write(params.toString(), 0, params.size)
            })*/

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
            //val gson = Gson()
            //val datos= gson.fromJson(result,Array<Taller>::class.java).toList()
            //Log.w("res",datos)
            return result

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
