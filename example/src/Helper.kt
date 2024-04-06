package github.nyayurn.qbot

import com.alibaba.fastjson2.parseObject
import com.reine.text2image.T2IConstant
import com.reine.text2image.T2IUtil
import github.nyayurn.yutori_next.Actions
import github.nyayurn.yutori_next.MessageEvent
import github.nyayurn.yutori_next.jsonObj
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import oshi.SystemInfo
import oshi.hardware.CentralProcessor
import oshi.hardware.HardwareAbstractionLayer
import java.net.URI
import java.net.URL
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Suppress("SpellCheckingInspection", "SameParameterValue")
object AiUtil {
    private const val URL = "https://spark-api.xf-yun.com/v3.1/chat"
    private val APP_ID = System.getenv("Spark-App-Id")
    private val API_SECRET = System.getenv("Spark-Api-Secret")
    private val API_KEY = System.getenv("Spark-Api-Key")

    fun send(text: String, actions: Actions, event: MessageEvent) {
        val jsonObject = jsonObj {
            putJsonObj("header") {
                put("app_id", APP_ID)
                put("uid", event.user.id)
            }
            putJsonObj("parameter") {
                putJsonObj("chat") {
                    put("domain", "generalv3")
                }
            }
            putJsonObj("payload") {
                putJsonObj("message") {
                    putJsonArr("text") {
                        addJsonObj {
                            put("role", "user")
                            put("content", text)
                        }
                    }
                }
            }
        }
        // 构建鉴权url
        val url = getAuthUrl(URL, API_KEY, API_SECRET).replace("http://", "ws://").replace("https://", "wss://")
        val client: WebSocketClient = SparkWebSocketClient(url, actions, event, jsonObject)
        client.connect()
    }

    private fun getAuthUrl(hostUrl: String, apiKey: String, apiSecret: String): String {
        val url = URL(hostUrl)
        // 时间
        val format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
        format.timeZone = TimeZone.getTimeZone("GMT")
        val date = format.format(Date())
        // 拼接
        val preStr = """
            host: ${url.host}
            date: $date
            GET ${url.path} HTTP/1.1
        """.trimIndent()
        // SHA256加密
        val mac = Mac.getInstance("hmacsha256")
        val spec = SecretKeySpec(apiSecret.toByteArray(StandardCharsets.UTF_8), "hmacsha256")
        mac.init(spec)

        val hexDigits = mac.doFinal(preStr.toByteArray(StandardCharsets.UTF_8))
        // Base64加密
        val sha = Base64.getEncoder().encodeToString(hexDigits)
        // 拼接
        val authorization = """
            api_key="$apiKey", algorithm="hmac-sha256", headers="host date request-line", signature="$sha"
        """.trimIndent()
        // 拼接地址
        return "https://${url.host}${url.path}?authorization=${
            Base64.getEncoder().encodeToString(authorization.toByteArray(StandardCharsets.UTF_8))
        }&date=${
            date.replace(" ", "%20")
        }&host=${url.host}"
    }

    private class SparkWebSocketClient(
        url: String, private val actions: Actions, private val event: MessageEvent, private val content: String
    ) : WebSocketClient(URI(url), Draft_6455()) {
        private val answer = StringBuilder()

        override fun onOpen(serverHandshake: ServerHandshake) = this.send(content)

        override fun onMessage(msg: String) {
            val entity = msg.parseObject<SparkResponse>()
            if (entity.header.code != 0) {
                val code = entity.header.code
                actions.message.create {
                    channel_id = event.channel.id
                    content {
                        quote { id = event.message.id }
                        text { "code: $code\n" }
                        text { entity.header.message }
                    }
                }
                return
            }
            entity.payload?.choices?.let {
                for ((content1) in it.text) {
                    answer.append(content1)
                }
            }
            if (entity.header.status == 2) {
                val content = answer.toString()
                actions.message.create {
                    channel_id = event.channel.id
                    content {
                        quote { id = event.message.id }
                        img {
                            src = "data:image/jpeg;base64," +
                                  T2IUtil(T2IConstant()).drawImageToBase64(content).substring(9)
                        }
                    }
                }
            }
        }

        override fun onClose(i: Int, s: String, b: Boolean) {
            // method is empty
        }

        override fun onError(e: java.lang.Exception) {
            // method is empty
        }
    }
}

object MonitorHelper {
    private val hardware: HardwareAbstractionLayer = SystemInfo().hardware
    val cpu_percent: Double
        get() {
            val pre_ticks = hardware.processor.systemCpuLoadTicks
            TimeUnit.SECONDS.sleep(1)
            val ticks = hardware.processor.systemCpuLoadTicks
            val idle = ticks[CentralProcessor.TickType.IDLE.index] - pre_ticks[CentralProcessor.TickType.IDLE.index]
            val total = ticks.sum() - pre_ticks.sum()
            return 1 - (idle.toDouble() / total)
        }
    val memory_percent: Double
        get() = (1 - memory_available.toDouble() / hardware.memory.total)
    val memory_available: Long
        get() = hardware.memory.available
    val swap_percent: Double
        get() = hardware.memory.virtualMemory.swapUsed.toDouble() / hardware.memory.virtualMemory.swapTotal
    val swap_available: Long
        get() = hardware.memory.virtualMemory.swapTotal - hardware.memory.virtualMemory.swapUsed
}