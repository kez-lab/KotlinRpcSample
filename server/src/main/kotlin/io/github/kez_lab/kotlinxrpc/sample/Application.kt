package io.github.kez_lab.kotlinxrpc.sample

import io.github.kez_lab.kotlinxrpc.sample.service.QuizService
import io.github.kez_lab.kotlinxrpc.sample.service.QuizServiceImpl
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.routing
import kotlinx.rpc.krpc.ktor.server.RPC
import kotlinx.rpc.krpc.ktor.server.rpc
import kotlinx.rpc.krpc.serialization.json.json
import kotlinx.serialization.json.Json


fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    installCORS()
    configureRPC()
}

fun Application.configureRPC() {
    install(RPC)
    routing {
        rpc("/quiz") {
            rpcConfig {
                serialization {
                    json(Json { prettyPrint = true })
                }
            }
            registerService<QuizService> { coroutineContext ->
                QuizServiceImpl(coroutineContext)
            }
        }
    }
}

fun Application.installCORS() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.Upgrade)
        allowNonSimpleContentTypes = true
        allowCredentials = true
        allowSameOrigin = true

        // webpack-dev-server and local development
        val allowedHosts =
            listOf("localhost:3000", "localhost:8080", "localhost:8081", "127.0.0.1:8080")
        allowedHosts.forEach { host ->
            allowHost(host, listOf("http", "https", "ws", "wss"))
        }
    }
}