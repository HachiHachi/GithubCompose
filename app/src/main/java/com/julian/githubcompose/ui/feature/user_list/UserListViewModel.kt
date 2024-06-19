package com.julian.githubcompose.ui.feature.user_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julian.githubcompose.model.repo.base.ApiErrorException
import com.julian.githubcompose.model.repo.user.UserRepository
import com.julian.githubcompose.model.response.UserListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UserListState> = MutableStateFlow(UserListState.Init)
    val uiState: StateFlow<UserListState> = _uiState

    // is last user or not
    private val _isLast: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLast: StateFlow<Boolean> = _isLast

    private val userList = mutableListOf<UserListResponse>()

    init {
        viewModelScope.launch {
            getUserList(page = 20)
        }
    }

    suspend fun getUserList(since: Int? = null, page: Int = 2) {
        viewModelScope.launch {
            val result = repository.getUserList(since, page)

            result.apply {
                if (isSuccess) {
                    getOrNull()?.apply {
                        setUserList(this)
                        _uiState.update {
                            UserListState.Success(list = userList)
                        }
                    }
                } else {
                    exceptionOrNull()?.apply {
                        if (this is ApiErrorException) {
                            _uiState.update {
                                UserListState.Error(msgCode = getMsgCode(), msgContent = getMsgContent())
                            }
                        } else {
                            _uiState.update {
                                UserListState.Error(msgContent = this.message)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setUserList(data: List<UserListResponse>) {
        userList.addAll(data)
        _isLast.value = userList.size >= 100
    }

    sealed interface UserListState {
        data object Init : UserListState
        data class Success(val list: List<UserListResponse> = listOf(), val tag: Long = System.currentTimeMillis()) :
            UserListState
        data class Error(val msgCode: String?= null, val msgContent: String? = null) : UserListState
    }
}