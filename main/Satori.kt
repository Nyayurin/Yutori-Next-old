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

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package github.nyayurn.yutori_next

import github.nyayurn.yutori_next.message.ExtendedMessageBuilder
import github.nyayurn.yutori_next.message.MessageBuilder
import github.nyayurn.yutori_next.message.NodeMessageElement
import org.jsoup.nodes.Element

fun satori(name: String = "Satori", block: Satori.() -> Unit) = Satori(name).apply(block)

@BuilderMarker
class Satori(val name: String) {
    val container = ListenersContainer()
    val modules = mutableListOf<Module>()
    val elements = mutableMapOf<String, (Element) -> NodeMessageElement>()
    val actions_containers = mutableMapOf<String, (String, String, ActionService) -> ExtendedActionsContainer>()
    val message_builders = mutableMapOf<String, (MessageBuilder) -> ExtendedMessageBuilder>()

    fun <T : Module> install(module: T, block: T.() -> Unit = {}) {
        module.block()
        module.install(this)
        modules.add(module)
    }

    fun listening(block: ListenersContainer.() -> Unit) = container.run(block)
    fun start() = modules.filterIsInstance<Adapter>().forEach { module -> module.start(this) }
    fun stop() = modules.filterIsInstance<Adapter>().forEach { module -> module.stop(this) }
}