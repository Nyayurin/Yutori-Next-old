package github.nyayurn.qbot

import github.nyayurn.yutori_next.Actions
import github.nyayurn.yutori_next.MessageEvent
import github.nyayurn.yutori_next.MessageUtil.decode

interface Command {
    fun test(actions: Actions, event: MessageEvent, msg: String): Boolean
    fun command(actions: Actions, event: MessageEvent, msg: String)
}

object AiCommand : Command {
    private val regex = Regex("^[Aa]i( [\\w\\W]*)?$")
    override fun test(actions: Actions, event: MessageEvent, msg: String) = regex.matches(msg)

    override fun command(actions: Actions, event: MessageEvent, msg: String) {
        regex.matchEntire(msg)?.groupValues?.get(1)?.let {
            run(actions, event, it)
        } ?: actions.message.create {
            channel_id = event.channel.id
            content {
                quote { id = event.message.id }
                text { "缺少参数" }
            }
        }
    }

    fun run(actions: Actions, event: MessageEvent, msg: String) {
        AiUtil.send(msg.trim(), actions, event)
    }
}

object HelpCommand : Command {
    private val regex = Regex("^[Hh]elp( [\\w\\W]*)?$")
    override fun test(actions: Actions, event: MessageEvent, msg: String) = regex.matches(msg)

    override fun command(actions: Actions, event: MessageEvent, msg: String) {
        actions.message.create {
            channel_id = event.channel.id
            content {
                quote { id = event.message.id }
                text {
                    """
                    反馈问题请找Bot姐姐(799712878)
                    命令前缀:
                        /(左斜线, 同MC的指令前缀)
                        !(英文感叹号)
                    命令列表:
                        ai
                        help
                    其他内容:
                        如果发送的消息是符合OpenGraph的网址的链接(如github), 会自动发送预览图
                    """.trimIndent()
                }
            }
        }
    }
}

object EchoCommand : Command {
    private val regex = Regex("^[Ee]cho ([\\w\\W]*)$")
    override fun test(actions: Actions, event: MessageEvent, msg: String) = regex.matches(msg)

    override fun command(actions: Actions, event: MessageEvent, msg: String) {
        val qq = event.platform == "chronocat" && (event.user.id == "799712878" || event.user.id == "3175473426")
        val guild = event.platform == "qqguild" && event.user.id == "6917646451525197539"
        if (qq || guild) {
            actions.message.create {
                channel_id = event.channel.id
                content(msg.decode().substring(5))
            }
            return
        }
        actions.message.create {
            channel_id = event.channel.id
            content {
                quote { id = event.message.id }
                text { "你不是Bot姐姐或Bot姐姐的男盆友, 别乱用指令!" }
            }
        }
    }
}

object StatusCommand : Command {
    private val regex = Regex("^[Ss]tatus( [\\w\\W]*)?$")
    override fun test(actions: Actions, event: MessageEvent, msg: String) = regex.matches(msg)

    override fun command(actions: Actions, event: MessageEvent, msg: String) {
        actions.message.create {
            channel_id = event.channel.id
            content {
                text { "处理器: " }
                text { String.format("%.2f%%", MonitorHelper.cpu_percent * 100) }
                text { "\n" }
                text { "物理内存: " }
                text { String.format("%.2f%%", MonitorHelper.memory_percent * 100) }
                text { String.format("(%.2f MiB free)", MonitorHelper.memory_available.toDouble() / 1024 / 1024) }
                text { "\n" }
                text { "交换内存: " }
                text { String.format("%.2f%%", MonitorHelper.swap_percent * 100) }
                text { String.format("(%.2f MiB free)", MonitorHelper.swap_available.toDouble() / 1024 / 1024) }
            }
        }
    }
}