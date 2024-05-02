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

@BuilderMarker
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

    fun runEvent(event: Event, satori: Satori, service: ActionService) {
        try {
            val actions = Actions(event, service, satori)
            for (listener in this.any) listener(Context(actions, event, satori))
            when (event) {
                is GuildEvent -> guild.runEvent(actions, event, satori)
                is GuildMemberEvent -> guild.member.runEvent(actions, event, satori)
                is GuildRoleEvent -> guild.role.runEvent(actions, event, satori)
                is InteractionButtonEvent, is InteractionCommandEvent -> interaction.runEvent(actions, event, satori)
                is LoginEvent -> login.runEvent(actions, event, satori)
                is MessageEvent -> message.runEvent(actions, event, satori)
                is ReactionEvent -> reaction.runEvent(actions, event, satori)
                is UserEvent -> friend.runEvent(actions, event, satori)
            }
        } catch (e: EventParsingException) {
            logger.error(satori.name, "$e, event: $event")
        }
    }

    @BuilderMarker
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

        fun runEvent(actions: Actions, event: GuildEvent, satori: Satori) = when (event.type) {
            GuildEvents.Added -> added.forEach { it(Context(actions, event, satori)) }
            GuildEvents.Updated -> updated.forEach { it(Context(actions, event, satori)) }
            GuildEvents.Removed -> removed.forEach { it(Context(actions, event, satori)) }
            GuildEvents.Request -> request.forEach { it(Context(actions, event, satori)) }
            else -> logger.warn(satori.name, "Unsupported event: $event")
        }

        @BuilderMarker
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

            fun runEvent(actions: Actions, event: GuildMemberEvent, satori: Satori) = when (event.type) {
                GuildMemberEvents.Added -> added.forEach { it(Context(actions, event, satori)) }
                GuildMemberEvents.Updated -> updated.forEach { it(Context(actions, event, satori)) }
                GuildMemberEvents.Removed -> removed.forEach { it(Context(actions, event, satori)) }
                GuildMemberEvents.Request -> request.forEach { it(Context(actions, event, satori)) }
                else -> logger.warn(satori.name, "Unsupported event: $event")
            }
        }

        @BuilderMarker
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

            fun runEvent(actions: Actions, event: GuildRoleEvent, satori: Satori) = when (event.type) {
                GuildRoleEvents.Created -> created.forEach { it(Context(actions, event, satori)) }
                GuildRoleEvents.Updated -> updated.forEach { it(Context(actions, event, satori)) }
                GuildRoleEvents.Deleted -> deleted.forEach { it(Context(actions, event, satori)) }
                else -> logger.warn(satori.name, "Unsupported event: $event")
            }
        }
    }

    @BuilderMarker
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

        fun runEvent(actions: Actions, event: Event, satori: Satori) = when (event) {
            is InteractionButtonEvent -> button.forEach { it(Context(actions, event, satori)) }
            is InteractionCommandEvent -> command.forEach { it(Context(actions, event, satori)) }
            else -> logger.warn(satori.name, "Unsupported event: $event")
        }
    }

    @BuilderMarker
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

        fun runEvent(actions: Actions, event: LoginEvent, satori: Satori) = when (event.type) {
            LoginEvents.Added -> added.forEach { it(Context(actions, event, satori)) }
            LoginEvents.Removed -> removed.forEach { it(Context(actions, event, satori)) }
            LoginEvents.Updated -> updated.forEach { it(Context(actions, event, satori)) }
            else -> logger.warn(satori.name, "Unsupported event: $event")
        }
    }

    @BuilderMarker
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

        fun runEvent(actions: Actions, event: MessageEvent, satori: Satori) = when (event.type) {
            MessageEvents.Created -> created.forEach { it(Context(actions, event, satori)) }
            MessageEvents.Updated -> updated.forEach { it(Context(actions, event, satori)) }
            MessageEvents.Deleted -> deleted.forEach { it(Context(actions, event, satori)) }
            else -> logger.warn(satori.name, "Unsupported event: $event")
        }
    }

    @BuilderMarker
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

        fun runEvent(actions: Actions, event: ReactionEvent, satori: Satori) = when (event.type) {
            ReactionEvents.Added -> added.forEach { it(Context(actions, event, satori)) }
            ReactionEvents.Removed -> removed.forEach { it(Context(actions, event, satori)) }
            else -> logger.warn(satori.name, "Unsupported event: $event")
        }
    }

    @BuilderMarker
    class FriendContainer {
        val request = mutableListOf<Listener<UserEvent>>()
        private val logger = GlobalLoggerFactory.getLogger(this::class.java)

        fun request(listener: Context<UserEvent>.() -> Unit) {
            request += Listener { it.listener() }
        }

        fun runEvent(actions: Actions, event: UserEvent, satori: Satori) = when (event.type) {
            UserEvents.Friend_Request -> request.forEach { it(Context(actions, event, satori)) }
            else -> logger.warn(satori.name, "Unsupported event: $event")
        }
    }
}