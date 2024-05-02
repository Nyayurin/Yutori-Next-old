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

package github.nyayurn.yutori_next.module.chronocat

import github.nyayurn.yutori_next.*
import kotlin.properties.Delegates

val Actions.chronocat: ChronocatAction
    get() = actions["chronocat"] as ChronocatAction

class ChronocatAction private constructor(val unsafe: UnsafeAction) : Action {
    constructor(platform: String, self_id: String, name: String, service: ActionService) : this(
        UnsafeAction(platform, self_id, name, service)
    )

    class UnsafeAction private constructor(
        val channel: ChannelAction,
        val guild: GuildAction,
        val friend: FriendAction
    ) : Action {
        constructor(platform: String, self_id: String, name: String, service: ActionService) : this(
            ChannelAction(platform, self_id, name, service),
            GuildAction(platform, self_id, name, service),
            FriendAction(platform, self_id, name, service)
        )

        class ChannelAction private constructor(private val action: GeneralAction) : Action {
            constructor(platform: String, self_id: String, name: String, service: ActionService) : this(
                GeneralAction(platform, self_id, "unsafe.channel", name, service)
            )

            fun mute(block: MuteBuilder.() -> Unit) {
                val builder = MuteBuilder().apply(block)
                action.send("mute") {
                    put("channel_id", builder.channel_id)
                    put("enable", builder.enable)
                }
            }

            @BuilderMarker
            class MuteBuilder {
                lateinit var channel_id: String
                var enable: Boolean by Delegates.notNull()
            }
        }

        class GuildAction private constructor(private val action: GeneralAction) : Action {
            constructor(platform: String, self_id: String, name: String, service: ActionService) : this(
                GeneralAction(platform, self_id, "unsafe.guild", name, service)
            )

            fun remove(block: RemoveBuilder.() -> Unit) {
                val builder = RemoveBuilder().apply(block)
                action.send("remove") {
                    put("guild_id", builder.guild_id)
                }
            }

            @BuilderMarker
            class RemoveBuilder {
                lateinit var guild_id: String
            }
        }

        class FriendAction private constructor(private val action: GeneralAction) : Action {
            constructor(platform: String, self_id: String, name: String, service: ActionService) : this(
                GeneralAction(platform, self_id, "unsafe.friend", name, service)
            )

            fun remove(block: RemoveBuilder.() -> Unit) {
                val builder = RemoveBuilder().apply(block)
                action.send("remove") {
                    put("user_id", builder.user_id)
                }
            }

            @BuilderMarker
            class RemoveBuilder {
                lateinit var user_id: String
            }
        }
    }
}