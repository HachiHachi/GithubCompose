package com.julian.githubcompose.ui.feature.user_list

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.julian.githubcompose.R
import com.julian.githubcompose.model.response.UserListResponse
import com.julian.githubcompose.ui.component.CircleAvatarImage
import com.julian.githubcompose.ui.component.BadgeTextView
import com.julian.githubcompose.ui.component.CircularLoadingBar
import com.julian.githubcompose.ui.component.CustomErrorView

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalCoilApi
@Composable
fun UserListScreen(
    userListViewModel: UserListViewModel,
    onNavigationRequested: (username: String) -> Unit
) {

    val uiState by userListViewModel.uiState.collectAsState()

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
                UserBody(uiState, userListViewModel) { item ->
                    onNavigationRequested(item.login)
                }
            }
        }
    )
}


@Composable
fun UserBody(
    uiState: UserListViewModel.UserListState,
    userListViewModel: UserListViewModel,
    onItemClicked: (item: UserListResponse) -> Unit = { }
) {
    Box(modifier = Modifier) {
        when (uiState) {
            is UserListViewModel.UserListState.Error -> {
                CustomErrorView(uiState.msgCode, uiState.msgContent)
            }

            is UserListViewModel.UserListState.Success -> {
                UserList(
                    data = uiState.list,
                    viewModel = userListViewModel,
                    onItemClicked = onItemClicked
                )
            }

            is UserListViewModel.UserListState.Init -> {
                CircularLoadingBar()
            }
        }
    }
}


@Composable
fun UserList(
    data: List<UserListResponse>,
    viewModel: UserListViewModel,
    onItemClicked: (item: UserListResponse) -> Unit
) {
    LazyColumn(Modifier.padding(4.dp)) {
        data.forEachIndexed { index, item ->
            item {
                UserItemCard(item = item, onItemClicked = onItemClicked)
            }

            // not last user and load more
            if (viewModel.isLast.value.not() && data.size - index < 2) {
                item {
                    CircularLoadingBar()
                    Spacer(modifier = Modifier.height(8.dp))
                    LaunchedEffect(Unit) {
                        viewModel.getUserList(data.last().id, 20)
                    }
                }
            }
        }
    }
}


@Composable
fun UserItemCard(item: UserListResponse, onItemClicked: (item: UserListResponse) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp)
            .shadow(
                shape = RoundedCornerShape(4.dp),
                elevation = 4.dp
            )
            .clickable {
                // click card
                onItemClicked(item)
            }
    ) {
        Row(
            modifier = Modifier
                .animateContentSize()
                .padding(
                    vertical = 16.dp, horizontal = 16.dp
                )
        ) {
            Box(modifier = Modifier.align(alignment = Alignment.CenterVertically)) {
                CircleAvatarImage(
                    avatarUrl = item.avatar_url,
                    default = R.drawable.img_default_avatar, modifier = Modifier.size(48.dp)
                )
            }
            UserItem(
                login = item.login,
                admin = item.site_admin,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun UserItem(login: String, admin: Boolean, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            text = login,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(2.dp))
        if (admin) {
            BadgeTextView(
                text = stringResource(id = R.string.user_staff),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
                    .padding(vertical = 2.dp, horizontal = 6.dp)
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun CategoriesAppBar() {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Home,
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(horizontal = 12.dp),
                contentDescription = "Action icon"
            )
        },
        title = { Text(stringResource(R.string.app_name)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            titleContentColor = MaterialTheme.colorScheme.surface,
        )
    )
}