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

@file:Suppress("unused")

package github.nyayurn.yutori_next.module.core

import github.nyayurn.yutori_next.*


/**
 * 群组事件列表
 */
object GuildEvents {
    const val Added = "guild-added"
    const val Updated = "guild-updated"
    const val Removed = "guild-removed"
    const val Request = "guild-request"
    val Types = arrayOf(Added, Updated, Removed, Request)
}

/**
 * 群组事件实体类
 */
class GuildEvent(event: Event, vararg pair: Pair<String, Any?>) : Event(
    event.id,
    event.type,
    event.platform,
    event.self_id,
    event.timestamp,
    event.argv,
    event.button,
    event.channel,
    event.guild!!,
    event.login,
    event.member,
    event.message,
    event.operator,
    event.role,
    event.user,
    *pair
) {
    override val guild: Guild by properties
}

/**
 * 群组成员事件列表
 */
object GuildMemberEvents {
    const val Added = "guild-member-added"
    const val Updated = "guild-member-updated"
    const val Removed = "guild-member-removed"
    const val Request = "guild-member-request"
    val Types = arrayOf(Added, Updated, Removed, Request)
}

/**
 * 群组成员事件实体类
 */
class GuildMemberEvent(event: Event, vararg pair: Pair<String, Any?>) : Event(
    event.id,
    event.type,
    event.platform,
    event.self_id,
    event.timestamp,
    event.argv,
    event.button,
    event.channel,
    event.guild!!,
    event.login,
    event.member!!,
    event.message,
    event.operator,
    event.role,
    event.user!!,
    *pair
) {
    override val guild: Guild by properties
    override val member: GuildMember by properties
    override val user: User by properties
}

/**
 * 群组角色事件列表
 */
object GuildRoleEvents {
    const val Created = "guild-role-created"
    const val Updated = "guild-role-updated"
    const val Deleted = "guild-role-deleted"
    val Types = arrayOf(Created, Updated, Deleted)
}

/**
 * 群组角色事件实体类
 */
class GuildRoleEvent(event: Event, vararg pair: Pair<String, Any?>) : Event(
    event.id,
    event.type,
    event.platform,
    event.self_id,
    event.timestamp,
    event.argv,
    event.button,
    event.channel,
    event.guild!!,
    event.login,
    event.member,
    event.message,
    event.operator,
    event.role!!,
    event.user,
    *pair
) {
    override val guild: Guild by properties
    override val role: GuildRole by properties
}

/**
 * 交互事件列表
 */
object InteractionEvents {
    const val Button = "interaction/button"
    const val Command = "interaction/command"
    val Types = arrayOf(Button, Command)
}

/**
 * 交互事件 interaction/button 实体类
 */
class InteractionButtonEvent(event: Event, vararg pair: Pair<String, Any?>) : Event(
    event.id,
    event.type,
    event.platform,
    event.self_id,
    event.timestamp,
    event.argv,
    event.button!!,
    event.channel,
    event.guild,
    event.login,
    event.member,
    event.message,
    event.operator,
    event.role,
    event.user,
    *pair
) {
    override val button: Interaction.Button by properties
}

/**
 * 交互事件 interaction/command 实体类
 */
class InteractionCommandEvent(event: Event, vararg pair: Pair<String, Any?>) : Event(
    event.id,
    event.type,
    event.platform,
    event.self_id,
    event.timestamp,
    event.argv,
    event.button,
    event.channel,
    event.guild,
    event.login,
    event.member,
    event.message,
    event.operator,
    event.role,
    event.user,
    *pair
)

/**
 * 登录事件列表
 */
object LoginEvents {
    const val Added = "login-added"
    const val Removed = "login-removed"
    const val Updated = "login-updated"
    val Types = arrayOf(Added, Removed, Updated)
}

/**
 * 登录事件实体类
 */
class LoginEvent(event: Event, vararg pair: Pair<String, Any?>) : Event(
    event.id,
    event.type,
    event.platform,
    event.self_id,
    event.timestamp,
    event.argv,
    event.button,
    event.channel,
    event.guild,
    event.login!!,
    event.member,
    event.message,
    event.operator,
    event.role,
    event.user,
    *pair
) {
    override val login: Login by properties
}

/**
 * 消息事件列表
 */
object MessageEvents {
    const val Created = "message-created"
    const val Updated = "message-updated"
    const val Deleted = "message-deleted"
    val Types = arrayOf(Created, Updated, Deleted)
}

/**
 * 消息事件实体类
 */
class MessageEvent(event: Event, vararg pair: Pair<String, Any?>) : Event(
    event.id,
    event.type,
    event.platform,
    event.self_id,
    event.timestamp,
    event.argv,
    event.button,
    event.channel!!,
    event.guild,
    event.login,
    event.member,
    event.message!!,
    event.operator,
    event.role,
    event.user!!,
    *pair
) {
    override val channel: Channel by properties
    override val message: Message by properties
    override val user: User by properties
}

/**
 * 表态事件列表
 */
object ReactionEvents {
    const val Added = "reaction-added"
    const val Removed = "reaction-removed"
    val Types = arrayOf(Added, Removed)
}

/**
 * 表态事件实体类
 */
class ReactionEvent(event: Event, vararg pair: Pair<String, Any?>) : Event(
    event.id,
    event.type,
    event.platform,
    event.self_id,
    event.timestamp,
    event.argv,
    event.button,
    event.channel,
    event.guild,
    event.login,
    event.member,
    event.message,
    event.operator,
    event.role,
    event.user,
    *pair
)

/**
 * 用户事件列表
 */
object UserEvents {
    const val Friend_Request = "friend-request"
    val Types = arrayOf(Friend_Request)
}

/**
 * 用户事件实体类
 */
class UserEvent(event: Event, vararg pair: Pair<String, Any?>) : Event(
    event.id,
    event.type,
    event.platform,
    event.self_id,
    event.timestamp,
    event.argv,
    event.button,
    event.channel,
    event.guild,
    event.login,
    event.member,
    event.message,
    event.operator,
    event.role,
    event.user!!,
    *pair
) {
    override val user: User by properties
}