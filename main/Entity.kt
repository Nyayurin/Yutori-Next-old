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
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

abstract class Entity(vararg pairs: Pair<String, Any?>) {
    protected val properties = mapOf(*pairs)
    override fun toString() = jsonObj { for ((key, value) in properties) put(key, value) }
}

/**
 * 频道
 * @property id 频道 ID
 * @property type 频道类型
 * @property name 频道名称
 * @property parent_id 父频道 ID
 */
class Channel(id: String, type: Type, name: String? = null, parent_id: String? = null) : Entity(
    "id" to id, "type" to type, "name" to name, "parent_id" to parent_id
) {
    val id: String by super.properties
    val type: Type by super.properties
    val name: String? by super.properties
    val parent_id: String? by super.properties

    enum class Type(val number: Number) {
        TEXT(0), VOICE(1), CATEGORY(2), DIRECT(3);

        override fun toString() = number.toString()
    }
}

/**
 * 群组
 * @property id 群组 ID
 * @property name 群组名称
 * @property avatar 群组头像
 */
class Guild(id: String, name: String? = null, avatar: String? = null) : Entity(
    "id" to id, "name" to name, "avatar" to avatar
) {
    val id: String by super.properties
    val name: String? by super.properties
    val avatar: String? by super.properties
}

/**
 * 群组成员
 * @property user 用户对象
 * @property nick 用户在群组中的名称
 * @property avatar 用户在群组中的头像
 * @property joined_at 加入时间
 */
class GuildMember(user: User? = null, nick: String? = null, avatar: String? = null, joined_at: Number?) : Entity(
    "user" to user, "nick" to nick, "avatar" to avatar, "joined_at" to joined_at
) {
    val user: User? by super.properties
    val nick: String? by super.properties
    val avatar: String? by super.properties
    val joined_at: Number? by super.properties
}

/**
 * 群组角色
 * @property id 角色 ID
 * @property name 角色名称
 */
class GuildRole(id: String, name: String? = null) : Entity("id" to id, "name" to name) {
    val id: String by super.properties
    val name: String? by super.properties
}

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
    class Argv(name: String, arguments: List<Any>, options: Any) : Entity(
        "name" to name, "arguments" to arguments, "options" to options
    ), Interaction {
        val name: String by super.properties
        val arguments: List<Any> by super.properties
        val options: Any by super.properties
    }

    /**
     * Button
     * @property id 按钮 ID
     */
    class Button(id: String) : Entity("id" to id), Interaction {
        val id: String by super.properties
    }
}

/**
 * 登录信息
 * @property user 用户对象
 * @property self_id 平台账号
 * @property platform 平台名称
 * @property status 登录状态
 */
class Login(user: User? = null, self_id: String? = null, platform: String? = null, status: Status) : Entity(
    "user" to user, "self_id" to self_id, "platform" to platform, "status" to status
) {
    val user: User? by super.properties
    val self_id: String? by super.properties
    val platform: String? by super.properties
    val status: Status by super.properties

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
class Message(
    id: String,
    content: String,
    channel: Channel? = null,
    guild: Guild? = null,
    member: GuildMember? = null,
    user: User? = null,
    created_at: Number? = null,
    updated_at: Number? = null
) : Entity(
    "id" to id,
    "content" to content,
    "channel" to channel,
    "guild" to guild,
    "member" to member,
    "user" to user,
    "created_at" to created_at,
    "updated_at" to updated_at
) {
    val id: String by super.properties
    val content: String by super.properties
    val channel: Channel? by super.properties
    val guild: Guild? by super.properties
    val member: GuildMember? by super.properties
    val user: User? by super.properties
    val created_at: Number? by super.properties
    val updated_at: Number? by super.properties
}

/**
 * 用户
 * @property id 用户 ID
 * @property name 用户名称
 * @property nick 用户昵称
 * @property avatar 用户头像
 * @property is_bot 是否为机器人
 */
class User(
    id: String, name: String? = null, nick: String? = null, avatar: String? = null, is_bot: Boolean? = null
) : Entity(
    "id" to id, "name" to name, "nick" to nick, "avatar" to avatar, "is_bot" to is_bot
) {
    val id: String by super.properties
    val name: String? by super.properties
    val nick: String? by super.properties
    val avatar: String? by super.properties
    val is_bot: Boolean? by super.properties
}

/**
 * 信令
 * @property op 信令类型
 * @property body 信令数据
 */
class Signaling(op: Int, body: Body? = null) : Entity("op" to op, "body" to body) {
    val op: Int by super.properties
    val body: Body? by super.properties

    interface Body

    companion object {
        fun parse(json: String): Signaling {
            val mapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            val node = mapper.readTree(json)
            return when (val op = node["op"].asInt()) {
                EVENT -> Signaling(op, parseEvent(mapper.readValue<Event>((node["body"] as ObjectNode).apply {
                    set<ObjectNode>("raw", TextNode(this.toString()))
                }.toString())))

                READY -> Signaling(op, Ready(mapper.readValue(node["body"]["logins"].toString())))
                PONG -> Signaling(op)
                else -> throw NoSuchElementException()
            }
        }

        private fun parseEvent(event: Event) = try {
            val type = event.type
            when (type) {
                in GuildMemberEvents.Types -> GuildMemberEvent.parse(event)
                in GuildRoleEvents.Types -> GuildRoleEvent.parse(event)
                in GuildEvents.Types -> GuildEvent.parse(event)
                InteractionEvents.Button -> InteractionButtonEvent.parse(event)
                InteractionEvents.Command -> InteractionCommandEvent.parse(event)
                in LoginEvents.Types -> LoginEvent.parse(event)
                in MessageEvents.Types -> MessageEvent.parse(event)
                in ReactionEvents.Types -> ReactionEvent.parse(event)
                in UserEvents.Types -> UserEvent.parse(event)
                else -> event
            }
        } catch (e: Throwable) {
            throw EventParsingException(e)
        }

        /**
         * 事件
         */
        const val EVENT = 0

        /**
         * 心跳
         */
        const val PING = 1

        /**
         * 心跳回复
         */
        const val PONG = 2

        /**
         * 鉴权
         */
        const val IDENTIFY = 3

        /**
         * 鉴权回复
         */
        const val READY = 4
    }
}

/**
 * 鉴权回复
 * @property logins 登录信息
 */
data class Ready(val logins: List<Login>) : Signaling.Body

/**
 * 鉴权
 * @property token 鉴权令牌
 * @property sequence 序列号
 */
class Identify(token: String? = null, sequence: Number? = null) : Entity(
    "token" to token, "sequence" to sequence
), Signaling.Body {
    val token: String? by super.properties
    val sequence: Number? by super.properties
}

/**
 * 分页数据
 * @param T 数据类型
 * @property data 数据
 * @property next 下一页的令牌
 */
data class PaginatedData<T>(
    val data: List<T>,
    val next: String? = null
)

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

data class Context<T : Event>(val actions: Actions, val event: T, val satori: Satori)