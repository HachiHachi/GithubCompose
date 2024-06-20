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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.julian.githubcompose.NavigationKeys.Arg.USER_LOGIN_NAME
import com.julian.githubcompose.ui.feature.user_info.UserInfoScreen
import com.julian.githubcompose.ui.feature.user_info.UserInfoViewModel
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
                GithubUserApp()
            }
        }
    }
}

@Composable
private fun GithubUserApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavigationKeys.Route.USER_LIST) {
        composable(route = NavigationKeys.Route.USER_LIST) {
            UserListDestination(navController)
        }
        composable(route = NavigationKeys.Route.USER_INFO, arguments = listOf(navArgument(USER_LOGIN_NAME) {
            type = NavType.StringType
        }) ) {
            UserInfoDestination()
        }
    }
}



@Composable
private fun UserListDestination(navController: NavHostController) {
    val viewModel: UserListViewModel = hiltViewModel()
    UserListScreen(userListViewModel = viewModel, onNavigationRequested = { item ->
        navController.navigate("${NavigationKeys.Route.USER_LIST}/${item}")
    })
}

@Composable
private fun UserInfoDestination() {
    val viewModel: UserInfoViewModel = hiltViewModel()
    UserInfoScreen(userInfoViewModel = viewModel)
}

object NavigationKeys {

    object Arg {
        const val USER_LOGIN_NAME = "userLoginName"
    }

    object Route {
        const val USER_LIST = "user_list"
        const val USER_INFO = "$USER_LIST/{$USER_LOGIN_NAME}"
    }

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