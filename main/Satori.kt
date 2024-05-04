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

import github.nyayurn.yutori_next.message.ExtendedDslBuilder
import github.nyayurn.yutori_next.message.MessageDslBuilder
import github.nyayurn.yutori_next.message.NodeMessageElement
import org.jsoup.nodes.Element

@BuilderMarker
class Satori(val name: String) {
    val container = ListenersContainer(this)
    val modules = mutableListOf<Module>()
    val elements = mutableMapOf<String, (Element) -> NodeMessageElement>()
    val actions = mutableMapOf<String, (platform: String, id: String, service: ActionService) -> ExtendedActions>()
    val message_builders = mutableMapOf<String, (MessageDslBuilder) -> ExtendedDslBuilder>()
    val listeners_containers = mutableMapOf<String, () -> ExtendedListenersContainer>()

    fun <T : Module> install(module: T, block: T.() -> Unit = {}) = modules.add(module.apply(block))
    fun listening(block: ListenersContainer.() -> Unit) = container.run(block)
    fun start() = modules.forEach { module -> module.install(this) }
    fun stop() = modules.forEach { module -> module.uninstall(this) }
}

fun satori(name: String = "Satori", block: Satori.() -> Unit) = Satori(name).apply(block)

interface Module {
    fun install(satori: Satori)
    fun uninstall(satori: Satori)

    companion object
}

interface Adapter : Module {
    companion object
}