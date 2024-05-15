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

@file:Suppress("MemberVisibilityCanBePrivate", "unused", "HttpUrlsUsage")

package github.nyayurn.yutori_next.module.adapter.satori

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import github.nyayurn.yutori_next.*
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Satori 事件服务的 WebSocket 实现
 * @param properties Satori Server 配置
 * @param satori Satori 配置
 */
class WebSocketEventService(val properties: SatoriProperties, val satori: Satori) : EventService {
    private var is_received_pong = false
    private var sequence: Number? = null
    private val service = SatoriActionService(properties, satori.name)
    private val client = HttpClient {
        install(WebSockets) {
            contentConverter = JacksonWebsocketContentConverter()
        }
    }
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)

    override suspend fun connect(): Unit = client.webSocket(
        HttpMethod.Get, properties.host, properties.port, "${properties.path}/${properties.version}/events"
    ) {
        try {
            logger.info(satori.name, "成功建立 WebSocket 连接, 发送身份验证并尝试建立事件推送服务")
            sendSerialized(IdentifySignaling(body = Identify(properties.token, sequence)))
            withTimeoutOrNull(10000L) {
                val ready = receiveDeserialized<ReadySignaling>().body
                logger.info(satori.name, "成功建立事件推送服务: ${ready.logins}")
            } ?: throw TimeoutException("无法建立事件推送服务: READY 响应超时")
            launch {
                delay(10000)
                logger.debug(satori.name, "发送 PING")
                sendSerialized(PingSignaling)
                delay(10000)
                if (!is_received_pong) throw TimeoutException("PONG 响应超时")
            }
            while (true) try {
                when (val signaling = receiveDeserialized<Signaling>()) {
                    is EventSignaling -> launch {
                        try {
                            val event = signaling.body
                            when (event.type) {
                                MessageEvents.Created -> logger.info(satori.name, buildString {
                                    append("${event.platform}(${event.self_id}) 接收事件(${event.type}): ")
                                    append("\u001B[38;5;4m").append("${event.channel!!.name}(${event.channel!!.id})")
                                    append("\u001B[38;5;8m").append("-")
                                    append("\u001B[38;5;6m")
                                    append(
                                        "${event.member?.nick ?: event.user!!.nick ?: event.user!!.name}(${event.user!!.id})"
                                    )
                                    append("\u001B[0m").append(": ").append(event.message!!.content)
                                })

                                else -> logger.info(
                                    satori.name, "${event.platform}(${event.self_id}) 接收事件: ${event.type}"
                                )
                            }
                            logger.debug(satori.name, "事件详细信息: $signaling")
                            sequence = event.id
                            satori.container(event, satori, service)
                        } catch (e: Exception) {
                            logger.warn(satori.name, "处理事件时出错($signaling): ${e.localizedMessage}")
                            e.printStackTrace()
                        }
                    }

                    is PongSignaling -> {
                        logger.debug(satori.name, "收到 PONG")
                        is_received_pong = true
                        launch {
                            delay(10000)
                            logger.debug(satori.name, "发送 PING")
                            sendSerialized(PingSignaling)
                            is_received_pong = false
                            delay(10000)
                            if (!is_received_pong) throw TimeoutException("PONG 响应超时")
                        }
                    }
                }
            } catch (e: JsonConvertException) {
                logger.warn(satori.name, "事件解析错误: ${e.localizedMessage}")
            }
        } catch (e: Exception) {
            logger.warn(satori.name, "WebSocket 连接断开: ${e.localizedMessage}")
            e.printStackTrace()
            launch {
                close()
                logger.info(satori.name, "将在5秒后尝试重新连接")
                delay(5000)
                logger.info(satori.name, "尝试重新连接")
                connect()
            }
        }
    }

    override fun close() {
        client.close()
    }
}

/**
 * Satori 事件服务的 WebHook 实现
 * @param properties Satori WebHook 配置
 * @param satori Satori 配置
 */
class WebhookEventService(
    val listen: String, val port: Int, val path: String, val properties: SatoriProperties, val satori: Satori
) : EventService {
    private var client: ApplicationEngine? = null
    private val service = SatoriActionService(properties, satori.name)
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)

    override suspend fun connect() {
        client = embeddedServer(io.ktor.server.cio.CIO, port, listen) {
            routing {
                post(path) {
                    val authorization = call.request.headers["Authorization"]
                    if (authorization != properties.token) {
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
                                is MessageEvent -> logger.info(satori.name, buildString {
                                    append("${event.platform}(${event.self_id}) 接收事件(${event.type}): ")
                                    append("\u001B[38;5;4m").append("${event.channel.name}(${event.channel.id})")
                                    append("\u001B[38;5;6m")
                                    append(event.member?.nick ?: event.user.nick ?: event.user.name)
                                    append("(${event.user.id})")
                                    append("\u001B[0m").append(": ").append(event.message.content)
                                })

                                else -> logger.info(
                                    satori.name, "${event.platform}(${event.self_id}) 接收事件: ${event.type}"
                                )
                            }
                            logger.debug(satori.name, "事件详细信息: $body")
                            satori.container(event, satori, service)
                        }
                        call.response.status(HttpStatusCode.OK)
                    } catch (e: Exception) {
                        logger.warn(satori.name, "处理事件时出错(${body}): ${e.localizedMessage}")
                        e.printStackTrace()
                        call.response.status(HttpStatusCode.InternalServerError)
                    }
                }
            }
        }.start()
        logger.info(satori.name, "成功启动 HTTP 服务器")
        ActionsContainer.AdminAction(satori.name, service).webhook.create {
            url = "http://${properties.host}:${properties.port}${properties.path}"
            token = properties.token
        }
    }

    override fun close() {
        ActionsContainer.AdminAction(satori.name, service).webhook.delete {
            url = "http://${properties.host}:${properties.port}${properties.path}"
        }
        client?.stop()
    }
}