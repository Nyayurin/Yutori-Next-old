/*
Copyright (c) 2024 Yurn
Yutori-Next is licensed under Mulan PSL v2.
You can use this software according to the terms and conditions of the Mulan PSL v2.
You may obtain a copy of Mulan PSL v2 at:
         http://license.coscl.org.cn/MulanPSL2
THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
See the Mulan PSL v2 for more details.
 */

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.github.nyayurn.yutori.next

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.websocket.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Satori 事件服务接口, 用于与 Satori Server 进行通信
 */
interface SatoriEventService : AutoCloseable {
    /**
     * 与 Satori Server 建立连接
     */
    fun connect(): SatoriEventService
}

/**
 * Satori 事件服务的 WebSocket 实现
 * @param container 监听器容器
 * @param properties Satori Server 配置
 * @param name 用于区分不同 Satori 事件服务的名称
 */
class WebSocketEventService(
    val container: ListenersContainer,
    val properties: SatoriProperties,
    val name: String = "Satori"
) : SatoriEventService {
    private var sequence: Number? = null
    private var isConnected = false
    private val client = HttpClient {
        install(WebSockets)
    }
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    override fun connect(): SatoriEventService {
        GlobalScope.launch {
            try {
                client.webSocket(
                    HttpMethod.Get,
                    properties.host,
                    properties.port,
                    "${properties.path}/${properties.version}/events"
                ) {
                    logger.info(name, "成功建立 WebSocket 连接")
                    isConnected = true
                    launch { sendIdentity(this@webSocket) }
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        try {
                            onEvent(frame.readText())
                        } catch (e: Exception) {
                            logger.warn(name, "处理事件时出错(${frame.readText()}): ${e.localizedMessage}")
                            e.printStackTrace()
                        }
                    }
                }
            } catch (e: Exception) {
                logger.warn(name, "WebSocket 连接断开: ${e.localizedMessage}")
                e.printStackTrace()
                isConnected = false
                // 重连
                launch {
                    logger.info(name, "将在5秒后尝试重新连接")
                    delay(5000)
                    logger.info(name, "尝试重新连接")
                    connect()
                }
            }
        }
        return this
    }

    override fun close() {
        isConnected = false
        client.close()
    }

    private suspend fun sendIdentity(session: DefaultClientWebSocketSession) {
        val token = properties.token
        val content = jsonObj {
            put("op", Signaling.IDENTIFY)
            if (token != null || sequence != null) putJsonObj("body") {
                put("token", token)
                put("sequence", sequence)
            }
        }
        logger.info(name, "发送身份验证: $content")
        session.send(content)
    }

    private fun DefaultClientWebSocketSession.onEvent(body: String) {
        val signaling = Signaling.parse(body)
        when (signaling.op) {
            Signaling.READY -> {
                val ready = signaling.body as Ready
                logger.info(name, "成功建立事件推送服务: ${
                    ready.logins.joinToString(", ") { "${it.platform}(${it.selfId}, ${it.status})" }
                }")
                // 心跳
                launch {
                    val content = jsonObj { put("op", Signaling.PING) }
                    while (isConnected) {
                        delay(10000)
                        send(content)
                    }
                }
            }

            Signaling.EVENT -> launch {
                val event = signaling.body as Event
                when (event) {
                    is MessageEvent -> logger.info(name, buildString {
                        append("${event.platform}(${event.selfId}) 接收事件(${event.type}): ")
                        append("\u001B[38;5;4m").append("${event.channel.name}(${event.channel.id})")
                        append("\u001B[38;5;8m").append("-")
                        append("\u001B[38;5;6m")
                        append("${event.member?.nick ?: event.user.nick ?: event.user.name}(${event.user.id})")
                        append("\u001B[0m").append(": ").append(event.message.content)
                    })

                    else -> logger.info(name, "${event.platform}(${event.selfId}) 接收事件: ${event.type}")
                }
                logger.debug(name, "事件详细信息: $body")
                sequence = event.id
                container.runEvent(event, properties)
            }

            Signaling.PONG -> logger.debug(name, "收到 PONG")
            else -> logger.error(name, "Unsupported $signaling")
        }
    }

    companion object {
        fun of(
            container: ListenersContainer,
            properties: SatoriProperties = SatoriProperties(),
            name: String = "Satori"
        ) = WebSocketEventService(container, properties, name)

        inline fun of(name: String = "Satori", dsl: Builder.() -> Unit) = Builder(name).apply(dsl).build()

        fun connect(
            container: ListenersContainer,
            properties: SatoriProperties = SatoriProperties(),
            name: String = "Satori"
        ) = WebSocketEventService(container, properties, name).connect()

        inline fun connect(name: String = "Satori", dsl: Builder.() -> Unit) =
            Builder(name).apply(dsl).build().connect()
    }

    class Builder(private val name: String) {
        var container: ListenersContainer = ListenersContainer.of(name)
        var properties: SatoriProperties = SatoriProperties()

        fun listeners(lambda: ListenersContainer.() -> Unit) {
            container = ListenersContainer.of(name, lambda)
        }

        fun properties(lambda: PropertiesBuilder.() -> Unit) {
            properties = PropertiesBuilder().apply(lambda).build()
        }

        fun build() = WebSocketEventService(container, properties, name)

        class PropertiesBuilder {
            var host: String = "127.0.0.1"
            var port: Int = 5500
            var path: String = ""
            var token: String? = null
            var version: String = "v1"
            fun build() = SatoriProperties(host, port, path, token, version)
        }
    }
}

/**
 * Satori 事件服务的 WebHook 实现
 * @param container 监听器容器
 * @param properties Satori WebHook 配置
 * @param name 用于区分不同 Satori 事件服务的名称
 */
class WebHookEventService(
    val container: ListenersContainer,
    val properties: WebHookProperties,
    val name: String = "Satori"
) : SatoriEventService {
    private var client: ApplicationEngine? = null
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    override fun connect(): SatoriEventService {
        GlobalScope.launch {
            client = embeddedServer(CIO, properties.serverPort, properties.serverHost) {
                routing {
                    post("/") {
                        val authorization = call.request.headers["Authorization"]
                        if (authorization != properties.server.token) {
                            call.response.status(HttpStatusCode.Unauthorized)
                            return@post
                        }
                        val body = call.receiveText()
                        try {
                            val mapper = jacksonObjectMapper().configure(
                                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false
                            )
                            val event = mapper.readValue<Event>(body)
                            launch {
                                when (event) {
                                    is MessageEvent -> logger.info(name, buildString {
                                        append("${event.platform}(${event.selfId}) 接收事件(${event.type}): ")
                                        append("\u001B[38;5;4m").append("${event.channel.name}(${event.channel.id})")
                                        append("\u001B[38;5;6m")
                                        append(event.member?.nick ?: event.user.nick ?: event.user.name)
                                        append("(${event.user.id})")
                                        append("\u001B[0m").append(": ").append(event.message.content)
                                    })

                                    else -> logger.info(
                                        name, "${event.platform}(${event.selfId}) 接收事件: ${event.type}"
                                    )
                                }
                                logger.debug(name, "事件详细信息: $body")
                                container.runEvent(event, properties.server)
                            }
                            call.response.status(HttpStatusCode.OK)
                        } catch (e: Exception) {
                            logger.warn(name, "处理事件时出错(${body}): ${e.localizedMessage}")
                            e.printStackTrace()
                            call.response.status(HttpStatusCode.InternalServerError)
                        }
                    }
                }
            }.start()
            logger.info(name, "成功启动 HTTP 服务器")
            @Suppress("HttpUrlsUsage") AdminAction.of(properties.server, name).webhook.create(
                "http://${properties.serverHost}:${properties.serverPort}", properties.server.token
            )
        }
        return this
    }

    override fun close() {
        client?.stop()
    }

    companion object {
        fun of(
            container: ListenersContainer,
            properties: WebHookProperties = WebHookProperties(server = SatoriProperties()),
            name: String = "Satori",
        ) = WebHookEventService(container, properties, name)

        fun of(name: String = "Satori", dsl: Builder.() -> Unit) = Builder(name).apply(dsl).build()

        fun connect(
            container: ListenersContainer,
            properties: WebHookProperties = WebHookProperties(server = SatoriProperties()),
            name: String = "Satori",
        ) = WebHookEventService(container, properties, name).connect()

        fun connect(name: String = "Satori", dsl: Builder.() -> Unit) = Builder(name).apply(dsl).build().connect()
    }

    class Builder(private val name: String) {
        var container: ListenersContainer = ListenersContainer.of(name)
        var properties: WebHookProperties = WebHookProperties(server = SatoriProperties())

        fun listeners(lambda: ListenersContainer.() -> Unit) {
            container = ListenersContainer.of(name, lambda)
        }

        fun properties(lambda: PropertiesBuilder.() -> Unit) {
            properties = PropertiesBuilder().apply(lambda).build()
        }

        fun properties(lambda: () -> WebHookProperties) {
            this.properties = lambda()
        }

        fun build() = WebHookEventService(container, properties, name)

        class PropertiesBuilder {
            var server: Server = Server()
            var host: String = "127.0.0.1"
            var port: Int = 5500
            var path: String = ""
            var token: String? = null
            var version: String = "v1"
            fun build() =
                WebHookProperties(server.host, server.port, SatoriProperties(host, port, path, token, version))

            class Server {
                var host: String = "0.0.0.0"
                var port: Int = 8080
            }
        }
    }
}