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

abstract class SigningEvent

interface ArgvNotNullEvent
interface ButtonNotNullEvent
interface ChannelNotNullEvent
interface GuildNotNullEvent
interface LoginNotNullEvent
interface MemberNotNullEvent
interface MessageNotNullEvent
interface OperatorNotNullEvent
interface RoleNotNullEvent
interface UserNotNullEvent

val <T> Event<T>.argv: Interaction.Argv where T : SigningEvent, T : ArgvNotNullEvent
    get() = nullable_argv as Interaction.Argv

val <T> Event<T>.button: Interaction.Button where T : SigningEvent, T : ButtonNotNullEvent
    get() = this.properties["button"] as Interaction.Button

val <T> Event<T>.channel: Channel where T : SigningEvent, T : ChannelNotNullEvent
    get() = this.properties["channel"] as Channel

val <T> Event<T>.guild: Guild where T : SigningEvent, T : GuildNotNullEvent
    get() = this.properties["guild"] as Guild

val <T> Event<T>.login: Login where T : SigningEvent, T : LoginNotNullEvent
    get() = this.properties["login"] as Login

val <T> Event<T>.member: GuildMember where T : SigningEvent, T : MemberNotNullEvent
    get() = this.properties["member"] as GuildMember

val <T> Event<T>.message: Message where T : SigningEvent, T : MessageNotNullEvent
    get() = this.properties["message"] as Message

val <T> Event<T>.operator: User where T : SigningEvent, T : OperatorNotNullEvent
    get() = this.properties["operator"] as User

val <T> Event<T>.role: GuildRole where T : SigningEvent, T : RoleNotNullEvent
    get() = this.properties["role"] as GuildRole

val <T> Event<T>.user: User where T : SigningEvent, T : UserNotNullEvent
    get() = properties["user"] as User

class AnyEvent private constructor() : SigningEvent()

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
class GuildEvent private constructor() : SigningEvent(), GuildNotNullEvent

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
class GuildMemberEvent private constructor() : SigningEvent(), GuildNotNullEvent, MemberNotNullEvent, UserNotNullEvent

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
class GuildRoleEvent private constructor() : SigningEvent(), GuildNotNullEvent, RoleNotNullEvent

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
class InteractionButtonEvent private constructor() : SigningEvent(), ButtonNotNullEvent

/**
 * 交互事件 interaction/command 实体类
 */
class InteractionCommandEvent private constructor() : SigningEvent()

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
class LoginEvent private constructor() : SigningEvent(), LoginNotNullEvent

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
class MessageEvent private constructor() : SigningEvent(), ChannelNotNullEvent, MessageNotNullEvent, UserNotNullEvent

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
class ReactionEvent private constructor() : SigningEvent()

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
class UserEvent private constructor() : SigningEvent(), UserNotNullEvent