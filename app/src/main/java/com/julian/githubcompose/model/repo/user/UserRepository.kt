package com.julian.githubcompose.model.repo.user

import com.julian.githubcompose.model.response.UserInfoResponse
import com.julian.githubcompose.model.response.UserListResponse
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) {

    suspend fun getUserList(since: Int?, page: Int): Result<List<UserListResponse>> {
        // mock data can do in here if need
        return userRemoteDataSource.getUserList(since, page)
    }

    suspend fun getUserInfo(username: String): Result<UserInfoResponse> {
        // mock data can do in here if need
        return userRemoteDataSource.getUserInfo(username)
    }

}