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

typealias Listener<T> = (context: Context<T>) -> Unit

abstract class ExtendedListenersContainer {
    abstract operator fun invoke(context: Context<AnyEvent>)
}

@BuilderMarker
class ListenersContainer {
    val any = mutableListOf<Listener<AnyEvent>>()
    val guild = Guild()
    val interaction = Interaction()
    val login = Login()
    val message = Message()
    val reaction = Reaction()
    val friend = Friend()
    val containers = mutableMapOf<String, ExtendedListenersContainer>()
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)

    fun any(listener: Context<AnyEvent>.() -> Unit) = any.add { it.listener() }

    operator fun invoke(event: Event<AnyEvent>, satori: Satori, service: ActionService) {
        try {
            val context = Context(RootActions(event.platform, event.self_id, service, satori), event, satori)
            for (listener in this.any) listener(context)
            guild(context)
            interaction(context)
            login(context)
            message(context)
            reaction(context)
            friend(context)
            for (container in containers.values) container(context)
        } catch (e: EventParsingException) {
            logger.error(satori.name, "$e, event: $event")
        }
    }

    @BuilderMarker
    class Guild {
        val added = mutableListOf<Listener<GuildEvent>>()
        val updated = mutableListOf<Listener<GuildEvent>>()
        val removed = mutableListOf<Listener<GuildEvent>>()
        val request = mutableListOf<Listener<GuildEvent>>()
        val member = Member()
        val role = Role()

        fun added(listener: Context<GuildEvent>.() -> Unit) = added.add { it.listener() }
        fun updated(listener: Context<GuildEvent>.() -> Unit) = updated.add { it.listener() }
        fun removed(listener: Context<GuildEvent>.() -> Unit) = removed.add { it.listener() }
        fun request(listener: Context<GuildEvent>.() -> Unit) = request.add { it.listener() }

        operator fun invoke(raw: Context<AnyEvent>) {
            if (raw.event.type !in GuildEvents.Types) {
                member(raw)
                role(raw)
                return
            }
            val context = Context(raw.actions, Event<GuildEvent>(raw.event), raw.satori)
            when (context.event.type) {
                GuildEvents.Added -> added.forEach { it(context) }
                GuildEvents.Updated -> updated.forEach { it(context) }
                GuildEvents.Removed -> removed.forEach { it(context) }
                GuildEvents.Request -> request.forEach { it(context) }
            }
        }

        @BuilderMarker
        class Member {
            val added = mutableListOf<Listener<GuildMemberEvent>>()
            val updated = mutableListOf<Listener<GuildMemberEvent>>()
            val removed = mutableListOf<Listener<GuildMemberEvent>>()
            val request = mutableListOf<Listener<GuildMemberEvent>>()

            fun added(listener: Context<GuildMemberEvent>.() -> Unit) = added.add { it.listener() }
            fun updated(listener: Context<GuildMemberEvent>.() -> Unit) = updated.add { it.listener() }
            fun removed(listener: Context<GuildMemberEvent>.() -> Unit) = removed.add { it.listener() }
            fun request(listener: Context<GuildMemberEvent>.() -> Unit) = request.add { it.listener() }

            operator fun invoke(raw: Context<AnyEvent>) {
                if (raw.event.type !in GuildMemberEvents.Types) return
                val context = Context(raw.actions, Event<GuildMemberEvent>(raw.event), raw.satori)
                when (context.event.type) {
                    GuildMemberEvents.Added -> added.forEach { it(context) }
                    GuildMemberEvents.Updated -> updated.forEach { it(context) }
                    GuildMemberEvents.Removed -> removed.forEach { it(context) }
                    GuildMemberEvents.Request -> request.forEach { it(context) }
                }
            }
        }

        @BuilderMarker
        class Role {
            val created = mutableListOf<Listener<GuildRoleEvent>>()
            val updated = mutableListOf<Listener<GuildRoleEvent>>()
            val deleted = mutableListOf<Listener<GuildRoleEvent>>()

            fun created(listener: Context<GuildRoleEvent>.() -> Unit) = created.add { it.listener() }
            fun updated(listener: Context<GuildRoleEvent>.() -> Unit) = updated.add { it.listener() }
            fun deleted(listener: Context<GuildRoleEvent>.() -> Unit) = deleted.add { it.listener() }

            operator fun invoke(raw: Context<AnyEvent>) {
                if (raw.event.type !in GuildRoleEvents.Types) return
                val context = Context(raw.actions, Event<GuildRoleEvent>(raw.event), raw.satori)
                when (context.event.type) {
                    GuildRoleEvents.Created -> created.forEach { it(context) }
                    GuildRoleEvents.Updated -> updated.forEach { it(context) }
                    GuildRoleEvents.Deleted -> deleted.forEach { it(context) }
                }
            }
        }
    }

    @BuilderMarker
    class Interaction {
        val button = mutableListOf<Listener<InteractionButtonEvent>>()
        val command = mutableListOf<Listener<InteractionCommandEvent>>()

        fun button(listener: Context<InteractionButtonEvent>.() -> Unit) = button.add { it.listener() }
        fun command(listener: Context<InteractionCommandEvent>.() -> Unit) = command.add { it.listener() }

        operator fun invoke(raw: Context<AnyEvent>) {
            when (raw.event.type) {
                InteractionEvents.Button -> button.forEach {
                    it(Context(raw.actions, Event(raw.event), raw.satori))
                }

                InteractionEvents.Command -> command.forEach {
                    it(Context(raw.actions, Event(raw.event), raw.satori))
                }
            }
        }
    }

    @BuilderMarker
    class Login {
        val added = mutableListOf<Listener<LoginEvent>>()
        val removed = mutableListOf<Listener<LoginEvent>>()
        val updated = mutableListOf<Listener<LoginEvent>>()

        fun added(listener: Context<LoginEvent>.() -> Unit) = added.add { it.listener() }
        fun removed(listener: Context<LoginEvent>.() -> Unit) = removed.add { it.listener() }
        fun updated(listener: Context<LoginEvent>.() -> Unit) = updated.add { it.listener() }

        operator fun invoke(raw: Context<AnyEvent>) {
            if (raw.event.type !in LoginEvents.Types) return
            val context = Context(raw.actions, Event<LoginEvent>(raw.event), raw.satori)
            when (context.event.type) {
                LoginEvents.Added -> added.forEach { it(context) }
                LoginEvents.Removed -> removed.forEach { it(context) }
                LoginEvents.Updated -> updated.forEach { it(context) }
            }
        }
    }

    @BuilderMarker
    class Message {
        val created = mutableListOf<Listener<MessageEvent>>()
        val updated = mutableListOf<Listener<MessageEvent>>()
        val deleted = mutableListOf<Listener<MessageEvent>>()

        fun created(listener: Context<MessageEvent>.() -> Unit) = created.add { it.listener() }
        fun updated(listener: Context<MessageEvent>.() -> Unit) = updated.add { it.listener() }
        fun deleted(listener: Context<MessageEvent>.() -> Unit) = deleted.add { it.listener() }

        operator fun invoke(raw: Context<AnyEvent>) {
            if (raw.event.type !in MessageEvents.Types) return
            val context = Context(raw.actions, Event<MessageEvent>(raw.event), raw.satori)
            when (context.event.type) {
                MessageEvents.Created -> created.forEach { it(context) }
                MessageEvents.Updated -> updated.forEach { it(context) }
                MessageEvents.Deleted -> deleted.forEach { it(context) }
            }
        }
    }

    @BuilderMarker
    class Reaction {
        val added = mutableListOf<Listener<ReactionEvent>>()
        val removed = mutableListOf<Listener<ReactionEvent>>()

        fun added(listener: Context<ReactionEvent>.() -> Unit) = added.add { it.listener() }
        fun removed(listener: Context<ReactionEvent>.() -> Unit) = removed.add { it.listener() }

        operator fun invoke(raw: Context<AnyEvent>) {
            if (raw.event.type !in ReactionEvents.Types) return
            val context = Context(raw.actions, Event<ReactionEvent>(raw.event), raw.satori)
            when (context.event.type) {
                ReactionEvents.Added -> added.forEach { it(context) }
                ReactionEvents.Removed -> removed.forEach { it(context) }
            }
        }
    }

    @BuilderMarker
    class Friend {
        val request = mutableListOf<Listener<UserEvent>>()

        fun request(listener: Context<UserEvent>.() -> Unit) = request.add { it.listener() }

        operator fun invoke(raw: Context<AnyEvent>) {
            when (raw.event.type) {
                UserEvents.Friend_Request -> request.forEach {
                    it(Context(raw.actions, Event(raw.event), raw.satori))
                }
            }
        }
    }
}