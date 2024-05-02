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

fun interface Listener<T : Event> {
    operator fun invoke(context: Context<T>)
}

class ListenersContainer private constructor() {
    val any = mutableListOf<Listener<Event>>()
    val guild = GuildContainer()
    val interaction = InteractionContainer()
    val login = LoginContainer()
    val message = MessageContainer()
    val reaction = ReactionContainer()
    val friend = FriendContainer()
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)

    fun any(listener: Context<Event>.() -> Unit) {
        any += Listener { it.listener() }
    }

    companion object {
        fun of(apply: ListenersContainer.() -> Unit = {}) = ListenersContainer().apply(apply)
    }

    fun runEvent(event: Event, name: String, config: Config, service: ActionService) {
        try {
            val actions = Actions(event, name, service)
            for (listener in this.any) listener(Context(actions, event, config))
            when (event) {
                is GuildEvent -> guild.runEvent(actions, event, name, config)
                is GuildMemberEvent -> guild.member.runEvent(actions, event, name, config)
                is GuildRoleEvent -> guild.role.runEvent(actions, event, name, config)
                is InteractionButtonEvent, is InteractionCommandEvent -> interaction.runEvent(actions, event, name, config)
                is LoginEvent -> login.runEvent(actions, event, name, config)
                is MessageEvent -> message.runEvent(actions, event, name, config)
                is ReactionEvent -> reaction.runEvent(actions, event, name, config)
                is UserEvent -> friend.runEvent(actions, event, name, config)
            }
        } catch (e: EventParsingException) {
            logger.error(name, "$e, event: $event")
        }
    }

    class GuildContainer {
        val added = mutableListOf<Listener<GuildEvent>>()
        val updated = mutableListOf<Listener<GuildEvent>>()
        val removed = mutableListOf<Listener<GuildEvent>>()
        val request = mutableListOf<Listener<GuildEvent>>()
        val member = MemberContainer()
        val role = RoleContainer()
        private val logger = GlobalLoggerFactory.getLogger(this::class.java)

        fun added(listener: Context<GuildEvent>.() -> Unit) {
            added += Listener { it.listener() }
        }

        fun updated(listener: Context<GuildEvent>.() -> Unit) {
            updated += Listener { it.listener() }
        }

        fun removed(listener: Context<GuildEvent>.() -> Unit) {
            removed += Listener { it.listener() }
        }

        fun request(listener: Context<GuildEvent>.() -> Unit) {
            request += Listener { it.listener() }
        }

        fun runEvent(actions: Actions, event: GuildEvent, name: String, config: Config) = when (event.type) {
            GuildEvents.Added -> added.forEach { it(Context(actions, event, config)) }
            GuildEvents.Updated -> updated.forEach { it(Context(actions, event, config)) }
            GuildEvents.Removed -> removed.forEach { it(Context(actions, event, config)) }
            GuildEvents.Request -> request.forEach { it(Context(actions, event, config)) }
            else -> logger.warn(name, "Unsupported event: $event")
        }

        class MemberContainer {
            val added = mutableListOf<Listener<GuildMemberEvent>>()
            val updated = mutableListOf<Listener<GuildMemberEvent>>()
            val removed = mutableListOf<Listener<GuildMemberEvent>>()
            val request = mutableListOf<Listener<GuildMemberEvent>>()
            private val logger = GlobalLoggerFactory.getLogger(this::class.java)

            fun added(listener: Context<GuildMemberEvent>.() -> Unit) {
                added += Listener { it.listener() }
            }

            fun updated(listener: Context<GuildMemberEvent>.() -> Unit) {
                updated += Listener { it.listener() }
            }

            fun removed(listener: Context<GuildMemberEvent>.() -> Unit) {
                removed += Listener { it.listener() }
            }

            fun request(listener: Context<GuildMemberEvent>.() -> Unit) {
                request += Listener { it.listener() }
            }

            fun runEvent(actions: Actions, event: GuildMemberEvent, name: String, config: Config) = when (event.type) {
                GuildMemberEvents.Added -> added.forEach { it(Context(actions, event, config)) }
                GuildMemberEvents.Updated -> updated.forEach { it(Context(actions, event, config)) }
                GuildMemberEvents.Removed -> removed.forEach { it(Context(actions, event, config)) }
                GuildMemberEvents.Request -> request.forEach { it(Context(actions, event, config)) }
                else -> logger.warn(name, "Unsupported event: $event")
            }
        }

        class RoleContainer {
            val created = mutableListOf<Listener<GuildRoleEvent>>()
            val updated = mutableListOf<Listener<GuildRoleEvent>>()
            val deleted = mutableListOf<Listener<GuildRoleEvent>>()
            private val logger = GlobalLoggerFactory.getLogger(this::class.java)

            fun created(listener: Context<GuildRoleEvent>.() -> Unit) {
                created += Listener { it.listener() }
            }

            fun updated(listener: Context<GuildRoleEvent>.() -> Unit) {
                updated += Listener { it.listener() }
            }

            fun deleted(listener: Context<GuildRoleEvent>.() -> Unit) {
                deleted += Listener { it.listener() }
            }

            fun runEvent(actions: Actions, event: GuildRoleEvent, name: String, config: Config) = when (event.type) {
                GuildRoleEvents.Created -> created.forEach { it(Context(actions, event, config)) }
                GuildRoleEvents.Updated -> updated.forEach { it(Context(actions, event, config)) }
                GuildRoleEvents.Deleted -> deleted.forEach { it(Context(actions, event, config)) }
                else -> logger.warn(name, "Unsupported event: $event")
            }
        }
    }

    class InteractionContainer {
        val button = mutableListOf<Listener<InteractionButtonEvent>>()
        val command = mutableListOf<Listener<InteractionCommandEvent>>()
        private val logger = GlobalLoggerFactory.getLogger(this::class.java)

        fun button(listener: Context<InteractionButtonEvent>.() -> Unit) {
            button += Listener { it.listener() }
        }

        fun command(listener: Context<InteractionCommandEvent>.() -> Unit) {
            command += Listener { it.listener() }
        }

        fun runEvent(actions: Actions, event: Event, name: String, config: Config) = when (event) {
            is InteractionButtonEvent -> button.forEach { it(Context(actions, event, config)) }
            is InteractionCommandEvent -> command.forEach { it(Context(actions, event, config)) }
            else -> logger.warn(name, "Unsupported event: $event")
        }
    }

    class LoginContainer {
        val added = mutableListOf<Listener<LoginEvent>>()
        val removed = mutableListOf<Listener<LoginEvent>>()
        val updated = mutableListOf<Listener<LoginEvent>>()
        private val logger = GlobalLoggerFactory.getLogger(this::class.java)

        fun added(listener: Context<LoginEvent>.() -> Unit) {
            added += Listener { it.listener() }
        }

        fun removed(listener: Context<LoginEvent>.() -> Unit) {
            removed += Listener { it.listener() }
        }

        fun updated(listener: Context<LoginEvent>.() -> Unit) {
            updated += Listener { it.listener() }
        }

        fun runEvent(actions: Actions, event: LoginEvent, name: String, config: Config) = when (event.type) {
            LoginEvents.Added -> added.forEach { it(Context(actions, event, config)) }
            LoginEvents.Removed -> removed.forEach { it(Context(actions, event, config)) }
            LoginEvents.Updated -> updated.forEach { it(Context(actions, event, config)) }
            else -> logger.warn(name, "Unsupported event: $event")
        }
    }

    class MessageContainer {
        val created = mutableListOf<Listener<MessageEvent>>()
        val updated = mutableListOf<Listener<MessageEvent>>()
        val deleted = mutableListOf<Listener<MessageEvent>>()
        private val logger = GlobalLoggerFactory.getLogger(this::class.java)

        fun created(listener: Context<MessageEvent>.() -> Unit) {
            created += Listener { it.listener() }
        }

        fun updated(listener: Context<MessageEvent>.() -> Unit) {
            updated += Listener { it.listener() }
        }

        fun deleted(listener: Context<MessageEvent>.() -> Unit) {
            deleted += Listener { it.listener() }
        }

        fun runEvent(actions: Actions, event: MessageEvent, name: String, config: Config) = when (event.type) {
            MessageEvents.Created -> created.forEach { it(Context(actions, event, config)) }
            MessageEvents.Updated -> updated.forEach { it(Context(actions, event, config)) }
            MessageEvents.Deleted -> deleted.forEach { it(Context(actions, event, config)) }
            else -> logger.warn(name, "Unsupported event: $event")
        }
    }

    class ReactionContainer {
        val added = mutableListOf<Listener<ReactionEvent>>()
        val removed = mutableListOf<Listener<ReactionEvent>>()
        private val logger = GlobalLoggerFactory.getLogger(this::class.java)

        fun added(listener: Context<ReactionEvent>.() -> Unit) {
            added += Listener { it.listener() }
        }

        fun removed(listener: Context<ReactionEvent>.() -> Unit) {
            removed += Listener { it.listener() }
        }

        fun runEvent(actions: Actions, event: ReactionEvent, name: String, config: Config) = when (event.type) {
            ReactionEvents.Added -> added.forEach { it(Context(actions, event, config)) }
            ReactionEvents.Removed -> removed.forEach { it(Context(actions, event, config)) }
            else -> logger.warn(name, "Unsupported event: $event")
        }
    }

    class FriendContainer {
        val request = mutableListOf<Listener<UserEvent>>()
        private val logger = GlobalLoggerFactory.getLogger(this::class.java)

        fun request(listener: Context<UserEvent>.() -> Unit) {
            request += Listener { it.listener() }
        }

        fun runEvent(actions: Actions, event: UserEvent, name: String, config: Config) = when (event.type) {
            UserEvents.Friend_Request -> request.forEach { it(Context(actions, event, config)) }
            else -> logger.warn(name, "Unsupported event: $event")
        }
    }
}