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

typealias Listener<T> = (context: Context<T>) -> Unit

abstract class ExtendedListenersContainer {
    abstract operator fun invoke(context: Context<Event>)
}

@BuilderMarker
class ListenersContainer {
    val any = mutableListOf<Listener<Event>>()
    val containers = mutableMapOf<String, ExtendedListenersContainer>()
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)

    fun any(listener: Context<Event>.() -> Unit) = any.add { it.listener() }

    operator fun invoke(event: Event, satori: Satori, service: ActionService) {
        try {
            val context = Context(ActionsContainer(event, service, satori), event, satori)
            for (listener in this.any) listener(context)
            for (container in containers.values) container(context)
        } catch (e: EventParsingException) {
            logger.error(satori.name, "$e, event: $event")
        }
    }
}