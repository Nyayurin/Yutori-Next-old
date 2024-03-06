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

package com.github.nyayurn.yutori.next

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.nyayurn.yutori.next.message.MessageDslBuilder
import com.github.nyayurn.yutori.next.message.MessageSegment
import com.github.nyayurn.yutori.next.message.message
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

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
    companion object {
        /**
         * 工厂方法
         * @param platform 平台
         * @param selfId 自己 ID
         * @param properties 配置
         */
        fun of(platform: String, selfId: String, properties: SatoriProperties, name: String) = Actions(
            ChannelAction.of(platform, selfId, properties, name),
            GuildAction.of(platform, selfId, properties, name),
            LoginAction.of(platform, selfId, properties, name),
            MessageAction.of(platform, selfId, properties, name),
            ReactionAction.of(platform, selfId, properties, name),
            UserAction.of(platform, selfId, properties, name),
            FriendAction.of(platform, selfId, properties, name),
            AdminAction.of(properties, name),
            properties,
            name
        )

        /**
         * 工厂方法
         * @param platform 平台
         * @param selfId 自己 ID
         * @param dsl 配置 DSL
         */
        inline fun of(
            platform: String,
            selfId: String,
            name: String,
            dsl: WebSocketEventService.Builder.PropertiesBuilder.() -> Unit
        ) = of(platform, selfId, WebSocketEventService.Builder.PropertiesBuilder().apply(dsl).build(), name)

        /**
         * 工厂方法
         * @param event 事件
         * @param properties 配置
         */
        fun of(event: Event, properties: SatoriProperties, name: String) =
            of(event.platform, event.selfId, properties, name)

        /**
         * 工厂方法
         * @param event 事件
         * @param dsl 配置 DSL
         */
        inline fun of(
            event: Event,
            name: String,
            dsl: WebSocketEventService.Builder.PropertiesBuilder.() -> Unit
        ) = of(event, WebSocketEventService.Builder.PropertiesBuilder().apply(dsl).build(), name)
    }
}

class ChannelAction private constructor(private val generalAction: GeneralAction) {
    /**
     * 获取群组频道
     * @param channelId 频道 ID
     */
    fun get(channelId: String): Channel {
        return generalAction.sendWithSerialize("get") {
            put("channel_id", channelId)
        }
    }

    /**
     * 获取群组频道列表
     * @param guildId 群组 ID
     * @param next 分页令牌
     */
    fun list(guildId: String, next: String? = null): List<PaginatedData<Channel>> {
        return generalAction.sendWithSerialize("list") {
            put("guild_id", guildId)
            put("next", next)
        }
    }

    /**
     * 创建群组频道
     * @param guildId 群组 ID
     * @param data 频道数据
     */
    fun create(guildId: String, data: Channel): Channel {
        return generalAction.sendWithSerialize("create") {
            put("guild_id", guildId)
            put("data", data)
        }
    }

    /**
     * 修改群组频道
     * @param channelId 频道 ID
     * @param data 频道数据
     */
    fun update(channelId: String, data: Channel) {
        generalAction.send("update") {
            put("channel_id", channelId)
            put("data", data)
        }
    }

    /**
     * 删除群组频道
     * @param channelId 频道 ID
     */
    fun delete(channelId: String) {
        generalAction.send("delete") {
            put("channel_id", channelId)
        }
    }

    companion object {
        fun of(platform: String, selfId: String, properties: SatoriProperties, name: String) =
            ChannelAction(GeneralAction(platform, selfId, properties, "channel", name))
    }
}

class GuildAction private constructor(
    val member: MemberAction,
    val role: RoleAction,
    private val generalAction: GeneralAction
) {
    /**
     * 获取群组
     * @param guildId 群组 ID
     */
    fun get(guildId: String): Guild {
        return generalAction.sendWithSerialize("get") {
            put("guild_id", guildId)
        }
    }

    /**
     * 获取群组列表
     * @param next 分页令牌
     */
    fun list(next: String? = null): List<PaginatedData<Guild>> {
        return generalAction.sendWithSerialize("list") {
            put("next", next)
        }
    }

    /**
     * 处理群组邀请
     * @param messageId 请求 ID
     * @param approve 是否通过请求
     * @param comment 备注信息
     */
    fun approve(messageId: String, approve: Boolean, comment: String) {
        generalAction.send("approve") {
            put("message_id", messageId)
            put("approve", approve)
            put("comment", comment)
        }
    }

    companion object {
        fun of(platform: String, selfId: String, properties: SatoriProperties, name: String) = GuildAction(
            MemberAction.of(platform, selfId, properties, name),
            RoleAction.of(platform, selfId, properties, name),
            GeneralAction(platform, selfId, properties, "guild", name)
        )
    }

    class MemberAction private constructor(
        val role: RoleAction,
        private val generalAction: GeneralAction
    ) {
        /**
         * 获取群组成员
         * @param guildId 群组 ID
         * @param userId 用户 ID
         */
        fun get(guildId: String, userId: String): GuildMember {
            return generalAction.sendWithSerialize("get") {
                put("guild_id", guildId)
                put("user_id", userId)
            }
        }

        /**
         * 获取群组成员列表
         * @param guildId 群组 ID
         * @param next 分页令牌
         */
        fun list(guildId: String, next: String? = null): List<PaginatedData<GuildMember>> {
            return generalAction.sendWithSerialize("list") {
                put("guild_id", guildId)
                put("next", next)
            }
        }

        /**
         * 踢出群组成员
         * @param guildId 群组 ID
         * @param userId 用户 ID
         * @param permanent 是否永久踢出 (无法再次加入群组)
         */
        fun kick(guildId: String, userId: String, permanent: Boolean? = null) {
            generalAction.send("kick") {
                put("guild_id", guildId)
                put("user_id", userId)
                put("permanent", permanent)
            }
        }

        /**
         * 通过群组成员申请
         * @param messageId 请求 ID
         * @param approve 是否通过请求
         * @param comment 备注信息
         */
        fun approve(messageId: String, approve: Boolean, comment: String? = null) {
            generalAction.send("approve") {
                put("message_id", messageId)
                put("approve", approve)
                put("comment", comment)
            }
        }

        companion object {
            fun of(platform: String, selfId: String, properties: SatoriProperties, name: String) = MemberAction(
                RoleAction.of(platform, selfId, properties, name),
                GeneralAction(platform, selfId, properties, "guild.member", name)
            )
        }

        class RoleAction private constructor(private val generalAction: GeneralAction) {
            /**
             * 设置群组成员角色
             * @param guildId 群组 ID
             * @param userId 用户 ID
             * @param roleId 角色 ID
             */
            fun set(guildId: String, userId: String, roleId: String) {
                generalAction.send("set") {
                    put("guild_id", guildId)
                    put("user_id", userId)
                    put("role_id", roleId)
                }
            }

            /**
             * 取消群组成员角色
             * @param guildId 群组 ID
             * @param userId 用户 ID
             * @param roleId 角色 ID
             */
            fun unset(guildId: String, userId: String, roleId: String) {
                generalAction.send("unset") {
                    put("guild_id", guildId)
                    put("user_id", userId)
                    put("role_id", roleId)
                }
            }

            companion object {
                fun of(platform: String, selfId: String, properties: SatoriProperties, name: String) =
                    RoleAction(GeneralAction(platform, selfId, properties, "guild.member.role", name))
            }
        }
    }

    class RoleAction private constructor(private val generalAction: GeneralAction) {
        /**
         * 获取群组角色列表
         * @param guildId 群组 ID
         * @param next 分页令牌
         */
        fun list(guildId: String, next: String? = null): List<PaginatedData<GuildRole>> {
            return generalAction.sendWithSerialize("list") {
                put("guild_id", guildId)
                put("next", next)
            }
        }

        /**
         * 创建群组角色
         * @param guildId 群组 ID
         * @param role 角色数据
         */
        fun create(guildId: String, role: GuildRole): GuildRole {
            return generalAction.sendWithSerialize("create") {
                put("guild_id", guildId)
                put("role", role)
            }
        }

        /**
         * 修改群组角色
         * @param guildId 群组 ID
         * @param roleId 角色 ID
         * @param role 角色数据
         */
        fun update(guildId: String, roleId: String, role: GuildRole) {
            generalAction.send("update") {
                put("guild_id", guildId)
                put("role_id", roleId)
                put("role", role)
            }
        }

        /**
         * 删除群组角色
         * @param guildId 群组 ID
         * @param roleId 角色 ID
         */
        fun delete(guildId: String, roleId: String) {
            generalAction.send("delete") {
                put("guild_id", guildId)
                put("role_id", roleId)
            }
        }

        companion object {
            fun of(platform: String, selfId: String, properties: SatoriProperties, name: String) =
                RoleAction(GeneralAction(platform, selfId, properties, "guild.role", name))
        }
    }
}

class LoginAction private constructor(private val generalAction: GeneralAction) {
    /**
     * 获取登录信息
     */
    fun get(): Login = generalAction.sendWithSerialize("get")

    companion object {
        fun of(platform: String, selfId: String, properties: SatoriProperties, name: String) =
            LoginAction(GeneralAction(platform, selfId, properties, "login", name))
    }
}

class MessageAction private constructor(private val generalAction: GeneralAction) {
    /**
     * 发送消息
     * @param channelId 频道 ID
     * @param content 消息内容
     */
    fun create(channelId: String, content: MessageSegment): List<Message> {
        return generalAction.sendWithSerialize("create") {
            put("channel_id", channelId)
            put("content", content.toString().replace("\n", "\\n").replace("\"", "\\\""))
        }
    }

    /**
     * 使用纯文本发送消息
     * @param channelId 频道 ID
     * @param text 消息内容
     */
    fun create(channelId: String, text: String) = create(channelId, MessageSegment.of(text))

    /**
     * 使用 DSL 发送消息
     * @param channelId 频道 ID
     * @param block 消息内容 DSL
     */
    inline fun create(channelId: String, block: MessageDslBuilder.() -> Unit) = create(channelId, message(block))


    /**
     * 获取消息
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     */
    fun get(channelId: String, messageId: String): Message {
        return generalAction.sendWithSerialize("get") {
            put("channel_id", channelId)
            put("message_id", messageId)
        }
    }

    /**
     * 撤回消息
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     */
    fun delete(channelId: String, messageId: String) {
        generalAction.send("delete") {
            put("channel_id", channelId)
            put("message_id", messageId)
        }
    }

    /**
     * 编辑消息
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @param content 消息内容
     */
    fun update(channelId: String, messageId: String, content: MessageSegment) {
        generalAction.send("update") {
            put("channel_id", channelId)
            put("message_id", messageId)
            put("content", content.toString().replace("\n", "\\n").replace("\"", "\\\""))
        }
    }

    /**
     * 使用纯文本编辑消息
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @param text 消息内容
     */
    fun update(channelId: String, messageId: String, text: String) =
        update(channelId, messageId, MessageSegment.of(text))

    /**
     * 使用 DSL 编辑消息
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @param block 消息内容 DSL
     */
    inline fun update(channelId: String, messageId: String, block: MessageDslBuilder.() -> Unit) =
        update(channelId, messageId, message(block))

    /**
     * 获取消息列表
     * @param channelId 频道 ID
     * @param next 分页令牌
     */
    fun list(channelId: String, next: String? = null): List<PaginatedData<Message>> {
        return generalAction.sendWithSerialize("list") {
            put("channel_id", channelId)
            put("next", next)
        }
    }

    companion object {
        fun of(platform: String, selfId: String, properties: SatoriProperties, name: String) =
            MessageAction(GeneralAction(platform, selfId, properties, "message", name))
    }
}

class ReactionAction private constructor(private val generalAction: GeneralAction) {
    /**
     * 添加表态
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @param emoji 表态名称
     */
    fun create(channelId: String, messageId: String, emoji: String) {
        generalAction.send("create") {
            put("channel_id", channelId)
            put("message_id", messageId)
            put("emoji", emoji)
        }
    }

    /**
     * 删除表态
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @param emoji 表态名称
     * @param userId 用户 ID
     */
    fun delete(channelId: String, messageId: String, emoji: String, userId: String? = null) {
        generalAction.send("delete") {
            put("channel_id", channelId)
            put("message_id", messageId)
            put("emoji", emoji)
            put("user_id", userId)
        }
    }

    /**
     * 清除表态
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @param emoji 表态名称
     */
    fun clear(channelId: String, messageId: String, emoji: String? = null) {
        generalAction.send("clear") {
            put("channel_id", channelId)
            put("message_id", messageId)
            put("emoji", emoji)
        }
    }

    /**
     * 获取表态列表
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @param emoji 表态名称
     * @param next 分页令牌
     */
    fun list(channelId: String, messageId: String, emoji: String, next: String? = null): List<PaginatedData<User>> {
        return generalAction.sendWithSerialize("list") {
            put("channel_id", channelId)
            put("message_id", messageId)
            put("emoji", emoji)
            put("next", next)
        }
    }

    companion object {
        fun of(platform: String, selfId: String, properties: SatoriProperties, name: String) =
            ReactionAction(GeneralAction(platform, selfId, properties, "reaction", name))
    }
}

class UserAction private constructor(val channel: ChannelAction, private val generalAction: GeneralAction) {
    /**
     * 获取用户信息
     * @param userId 用户 ID
     */
    fun get(userId: String): User {
        return generalAction.sendWithSerialize("get") {
            put("user_id", userId)
        }
    }

    companion object {
        fun of(platform: String, selfId: String, properties: SatoriProperties, name: String) = UserAction(
            ChannelAction.of(platform, selfId, properties, name),
            GeneralAction(platform, selfId, properties, "user", name)
        )
    }

    class ChannelAction private constructor(private val generalAction: GeneralAction) {
        /**
         * 创建私聊频道
         * @param userId 用户 ID
         * @param guildId 群组 ID
         */
        fun create(userId: String, guildId: String? = null): Channel {
            return generalAction.sendWithSerialize("create") {
                put("user_id", userId)
                put("guild_id", guildId)
            }
        }

        companion object {
            fun of(platform: String, selfId: String, properties: SatoriProperties, name: String) =
                ChannelAction(GeneralAction(platform, selfId, properties, "user.channel", name))
        }
    }
}

class FriendAction private constructor(private val generalAction: GeneralAction) {
    /**
     * 获取好友列表
     * @param next 分页令牌
     */
    fun list(next: String? = null): List<PaginatedData<User>> {
        return generalAction.sendWithSerialize("list") {
            put("next", next)
        }
    }

    /**
     * 处理好友申请
     * @param messageId 请求 ID
     * @param approve 是否通过请求
     * @param comment 备注信息
     */
    fun approve(messageId: String, approve: Boolean, comment: String? = null) {
        generalAction.send("approve") {
            put("message_id", messageId)
            put("approve", approve)
            put("comment", comment)
        }
    }

    companion object {
        fun of(platform: String, selfId: String, properties: SatoriProperties, name: String) =
            FriendAction(GeneralAction(platform, selfId, properties, "friend", name))
    }
}


class AdminAction private constructor(val login: LoginAction, val webhook: WebhookAction) {
    companion object {
        fun of(properties: SatoriProperties, name: String) = AdminAction(
            LoginAction.of(properties, name),
            WebhookAction.of(properties, name)
        )
    }


    class LoginAction private constructor(private val generalAction: GeneralAction) {
        /**
         * 获取登录信息列表
         */
        fun list(): List<Login> = generalAction.sendWithSerialize("list")

        companion object {
            fun of(properties: SatoriProperties, name: String) = LoginAction(
                GeneralAction(null, null, properties, "login", name)
            )
        }
    }


    class WebhookAction private constructor(private val generalAction: GeneralAction) {
        /**
         * 创建 WebHook
         * @param url WebHook 地址
         * @param token 鉴权令牌
         */
        fun create(url: String, token: String? = null) {
            generalAction.send("list") {
                put("url", url)
                put("token", token)
            }
        }

        /**
         * 移除 WebHook
         * @param url WebHook 地址
         */
        fun delete(url: String) {
            generalAction.send("approve") {
                put("url", url)
            }
        }

        companion object {
            fun of(properties: SatoriProperties, name: String) = WebhookAction(
                GeneralAction(null, null, properties, "webhook", name)
            )
        }
    }
}

/**
 * Satori Action 实现
 * @property platform 平台
 * @property selfId 自身的 ID
 * @property properties 配置
 * @property resource 资源路径
 * @property name 隶属哪个 Event Service
 * @property mapper JSON 反序列化
 * @property logger 日志接口
 */
class GeneralAction(
    val platform: String?,
    val selfId: String?,
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
                    properties.token?.let { append(HttpHeaders.Authorization, "Bearer $it") }
                    platform?.let { append("X-Platform", it) }
                    selfId?.let { append("X-Self-ID", selfId) }
                }
                body?.let { setBody(it) }
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

    inline fun <reified T> sendWithSerialize(method: String, body: String? = null): T = try {
        mapper.readValue<T>(send(method, body))
    } catch (e: Exception) {
        logger.warn(name, e.localizedMessage)
        throw e
    }

    inline fun send(method: String, dsl: JsonObjectDSLBuilder.() -> Unit) = send(method, jsonObj(dsl))

    inline fun <reified T> sendWithSerialize(method: String, dsl: JsonObjectDSLBuilder.() -> Unit): T =
        sendWithSerialize(method, jsonObj(dsl))
}