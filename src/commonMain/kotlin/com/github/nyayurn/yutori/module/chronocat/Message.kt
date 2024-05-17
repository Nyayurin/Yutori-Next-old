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
    inline fun poke(block: Poke.() -> Unit) =
        Poke(satori).apply(block).buildElement().apply { elements += this }

    @BuilderMarker
    class Poke(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>("user-id" to null)
        var user_id: Number?
            get() = properties["user-id"] as Number?
            set(value) {
                properties["user-id"] = value
            }
        override fun buildElement(): NodeMessageElement = buildElement(Chronocat.Poke(user_id))
    }
}