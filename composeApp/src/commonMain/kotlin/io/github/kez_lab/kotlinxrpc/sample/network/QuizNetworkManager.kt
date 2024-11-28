package io.github.kez_lab.kotlinxrpc.sample.network

import io.github.kez_lab.kotlinxrpc.sample.DEV_SERVER_HOST
import io.github.kez_lab.kotlinxrpc.sample.model.Quiz
import io.github.kez_lab.kotlinxrpc.sample.model.QuizResult
import io.github.kez_lab.kotlinxrpc.sample.service.QuizService
import io.ktor.client.HttpClient
import io.ktor.http.encodedPath
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.rpc.krpc.ktor.client.installRPC
import kotlinx.rpc.krpc.ktor.client.rpc
import kotlinx.rpc.krpc.ktor.client.rpcConfig
import kotlinx.rpc.krpc.serialization.json.json
import kotlinx.rpc.withService

class QuizNetworkManager {
    private val quizServiceDeferred: CompletableDeferred<QuizService> = CompletableDeferred()

    init {
        CoroutineScope(Dispatchers.Default).launch {
            val client = HttpClient {
                installRPC()
            }.rpc {
                url {
                    host = DEV_SERVER_HOST
                    port = 8080
                    encodedPath = "/quiz"
                }
                rpcConfig {
                    serialization {
                        json()
                    }
                }
            }
            val service = client.withService<QuizService>()
            quizServiceDeferred.complete(service)
        }
    }

    private suspend fun getQuizService(): QuizService {
        return quizServiceDeferred.await()
    }

    suspend fun fetchQuizzes(): List<Quiz> {
        return runCatching {
            getQuizService().getQuiz()
        }.onFailure { e ->
            println("Error fetching quizzes: ${e.message}")
        }.getOrDefault(emptyList())
    }

    suspend fun submitAnswers(answerIndexes: List<Int>): QuizResult {
        return runCatching {
            getQuizService().checkAnswer(answerIndexes)
        }.onFailure { e ->
            println("Error submitting answers: ${e.message}")
        }.getOrDefault(QuizResult(0))
    }
}
