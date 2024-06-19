package com.julian.githubcompose.model.repo.user

import com.julian.githubcompose.model.response.UserListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    @GET("users")
    suspend fun getUserList(
        @Query("since") sinceID: Int,
        @Query("per_page") page: Int
    ) : Response<List<UserListResponse>>


//    https://api.github.com/users?since=26&per_page=5
}