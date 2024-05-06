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

package github.nyayurn.yutori_next.message

import github.nyayurn.yutori_next.MessageUtil.encode

/**
 * 消息元素
 */
abstract class MessageElement {
    abstract override fun toString(): String
}

/**
 * 纯文本
 * @property text 文本
 */
class Text(var text: String) : MessageElement() {
    override fun toString() = text.encode()
}

/**
 * 节点消息元素
 * @property node_name 节点名称
 * @property properties 属性
 * @property children 子元素
 */
open class NodeMessageElement(val node_name: String, vararg pairs: Pair<String, Any?>) : MessageElement() {
    val properties: MutableMap<String, Any?> = mutableMapOf(*pairs)
    val children: MutableList<MessageElement> = mutableListOf()

    override fun toString() = buildString {
        append("<$node_name")
        for (item in properties) {
            val key = item.key
            val value = item.value ?: continue
            append(" ").append(
                when (value) {
                    is String -> "$key=\"${value.encode()}\""
                    is Number -> "$key=$value"
                    is Boolean -> if (value) key else ""
                    else -> throw Exception("Invalid type")
                }
            )
        }
        if (children.isEmpty()) {
            append("/>")
        } else {
            append(">")
            for (item in children) append(item)
            append("</$node_name>")
        }
    }

    fun select(element: String): MessageElement? {
        for (child in children) {
            if (element == "text" && child is Text) return child
            child as NodeMessageElement
            if (child.node_name == element) return child
            return child.select(element) ?: continue
        }
        return null
    }
}