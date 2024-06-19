package com.julian.githubcompose.model.repo.base

import retrofit2.Response

open class BaseRemoteDataSource {

    fun <T> result(response: Response<T>) =
        if (response.isSuccessful) {
            // handle systemTime or updateToken if need
            Result.success(
                response.body() ?: throw NullPointerException()
            )
        } else {
            // handle error response if need custom situation
            if (response.body() != null) {
                Result.failure(ApiErrorException(msgCode = response.code().toString(), msgContent = response.body().toString()))
            } else {
                Result.failure(ApiErrorException(response.code().toString(), msgContent = "Http Error"))
            }
        }

}