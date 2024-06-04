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

@file:Suppress("unused")

package com.github.nyayurn.yutori

import com.github.nyayurn.yutori.message.MessageBuilder
import com.github.nyayurn.yutori.message.element.MessageElement
import com.github.nyayurn.yutori.message.element.NodeMessageElement
import com.github.nyayurn.yutori.message.element.Text
import org.jsoup.Jsoup
import org.jsoup.nodes.*
import kotlin.streams.toList

@DslMarker
annotation class BuilderMarker

fun String.encode() = replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;")
fun String.decode() = replace("&gt;", ">").replace("&lt;", "<").replace("&quot;", "\"").replace("&amp;", "&")
fun String.toElements(satori: Satori) = MessageUtil.parse(satori, this)

object MessageUtil {
    fun select(element: String, vararg elements: MessageElement): MessageElement? {
        for (e in elements) {
            if (e is NodeMessageElement) return e.select(element) ?: continue
            if (e is Text && element == "text") return e
        }
        return null
    }

    fun parse(satori: Satori, context: String): List<MessageElement> {
        val nodes = Jsoup.parse(context).body().childNodes().stream().filter {
            it !is Comment && it !is DocumentType
        }.toList()
        return List(nodes.size) { i -> parseElement(satori, nodes[i]) }
    }

    private fun parseElement(satori: Satori, node: Node): MessageElement = when (node) {
        is TextNode -> Text(node.text())
        is Element -> {
            val container = satori.elements[node.tagName()] ?: NodeMessageElement
            container(node).apply {
                for (attr in node.attributes()) {
                    val key = attr.key
                    val value = attr.value
                    this.properties[key] = when (val type = container.properties_default[key] ?: "") {
                        is String -> value
                        is Number -> try {
                            if (value.contains(".")) {
                                runCatching {
                                    value.toDouble()
                                }.getOrElse {
                                    value.toBigDecimal()
                                }
                            } else {
                                runCatching {
                                    value.toInt()
                                }.getOrElse {
                                    runCatching {
                                        value.toLong()
                                    }.getOrElse {
                                        value.toBigInteger()
                                    }
                                }
                            }
                        } catch (_: NumberFormatException) {
                            throw NumberParsingException(value)
                        }

                        is Boolean -> try {
                            if (attr.toString().contains("=")) value.toBooleanStrict() else true
                        } catch (_: IllegalArgumentException) {
                            throw NumberParsingException(value)
                        }

                        else -> throw MessageElementPropertyParsingException(type::class.toString())
                    }
                }
                for (child in node.childNodes()) this.children += parseElement(satori, child)
            }
        }

        else -> throw MessageElementParsingException(node.toString())
    }
}

fun Context<*>.reply(quote: Boolean = true, content: MessageBuilder.() -> Unit) {
    actions.message.create(
        channel_id = event.nullable_channel!!.id,
        content = {
            if (quote) quote { id = event.nullable_message!!.id }
            content()
        }
    )
}

fun Event<*>.nick() = nullable_member?.nick ?: nullable_user?.nick ?: nullable_user?.name