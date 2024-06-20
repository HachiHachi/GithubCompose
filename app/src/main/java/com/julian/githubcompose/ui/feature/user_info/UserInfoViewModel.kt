package com.julian.githubcompose.ui.feature.user_info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julian.githubcompose.NavigationKeys
import com.julian.githubcompose.model.repo.base.ApiErrorException
import com.julian.githubcompose.model.repo.user.UserRepository
import com.julian.githubcompose.model.response.UserInfoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UserInfoState> = MutableStateFlow(UserInfoState.Init)
    val uiState = _uiState.asStateFlow()

    var loginName = ""

    init {
        // get navigation argument
        loginName = stateHandle.get<String>(NavigationKeys.Arg.USER_LOGIN_NAME) ?: ""
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            val result = repository.getUserInfo(loginName)

            result.apply {
                if (isSuccess) {
                    getOrNull()?.apply {
                        _uiState.update {
                            UserInfoState.Success(this)
                        }
                    }
                } else {
                    exceptionOrNull()?.apply {
                        if (this is ApiErrorException) {
                            _uiState.update {
                                UserInfoState.Error(msgCode = getMsgCode(), msgContent = getMsgContent())
                            }
                        } else {
                            _uiState.update {
                                UserInfoState.Error(msgContent = this.message)
                            }
                        }
                    }
                }
            }
        }
    }


    sealed interface UserInfoState {
        data object Init : UserInfoState
        data class Success(val info: UserInfoResponse): UserInfoState
        data class Error(val msgCode: String?= null, val msgContent: String? = null): UserInfoState
    }
}