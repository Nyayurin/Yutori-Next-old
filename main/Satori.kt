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
import org.jsoup.nodes.Element

fun satori(name: String = "Satori", block: Satori.() -> Unit) = Satori(name).apply(block)

@BuilderMarker
class Satori(val name: String) {
    val container = ListenersContainer()
    val modules = mutableMapOf<Class<out Module>, Module>()
    val elements = mutableMapOf<String, MessageElementContainer>()
    val actions_containers = mutableMapOf<String, (String, String, ActionService) -> ExtendedActionsContainer>()
    val message_builders = mutableMapOf<String, (MessageBuilder) -> ExtendedMessageBuilder>()

    init {
        elements["at"] = AtContainer
        elements["sharp"] = SharpContainer
        elements["a"] = HrefContainer
        elements["img"] = ImageContainer
        elements["audio"] = AudioContainer
        elements["video"] = VideoContainer
        elements["file"] = FileContainer
        elements["b"] = BoldContainer
        elements["strong"] = StrongContainer
        elements["i"] = IdiomaticContainer
        elements["em"] = EmContainer
        elements["u"] = UnderlineContainer
        elements["ins"] = InsContainer
        elements["s"] = StrikethroughContainer
        elements["del"] = DeleteContainer
        elements["spl"] = SplContainer
        elements["code"] = CodeContainer
        elements["sup"] = SupContainer
        elements["sub"] = SubContainer
        elements["br"] = BrContainer
        elements["p"] = ParagraphContainer
        elements["message"] = MessageContainer
        elements["quote"] = QuoteContainer
        elements["author"] = AuthorContainer
        elements["button"] = ButtonContainer
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