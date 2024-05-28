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

package com.github.nyayurn.yutori.module.chronocat

import com.github.nyayurn.yutori.Action
import com.github.nyayurn.yutori.ActionService
import com.github.nyayurn.yutori.Actions
import com.github.nyayurn.yutori.RootActions

val RootActions.chronocat: ChronocatActions
    get() = containers["chronocat"] as ChronocatActions

class ChronocatActions(platform: String, self_id: String, service: ActionService) : Actions() {
    val unsafe = UnsafeAction(platform, self_id, service)
    val guild = GuildAction(platform, self_id, service)

    class UnsafeAction(platform: String, self_id: String, service: ActionService) {
        val channel = ChannelAction(platform, self_id, service)
        val guild = GuildAction(platform, self_id, service)
        val friend = FriendAction(platform, self_id, service)

        class ChannelAction(platform: String, self_id: String, service: ActionService) : Action(
            platform, self_id, "unsafe.channel", service
        ) {
            val member = MemberAction(platform, self_id, service)

            fun mute(channel_id: String, enable: Boolean): Unit =
                send("mute", "channel_id" to channel_id, "enable" to enable)

            class MemberAction(platform: String, self_id: String, service: ActionService) : Action(
                platform, self_id, "unsafe.channel.member", service
            ) {
                @Deprecated("This api already has a same standard api", ReplaceWith("guild.member.mute"))
                fun mute(channel_id: String, user_id: String, duration: Number): Unit =
                    send("mute", "channel_id" to channel_id, "user_id" to user_id, "duration" to duration)
            }
        }

        class GuildAction(platform: String, self_id: String, service: ActionService) : Action(
            platform, self_id, "unsafe.guild", service
        ) {
            fun remove(guild_id: String): Unit = send("remove", "guild_id" to guild_id)
        }

        class FriendAction(platform: String, self_id: String, service: ActionService) : Action(
            platform, self_id, "unsafe.friend", service
        ) {
            fun remove(user_id: String): Unit = send("remove", "user_id" to user_id)
        }
    }

    class GuildAction(platform: String, self_id: String, service: ActionService) {
        val member = MemberAction(platform, self_id, service)

        class MemberAction(platform: String, self_id: String, service: ActionService) {
            val title = TitleAction(platform, self_id, service)

            class TitleAction(platform: String, self_id: String, service: ActionService) : Action(
                platform, self_id, "chronocat.guild.member.title", service
            ) {
                fun set(guild_id: String, user_id: String, title: String?): Unit =
                    send("set", "guild_id" to guild_id, "user_id" to user_id, "title" to title)
            }
        }
    }
}