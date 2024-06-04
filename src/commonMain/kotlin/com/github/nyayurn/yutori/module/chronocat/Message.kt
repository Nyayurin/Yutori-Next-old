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

import com.github.nyayurn.yutori.BuilderMarker
import com.github.nyayurn.yutori.Satori
import com.github.nyayurn.yutori.message.ExtendedMessageBuilder
import com.github.nyayurn.yutori.message.MessageBuilder
import com.github.nyayurn.yutori.message.PropertiedMessageBuilder
import com.github.nyayurn.yutori.message.element.NodeMessageElement

@BuilderMarker
open class ChronocatMessageBuilder(builder: MessageBuilder) : ExtendedMessageBuilder(builder) {
    inline fun pc_poke(block: PcPoke.() -> Unit) = PcPoke(satori).apply(block).buildElement().apply { elements += this }

    inline fun face(block: Face.() -> Unit) = Face(satori).apply(block).buildElement().apply { elements += this }

    inline fun market_face(block: MarketFace.() -> Unit) =
        MarketFace(satori).apply(block).buildElement().apply { elements += this }

    inline fun face_bubble(block: FaceBubble.() -> Unit) =
        FaceBubble(satori).apply(block).buildElement().apply { elements += this }

    @BuilderMarker
    class PcPoke(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to 0)
        var id: Number by properties
        override fun buildElement(): NodeMessageElement = buildElement(Chronocat.PcPoke(id))
    }

    @BuilderMarker
    class Face(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to 0, "unsafe-super" to false)
        var id: Number by properties
        var unsafe_super: Boolean
            get() = properties["unsafe-super"] as Boolean
            set(value) {
                properties["unsafe-super"] = value
            }

        override fun buildElement(): NodeMessageElement = buildElement(Chronocat.Face(id, unsafe_super))
    }

    @BuilderMarker
    class MarketFace(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>("tab-id" to 0, "face-id" to "", "key" to "")
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
        override fun buildElement(): NodeMessageElement = buildElement(Chronocat.MarketFace(tab_id, face_id, key))
    }

    @BuilderMarker
    class FaceBubble(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to 0, "count" to 0, "name" to "", "content" to "")
        var id: Number by properties
        var count: Number by properties
        var name: String by properties
        var content: String by properties
        override fun buildElement(): NodeMessageElement = buildElement(Chronocat.FaceBubble(id, count, name, content))
    }
}