package com.julian.githubcompose.model.repo.base

import okhttp3.Interceptor
import okhttp3.Response

class DecryptionInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        // this way can implement decrypt body if need
        if (response.isSuccessful) {
            val newResponse = response.newBuilder()

            return newResponse.build()
        }

        return response
    }
}