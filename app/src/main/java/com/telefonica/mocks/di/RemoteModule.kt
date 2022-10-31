package com.telefonica.mocks.di

import android.content.Context
import com.telefonica.mocks.data.user.UserWs
import com.telefonica.mocks.domain.backend.InitBackendUrl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.telefonica.mock.MockHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteModule {

    @Provides
    fun provideInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BASIC)
    }

    @Provides
    fun provideOkHttpClient(
        interceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    @Provides
    fun provideMoshi(): Moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
        initBackendUrl: InitBackendUrl
    ): Retrofit = Retrofit
        .Builder()
        .client(okHttpClient)
        .baseUrl(initBackendUrl.provideBackendUrl())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    fun provideUserWebService(retrofit: Retrofit): UserWs = retrofit.create(UserWs::class.java)

    @Provides
    @Singleton
    fun provideMockHelper(
        @ApplicationContext context: Context
    ): MockHelper = MockHelper(context)

}
