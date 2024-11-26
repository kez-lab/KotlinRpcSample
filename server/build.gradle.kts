plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinPluginSerialization)
    application
}

group = "io.github.kez_lab.kotlinxrpc.sample"
version = "1.0.0"
application {
    mainClass.set("io.github.kez_lab.kotlinxrpc.sample.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)

    implementation(libs.kotlinx.rpc.krpc.ktor.server)
    implementation(libs.kotlinx.rpc.krpc.serialization.json)
    implementation(libs.ktor.server.websockets.jvm)
    implementation(libs.ktor.server.cors.jvm)
    implementation(libs.ktor.server.host.common.jvm)

}