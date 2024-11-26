package io.github.kez_lab.kotlinxrpc.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        QuizScreen(QuizViewModel())
    }
}

@Composable
fun QuizScreen(viewModel: QuizViewModel) {
    val quiz = viewModel.quiz ?: return
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = quiz.question,
            modifier = Modifier.padding(8.dp)
        )

        quiz.options.forEachIndexed { index, option ->
            ClickableText(
                text = AnnotatedString(option),
                onClick = { },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}