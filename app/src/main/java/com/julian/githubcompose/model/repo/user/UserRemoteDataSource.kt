package com.julian.githubcompose.model.repo.user

import com.julian.githubcompose.model.repo.base.BaseRemoteDataSource
import com.julian.githubcompose.model.response.UserListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val api: UserApi,
) : BaseRemoteDataSource() {

    suspend fun getUserList(since: Int, page: Int): Result<List<UserListResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getUserList(sinceID = since, page = page)
                result(response)
            }  catch (throwable: Throwable) {
                Result.failure(throwable)
            }
        }
    }

}