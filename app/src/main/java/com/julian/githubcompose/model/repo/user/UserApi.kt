package com.julian.githubcompose.model.repo.user

import com.julian.githubcompose.model.response.UserListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface UserApi {

    @GET("users")
    suspend fun getUserList(
        @QueryMap queryMap: Map<String, Int>
    ) : Response<List<UserListResponse>>
}