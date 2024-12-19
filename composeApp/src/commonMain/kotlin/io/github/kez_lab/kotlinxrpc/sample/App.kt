package io.github.kez_lab.kotlinxrpc.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.kez_lab.kotlinxrpc.sample.QuizEffect.QuizCompleted
import io.github.kez_lab.kotlinxrpc.sample.QuizEffect.QuizReset
import io.github.kez_lab.kotlinxrpc.sample.network.QuizNetworkManager
import io.github.kez_lab.kotlinxrpc.sample.ui.QuizTheme
import io.github.kez_lab.kotlinxrpc.sample.ui.quiz.QuizScreen
import io.github.kez_lab.kotlinxrpc.sample.ui.quiz.ResultScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.ui.tooling.preview.Preview

private val appViewModel = AppViewModel(QuizNetworkManager())

@Composable
@Preview
fun App() {
    QuizTheme {
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
