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

import com.github.nyayurn.yutori.message.MessageBuilder
import com.github.nyayurn.yutori.message.message
import io.ktor.util.reflect.*

abstract class Actions
abstract class Action(
    val platform: String?, val self_id: String?, val resource: String, val service: ActionService
) {
    protected inline fun <reified T> send(method: String, vararg content: Pair<String, Any?>): T =
        service.send(resource, method, platform, self_id, mapOf(*content), typeInfo<T>()) as T

    protected fun upload(method: String, content: List<FormData>): Map<String, String> =
        service.upload(resource, method, platform!!, self_id!!, content)
}

class RootActions(platform: String, self_id: String, service: ActionService, satori: Satori) : Actions() {
    val channel = ChannelAction(platform, self_id, service)
    val guild = GuildAction(platform, self_id, service)
    val login = LoginAction(platform, self_id, service)
    val message = MessageAction(satori, platform, self_id, service)
    val reaction = ReactionAction(platform, self_id, service)
    val user = UserAction(platform, self_id, service)
    val friend = FriendAction(platform, self_id, service)
    val upload = UploadAction(platform, self_id, service)
    val admin = AdminAction(service)
    val containers = mutableMapOf<String, Actions>().apply {
        for ((key, value) in satori.actions_containers) this[key] = value(platform, self_id, service)
    }

    class ChannelAction(platform: String, self_id: String, service: ActionService) : Action(
        platform, self_id, "channel", service
    ) {
        fun get(channel_id: String): Channel = send("get", "channel_id" to channel_id)
        fun list(guild_id: String, next: String? = null): PagingList<Channel> =
            send("list", "guild_id" to guild_id, "next" to next)

        fun create(guild_id: String, data: Channel): Channel = send("create", "guild_id" to guild_id, "data" to data)
        fun update(channel_id: String, data: Channel): Unit = send("update", "channel_id" to channel_id, "data" to data)
        fun delete(channel_id: String): Unit = send("delete", "channel_id" to channel_id)
        fun mute(channel_id: String, duration: Number): Unit =
            send("mute", "channel_id" to channel_id, "duration" to duration)
    }

    class GuildAction(platform: String, self_id: String, service: ActionService) : Action(
        platform, self_id, "guild", service
    ) {
        val member = MemberAction(platform, self_id, service)
        val role = RoleAction(platform, self_id, service)

        fun get(guild_id: String): Guild = send("get", "guild_id" to guild_id)
        fun list(next: String? = null): PagingList<Guild> = send("list", "next" to next)
        fun approve(message_id: String, approve: Boolean, comment: String): Unit =
            send("approve", "message_id" to message_id, "approve" to approve, "comment" to comment)

        class MemberAction(platform: String, self_id: String, service: ActionService) : Action(
            platform, self_id, "guild.member", service
        ) {
            val role = RoleAction(platform, self_id, service)

            fun get(guild_id: String, user_id: String): GuildMember =
                send("get", "guild_id" to guild_id, "user_id" to user_id)

            fun list(guild_id: String, next: String? = null): PagingList<GuildMember> =
                send("list", "guild_id" to guild_id, "next" to next)

            fun kick(guild_id: String, user_id: String, permanent: Boolean? = null): Unit =
                send("kick", "guild_id" to guild_id, "user_id" to user_id, "permanent" to permanent)

            fun mute(guild_id: String, user_id: String, duration: Number): Unit =
                send("mute", "guild_id" to guild_id, "user_id" to user_id, "duration" to duration)

            fun approve(message_id: String, approve: Boolean, comment: String): Unit =
                send("approve", "message_id" to message_id, "approve" to approve, "comment" to comment)

            class RoleAction(platform: String, self_id: String, service: ActionService) : Action(
                platform, self_id, "guild.member.role", service
            ) {
                fun set(guild_id: String, user_id: String, role_id: String): Unit =
                    send("set", "guild_id" to guild_id, "user_id" to user_id, "role_id" to role_id)

                fun unset(guild_id: String, user_id: String, role_id: String): Unit =
                    send("unset", "guild_id" to guild_id, "user_id" to user_id, "role_id" to role_id)
            }
        }

        class RoleAction(platform: String, self_id: String, service: ActionService) : Action(
            platform, self_id, "guild.role", service
        ) {
            fun list(guild_id: String, next: String? = null): PagingList<GuildRole> =
                send("list", "guild_id" to guild_id, "next" to next)

            fun create(guild_id: String, role: GuildRole): GuildRole =
                send("create", "guild_id" to guild_id, "role" to role)

            fun update(guild_id: String, role_id: String, role: GuildRole): Unit =
                send("update", "guild_id" to guild_id, "role_id" to role_id, "role" to role)

            fun delete(guild_id: String, role_id: String): Unit =
                send("delete", "guild_id" to guild_id, "role_id" to role_id)
        }
    }

    class LoginAction(platform: String, self_id: String, service: ActionService) : Action(
        platform, self_id, "login", service
    ) {
        fun get(): Login = send("get")
    }

    class MessageAction(private val satori: Satori, platform: String, self_id: String, service: ActionService) : Action(
        platform, self_id, "message", service
    ) {
        fun create(channel_id: String, content: String): List<Message> =
            send("create", "channel_id" to channel_id, "content" to content.replace("\n", "\\n").replace("\"", "\\\""))

        fun create(channel_id: String, content: MessageBuilder.() -> Unit) =
            create(channel_id, message(satori, content))

        fun get(channel_id: String, message_id: String): Message =
            send("get", "channel_id" to channel_id, "message_id" to message_id)

        fun delete(channel_id: String, message_id: String): Unit =
            send("delete", "channel_id" to channel_id, "message_id" to message_id)

        fun update(channel_id: String, message_id: String, content: String): Unit = send(
            "update", "channel_id" to channel_id, "message_id" to message_id,
            "content" to content.replace("\n", "\\n").replace("\"", "\\\"")
        )

        fun update(channel_id: String, message_id: String, content: MessageBuilder.() -> Unit) =
            update(channel_id, message_id, message(satori, content))

        fun list(
            channel_id: String,
            next: String? = null,
            direction: BidiPagingList.Direction? = null,
            limit: Number? = null,
            order: BidiPagingList.Order? = null
        ): BidiPagingList<Message> = send(
            "list", "channel_id" to channel_id, "next" to next, "direction" to direction?.value, "limit" to limit,
            "order" to order?.value
        )
    }

    class ReactionAction(platform: String, self_id: String, service: ActionService) : Action(
        platform, self_id, "reaction", service
    ) {
        fun create(channel_id: String, message_id: String, emoji: String): Unit =
            send("create", "channel_id" to channel_id, "message_id" to message_id, "emoji" to emoji)

        fun delete(channel_id: String, message_id: String, emoji: String, user_id: String? = null): Unit = send(
            "delete", "channel_id" to channel_id, "message_id" to message_id, "emoji" to emoji, "user_id" to user_id
        )

        fun clear(channel_id: String, message_id: String, emoji: String? = null): Unit =
            send("clear", "channel_id" to channel_id, "message_id" to message_id, "emoji" to emoji)

        fun list(channel_id: String, message_id: String, emoji: String, next: String? = null): PagingList<User> = send(
            "list", "channel_id" to channel_id, "message_id" to message_id, "emoji" to emoji, "next" to next
        )
    }

    class UserAction(platform: String, self_id: String, service: ActionService) : Action(
        platform, self_id, "user", service
    ) {
        val channel = ChannelAction(platform, self_id, service)

        fun get(user_id: String): User = send("get", "user_id" to user_id)

        class ChannelAction(platform: String, self_id: String, service: ActionService) : Action(
            platform, self_id, "user.channel", service
        ) {
            fun create(user_id: String, guild_id: String? = null): Channel =
                send("create", "user_id" to user_id, "guild_id" to guild_id)
        }
    }

    class FriendAction(platform: String, self_id: String, service: ActionService) : Action(
        platform, self_id, "friend", service
    ) {
        fun list(next: String? = null): PagingList<User> = send("list", "next" to next)
        fun approve(message_id: String, approve: Boolean, comment: String? = null): Unit =
            send("approve", "message_id" to message_id, "approve" to approve, "comment" to comment)
    }

    class UploadAction(platform: String, self_id: String, service: ActionService) : Action(
        platform, self_id, "upload", service
    ) {
        fun create(next: String? = null): PagingList<User> = send("list", "next" to next)
    }

    class AdminAction(service: ActionService) {
        val login = LoginAction(service)
        val webhook = WebhookAction(service)

        class LoginAction(service: ActionService) : Action(null, null, "login", service) {
            fun list(): List<Login> = send("list")
        }

        class WebhookAction(service: ActionService) : Action(null, null, "webhook", service) {
            fun create(url: String, token: String? = null): Unit = send("list", "url" to url, "token" to token)
            fun delete(url: String): Unit = send("approve", "url" to url)
        }
    }
}