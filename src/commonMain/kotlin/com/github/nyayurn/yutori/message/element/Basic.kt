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

package com.github.nyayurn.yutori.message.element

import com.github.nyayurn.yutori.encode
import org.jsoup.nodes.Element

class Text(var text: String) : MessageElement() {
    override fun toString() = text.encode()
}

class At(
    id: String? = null,
    name: String? = null,
    role: String? = null,
    type: String? = null
) : NodeMessageElement("at", "id" to id, "name" to name, "role" to role, "type" to type) {
    var id: String? by super.properties
    var name: String? by super.properties
    var role: String? by super.properties
    var type: String? by super.properties

    companion object : MessageElementContainer("id" to "", "name" to "", "role" to "", "type" to "") {
        override operator fun invoke(element: Element) = At()
    }
}

class Sharp(id: String, name: String? = null) : NodeMessageElement("sharp", "id" to id, "name" to name) {
    var id: String by super.properties
    var name: String? by super.properties

    companion object : MessageElementContainer("id" to "", "name" to "") {
        override operator fun invoke(element: Element) = Sharp(element.attr("id"))
    }
}

class Href(href: String) : NodeMessageElement("a", "href" to href) {
    var href: String by super.properties

    companion object : MessageElementContainer("href" to "") {
        override operator fun invoke(element: Element) = Href(element.attr("href"))
    }
}