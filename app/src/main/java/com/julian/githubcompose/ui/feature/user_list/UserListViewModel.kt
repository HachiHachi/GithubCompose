package com.julian.githubcompose.ui.feature.user_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Random
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState: MutableStateFlow<UserListState> = MutableStateFlow(UserListState.Init)
    val uiState: StateFlow<UserListState> = _uiState

    // is last user or not
    private val _isLast: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLast: StateFlow<Boolean> = _isLast

    private val listData = mutableListOf<TestItem>()


    init {
        viewModelScope.launch {
            getUserList()
        }
    }


    suspend fun getUserList() {
        val categories = mutableListOf<TestItem>()
        for (i in 1..20) {
            categories.add(TestItem(i, getRandomString(10)))
        }

        listData.addAll(categories)
        _isLast.value = listData.size >= 100

        viewModelScope.launch {
            delay(2000)
            _uiState.update {
                UserListState.Success(categories = listData)
            }
        }
    }

    private fun getRandomString(sizeOfRandomString: Int): String {
        val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"
        val random = Random()
        val sb = StringBuilder(sizeOfRandomString)
        for (i in 0 until sizeOfRandomString)
            sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
        return sb.toString()
    }


    sealed interface UserListState {
        data object Init : UserListState

        data class Success(val categories: List<TestItem> = listOf(), val tag: Long = System.currentTimeMillis()) : UserListState

        data class Error(val msgCode: String) : UserListState
    }


    data class TestItem(
        val key: Int,
        val content: String
    )

}