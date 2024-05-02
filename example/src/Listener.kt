package github.nyayurn.qbot

import github.nyayurn.yutori_next.*
import github.nyayurn.yutori_next.message.elements.At
import github.nyayurn.yutori_next.message.elements.Text
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import java.io.IOException
import java.util.regex.Pattern

object CommandListener : Listener<MessageEvent> {
    private val commands: Array<Command> = arrayOf(AiCommand, HelpCommand, EchoCommand, StatusCommand)
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)
    override fun invoke(context: Context<MessageEvent>) {
        val (actions, event) = context
        if (qqHelperFilter(event)) return
        val content = event.message.content
        if (content.startsWith("/") or content.startsWith("!")) {
            val msg = content.substring(1)
            if (msg.isNotEmpty()) {
                commands.find { it.test(actions, event, msg) }?.let {
                    logger.info(
                        actions.name,
                        "User ${event.user.id} 触发命令: ${it::class.simpleName}(${event.message.content})"
                    )
                    it.command(actions, event, msg)
                } ?: actions.message.create {
                    channel_id = event.channel.id
                    content {
                        quote { id = event.message.id }
                        text { "未知命令: ${msg.split(" ")[0]}" }
                    }
                }
            }
        }
    }
}

object OpenGraphListener : Listener<MessageEvent> {
    private val pattern = Pattern.compile("(http(s)?://[\\w./-]+)")
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)
    override fun invoke(context: Context<MessageEvent>) {
        val (actions, event) = context
        if (qqHelperFilter(event)) return
        val elementList = MessageUtil.parse(context.config, event.message.content)
        val msg = elementList.filterIsInstance<Text>().joinToString { it.toString() }
        val matcher = pattern.matcher(msg)
        if (matcher.find()) runBlocking {
            val request = HttpClient(CIO) {
                engine { https { trustManager = trustAllCerts } }
            }.use { it.get(matcher.group(0)) }
            try {
                val head = Jsoup.parse(request.body<String>()).head()
                val title = head.getElementsByAttributeValue("property", "og:title").first()?.attr("content")
                val desc = head.getElementsByAttributeValue("property", "og:description").first()?.attr("content")
                val img = head.getElementsByAttributeValue("property", "og:image").first()?.attr("content")
                if (title != null || desc != null || img != null) {
                    logger.info(actions.name, "User ${event.user.id} 触发监听器: $OpenGraphListener")
                    actions.message.create {
                        channel_id = event.channel.id
                        content {
                            quote { id = event.message.id }
                            title?.let {
                                text { title }
                            }
                            desc?.let {
                                if (title != null) {
                                    text { "\n" }
                                }
                                text { desc }
                            }
                            img?.let {
                                img { src = img }
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                if (request.status.value != 200) {
                    actions.message.create {
                        channel_id = event.channel.id
                        content {
                            quote { id = event.message.id }
                            text { "OpenGraph: 获取失败: ${request.status}" }
                        }
                    }
                }
                e.printStackTrace()
            }
        }
    }
}

object AtListener : Listener<MessageEvent> {
    override fun invoke(context: Context<MessageEvent>) {
        val (actions, event) = context
        if (qqHelperFilter(event)) return
        val elementList = MessageUtil.parse(context.config, event.message.content)
        val msg = elementList.filterIsInstance<Text>().joinToString { it.toString() }
        val atBot = elementList[0].let { it is At && it.id == event.self_id }
        if (atBot && msg.isNotEmpty()) {
            GlobalLoggerFactory.getLogger(this::class.java)
                .info(actions.name, "User ${event.user.id} 触发命令: $AiCommand")
            AiCommand.run(actions, event, msg)
        }
    }
}

object YzListener : Listener<MessageEvent> {
    override fun invoke(context: Context<MessageEvent>) {
        val (actions, event) = context
        if (event.user.id == "3583477473") {
            actions.message.delete {
                channel_id = event.channel.id
                message_id = actions.message.create {
                    channel_id = event.channel.id
                    content {
                        quote { id = event.message.id }
                        text { "#撤回" }
                    }
                }[0].id
            }
        }
    }
}