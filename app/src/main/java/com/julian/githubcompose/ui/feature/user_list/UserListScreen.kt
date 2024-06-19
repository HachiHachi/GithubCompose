package com.julian.githubcompose.ui.feature.user_list

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.julian.githubcompose.R
import com.julian.githubcompose.ui.component.CircularLoadingBar

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalCoilApi
@Composable
fun UserListScreen(
    userListViewModel: UserListViewModel
) {

    val uiState by userListViewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CategoriesAppBar()
        },
        content = {
            Box(modifier = Modifier
                .padding(it)
                .background(color = MaterialTheme.colorScheme.background)) {
                UserBody(uiState, userListViewModel)
            }

        }
    )
}


@Composable
fun UserBody(
    uiState: UserListViewModel.UserListState,
    userListViewModel: UserListViewModel,
    onItemClicked: (id: UserListViewModel.TestItem) -> Unit = { }
) {
    Box(modifier = Modifier) {
        when (uiState) {
            is UserListViewModel.UserListState.Error -> {
                ErrorView(uiState.msgCode)
            }

            is UserListViewModel.UserListState.Success -> {
                Log.d("dadaSuccess", uiState.categories.size.toString())
                UserList(
                    data = uiState.categories,
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
    data: List<UserListViewModel.TestItem>,
    viewModel: UserListViewModel,
    onItemClicked: (id: UserListViewModel.TestItem) -> Unit
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
                        Log.d("dada", "aaa")
                        viewModel.getUserList()
                    }
                }
            }
        }
    }
}


@Composable
fun UserItemCard(item: UserListViewModel.TestItem, onItemClicked: (id: UserListViewModel.TestItem) -> Unit) {
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
                UserAvatar("avatar Url")
            }
            UserItem(
                item = item,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun UserItem(item: UserListViewModel.TestItem, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "${item.key}",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        if (item.content.isNotEmpty())
            Text(
                text = item.content,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall,
            )
    }
}

@Composable
fun UserAvatar(imgUrl: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://huggingface.co/datasets/huggingfacejs/tasks/resolve/main/image-classification/image-classification-input.jpeg")
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .transformations(CircleCropTransformation())
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = Modifier.size(48.dp)
    )
}

@ExperimentalMaterial3Api
@Composable
private fun CategoriesAppBar() {
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

@Composable
fun ErrorView(msgCode: String) {
    Text(text = msgCode)
}