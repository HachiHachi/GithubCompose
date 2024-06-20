package com.julian.githubcompose.model.repo.user

import com.julian.githubcompose.model.repo.base.BaseRemoteDataSource
import com.julian.githubcompose.model.response.UserInfoResponse
import com.julian.githubcompose.model.response.UserListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.HashMap
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val api: UserApi,
) : BaseRemoteDataSource() {

    suspend fun getUserList(since: Int?, page: Int): Result<List<UserListResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val map = HashMap<String, Int>().apply {
                    since?.apply {
                        put("since", this)
                    }
                    put("per_page", page)
                }

                val response = api.getUserList(queryMap = map)
                result(response)
            }  catch (throwable: Throwable) {
                Result.failure(throwable)
            }
        }
    }

    suspend fun getUserInfo(username: String): Result<UserInfoResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getUserInfo(username = username)
                result(response)
            } catch (throwable: Throwable) {
                Result.failure(throwable)
            }
        }
    }

}