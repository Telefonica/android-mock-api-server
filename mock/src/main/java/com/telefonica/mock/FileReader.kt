package com.telefonica.mock

import android.content.Context
import com.google.gson.Gson
import okio.Buffer
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import javax.inject.Inject

open class FileReader @Inject constructor(
    private val context: Context,
) {

    fun readJsonFile(jsonFilePath: String?): String? {
        return jsonFilePath?.let {
            var bufferedReader: BufferedReader? = null
            try {
                bufferedReader = BufferedReader(InputStreamReader(context.assets.open(jsonFilePath), StandardCharsets.UTF_8))
                var line: String?
                val text = StringBuilder()
                do {
                    line = bufferedReader.readLine()
                    line?.let { text.append(line) }
                } while (line != null)
                bufferedReader.close()
                text.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } finally {
                bufferedReader?.close()
            }
        }
    }

    fun readRawFile(fileName: String): Buffer {
        val inputStream = context.resources.openRawResource(context.resources.getIdentifier(fileName, "raw", context.packageName))
        return Buffer().readFrom(inputStream)
    }
}
