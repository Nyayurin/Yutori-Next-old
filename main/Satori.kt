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

import github.nyayurn.yutori_next.message.*
import github.nyayurn.yutori_next.message.element.*
import github.nyayurn.yutori_next.message.element.NodeMessageElement
import org.jsoup.nodes.Element

fun satori(name: String = "Satori", block: Satori.() -> Unit) = Satori(name).apply(block)

@BuilderMarker
class Satori(val name: String) {
    val container = ListenersContainer()
    val modules = mutableMapOf<Class<out Module>, Module>()
    val elements = mutableMapOf<String, (Element) -> NodeMessageElement>()
    val actions_containers = mutableMapOf<String, (String, String, ActionService) -> ExtendedActionsContainer>()
    val message_builders = mutableMapOf<String, (MessageBuilder) -> ExtendedMessageBuilder>()

    init {
        elements["at"] = { At() }
        elements["sharp"] = { Sharp(it.attr("id")) }
        elements["a"] = { Href(it.attr("href")) }
        elements["img"] = { Image(it.attr("src")) }
        elements["audio"] = { Audio(it.attr("src")) }
        elements["video"] = { Video(it.attr("src")) }
        elements["file"] = { File(it.attr("src")) }
        elements["b"] = { Bold() }
        elements["strong"] = { Strong() }
        elements["i"] = { Idiomatic() }
        elements["em"] = { Em() }
        elements["u"] = { Underline() }
        elements["ins"] = { Ins() }
        elements["s"] = { Strikethrough() }
        elements["del"] = { Delete() }
        elements["spl"] = { Spl() }
        elements["code"] = { Code() }
        elements["sup"] = { Sup() }
        elements["sub"] = { Sub() }
        elements["br"] = { Br() }
        elements["p"] = { Paragraph() }
        elements["message"] = { Message() }
        elements["quote"] = { Quote() }
        elements["author"] = { Author() }
        elements["button"] = { Button() }
    }

    fun <T : Module> install(module: T, block: T.() -> Unit = {}) {
        module.block()
        module.install(this)
        modules[module::class.java] = module
    }

    inline fun <reified T : Module> uninstall() {
        modules[T::class.java]?.uninstall(this)
        modules.remove(T::class.java)
    }

    fun listening(block: ListenersContainer.() -> Unit) = container.run(block)
    fun start() = modules.values.filterIsInstance<Adapter>().forEach { module -> module.start(this) }
    fun stop() = modules.values.filterIsInstance<Adapter>().forEach { module -> module.stop(this) }
}