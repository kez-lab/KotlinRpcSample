package io.github.kez_lab.kotlinxrpc.sample

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.kez_lab.kotlinxrpc.sample.network.QuizNetworkManager
import io.github.kez_lab.kotlinxrpc.sample.ui.AppViewModel
import io.github.kez_lab.kotlinxrpc.sample.ui.QuizEffect.*
import io.github.kez_lab.kotlinxrpc.sample.ui.quiz.QuizScreen
import io.github.kez_lab.kotlinxrpc.sample.ui.quiz.ResultScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.ui.tooling.preview.Preview

expect val DEV_SERVER_HOST: String

private val appViewModel = AppViewModel(QuizNetworkManager())

@Composable
@Preview
fun App() {
    MaterialTheme {
        QuizApp()
    }
}


@Composable
fun QuizApp() {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Screen.QuizScreen.route
    ) {
        composable(Screen.QuizScreen.route) { backStackEntry ->
            QuizScreen(viewModel = appViewModel)
        }
        composable(Screen.QuizResultResultScreen.route) { backStackEntry ->
            ResultScreen(
                viewModel = appViewModel,
                onClickRestart = {
                    appViewModel.resetQuiz()
                    navController.navigate(Screen.QuizScreen.route)
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        appViewModel.quizEffect.onEach { state ->
            when (state) {
                QuizReset -> {
                    navController.navigate(Screen.QuizScreen.route)
                }

                QuizCompleted -> {
                    navController.navigate(Screen.QuizResultResultScreen.route)
                }
            }
        }.launchIn(coroutineScope)
    }
}
