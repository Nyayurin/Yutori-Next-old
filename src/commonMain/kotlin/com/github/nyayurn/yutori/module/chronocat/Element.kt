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
    }

    class Face(id: Number, unsafe_super: Boolean) : NodeMessageElement(
        "chronocat:face", "id" to id, "unsafe_super" to unsafe_super
    ) {
        var id: Number by properties
        var unsafe_super: Boolean by properties
    }

    class MarketFace(tab_id: Number, face_id: String, key: String) : NodeMessageElement(
        "chronocat:marketface", "tab_id" to tab_id, "face_id" to face_id, "key" to key
    ) {
        var tab_id: Number by properties
        var face_id: String by properties
        var key: String by properties
    }

    class FaceBubble(id: Number, count: Number? = null, name: String? = null, content: String? = null) :
            NodeMessageElement(
                "chronocat:facebubble", "id" to id, "count" to count, "name" to name, "content" to content
            ) {
        var id: Number by properties
        var count: Number by properties
        var name: String by properties
        var content: String by properties
    }

    object PcPokeContainer : MessageElementContainer("id" to 0) {
        override fun invoke(element: Element) = PcPoke(0)
    }

    object FaceContainer : MessageElementContainer("id" to 0, "unsafe_super" to false) {
        override fun invoke(element: Element) = Face(0, false)
    }

    object MarketFaceContainer : MessageElementContainer("tab_id" to 0, "face_id" to "", "key" to "") {
        override fun invoke(element: Element) = MarketFace(0, "", "")
    }

    object FaceBubbleContainer : MessageElementContainer("id" to 0, "count" to 0, "name" to "", "content" to "") {
        override fun invoke(element: Element) = FaceBubble(0)
    }
}