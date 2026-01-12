package com.telefonica.mock.di

import android.content.Context
import com.telefonica.mock.MockedServer
import com.telefonica.mock.FileReader
import com.telefonica.mock.ResponseDispatcher
import dagger.Module
import dagger.Provides
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
        mockWebServer: MockWebServer,
        responseDispatcher: ResponseDispatcher,
    ): MockedServer = MockedServer(mockWebServer, responseDispatcher)
}
