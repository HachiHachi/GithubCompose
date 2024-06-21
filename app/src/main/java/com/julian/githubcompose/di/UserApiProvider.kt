package com.julian.githubcompose.di

import android.content.Context
import com.julian.githubcompose.model.repo.base.ConnectivityInterceptor
import com.julian.githubcompose.model.repo.base.DecryptionInterceptor
import com.julian.githubcompose.model.repo.base.EncryptionInterceptor
import com.julian.githubcompose.model.repo.user.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class UserApiProvider {

    private val baseUrl = "ht" + "tps://" + "api.github.com/"

    @Provides
    @Singleton
    fun provideAuthInterceptorOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(EncryptionInterceptor())
            .addInterceptor(DecryptionInterceptor())
            .addInterceptor(ConnectivityInterceptor(context))
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit
            .Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit) : UserApi = retrofit.create(UserApi::class.java)
}