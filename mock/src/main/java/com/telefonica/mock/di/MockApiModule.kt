package com.telefonica.mock.di

import android.content.Context
import com.google.gson.Gson
import com.telefonica.mock.MockApiClient
import com.telefonica.mock.FileReader
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton

@Module
class MockApiModule(private val context: Context) {

    @Provides
    fun provideContext(): Context = context

    @Provides
    @Singleton
    fun provideMockWebServer(): MockWebServer = MockWebServer()

    @Provides
    fun provideMockProvider(context: Context) = FileReader(context)

    @Provides
    fun provideMockApiClient(
        mockWebServer: MockWebServer
    ): MockApiClient = MockApiClient(Dispatchers.IO, mockWebServer)
}
