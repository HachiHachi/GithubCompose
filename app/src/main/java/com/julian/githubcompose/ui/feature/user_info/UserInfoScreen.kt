package com.julian.githubcompose.ui.feature.user_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.julian.githubcompose.ui.feature.user_list.CategoriesAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(userInfoViewModel: UserInfoViewModel) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CategoriesAppBar()
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(it)
                    .background(color = MaterialTheme.colorScheme.background)
            ) {

            }
        }
    )

}