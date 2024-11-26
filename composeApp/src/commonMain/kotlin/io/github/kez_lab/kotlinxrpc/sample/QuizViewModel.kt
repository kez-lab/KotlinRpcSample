package io.github.kez_lab.kotlinxrpc.sample

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.kez_lab.kotlinxrpc.sample.model.Quiz
import io.github.kez_lab.kotlinxrpc.sample.model.QuizResult
import io.github.kez_lab.kotlinxrpc.sample.service.QuizService
import io.ktor.client.HttpClient
import io.ktor.http.encodedPath
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.rpc.krpc.ktor.client.KtorRPCClient
import kotlinx.rpc.krpc.ktor.client.installRPC
import kotlinx.rpc.krpc.ktor.client.rpc
import kotlinx.rpc.krpc.ktor.client.rpcConfig
import kotlinx.rpc.krpc.serialization.json.json
import kotlinx.rpc.withService

expect val DEV_SERVER_HOST: String

val ktorClient = HttpClient {
    installRPC()
}


class QuizViewModel {
    private val viewModelJob = SupervisorJob()
    protected val viewModelScope = CoroutineScope(Dispatchers.Main.immediate + viewModelJob)


    private lateinit var client: KtorRPCClient
    private lateinit var quizService: QuizService

    init {
        viewModelScope.launch {
            client = ktorClient.rpc {
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
            quizService = client.withService<QuizService>()
            loadQuiz()
        }
    }

    var quiz by mutableStateOf<Quiz?>(null)
        private set

    var quizResult by mutableStateOf<QuizResult?>(null)
        private set

    fun loadQuiz() {
        viewModelScope.launch {
            quiz = quizService.getQuiz()
            println(quiz.toString())
        }
    }

    fun clear() {
        viewModelScope.cancel()
    }

    fun submitAnswer(index: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            quizResult = quizService.checkAnswer(index)
        }
    }
}
