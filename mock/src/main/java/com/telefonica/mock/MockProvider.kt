package com.telefonica.mock

import android.content.Context
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import javax.inject.Inject

open class MockProvider @Inject constructor(
    private val context: Context,
    private val gson: Gson
) {

    open fun fromFile(
        path: String,
        method: Method? = null,
        httpResponseCode: Int,
        delayInMillis: Long,
        localJsonFile: String,
    ): Mock {
        val body = readJsonFile(localJsonFile)
        return Mock(
            path = path,
            body = body ?: "{}",
            method = method,
            httpResponseCode = httpResponseCode.takeIf { body != null } ?: 500,
            delayInMillis = delayInMillis
        )
    }

    open fun fromString(
        path: String,
        method: Method? = null,
        httpResponseCode: Int,
        delayInMillis: Long,
        body: String,
    ): Mock = Mock(
        path = path,
        method = method,
        body = body,
        httpResponseCode = httpResponseCode,
        delayInMillis = delayInMillis
    )

    open fun <T> fromObject(
        path: String,
        method: Method? = null,
        httpResponseCode: Int,
        delayInMillis: Long,
        dataObject: T
    ): Mock = Mock(
        path = path,
        method = method,
        body = gson.toJson(dataObject),
        httpResponseCode = httpResponseCode,
        delayInMillis = delayInMillis
    )

    open fun <T> fromObject(
        path: String,
        method: Method? = null,
        httpResponseCode: Int,
        delayInMillis: Long,
        list: List<T>
    ): Mock = Mock(
        path = path,
        method = method,
        body = gson.toJson(list),
        httpResponseCode = httpResponseCode,
        delayInMillis = delayInMillis
    )

    private fun readJsonFile(jsonFilePath: String?): String? {
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
}
