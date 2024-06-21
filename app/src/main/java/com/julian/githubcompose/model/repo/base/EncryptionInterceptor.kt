package com.julian.githubcompose.model.repo.base

import okhttp3.Interceptor
import okhttp3.Response

class EncryptionInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        // this way can implement encrypt body or feature if need
        val rawBody = request.body

        // add header
        request = request.newBuilder()
            .addHeader("Accept", "application/vnd.github+json")
            .build()

        return chain.proceed(request)
    }
}