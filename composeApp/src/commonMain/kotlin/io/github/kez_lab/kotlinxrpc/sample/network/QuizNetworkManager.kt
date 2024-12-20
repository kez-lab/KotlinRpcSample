package io.github.kez_lab.kotlinxrpc.sample.network

import io.github.kez_lab.kotlinxrpc.sample.model.Quiz
import io.github.kez_lab.kotlinxrpc.sample.model.QuizResult
import io.github.kez_lab.kotlinxrpc.sample.service.QuizService
import io.ktor.client.HttpClient
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import kotlinx.rpc.krpc.ktor.client.installRPC
import kotlinx.rpc.krpc.ktor.client.rpc
import kotlinx.rpc.krpc.ktor.client.rpcConfig
import kotlinx.rpc.krpc.serialization.json.json
import kotlinx.rpc.withService

class QuizNetworkManager {
    private lateinit var quizService: QuizService

    suspend fun init() {
        val client = HttpClient {
            installRPC()
        }.rpc {
            url {
                protocol = URLProtocol.WSS
                host = "kezlab.site"
                port = 443
                encodedPath = "/quiz"
            }
            rpcConfig {
                serialization {
                    json()
                }
            }
        }
        quizService = client.withService<QuizService>()
    }

    suspend fun fetchQuizzes(): List<Quiz> {
        return runCatching {
            quizService.getQuiz()
        }.onFailure { e ->
            println("Error fetching quizzes: ${e.message}")
        }.getOrDefault(emptyList())
    }

    suspend fun submitAnswers(answers: List<Int>): QuizResult {
        return runCatching {
            quizService.calculateQuizScore(answers)
        }.onFailure { e ->
            println("Error submitting answers: ${e.message}")
        }.getOrDefault(QuizResult(0))
    }
}
