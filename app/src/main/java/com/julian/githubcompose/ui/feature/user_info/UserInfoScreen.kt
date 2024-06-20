package com.julian.githubcompose.ui.feature.user_info

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.julian.githubcompose.R
import com.julian.githubcompose.model.response.UserInfoResponse
import com.julian.githubcompose.ui.component.CircleAvatarImage
import com.julian.githubcompose.ui.component.CircularLoadingBar
import com.julian.githubcompose.ui.component.CustomErrorView
import com.julian.githubcompose.ui.component.MyAlertDialog
import com.julian.githubcompose.ui.feature.user_list.UserItem
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(userInfoViewModel: UserInfoViewModel) {
    val uiState by userInfoViewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            InfoAppBar(userInfoViewModel.loginName)
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(it)
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                UserPage(uiState, userInfoViewModel)
            }
        }
    )
}

@Composable
private fun UserPage(
    uiState: UserInfoViewModel.UserInfoState,
    userInfoViewModel: UserInfoViewModel
) {
    val scrollState = rememberLazyListState()
    val scrollOffset: Float = min(
        1f,
        1 - (scrollState.firstVisibleItemScrollOffset / 600f + scrollState.firstVisibleItemIndex)
    )


    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            when (uiState) {
                is UserInfoViewModel.UserInfoState.Init -> {
                    CircularLoadingBar()
                }

                is UserInfoViewModel.UserInfoState.Success -> {
                    UserInfo(uiState.info, scrollState, scrollOffset)
                }

                is UserInfoViewModel.UserInfoState.Error -> {
                    CustomErrorView(msgCode = uiState.msgCode, msgContent = uiState.msgContent)
                }
            }
        }
    }
}


@Composable
private fun UserInfo(info: UserInfoResponse, scrollState: LazyListState, scrollOffset: Float) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxHeight()) {
            Surface(tonalElevation = 4.dp) {
                UserInfoCollapsingToolbar(info = info, scrollOffset = scrollOffset)
            }
            Spacer(modifier = Modifier.height(2.dp))
            LazyColumn(
                state = scrollState,
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                item {
                    Row {
                        UserInfoImage(idRes = R.drawable.ic_account_box)
                        UserItem(
                            login = info.login, admin = info.siteAdmin, modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    UserInfoItem(idRes = R.drawable.ic_location_pin, content = info.location ?: "")
                }
                item {
                    val annotatedString = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Blue)) {
                            //Tag will be use to identify which link or text clicked | Annotation is what to show the text for tag
                            pushStringAnnotation(tag = info.blog, annotation = info.blog)
                            append(info.blog)
                        }
                    }

                    Row {
                        UserInfoImage(idRes = R.drawable.ic_link)
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                            ClickableText(
                                text = annotatedString,
                                onClick = { offset ->
                                    annotatedString.getStringAnnotations(offset, offset).firstOrNull()?.let {
                                        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.item))
                                        context.startActivity(webIntent)
                                    }
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                }
                item {
                    UserInfoItem(idRes = R.drawable.ic_email, content = info.email ?: "")
                }
                item {
                    UserInfoItem(idRes = R.drawable.ic_home_work, content = info.company ?: "")
                }
            }

            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(text = stringResource(id = R.string.user_last_update_title) + info.updatedAt)
            }
        }
    }
}

@Composable
private fun UserInfoCollapsingToolbar(info: UserInfoResponse, scrollOffset: Float) {
    val imageSize by animateDpAsState(targetValue = max(100.dp, 136.dp * scrollOffset), label = "")
    Row(modifier = Modifier.padding(16.dp)) {
        Card(
            shape = CircleShape,
            border = BorderStroke(width = 2.dp, color = Color.Black),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            CircleAvatarImage(
                avatarUrl = info.avatarUrl, default = R.drawable.img_default_avatar, modifier = Modifier.size(
                    max(100.dp, imageSize)
                )
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f, false)
                .padding(vertical = 8.dp)
        ) {

            var editName by remember { mutableStateOf("") }
            var displayName by remember { mutableStateOf(info.name) }
            var isDialogVisible by remember { mutableStateOf(false) }

            if (isDialogVisible) {
                MyAlertDialog(
                    title = { Text(text = "Edit Name") },
                    content = {
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text(stringResource(id = R.string.user_edit_name_hint)) },
                        )
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { isDialogVisible = false },
                            content = { Text(stringResource(id = R.string.all_cancel)) },
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                displayName = editName
                                isDialogVisible = false
                            },
                            content = { Text(stringResource(id = R.string.all_ok)) },
                        )
                    }
                ) {

                }
            }

            Image(
                painterResource(R.drawable.ic_edit),
                contentDescription = "Error View",
                colorFilter = ColorFilter.tint(Color.DarkGray),
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.End)
                    .clickable {
                        isDialogVisible = true
                    }
            )

            Row {
                Text(
                    text = displayName ?: info.login, // if no name then show login
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
            Text(
                text = info.bio ?: "This User No create bio.",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.W400,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Row {
                Text(
                    text = "${info.publicRepos} " + stringResource(id = R.string.user_repository),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.padding(2.dp)
                )
                Text(
                    text = "${info.publicGists} " + stringResource(id = R.string.user_gist),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.padding(2.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.align(Alignment.End)) {
                Text(text = info.createdAt, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.End)
            }

        }

    }
}

@Composable
private fun UserInfoImage(@DrawableRes idRes: Int) {
    Image(
        painterResource(idRes),
        contentDescription = "Info",
        modifier = Modifier
            .size(36.dp)
    )
}

@Composable
private fun UserInfoItem(@DrawableRes idRes: Int, content: String) {
    Row {
        UserInfoImage(idRes = idRes)
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier.align(Alignment.CenterVertically)) {
            Text(text = content)
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@ExperimentalMaterial3Api
@Composable
private fun InfoAppBar(name: String) {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Home,
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(horizontal = 12.dp),
                contentDescription = "Action icon"
            )
        },
        title = { Text(name) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            titleContentColor = MaterialTheme.colorScheme.surface,
        )
    )
}