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
import github.nyayurn.yutori_next.module.core.Core
import org.jsoup.nodes.Element

class Satori(val name: String, val container: ListenersContainer, val modules: MutableList<Module>) {
    val elements = mutableMapOf<String, (Element) -> NodeMessageElement>()
    val actions = mutableMapOf<String, (platform: String, id: String, service: ActionService) -> Action>()
    val message_builders = mutableMapOf<String, (MessageDslBuilder) -> ExtendedDslBuilder>()

    fun start() = modules.forEach { module -> module.install(this) }
    fun stop() = modules.forEach { module -> module.uninstall(this) }
}

fun satori(name: String = "Satori", block: SatoriBuilder.() -> Unit) = SatoriBuilder(name).apply(block).build()

@BuilderMarker
class SatoriBuilder(var name: String) {
    var container: ListenersContainer = ListenersContainer.of()
    var modules: MutableList<Module> = mutableListOf(Module.Core)

    fun <T : Module> install(module: T, block: T.() -> Unit = {}) {
        modules += module.apply(block)
    }

    fun listening(lambda: ListenersContainer.() -> Unit) {
        container = ListenersContainer.of(lambda)
    }

    fun build() = Satori(name, container, modules)
}

interface Module {
    fun install(satori: Satori)
    fun uninstall(satori: Satori)
    companion object
}

interface Adapter : Module {
    companion object
}