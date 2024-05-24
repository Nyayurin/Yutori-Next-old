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

package com.github.nyayurn.yutori

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * 频道
 * @property id 频道 ID
 * @property type 频道类型
 * @property name 频道名称
 * @property parent_id 父频道 ID
 */
data class Channel(val id: String, val type: Type, val name: String? = null, val parent_id: String? = null) {
    enum class Type(val number: Number) {
        TEXT(0), DIRECT(1), CATEGORY(2), VOICE(3);

        override fun toString() = number.toString()
    }
}

/**
 * 群组
 * @property id 群组 ID
 * @property name 群组名称
 * @property avatar 群组头像
 */
data class Guild(val id: String, val name: String? = null, val avatar: String? = null)

/**
 * 群组成员
 * @property user 用户对象
 * @property nick 用户在群组中的名称
 * @property avatar 用户在群组中的头像
 * @property joined_at 加入时间
 */
data class GuildMember(
    val user: User? = null, val nick: String? = null, val avatar: String? = null, val joined_at: Number?
)

/**
 * 群组角色
 * @property id 角色 ID
 * @property name 角色名称
 */
data class GuildRole(val id: String, val name: String? = null)

/**
 * 交互
 */
interface Interaction {
    /**
     * Argv
     * @property name 指令名称
     * @property arguments 参数
     * @property options 选项
     */
    data class Argv(val name: String, val arguments: List<Any>, val options: Any) : Interaction

    /**
     * Button
     * @property id 按钮 ID
     */
    data class Button(val id: String) : Interaction
}

/**
 * 登录信息
 * @property user 用户对象
 * @property self_id 平台账号
 * @property platform 平台名称
 * @property status 登录状态
 */
data class Login(
    val user: User? = null,
    val self_id: String? = null,
    val platform: String? = null,
    val status: Status,
    val features: List<String> = listOf(),
    val resourceUrls: List<String> = listOf(),
) {
    enum class Status(val number: Number) {
        OFFLINE(0), ONLINE(1), CONNECT(2), DISCONNECT(3), RECONNECT(4);

        override fun toString() = number.toString()
    }
}

/**
 * 消息
 * @property id 消息 ID
 * @property content 消息内容
 * @property channel 频道对象
 * @property guild 群组对象
 * @property member 成员对象
 * @property user 用户对象
 * @property created_at 消息发送的时间戳
 * @property updated_at 消息修改的时间戳
 */
data class Message(
    val id: String,
    val content: String,
    val channel: Channel? = null,
    val guild: Guild? = null,
    val member: GuildMember? = null,
    val user: User? = null,
    val created_at: Number? = null,
    val updated_at: Number? = null
)

/**
 * 用户
 * @property id 用户 ID
 * @property name 用户名称
 * @property nick 用户昵称
 * @property avatar 用户头像
 * @property is_bot 是否为机器人
 */
data class User(
    val id: String,
    val name: String? = null,
    val nick: String? = null,
    val avatar: String? = null,
    val is_bot: Boolean? = null
)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "op")
@JsonSubTypes(value = [
    JsonSubTypes.Type(value = EventSignal::class, name = "0"),
    JsonSubTypes.Type(value = PingSignal::class, name = "1"),
    JsonSubTypes.Type(value = PongSignal::class, name = "2"),
    JsonSubTypes.Type(value = IdentifySignal::class, name = "3"),
    JsonSubTypes.Type(value = ReadySignal::class, name = "4")
])
abstract class Signal(val op: Int) {
    interface Body
    companion object {
        const val EVENT = 0
        const val PING = 1
        const val PONG = 2
        const val IDENTIFY = 3
        const val READY = 4
    }
}

data class EventSignal(val body: Event<AnyEvent>) : Signal(EVENT)
object PingSignal : Signal(PING)
object PongSignal : Signal(PONG)
data class IdentifySignal(val body: Identify) : Signal(IDENTIFY)
data class ReadySignal(val body: Ready) : Signal(READY)

data class Identify(val token: String? = null, val sequence: Number? = null) : Signal.Body
data class Ready(val logins: List<Login>) : Signal.Body

/**
 * 分页列表
 * @param T 数据类型
 * @property data 数据
 * @property next 下一页的令牌
 */
data class PagingList<T>(val data: List<T>, val next: String? = null)

/**
 * 双向分页列表
 * @param T 数据类型
 * @property data 数据
 * @property next 下一页的令牌
 */
data class BidiPagingList<T>(val data: List<T>, val prev: String? = null, val next: String? = null) {
    enum class Direction(val value: String) {
        Before("before"), After("after"), Around("around")
    }
    enum class Order(val value: String) {
        Asc("asc"), Desc("desc")
    }
}

data class FormData(val name: String, val filename: String? = null, val contentType: String, val content: ByteArray)

/**
 * Satori Server 配置
 * @property host Satori Server 主机
 * @property port Satori Server 端口
 * @property path Satori Server 路径
 * @property token Satori Server 鉴权令牌
 * @property version Satori Server 协议版本
 */
data class SatoriProperties(
    val host: String = "127.0.0.1",
    val port: Int = 5500,
    val path: String = "",
    val token: String? = null,
    val version: String = "v1"
)

data class Context<T : SigningEvent>(val actions: RootActions, val event: Event<T>, val satori: Satori)

data class Event<T : SigningEvent>(val properties: MutableMap<String, Any?> = mutableMapOf()) {
    val id: Number by properties
    val type: String by properties
    val platform: String by properties
    val self_id: String by properties
    val timestamp: Number by properties
    val nullable_argv: Interaction.Argv?
        get() = properties["argv"] as Interaction.Argv?
    val nullable_button: Interaction.Button?
        get() = properties["button"] as Interaction.Button?
    val nullable_channel: Channel?
        get() = properties["channel"] as Channel?
    val nullable_guild: Guild?
        get() = properties["guild"] as Guild?
    val nullable_login: Login?
        get() = properties["login"] as Login?
    val nullable_member: GuildMember?
        get() = properties["member"] as GuildMember?
    val nullable_message: Message?
        get() = properties["message"] as Message?
    val nullable_operator: User?
        get() = properties["operator"] as User?
    val nullable_role: GuildRole?
        get() = properties["role"] as GuildRole?
    val nullable_user: User?
        get() = properties["user"] as User?

    constructor(event: Event<*>) : this(event.properties)

    @JsonCreator
    constructor(
        id: Number,
        type: String,
        platform: String,
        self_id: String,
        timestamp: Number,
        argv: Interaction.Argv? = null,
        button: Interaction.Button? = null,
        channel: Channel? = null,
        guild: Guild? = null,
        login: Login? = null,
        member: GuildMember? = null,
        message: Message? = null,
        operator: User? = null,
        role: GuildRole? = null,
        user: User? = null,
        vararg pair: Pair<String, Any?> = arrayOf(),
    ) : this(mutableMapOf(
        "id" to id,
        "type" to type,
        "platform" to platform,
        "self_id" to self_id,
        "timestamp" to timestamp,
        "argv" to argv,
        "button" to button,
        "channel" to channel,
        "guild" to guild,
        "login" to login,
        "member" to member,
        "message" to message,
        "operator" to operator,
        "role" to role,
        "user" to user,
        *pair
    ))
}