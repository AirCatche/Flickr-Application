package com.slobodianiuk.flickrapplication

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class GetRawData {
    private var downloadStatus: DownloadStatus = DownloadStatus.IDLE
    var reader: BufferedReader? = null

    companion object {
        private const val TAG = "AAAA"
    }


    fun dataRequest(urlLink: String): String? {
        var connection: HttpURLConnection? = null
        try {
            val url: URL = URL(urlLink)
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

//            val response: Int = connection.responseCode
            val result: StringBuilder = StringBuilder("")
            reader = BufferedReader(InputStreamReader(connection.inputStream))

//            var line: String = ""
//            while (null != (line)) {
//                line = reader!!.readLine()
//                result.append(line).append("\n")
//            }
            for (line in reader!!.readLine()){
                result.append(line).append("\n")
            }
            downloadStatus = DownloadStatus.OK
            return result.toString()

        } catch (e: MalformedURLException) {
            Log.e(TAG, "dataRequest: Invalid URL ${e.message}")
        } catch (e: IOException) {
            Log.e(TAG, "dataRequest: IO exception ${e.message}")
        } catch (e: SecurityException) {
            Log.e(TAG, "dataRequest: Security problem ${e.message}")
        } finally {
            connection?.disconnect()
            try {
                reader?.close()
            } catch (e: IOException) {
                Log.e(TAG, "dataRequest: Error closing stream ${e.message}" )
            }
        }
        downloadStatus = DownloadStatus.FAILED_OR_EMPTY
        return null
    }
}