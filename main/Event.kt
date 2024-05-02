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

package github.nyayurn.yutori_next


/**
 * Satori 事件服务接口, 用于与 Satori Server 进行通信
 */
interface EventService : AutoCloseable {
    /**
     * 与 Satori Server 建立连接
     */
    fun connect(): EventService
}

/**
 * 事件
 * @property id 事件 ID
 * @property type 事件类型
 * @property platform 接收者的平台名称
 * @property self_id 接收者的平台账号
 * @property timestamp 事件的时间戳
 * @property argv 交互指令
 * @property button 交互按钮
 * @property channel 事件所属的频道
 * @property guild 事件所属的群组
 * @property login 事件的登录信息
 * @property member 事件的目标成员
 * @property message 事件的消息
 * @property operator 事件的操作者
 * @property role 事件的目标角色
 * @property user 事件的目标用户
 */
open class Event(
    val id: Number,
    val type: String,
    val platform: String,
    val self_id: String,
    val timestamp: Number,
    open val argv: Interaction.Argv? = null,
    open val button: Interaction.Button? = null,
    open val channel: Channel? = null,
    open val guild: Guild? = null,
    open val login: Login? = null,
    open val member: GuildMember? = null,
    open val message: Message? = null,
    open val operator: User? = null,
    open val role: GuildRole? = null,
    open val user: User? = null,
    val raw: String
) : Signaling.Body {
    override fun toString(): String {
        return "Event(id=$id, type='$type', platform='$platform', self_id='$self_id', timestamp=$timestamp, argv=$argv, button=$button, channel=$channel, guild=$guild, login=$login, member=$member, message=$message, operator=$operator, role=$role, user=$user, raw='$raw')"
    }
}

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
class GuildEvent(
    id: Number,
    type: String,
    platform: String,
    self_id: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    override val guild: Guild,
    login: Login? = null,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    user: User? = null,
    raw: String
) : Event(
    id,
    type,
    platform,
    self_id,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = GuildEvent(
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
            event.raw
        )
    }
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
class GuildMemberEvent(
    id: Number,
    type: String,
    platform: String,
    self_id: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    override val guild: Guild,
    login: Login? = null,
    override val member: GuildMember,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    override val user: User,
    raw: String
) : Event(
    id,
    type,
    platform,
    self_id,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = GuildMemberEvent(
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
            event.raw
        )
    }
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
class GuildRoleEvent(
    id: Number,
    type: String,
    platform: String,
    self_id: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    override val guild: Guild,
    login: Login? = null,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    override val role: GuildRole,
    user: User? = null,
    raw: String
) : Event(
    id,
    type,
    platform,
    self_id,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = GuildRoleEvent(
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
            event.raw
        )
    }
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
class InteractionButtonEvent(
    id: Number,
    type: String,
    platform: String,
    self_id: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    override val button: Interaction.Button,
    channel: Channel? = null,
    guild: Guild? = null,
    login: Login? = null,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    user: User? = null,
    raw: String
) : Event(
    id,
    type,
    platform,
    self_id,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = InteractionButtonEvent(
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
            event.raw
        )
    }
}

/**
 * 交互事件 interaction/command 实体类
 */
class InteractionCommandEvent(
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
    raw: String
) : Event(
    id,
    type,
    platform,
    self_id,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = InteractionCommandEvent(
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
            event.raw
        )
    }
}

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
class LoginEvent(
    id: Number,
    type: String,
    platform: String,
    self_id: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    guild: Guild? = null,
    override val login: Login,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    user: User? = null,
    raw: String
) : Event(
    id,
    type,
    platform,
    self_id,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = LoginEvent(
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
            event.raw
        )
    }
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
class MessageEvent(
    id: Number,
    type: String,
    platform: String,
    self_id: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    override val channel: Channel,
    guild: Guild? = null,
    login: Login? = null,
    member: GuildMember? = null,
    override val message: Message,
    operator: User? = null,
    role: GuildRole? = null,
    override val user: User,
    raw: String
) : Event(
    id,
    type,
    platform,
    self_id,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = MessageEvent(
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
            event.raw
        )
    }
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
class ReactionEvent(
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
    raw: String
) : Event(
    id,
    type,
    platform,
    self_id,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = ReactionEvent(
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
            event.raw
        )
    }
}

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
class UserEvent(
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
    override val user: User,
    raw: String
) : Event(
    id,
    type,
    platform,
    self_id,
    timestamp,
    argv,
    button,
    channel,
    guild,
    login,
    member,
    message,
    operator,
    role,
    user,
    raw
) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = UserEvent(
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
            event.raw
        )
    }
}