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

import org.jsoup.nodes.Element

class Br : NodeMessageElement("br")
class Paragraph : NodeMessageElement("p")
class Message(
    id: String? = null,
    forward: Boolean? = null
) : NodeMessageElement("message", "id" to id, "forward" to forward) {
    var id: String? by super.properties
    var forward: Boolean? by super.properties
}

object BrContainer : MessageElementContainer("id" to "", "name" to "", "role" to "", "type" to "") {
    override operator fun invoke(element: Element) = Br()
}

object ParagraphContainer : MessageElementContainer("id" to "", "name" to "", "role" to "", "type" to "") {
    override operator fun invoke(element: Element) = Paragraph()
}

object MessageContainer : MessageElementContainer("id" to "", "name" to "", "role" to "", "type" to "") {
    override operator fun invoke(element: Element) = Message()
}