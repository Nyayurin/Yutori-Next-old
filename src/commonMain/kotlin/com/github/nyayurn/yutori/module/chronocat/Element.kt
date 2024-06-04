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

package com.github.nyayurn.yutori.module.chronocat

import com.github.nyayurn.yutori.message.MessageBuilder
import com.github.nyayurn.yutori.message.element.MessageElementContainer
import com.github.nyayurn.yutori.message.element.NodeMessageElement
import org.jsoup.nodes.Element

val MessageBuilder.chronocat: ChronocatMessageBuilder
    get() = this.builders["chronocat"] as ChronocatMessageBuilder

object Chronocat {
    class PcPoke(id: Number) : NodeMessageElement("chronocat:pcpoke", "id" to id) {
        var id: Number by properties

        companion object : MessageElementContainer("id" to 0) {
            override fun invoke(element: Element) = PcPoke(0)
        }
    }

    class Face(id: Number, unsafe_super: Boolean) : NodeMessageElement(
        "chronocat:face", "id" to id, "unsafe-super" to unsafe_super
    ) {
        var id: Number by properties
        var unsafe_super: Boolean
            get() = properties["unsafe-super"] as Boolean
            set(value) {
                properties["unsafe-super"] = value
            }

        companion object : MessageElementContainer("id" to 0, "unsafe-super" to false) {
            override fun invoke(element: Element) = Face(0, false)
        }
    }

    class MarketFace(tab_id: Number, face_id: String, key: String) : NodeMessageElement(
        "chronocat:marketface", "tab-id" to tab_id, "face-id" to face_id, "key" to key
    ) {
        var tab_id: Number
            get() = properties["tab-id"] as Number
            set(value) {
                properties["tab-id"] = value
            }
        var face_id: String
            get() = properties["face-id"] as String
            set(value) {
                properties["face-id"] = value
            }
        var key: String by properties

        companion object : MessageElementContainer("tab-id" to 0, "face-id" to "", "key" to "") {
            override fun invoke(element: Element) = MarketFace(0, "", "")
        }
    }

    class FaceBubble(id: Number, count: Number? = null, name: String? = null, content: String? = null) :
            NodeMessageElement(
                "chronocat:facebubble", "id" to id, "count" to count, "name" to name, "content" to content
            ) {
        var id: Number by properties
        var count: Number by properties
        var name: String by properties
        var content: String by properties

        companion object : MessageElementContainer("id" to 0, "count" to 0, "name" to "", "content" to "") {
            override fun invoke(element: Element) = FaceBubble(0)
        }
    }
}