
package io.github.kez_lab.kotlinxrpc.sample

sealed class Screen(val route: String) {
    data object QuizScreen : Screen("quiz")
    data object QuizResultResultScreen : Screen("result")
}

