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

import github.nyayurn.yutori_next.message.MessageElement
import github.nyayurn.yutori_next.message.NodeMessageElement
import github.nyayurn.yutori_next.message.Text
import org.jsoup.Jsoup
import org.jsoup.nodes.*
import org.jsoup.nodes.Element

@DslMarker
annotation class BuilderMarker

/**
 * JsonObject 字符串 DSL 构建器
 */
inline fun jsonObj(dsl: JsonObjectDSLBuilder.() -> Unit) = JsonObjectDSLBuilder().apply(dsl).toString()

/**
 * JsonArray 字符串 DSL 构建器
 */
inline fun jsonArr(dsl: JsonArrayDSLBuilder.() -> Unit) = JsonArrayDSLBuilder().apply(dsl).toString()

class JsonObjectDSLBuilder {
    val map = mutableMapOf<String, Any?>()
    fun put(key: String, value: Any?) {
        map[key] = value
    }

    fun put(key: String, block: () -> Any?) {
        map[key] = block()
    }

    fun putJsonObj(key: String, dsl: JsonObjectDSLBuilder.() -> Unit) {
        map[key] = JsonObjectDSLBuilder().apply(dsl)
    }

    fun putJsonArr(key: String, block: JsonArrayDSLBuilder.() -> Unit) {
        map[key] = JsonArrayDSLBuilder().apply(block)
    }

    override fun toString() = map.entries.filter { it.value != null }.joinToString(",", "{", "}") { (key, value) ->
        buildString {
            append("\"$key\":")
            append(
                when (value) {
                    is String -> "\"$value\""
                    else -> value.toString()
                }
            )
        }
    }
}

class JsonArrayDSLBuilder {
    val list = mutableListOf<Any?>()
    fun add(value: Any?) {
        list += value
    }

    fun add(block: () -> Any?) {
        list += block
    }

    fun addJsonArr(dsl: JsonArrayDSLBuilder.() -> Unit) {
        list += JsonArrayDSLBuilder().apply(dsl)
    }

    fun addJsonObj(block: JsonObjectDSLBuilder.() -> Unit) {
        list += JsonObjectDSLBuilder().apply(block)
    }

    override fun toString() = list.filterNotNull().joinToString(",", "[", "]") { value ->
        buildString {
            append(
                when (value) {
                    is String -> "\"$value\""
                    else -> value.toString()
                }
            )
        }
    }
}

object MessageUtil {
    fun String.encode() = replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;")
    fun String.decode() = replace("&gt;", ">").replace("&lt;", "<").replace("&quot;", "\"").replace("&amp;", "&")
    fun String.toElements(satori: Satori) = parse(satori, this)

    fun parse(satori: Satori, str: String): List<MessageElement> {
        val nodes = Jsoup.parse(str).body().childNodes().stream().filter {
            it !is Comment && it !is DocumentType
        }.toList()
        return List(nodes.size) { i -> parseElement(satori, nodes[i]) }
    }

    private fun parseElement(satori: Satori, node: Node): MessageElement = when (node) {
        is TextNode -> Text(node.text())
        is Element -> {
            val function = satori.elements[node.tagName()] ?: { NodeMessageElement(it.tagName()) }
            function(node).apply {
                for (attr in node.attributes()) this.properties[attr.key] = attr.value
                for (child in node.childNodes()) this.children += parseElement(satori, child)
            }
        }

        else -> throw UnsupportedOperationException(node.toString())
    }
}