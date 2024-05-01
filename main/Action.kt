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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package github.nyayurn.yutori_next

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import github.nyayurn.yutori_next.message.ExplainedMessage
import github.nyayurn.yutori_next.message.MessageDslBuilder
import github.nyayurn.yutori_next.message.message
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates

@DslMarker
annotation class ActionDSL

/**
 * 封装所有 Action, 应通过本类对 Satori Server 发送 Satori Action
 * @property channel 频道 API
 * @property guild 群组 API
 * @property login 登录信息 API
 * @property message 消息 API
 * @property reaction 表态 API
 * @property user 用户 API
 * @property friend 好友 API
 * @property properties 配置信息, 供使用者获取
 * @property name 属于哪个 Event Service
 */
class Actions private constructor(
    val channel: ChannelAction,
    val guild: GuildAction,
    val login: LoginAction,
    val message: MessageAction,
    val reaction: ReactionAction,
    val user: UserAction,
    val friend: FriendAction,
    val admin: AdminAction,
    val properties: SatoriProperties,
    val name: String
) {
    constructor(platform: String, self_id: String, properties: SatoriProperties, name: String) : this(
        ChannelAction(platform, self_id, properties, name),
        GuildAction(platform, self_id, properties, name),
        LoginAction(platform, self_id, properties, name),
        MessageAction(platform, self_id, properties, name),
        ReactionAction(platform, self_id, properties, name),
        UserAction(platform, self_id, properties, name),
        FriendAction(platform, self_id, properties, name),
        AdminAction(properties, name), properties, name
    )

    constructor(event: Event, properties: SatoriProperties, name: String) : this(
        event.platform, event.self_id, properties, name
    )
}

class ChannelAction private constructor(private val action: GeneralAction) {
    constructor(platform: String, self_id: String, properties: SatoriProperties, name: String) : this(
        GeneralAction(platform, self_id, properties, "channel", name)
    )

    /**
     * 获取群组频道
     */
    fun get(block: GetBuilder.() -> Unit): Channel {
        val builder = GetBuilder().apply(block)
        return action.sendWithParse("get") {
            put("channel_id", builder.channel_id)
        }
    }

    /**
     * 获取群组频道列表
     */
    fun list(block: ListBuilder.() -> Unit): List<PaginatedData<Channel>> {
        val builder = ListBuilder().apply(block)
        return action.sendWithParse("list") {
            put("guild_id", builder.guild_id)
            put("next", builder.next)
        }
    }

    /**
     * 创建群组频道
     */
    fun create(block: CreateBuilder.() -> Unit): Channel {
        val builder = CreateBuilder().apply(block)
        return action.sendWithParse("create") {
            put("guild_id", builder.guild_id)
            put("data", builder.data)
        }
    }

    /**
     * 修改群组频道
     */
    fun update(block: UpdateBuilder.() -> Unit) {
        val builder = UpdateBuilder().apply(block)
        action.send("update") {
            put("channel_id", builder.channel_id)
            put("data", builder.data)
        }
    }

    /**
     * 删除群组频道
     */
    fun delete(block: DeleteBuilder.() -> Unit) {
        val builder = DeleteBuilder().apply(block)
        action.send("delete") {
            put("channel_id", builder.channel_id)
        }
    }

    @ActionDSL
    class GetBuilder {
        lateinit var channel_id: String
    }

    @ActionDSL
    class ListBuilder {
        lateinit var guild_id: String
        var next: String? = null
    }

    @ActionDSL
    class CreateBuilder {
        lateinit var guild_id: String
        lateinit var data: Channel
    }

    @ActionDSL
    class UpdateBuilder {
        lateinit var channel_id: String
        lateinit var data: Channel
    }

    @ActionDSL
    class DeleteBuilder {
        lateinit var channel_id: String
    }
}

class GuildAction private constructor(
    val member: MemberAction,
    val role: RoleAction,
    private val action: GeneralAction
) {
    constructor(platform: String, self_id: String, properties: SatoriProperties, name: String) : this(
        MemberAction(platform, self_id, properties, name), RoleAction(platform, self_id, properties, name),
        GeneralAction(platform, self_id, properties, "guild", name)
    )

    /**
     * 获取群组
     */
    fun get(block: GetBuilder.() -> Unit): Guild {
        val builder = GetBuilder().apply(block)
        return action.sendWithParse("get") {
            put("guild_id", builder.guild_id)
        }
    }

    /**
     * 获取群组列表
     */
    fun list(block: ListBuilder.() -> Unit): List<PaginatedData<Guild>> {
        val builder = ListBuilder().apply(block)
        return action.sendWithParse("list") {
            put("next", builder.next)
        }
    }

    /**
     * 处理群组邀请
     */
    fun approve(block: ApproveBuilder.() -> Unit) {
        val builder = ApproveBuilder().apply(block)
        action.send("approve") {
            put("message_id", builder.message_id)
            put("approve", builder.approve)
            put("comment", builder.comment)
        }
    }

    @ActionDSL
    class GetBuilder {
        lateinit var guild_id: String
    }

    @ActionDSL
    class ListBuilder {
        var next: String? = null
    }

    @ActionDSL
    class ApproveBuilder {
        lateinit var message_id: String
        var approve: Boolean by Delegates.notNull()
        lateinit var comment: String
    }

    class MemberAction private constructor(val role: RoleAction, private val action: GeneralAction) {
        constructor(platform: String, self_id: String, properties: SatoriProperties, name: String) : this(
            RoleAction(platform, self_id, properties, name),
            GeneralAction(platform, self_id, properties, "guild.member", name)
        )

        /**
         * 获取群组成员
         */
        fun get(block: GetBuilder.() -> Unit): GuildMember {
            val builder = GetBuilder().apply(block)
            return action.sendWithParse("get") {
                put("guild_id", builder.guild_id)
                put("user_id", builder.user_id)
            }
        }

        /**
         * 获取群组成员列表
         */
        fun list(block: ListBuilder.() -> Unit): List<PaginatedData<GuildMember>> {
            val builder = ListBuilder().apply(block)
            return action.sendWithParse("list") {
                put("guild_id", builder.guild_id)
                put("next", builder.next)
            }
        }

        /**
         * 踢出群组成员
         */
        fun kick(block: KickBuilder.() -> Unit) {
            val builder = KickBuilder().apply(block)
            action.send("kick") {
                put("guild_id", builder.guild_id)
                put("user_id", builder.user_id)
                put("permanent", builder.permanent)
            }
        }

        /**
         * 通过群组成员申请
         */
        fun approve(block: ApproveBuilder.() -> Unit) {
            val builder = ApproveBuilder().apply(block)
            action.send("approve") {
                put("message_id", builder.message_id)
                put("approve", builder.approve)
                put("comment", builder.comment)
            }
        }

        @ActionDSL
        class GetBuilder {
            lateinit var guild_id: String
            lateinit var user_id: String
        }

        @ActionDSL
        class ListBuilder {
            lateinit var guild_id: String
            var next: String? = null
        }

        @ActionDSL
        class KickBuilder {
            lateinit var guild_id: String
            lateinit var user_id: String
            var permanent: Boolean? = null
        }

        @ActionDSL
        class ApproveBuilder {
            lateinit var message_id: String
            var approve: Boolean by Delegates.notNull()
            lateinit var comment: String
        }

        class RoleAction private constructor(private val action: GeneralAction) {
            constructor(platform: String, self_id: String, properties: SatoriProperties, name: String) : this(
                GeneralAction(platform, self_id, properties, "guild.member.role", name)
            )

            /**
             * 设置群组成员角色
             */
            fun set(block: SetBuilder.() -> Unit) {
                val builder = SetBuilder().apply(block)
                action.send("set") {
                    put("guild_id", builder.guild_id)
                    put("user_id", builder.user_id)
                    put("role_id", builder.role_id)
                }
            }

            /**
             * 取消群组成员角色
             */
            fun unset(block: UnsetBuilder.() -> Unit) {
                val builder = UnsetBuilder().apply(block)
                action.send("unset") {
                    put("guild_id", builder.guild_id)
                    put("user_id", builder.user_id)
                    put("role_id", builder.role_id)
                }
            }

            @ActionDSL
            class SetBuilder {
                lateinit var guild_id: String
                lateinit var user_id: String
                lateinit var role_id: String
            }

            @ActionDSL
            class UnsetBuilder {
                lateinit var guild_id: String
                lateinit var user_id: String
                lateinit var role_id: String
            }
        }
    }

    class RoleAction private constructor(private val action: GeneralAction) {
        constructor(platform: String, self_id: String, properties: SatoriProperties, name: String) : this(
            GeneralAction(platform, self_id, properties, "guild.role", name)
        )

        /**
         * 获取群组角色列表
         */
        fun list(block: ListBuilder.() -> Unit): List<PaginatedData<GuildRole>> {
            val builder = ListBuilder().apply(block)
            return action.sendWithParse("list") {
                put("guild_id", builder.guild_id)
                put("next", builder.next)
            }
        }

        /**
         * 创建群组角色
         */
        fun create(block: CreateBuilder.() -> Unit): GuildRole {
            val builder = CreateBuilder().apply(block)
            return action.sendWithParse("create") {
                put("guild_id", builder.guild_id)
                put("role", builder.role)
            }
        }

        /**
         * 修改群组角色
         */
        fun update(block: UpdateBuilder.() -> Unit) {
            val builder = UpdateBuilder().apply(block)
            action.send("update") {
                put("guild_id", builder.guild_id)
                put("role_id", builder.role_id)
                put("role", builder.role)
            }
        }

        /**
         * 删除群组角色
         */
        fun delete(block: DeleteBuilder.() -> Unit) {
            val builder = DeleteBuilder().apply(block)
            action.send("delete") {
                put("guild_id", builder.guild_id)
                put("role_id", builder.role_id)
            }
        }

        @ActionDSL
        class ListBuilder {
            lateinit var guild_id: String
            var next: String? = null
        }

        @ActionDSL
        class CreateBuilder {
            lateinit var guild_id: String
            lateinit var role: GuildRole
        }

        @ActionDSL
        class UpdateBuilder {
            lateinit var guild_id: String
            lateinit var role_id: String
            lateinit var role: GuildRole
        }

        @ActionDSL
        class DeleteBuilder {
            lateinit var guild_id: String
            lateinit var role_id: String
        }
    }
}

class LoginAction private constructor(private val action: GeneralAction) {
    constructor(platform: String, self_id: String, properties: SatoriProperties, name: String) : this(
        GeneralAction(platform, self_id, properties, "login", name)
    )

    /**
     * 获取登录信息
     */
    fun get(): Login = action.sendWithParse("get")
}

class MessageAction private constructor(private val action: GeneralAction) {
    constructor(platform: String, self_id: String, properties: SatoriProperties, name: String) : this(
        GeneralAction(platform, self_id, properties, "message", name)
    )

    /**
     * 发送消息
     */
    fun create(block: CreateBuilder.() -> Unit): List<Message> {
        val builder = CreateBuilder().apply(block)
        return action.sendWithParse("create") {
            put("channel_id", builder.channel_id)
            put("content", builder.content.toString().replace("\n", "\\n").replace("\"", "\\\""))
        }
    }

    /**
     * 获取消息
     */
    fun get(block: GetBuilder.() -> Unit): Message {
        val builder = GetBuilder().apply(block)
        return action.sendWithParse("get") {
            put("channel_id", builder.channel_id)
            put("message_id", builder.message_id)
        }
    }

    /**
     * 撤回消息
     */
    fun delete(block: DeleteBuilder.() -> Unit) {
        val builder = DeleteBuilder().apply(block)
        action.send("delete") {
            put("channel_id", builder.channel_id)
            put("message_id", builder.message_id)
        }
    }

    /**
     * 编辑消息
     */
    fun update(block: UpdateBuilder.() -> Unit) {
        val builder = UpdateBuilder().apply(block)
        action.send("update") {
            put("channel_id", builder.channel_id)
            put("message_id", builder.message_id)
            put("content", builder.content.toString().replace("\n", "\\n").replace("\"", "\\\""))
        }
    }

    /**
     * 获取消息列表
     */
    fun list(block: ListBuilder.() -> Unit): List<PaginatedData<Message>> {
        val builder = ListBuilder().apply(block)
        return action.sendWithParse("list") {
            put("channel_id", builder.channel_id)
            put("next", builder.next)
        }
    }

    @ActionDSL
    class CreateBuilder {
        lateinit var channel_id: String
        lateinit var content: ExplainedMessage

        fun content(text: String) {
            content = ExplainedMessage.parse(text)
        }

        fun content(block: MessageDslBuilder.() -> Unit) {
            content = message(block)
        }
    }

    @ActionDSL
    class GetBuilder {
        lateinit var channel_id: String
        lateinit var message_id: String
    }

    @ActionDSL
    class DeleteBuilder {
        lateinit var channel_id: String
        lateinit var message_id: String
    }

    @ActionDSL
    class UpdateBuilder {
        lateinit var channel_id: String
        lateinit var message_id: String
        lateinit var content: ExplainedMessage

        fun content(text: String) {
            content = ExplainedMessage.parse(text)
        }

        fun content(block: MessageDslBuilder.() -> Unit) {
            content = message(block)
        }
    }

    @ActionDSL
    class ListBuilder {
        lateinit var channel_id: String
        var next: String? = null
    }
}

class ReactionAction private constructor(private val action: GeneralAction) {
    constructor(platform: String, self_id: String, properties: SatoriProperties, name: String) : this(
        GeneralAction(platform, self_id, properties, "reaction", name)
    )

    /**
     * 添加表态
     */
    fun create(block: CreateBuilder.() -> Unit) {
        val builder = CreateBuilder().apply(block)
        action.send("create") {
            put("channel_id", builder.channel_id)
            put("message_id", builder.message_id)
            put("emoji", builder.emoji)
        }
    }

    /**
     * 删除表态
     */
    fun delete(block: DeleteBuilder.() -> Unit) {
        val builder = DeleteBuilder().apply(block)
        action.send("delete") {
            put("channel_id", builder.channel_id)
            put("message_id", builder.message_id)
            put("emoji", builder.emoji)
            put("user_id", builder.user_id)
        }
    }

    /**
     * 清除表态
     */
    fun clear(block: ClearBuilder.() -> Unit) {
        val builder = ClearBuilder().apply(block)
        action.send("clear") {
            put("channel_id", builder.channel_id)
            put("message_id", builder.message_id)
            put("emoji", builder.emoji)
        }
    }

    /**
     * 获取表态列表
     */
    fun list(block: ListBuilder.() -> Unit): List<PaginatedData<User>> {
        val builder = ListBuilder().apply(block)
        return action.sendWithParse("list") {
            put("channel_id", builder.channel_id)
            put("message_id", builder.message_id)
            put("emoji", builder.emoji)
            put("next", builder.next)
        }
    }

    @ActionDSL
    class CreateBuilder {
        lateinit var channel_id: String
        lateinit var message_id: String
        lateinit var emoji: String
    }

    @ActionDSL
    class DeleteBuilder {
        lateinit var channel_id: String
        lateinit var message_id: String
        lateinit var emoji: String
        var user_id: String? = null
    }

    @ActionDSL
    class ClearBuilder {
        lateinit var channel_id: String
        lateinit var message_id: String
        var emoji: String? = null
    }

    @ActionDSL
    class ListBuilder {
        lateinit var channel_id: String
        lateinit var message_id: String
        lateinit var emoji: String
        var next: String? = null
    }
}

class UserAction private constructor(val channel: ChannelAction, private val action: GeneralAction) {
    constructor(platform: String, self_id: String, properties: SatoriProperties, name: String) : this(
        ChannelAction(platform, self_id, properties, name), GeneralAction(platform, self_id, properties, "user", name)
    )

    /**
     * 获取用户信息
     */
    fun get(block: GetBuilder.() -> Unit): User {
        val builder = GetBuilder().apply(block)
        return action.sendWithParse("get") {
            put("user_id", builder.user_id)
        }
    }

    @ActionDSL
    class GetBuilder {
        lateinit var user_id: String
    }

    class ChannelAction private constructor(private val action: GeneralAction) {
        constructor(platform: String, self_id: String, properties: SatoriProperties, name: String) : this(
            GeneralAction(platform, self_id, properties, "user.channel", name)
        )

        /**
         * 创建私聊频道
         */
        fun create(block: CreateBuilder.() -> Unit): Channel {
            val builder = CreateBuilder().apply(block)
            return action.sendWithParse("create") {
                put("user_id", builder.user_id)
                put("guild_id", builder.guild_id)
            }
        }

        @ActionDSL
        class CreateBuilder {
            lateinit var user_id: String
            var guild_id: String? = null
        }
    }
}

class FriendAction private constructor(private val action: GeneralAction) {
    constructor(platform: String, self_id: String, properties: SatoriProperties, name: String) : this(
        GeneralAction(platform, self_id, properties, "friend", name)
    )

    /**
     * 获取好友列表
     */
    fun list(block: ListBuilder.() -> Unit): List<PaginatedData<User>> {
        val builder = ListBuilder().apply(block)
        return action.sendWithParse("list") {
            put("next", builder.next)
        }
    }

    /**
     * 处理好友申请
     */
    fun approve(block: ApproveBuilder.() -> Unit) {
        val builder = ApproveBuilder().apply(block)
        action.send("approve") {
            put("message_id", builder.message_id)
            put("approve", builder.approve)
            put("comment", builder.comment)
        }
    }

    @ActionDSL
    class ListBuilder {
        var next: String? = null
    }

    @ActionDSL
    class ApproveBuilder {
        lateinit var message_id: String
        var approve: Boolean by Delegates.notNull()
        var comment: String? = null
    }
}


class AdminAction private constructor(val login: LoginAction, val webhook: WebhookAction) {
    constructor(properties: SatoriProperties, name: String) : this(
        LoginAction(properties, name), WebhookAction(properties, name)
    )

    class LoginAction private constructor(private val action: GeneralAction) {
        constructor(properties: SatoriProperties, name: String) : this(
            GeneralAction(null, null, properties, "login", name)
        )

        /**
         * 获取登录信息列表
         */
        fun list(): List<Login> = action.sendWithParse("list")
    }


    class WebhookAction private constructor(private val action: GeneralAction) {
        constructor(properties: SatoriProperties, name: String) : this(
            GeneralAction(null, null, properties, "webhook", name)
        )

        /**
         * 创建 WebHook
         */
        fun create(block: CreateBuilder.() -> Unit) {
            val builder = CreateBuilder().apply(block)
            action.send("list") {
                put("url", builder.url)
                put("token", builder.token)
            }
        }

        /**
         * 移除 WebHook
         */
        fun delete(block: DeleteBuilder.() -> Unit) {
            val builder = DeleteBuilder().apply(block)
            action.send("approve") {
                put("url", builder.url)
            }
        }


        @ActionDSL
        class CreateBuilder {
            lateinit var url: String
            var token: String? = null
        }


        @ActionDSL
        class DeleteBuilder {
            lateinit var url: String
        }
    }
}

/**
 * Satori Action 实现
 * @property platform 平台
 * @property self_id 自身的 ID
 * @property properties 配置
 * @property resource 资源路径
 * @property name 隶属哪个 Event Service
 * @property mapper JSON 反序列化
 * @property logger 日志接口
 */
class GeneralAction(
    val platform: String?,
    val self_id: String?,
    val properties: SatoriProperties,
    val resource: String,
    val name: String
) {
    val mapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    val logger = GlobalLoggerFactory.getLogger(this::class.java)

    fun send(method: String, body: String? = null): String = runBlocking {
        HttpClient(CIO).use { client ->
            val response = client.post {
                url {
                    host = properties.host
                    port = properties.port
                    appendPathSegments(properties.path, properties.version, "$resource.$method")
                }
                contentType(ContentType.Application.Json)
                headers {
                    properties.token?.let { append(HttpHeaders.Authorization, "Bearer ${properties.token}") }
                    platform?.let {
                        @Suppress("UastIncorrectHttpHeaderInspection") append("X-Platform", platform)
                    }
                    self_id?.let {
                        @Suppress("UastIncorrectHttpHeaderInspection") append("X-Self-ID", self_id)
                    }
                }
                body?.let { setBody(body) }
                logger.debug(
                    name, """
                    Satori Action: url: ${this.url},
                        headers: ${this.headers.build()},
                        body: ${this.body}
                    """.trimIndent()
                )
            }
            logger.debug(name, "Satori Action Response: $response")
            response.body()
        }
    }

    inline fun send(method: String, block: JsonObjectDSLBuilder.() -> Unit) = send(method, jsonObj(block))

    inline fun <reified T> sendWithParse(method: String, block: JsonObjectDSLBuilder.() -> Unit = {}): T {
        val response = send(method, jsonObj(block))
        return try {
            mapper.readValue<T>(response)
        } catch (e: Exception) {
            logger.warn(name, response)
            throw ResponseParsingException(response)
        }
    }
}