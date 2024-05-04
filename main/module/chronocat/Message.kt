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

package github.nyayurn.yutori_next.module.chronocat

import github.nyayurn.yutori_next.BuilderMarker
import github.nyayurn.yutori_next.Satori
import github.nyayurn.yutori_next.message.ExtendedMessageBuilder
import github.nyayurn.yutori_next.message.MessageBuilder
import github.nyayurn.yutori_next.message.NodeMessageElement
import github.nyayurn.yutori_next.message.PropertiedMessageBuilder

@BuilderMarker
open class ChronocatMessageBuilder(builder: MessageBuilder) : ExtendedMessageBuilder(builder) {
    inline fun poke(block: Poke.() -> Unit) =
        Poke(satori).apply(block).buildElement().apply { elements += this }

    @BuilderMarker
    class Poke(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>("user-id" to null)
        var user_id: Number? by properties
        override fun buildElement(): NodeMessageElement = buildElement(Chronocat.Poke(user_id))
    }
}