package io.github.kez_lab.kotlinxrpc.sample.service

import io.github.kez_lab.kotlinxrpc.sample.model.Quiz
import io.github.kez_lab.kotlinxrpc.sample.model.QuizResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class QuizServiceImpl(
    override val coroutineContext: CoroutineContext
) : QuizService {
    private val quiz = Quiz(
        question = "What is the capital of France?",
        options = listOf("Paris", "London", "Berlin", "Madrid"),
        correctIndex = 0
    )

    override suspend fun getQuiz(): Quiz = quiz

    override suspend fun checkAnswer(index: Int): QuizResult {
        val isCorrect = index == quiz.correctIndex
        return QuizResult(
            isCorrect = isCorrect,
            explanation = if (isCorrect) "Correct!" else "The correct answer is Paris."
        )
    }
}
