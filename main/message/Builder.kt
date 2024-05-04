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

package github.nyayurn.yutori_next.message

import github.nyayurn.yutori_next.BuilderMarker
import github.nyayurn.yutori_next.Satori

inline fun message(satori: Satori, block: MessageBuilder.() -> Unit) =
    MessageBuilder(satori).apply(block).buildMessage()

interface ChildedMessageBuilder {
    val elements: MutableList<MessageElement>
    operator fun get(index: Int) = elements[index]
    operator fun set(index: Int, element: MessageElement) {
        elements[index] = element
    }

    fun buildMessage(): String
}

interface PropertiedMessageBuilder : ChildedMessageBuilder {
    val properties: MutableMap<String, Any?>
    operator fun get(key: String) = properties[key]
    operator fun set(key: String, value: Any?) {
        properties[key] = value
    }

    fun buildElement(): NodeMessageElement

    fun buildElement(element: NodeMessageElement): NodeMessageElement {
        element.properties.putAll(this.properties)
        element.children.addAll(this.elements)
        return element
    }
}

abstract class ExtendedMessageBuilder(builder: MessageBuilder) {
    val satori: Satori = builder.satori
    val elements: MutableList<MessageElement> = builder.elements
}

@BuilderMarker
open class MessageBuilder(val satori: Satori) : ChildedMessageBuilder {
    override val elements = mutableListOf<MessageElement>()
    val builders = mutableMapOf<String, ExtendedMessageBuilder>().apply {
        for ((key, value) in satori.message_builders) this[key] = value(this@MessageBuilder)
    }

    fun element(element: MessageElement) = elements.add(element)

    inline fun text(block: () -> String) = Text(block()).apply { elements += this }
    inline fun node(block: Node.() -> Unit) = Node(satori).apply(block).buildElement().apply { elements += this }

    override fun buildMessage() = elements.joinToString("") { it.toString() }
    override fun toString() = elements.joinToString("") { it.toString() }

    @BuilderMarker
    class Node(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>()
        lateinit var node_name: String
        override fun buildElement(): NodeMessageElement = buildElement(NodeMessageElement(node_name))
    }
}