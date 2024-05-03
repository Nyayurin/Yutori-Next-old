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

package github.nyayurn.yutori_next.module.core

import github.nyayurn.yutori_next.*
import github.nyayurn.yutori_next.message.MessageDslBuilder
import github.nyayurn.yutori_next.message.message
import kotlin.properties.Delegates

val Actions.core: CoreAction
    get() = actions["core"] as CoreAction

class CoreAction private constructor(
    val channel: ChannelAction,
    val guild: GuildAction,
    val login: LoginAction,
    val message: MessageAction,
    val reaction: ReactionAction,
    val user: UserAction,
    val friend: FriendAction,
    val admin: AdminAction
) : ExtendedActions() {
    constructor(satori: Satori, platform: String, self_id: String, service: ActionService) : this(
        ChannelAction(platform, self_id, satori.name, service),
        GuildAction(platform, self_id, satori.name, service),
        LoginAction(platform, self_id, satori.name, service),
        MessageAction(satori, platform, self_id, service),
        ReactionAction(platform, self_id, satori.name, service),
        UserAction(platform, self_id, satori.name, service),
        FriendAction(platform, self_id, satori.name, service),
        AdminAction(satori.name, service)
    )

    class ChannelAction private constructor(private val action: GeneralAction) {
        constructor(platform: String, self_id: String, name: String, service: ActionService) : this(
            GeneralAction(platform, self_id, "channel", name, service)
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

    class GuildAction private constructor(
        val member: MemberAction, val role: RoleAction, private val action: GeneralAction
    ) {
        constructor(platform: String, self_id: String, name: String, service: ActionService) : this(
            MemberAction(platform, self_id, name, service), RoleAction(platform, self_id, name, service),
            GeneralAction(platform, self_id, "guild", name, service)
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

        class MemberAction private constructor(val role: RoleAction, private val action: GeneralAction) {
            constructor(platform: String, self_id: String, name: String, service: ActionService) : this(
                RoleAction(platform, self_id, name, service),
                GeneralAction(platform, self_id, "guild.member", name, service)
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

            class RoleAction private constructor(private val action: GeneralAction) {
                constructor(platform: String, self_id: String, name: String, service: ActionService) : this(
                    GeneralAction(platform, self_id, "guild.member.role", name, service)
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

        class RoleAction private constructor(private val action: GeneralAction) {
            constructor(platform: String, self_id: String, name: String, service: ActionService) : this(
                GeneralAction(platform, self_id, "guild.role", name, service)
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

    class LoginAction private constructor(private val action: GeneralAction) {
        constructor(platform: String, self_id: String, name: String, service: ActionService) : this(
            GeneralAction(platform, self_id, "login", name, service)
        )

        /**
         * 获取登录信息
         */
        fun get(): Login = action.sendWithParse("get")
    }

    class MessageAction private constructor(private val action: GeneralAction, private val satori: Satori) {
        constructor(satori: Satori, platform: String, self_id: String, service: ActionService) : this(
            GeneralAction(platform, self_id, "message", satori.name, service), satori
        )

        /**
         * 发送消息
         */
        fun create(block: CreateBuilder.() -> Unit): List<Message> {
            val builder = CreateBuilder(satori).apply(block)
            return action.sendWithParse("create") {
                put("channel_id", builder.channel_id)
                put("content", builder.content.replace("\n", "\\n").replace("\"", "\\\""))
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
            val builder = UpdateBuilder(satori).apply(block)
            action.send("update") {
                put("channel_id", builder.channel_id)
                put("message_id", builder.message_id)
                put("content", builder.content.replace("\n", "\\n").replace("\"", "\\\""))
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

        @BuilderMarker
        class CreateBuilder(val satori: Satori) {
            lateinit var channel_id: String
            lateinit var content: String

            fun content(block: MessageDslBuilder.() -> Unit) {
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

            fun content(block: MessageDslBuilder.() -> Unit) {
                content = message(satori, block)
            }
        }

        @BuilderMarker
        class ListBuilder {
            lateinit var channel_id: String
            var next: String? = null
        }
    }

    class ReactionAction private constructor(private val action: GeneralAction) {
        constructor(platform: String, self_id: String, name: String, service: ActionService) : this(
            GeneralAction(platform, self_id, "reaction", name, service)
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

    class UserAction private constructor(val channel: ChannelAction, private val action: GeneralAction) {
        constructor(platform: String, self_id: String, name: String, service: ActionService) : this(
            ChannelAction(platform, self_id, name, service), GeneralAction(platform, self_id, "user", name, service)
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

        @BuilderMarker
        class GetBuilder {
            lateinit var user_id: String
        }

        class ChannelAction private constructor(private val action: GeneralAction) {
            constructor(platform: String, self_id: String, name: String, service: ActionService) : this(
                GeneralAction(platform, self_id, "user.channel", name, service)
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

            @BuilderMarker
            class CreateBuilder {
                lateinit var user_id: String
                var guild_id: String? = null
            }
        }
    }

    class FriendAction private constructor(private val action: GeneralAction) {
        constructor(platform: String, self_id: String, name: String, service: ActionService) : this(
            GeneralAction(platform, self_id, "friend", name, service)
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


    class AdminAction private constructor(val login: LoginAction, val webhook: WebhookAction) {
        constructor(name: String, service: ActionService) : this(
            LoginAction(name, service), WebhookAction(name, service)
        )

        class LoginAction private constructor(private val action: GeneralAction) {
            constructor(name: String, service: ActionService) : this(
                GeneralAction(null, null, "login", name, service)
            )

            /**
             * 获取登录信息列表
             */
            fun list(): List<Login> = action.sendWithParse("list")
        }


        class WebhookAction private constructor(private val action: GeneralAction) {
            constructor(name: String, service: ActionService) : this(
                GeneralAction(null, null, "webhook", name, service)
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