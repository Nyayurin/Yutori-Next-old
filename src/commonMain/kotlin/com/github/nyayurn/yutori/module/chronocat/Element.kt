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
    class Poke(user_id: Number? = null) : NodeMessageElement("chronocat:poke", "user-id" to user_id) {
        var user_id: Number?
            get() = properties["user-id"] as Number?
            set(value) {
                properties["user-id"] = value
            }
    }

    object PokeContainer : MessageElementContainer("user-id" to 0) {
        override fun invoke(element: Element) = Poke()
    }
}