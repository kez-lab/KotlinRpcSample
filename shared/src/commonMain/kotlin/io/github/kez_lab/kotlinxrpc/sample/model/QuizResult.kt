package io.github.kez_lab.kotlinxrpc.sample.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizResult(
    val isCorrect: Boolean,
    val explanation: String
)