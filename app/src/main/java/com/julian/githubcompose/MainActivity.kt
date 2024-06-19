@file:OptIn(ExperimentalCoilApi::class)

package com.julian.githubcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.julian.githubcompose.ui.feature.user_list.UserListScreen
import com.julian.githubcompose.ui.feature.user_list.UserListViewModel
import com.julian.githubcompose.ui.theme.GithubComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubComposeTheme {
                UserListDestination()
            }
        }
    }
}
@Composable
private fun UserListDestination() {
    val viewModel: UserListViewModel = hiltViewModel()
    UserListScreen(userListViewModel = viewModel)
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GithubComposeTheme {
        Greeting("Android")
    }
}