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

package github.nyayurn.yutori_next.message

import github.nyayurn.yutori_next.message.elements.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

class MessageSegment(private val elements: List<MessageElement>): List<MessageElement> by elements {
    override fun toString() = elements.joinToString("") { it.toString() }

    override fun equals(other: Any?): Boolean {
        if (other is String) return toString() == other
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageSegment

        return elements == other.elements
    }

    override fun hashCode(): Int {
        return elements.hashCode()
    }

    companion object {
        fun of(str: String): MessageSegment {
            val nodes = Jsoup.parse(str).body().childNodes()
            return MessageSegment(List(nodes.size) { i -> parseElement(nodes[i]) })
        }

        private fun parseElement(node: Node): MessageElement = when (node) {
            is TextNode -> Text(node.text())
            is Element -> when (node.tagName()) {
                              "at" -> At()
                              "sharp" -> Sharp(node.attr("id"))
                              "a" -> Href(node.attr("href"))
                              "img" -> Image(node.attr("src"))
                              "audio" -> Audio(node.attr("src"))
                              "video" -> Video(node.attr("src"))
                              "file" -> File(node.attr("src"))
                              "b" -> Bold()
                              "strong" -> Strong()
                              "i" -> Idiomatic()
                              "em" -> Em()
                              "u" -> Underline()
                              "ins" -> Ins()
                              "s" -> Strikethrough()
                              "del" -> Delete()
                              "spl" -> Spl()
                              "code" -> Code()
                              "sup" -> Sup()
                              "sub" -> Sub()
                              "br" -> Br()
                              "p" -> Paragraph()
                              "message" -> Message()
                              "quote" -> Quote()
                              "author" -> Author()
                              "button" -> Button()
                              else -> null
                          }?.apply {
                for (attr in node.attributes()) this[attr.key] = attr.value
                for (child in node.childNodes()) this += parseElement(child)
            } ?: Custom(node.toString())

            else -> Custom(node.toString())
        }
    }
}