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

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import github.nyayurn.yutori_next.message.MessageBuilder
import github.nyayurn.yutori_next.message.message
import kotlin.properties.Delegates

abstract class ExtendedActionsContainer

class ActionsContainer(platform: String, self_id: String, service: ActionService, satori: Satori) {
    val channel = ChannelAction(platform, self_id, satori.name, service)
    val guild = GuildAction(platform, self_id, satori.name, service)
    val login = LoginAction(platform, self_id, satori.name, service)
    val message = MessageAction(satori, platform, self_id, service)
    val reaction = ReactionAction(platform, self_id, satori.name, service)
    val user = UserAction(platform, self_id, satori.name, service)
    val friend = FriendAction(platform, self_id, satori.name, service)
    val admin = AdminAction(satori.name, service)
    val containers = mutableMapOf<String, ExtendedActionsContainer>().apply {
        for ((key, value) in satori.actions_containers) this[key] = value(platform, self_id, service)
    }

    class ChannelAction(platform: String, self_id: String, name: String, service: ActionService) {
        private val action = GeneralAction(platform, self_id, "channel", name, service)

        fun get(block: GetBuilder.() -> Unit): Channel {
            val builder = GetBuilder().apply(block)
            return action.sendWithParse("get") {
                put("channel_id", builder.channel_id)
            }
        }

        fun list(block: ListBuilder.() -> Unit): List<PaginatedData<Channel>> {
            val builder = ListBuilder().apply(block)
            return action.sendWithParse("list") {
                put("guild_id", builder.guild_id)
                put("next", builder.next)
            }
        }

        fun create(block: CreateBuilder.() -> Unit): Channel {
            val builder = CreateBuilder().apply(block)
            return action.sendWithParse("create") {
                put("guild_id", builder.guild_id)
                put("data", builder.data)
            }
        }

        fun update(block: UpdateBuilder.() -> Unit) {
            val builder = UpdateBuilder().apply(block)
            action.send("update") {
                put("channel_id", builder.channel_id)
                put("data", builder.data)
            }
        }

        fun delete(block: DeleteBuilder.() -> Unit) {
            val builder = DeleteBuilder().apply(block)
            action.send("delete") {
                put("channel_id", builder.channel_id)
            }
        }

        @BuilderMarker
        class GetBuilder {
            lateinit var channel_id: String
        }

        @BuilderMarker
        class ListBuilder {
            lateinit var guild_id: String
            var next: String? = null
        }

        @BuilderMarker
        class CreateBuilder {
            lateinit var guild_id: String
            lateinit var data: Channel
        }

        @BuilderMarker
        class UpdateBuilder {
            lateinit var channel_id: String
            lateinit var data: Channel
        }

        @BuilderMarker
        class DeleteBuilder {
            lateinit var channel_id: String
        }
    }

    class GuildAction(platform: String, self_id: String, name: String, service: ActionService) {
        val member = MemberAction(platform, self_id, name, service)
        val role = RoleAction(platform, self_id, name, service)
        private val action = GeneralAction(platform, self_id, "guild", name, service)

        fun get(block: GetBuilder.() -> Unit): Guild {
            val builder = GetBuilder().apply(block)
            return action.sendWithParse("get") {
                put("guild_id", builder.guild_id)
            }
        }

        fun list(block: ListBuilder.() -> Unit): List<PaginatedData<Guild>> {
            val builder = ListBuilder().apply(block)
            return action.sendWithParse("list") {
                put("next", builder.next)
            }
        }

        fun approve(block: ApproveBuilder.() -> Unit) {
            val builder = ApproveBuilder().apply(block)
            action.send("approve") {
                put("message_id", builder.message_id)
                put("approve", builder.approve)
                put("comment", builder.comment)
            }
        }

        @BuilderMarker
        class GetBuilder {
            lateinit var guild_id: String
        }

        @BuilderMarker
        class ListBuilder {
            var next: String? = null
        }

        @BuilderMarker
        class ApproveBuilder {
            lateinit var message_id: String
            var approve: Boolean by Delegates.notNull()
            lateinit var comment: String
        }

        class MemberAction(platform: String, self_id: String, name: String, service: ActionService) {
            val role = RoleAction(platform, self_id, name, service)
            private val action = GeneralAction(platform, self_id, "guild.member", name, service)

            fun get(block: GetBuilder.() -> Unit): GuildMember {
                val builder = GetBuilder().apply(block)
                return action.sendWithParse("get") {
                    put("guild_id", builder.guild_id)
                    put("user_id", builder.user_id)
                }
            }

            fun list(block: ListBuilder.() -> Unit): List<PaginatedData<GuildMember>> {
                val builder = ListBuilder().apply(block)
                return action.sendWithParse("list") {
                    put("guild_id", builder.guild_id)
                    put("next", builder.next)
                }
            }

            fun kick(block: KickBuilder.() -> Unit) {
                val builder = KickBuilder().apply(block)
                action.send("kick") {
                    put("guild_id", builder.guild_id)
                    put("user_id", builder.user_id)
                    put("permanent", builder.permanent)
                }
            }

            fun approve(block: ApproveBuilder.() -> Unit) {
                val builder = ApproveBuilder().apply(block)
                action.send("approve") {
                    put("message_id", builder.message_id)
                    put("approve", builder.approve)
                    put("comment", builder.comment)
                }
            }

            @BuilderMarker
            class GetBuilder {
                lateinit var guild_id: String
                lateinit var user_id: String
            }

            @BuilderMarker
            class ListBuilder {
                lateinit var guild_id: String
                var next: String? = null
            }

            @BuilderMarker
            class KickBuilder {
                lateinit var guild_id: String
                lateinit var user_id: String
                var permanent: Boolean? = null
            }

            @BuilderMarker
            class ApproveBuilder {
                lateinit var message_id: String
                var approve: Boolean by Delegates.notNull()
                lateinit var comment: String
            }

            class RoleAction(platform: String, self_id: String, name: String, service: ActionService) {
                private val action = GeneralAction(platform, self_id, "guild.member.role", name, service)

                fun set(block: SetBuilder.() -> Unit) {
                    val builder = SetBuilder().apply(block)
                    action.send("set") {
                        put("guild_id", builder.guild_id)
                        put("user_id", builder.user_id)
                        put("role_id", builder.role_id)
                    }
                }

                fun unset(block: UnsetBuilder.() -> Unit) {
                    val builder = UnsetBuilder().apply(block)
                    action.send("unset") {
                        put("guild_id", builder.guild_id)
                        put("user_id", builder.user_id)
                        put("role_id", builder.role_id)
                    }
                }

                @BuilderMarker
                class SetBuilder {
                    lateinit var guild_id: String
                    lateinit var user_id: String
                    lateinit var role_id: String
                }

                @BuilderMarker
                class UnsetBuilder {
                    lateinit var guild_id: String
                    lateinit var user_id: String
                    lateinit var role_id: String
                }
            }
        }

        class RoleAction(platform: String, self_id: String, name: String, service: ActionService) {
            private val action = GeneralAction(platform, self_id, "guild.role", name, service)

            fun list(block: ListBuilder.() -> Unit): List<PaginatedData<GuildRole>> {
                val builder = ListBuilder().apply(block)
                return action.sendWithParse("list") {
                    put("guild_id", builder.guild_id)
                    put("next", builder.next)
                }
            }

            fun create(block: CreateBuilder.() -> Unit): GuildRole {
                val builder = CreateBuilder().apply(block)
                return action.sendWithParse("create") {
                    put("guild_id", builder.guild_id)
                    put("role", builder.role)
                }
            }

            fun update(block: UpdateBuilder.() -> Unit) {
                val builder = UpdateBuilder().apply(block)
                action.send("update") {
                    put("guild_id", builder.guild_id)
                    put("role_id", builder.role_id)
                    put("role", builder.role)
                }
            }

            fun delete(block: DeleteBuilder.() -> Unit) {
                val builder = DeleteBuilder().apply(block)
                action.send("delete") {
                    put("guild_id", builder.guild_id)
                    put("role_id", builder.role_id)
                }
            }

            @BuilderMarker
            class ListBuilder {
                lateinit var guild_id: String
                var next: String? = null
            }

            @BuilderMarker
            class CreateBuilder {
                lateinit var guild_id: String
                lateinit var role: GuildRole
            }

            @BuilderMarker
            class UpdateBuilder {
                lateinit var guild_id: String
                lateinit var role_id: String
                lateinit var role: GuildRole
            }

            @BuilderMarker
            class DeleteBuilder {
                lateinit var guild_id: String
                lateinit var role_id: String
            }
        }
    }

    class LoginAction(platform: String, self_id: String, name: String, service: ActionService) {
        private val action = GeneralAction(platform, self_id, "login", name, service)

        fun get(): Login = action.sendWithParse("get")
    }

    class MessageAction(private val satori: Satori, platform: String, self_id: String, service: ActionService) {
        private val action = GeneralAction(platform, self_id, "message", satori.name, service)

        fun create(block: CreateBuilder.() -> Unit): List<Message> {
            val builder = CreateBuilder(satori).apply(block)
            return action.sendWithParse("create") {
                put("channel_id", builder.channel_id)
                put("content", builder.content.replace("\n", "\\n").replace("\"", "\\\""))
            }
        }

        fun get(block: GetBuilder.() -> Unit): Message {
            val builder = GetBuilder().apply(block)
            return action.sendWithParse("get") {
                put("channel_id", builder.channel_id)
                put("message_id", builder.message_id)
            }
        }

        fun delete(block: DeleteBuilder.() -> Unit) {
            val builder = DeleteBuilder().apply(block)
            action.send("delete") {
                put("channel_id", builder.channel_id)
                put("message_id", builder.message_id)
            }
        }

        fun update(block: UpdateBuilder.() -> Unit) {
            val builder = UpdateBuilder(satori).apply(block)
            action.send("update") {
                put("channel_id", builder.channel_id)
                put("message_id", builder.message_id)
                put("content", builder.content.replace("\n", "\\n").replace("\"", "\\\""))
            }
        }

        fun list(block: ListBuilder.() -> Unit): List<PaginatedData<Message>> {
            val builder = ListBuilder().apply(block)
            return action.sendWithParse("list") {
                put("channel_id", builder.channel_id)
                put("next", builder.next)
            }
        }

        @BuilderMarker
        class CreateBuilder(val satori: Satori) {
            lateinit var channel_id: String
            lateinit var content: String

            fun content(block: MessageBuilder.() -> Unit) {
                content = message(satori, block)
            }
        }

        @BuilderMarker
        class GetBuilder {
            lateinit var channel_id: String
            lateinit var message_id: String
        }

        @BuilderMarker
        class DeleteBuilder {
            lateinit var channel_id: String
            lateinit var message_id: String
        }

        @BuilderMarker
        class UpdateBuilder(val satori: Satori) {
            lateinit var channel_id: String
            lateinit var message_id: String
            lateinit var content: String

            fun content(block: MessageBuilder.() -> Unit) {
                content = message(satori, block)
            }
        }

        @BuilderMarker
        class ListBuilder {
            lateinit var channel_id: String
            var next: String? = null
        }
    }

    class ReactionAction(platform: String, self_id: String, name: String, service: ActionService) {
        private val action = GeneralAction(platform, self_id, "reaction", name, service)

        fun create(block: CreateBuilder.() -> Unit) {
            val builder = CreateBuilder().apply(block)
            action.send("create") {
                put("channel_id", builder.channel_id)
                put("message_id", builder.message_id)
                put("emoji", builder.emoji)
            }
        }

        fun delete(block: DeleteBuilder.() -> Unit) {
            val builder = DeleteBuilder().apply(block)
            action.send("delete") {
                put("channel_id", builder.channel_id)
                put("message_id", builder.message_id)
                put("emoji", builder.emoji)
                put("user_id", builder.user_id)
            }
        }

        fun clear(block: ClearBuilder.() -> Unit) {
            val builder = ClearBuilder().apply(block)
            action.send("clear") {
                put("channel_id", builder.channel_id)
                put("message_id", builder.message_id)
                put("emoji", builder.emoji)
            }
        }

        fun list(block: ListBuilder.() -> Unit): List<PaginatedData<User>> {
            val builder = ListBuilder().apply(block)
            return action.sendWithParse("list") {
                put("channel_id", builder.channel_id)
                put("message_id", builder.message_id)
                put("emoji", builder.emoji)
                put("next", builder.next)
            }
        }

        @BuilderMarker
        class CreateBuilder {
            lateinit var channel_id: String
            lateinit var message_id: String
            lateinit var emoji: String
        }

        @BuilderMarker
        class DeleteBuilder {
            lateinit var channel_id: String
            lateinit var message_id: String
            lateinit var emoji: String
            var user_id: String? = null
        }

        @BuilderMarker
        class ClearBuilder {
            lateinit var channel_id: String
            lateinit var message_id: String
            var emoji: String? = null
        }

        @BuilderMarker
        class ListBuilder {
            lateinit var channel_id: String
            lateinit var message_id: String
            lateinit var emoji: String
            var next: String? = null
        }
    }

    class UserAction(platform: String, self_id: String, name: String, service: ActionService) {
        val channel = ChannelAction(platform, self_id, name, service)
        private val action = GeneralAction(platform, self_id, "user", name, service)

        fun get(block: GetBuilder.() -> Unit): User {
            val builder = GetBuilder().apply(block)
            return action.sendWithParse("get") {
                put("user_id", builder.user_id)
            }
        }

        @BuilderMarker
        class GetBuilder {
            lateinit var user_id: String
        }

        class ChannelAction(platform: String, self_id: String, name: String, service: ActionService) {
            private val action = GeneralAction(platform, self_id, "user.channel", name, service)

            fun create(block: CreateBuilder.() -> Unit): Channel {
                val builder = CreateBuilder().apply(block)
                return action.sendWithParse("create") {
                    put("user_id", builder.user_id)
                    put("guild_id", builder.guild_id)
                }
            }

            @BuilderMarker
            class CreateBuilder {
                lateinit var user_id: String
                var guild_id: String? = null
            }
        }
    }

    class FriendAction(platform: String, self_id: String, name: String, service: ActionService) {
        private val action = GeneralAction(platform, self_id, "friend", name, service)

        fun list(block: ListBuilder.() -> Unit): List<PaginatedData<User>> {
            val builder = ListBuilder().apply(block)
            return action.sendWithParse("list") {
                put("next", builder.next)
            }
        }

        fun approve(block: ApproveBuilder.() -> Unit) {
            val builder = ApproveBuilder().apply(block)
            action.send("approve") {
                put("message_id", builder.message_id)
                put("approve", builder.approve)
                put("comment", builder.comment)
            }
        }

        @BuilderMarker
        class ListBuilder {
            var next: String? = null
        }

        @BuilderMarker
        class ApproveBuilder {
            lateinit var message_id: String
            var approve: Boolean by Delegates.notNull()
            var comment: String? = null
        }
    }


    class AdminAction(name: String, service: ActionService) {
        val login = LoginAction(name, service)
        val webhook = WebhookAction(name, service)

        class LoginAction(name: String, service: ActionService) {
            private val action = GeneralAction(null, null, "login", name, service)

            fun list(): List<Login> = action.sendWithParse("list")
        }


        class WebhookAction(name: String, service: ActionService) {
            private val action = GeneralAction(null, null, "webhook", name, service)

            fun create(block: CreateBuilder.() -> Unit) {
                val builder = CreateBuilder().apply(block)
                action.send("list") {
                    put("url", builder.url)
                    put("token", builder.token)
                }
            }

            fun delete(block: DeleteBuilder.() -> Unit) {
                val builder = DeleteBuilder().apply(block)
                action.send("approve") {
                    put("url", builder.url)
                }
            }


            @BuilderMarker
            class CreateBuilder {
                lateinit var url: String
                var token: String? = null
            }


            @BuilderMarker
            class DeleteBuilder {
                lateinit var url: String
            }
        }
    }
}

/**
 * Satori Action 对接层
 * @property platform 平台
 * @property self_id 自身的 ID
 * @property resource 资源路径
 * @property name Satori.name
 * @property service ActionService 实现
 * @property mapper JSON 反序列化
 * @property logger 日志接口
 */
class GeneralAction(
    val platform: String?,
    val self_id: String?,
    val resource: String,
    val name: String,
    val service: ActionService
) {
    val mapper: ObjectMapper = jacksonObjectMapper()
    val logger = GlobalLoggerFactory.getLogger(this::class.java)

    fun send(method: String, content: String? = null) = service.send(resource, method, platform, self_id, content)
    inline fun send(method: String, block: JsonObjectDSLBuilder.() -> Unit) = send(method, jsonObj(block))
    inline fun <reified T> sendWithParse(method: String, block: JsonObjectDSLBuilder.() -> Unit = {}): T {
        val response = send(method, jsonObj(block))
        try {
            return mapper.readValue<T>(response)
        } catch (e: Exception) {
            logger.warn(name, response)
            throw ResponseParsingException(response)
        }
    }
}