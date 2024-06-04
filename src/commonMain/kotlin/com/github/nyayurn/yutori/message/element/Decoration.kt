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

class Bold : NodeMessageElement("b") {
    companion object : MessageElementContainer() {
        override operator fun invoke(element: Element) = Bold()
    }
}

class Strong : NodeMessageElement("strong") {
    companion object : MessageElementContainer() {
        override operator fun invoke(element: Element) = Strong()
    }
}

class Idiomatic : NodeMessageElement("i") {
    companion object : MessageElementContainer() {
        override operator fun invoke(element: Element) = Idiomatic()
    }
}

class Em : NodeMessageElement("em") {
    companion object : MessageElementContainer() {
        override operator fun invoke(element: Element) = Em()
    }
}

class Underline : NodeMessageElement("u") {
    companion object : MessageElementContainer() {
        override operator fun invoke(element: Element) = Underline()
    }
}

class Ins : NodeMessageElement("ins") {
    companion object : MessageElementContainer() {
        override operator fun invoke(element: Element) = Ins()
    }
}

class Strikethrough : NodeMessageElement("s") {
    companion object : MessageElementContainer() {
        override operator fun invoke(element: Element) = Strikethrough()
    }
}

class Delete : NodeMessageElement("del") {
    companion object : MessageElementContainer() {
        override operator fun invoke(element: Element) = Delete()
    }
}

class Spl : NodeMessageElement("spl") {
    companion object : MessageElementContainer() {
        override operator fun invoke(element: Element) = Spl()
    }
}

class Code : NodeMessageElement("code") {
    companion object : MessageElementContainer() {
        override operator fun invoke(element: Element) = Code()
    }
}

class Sup : NodeMessageElement("sup") {
    companion object : MessageElementContainer() {
        override operator fun invoke(element: Element) = Sup()
    }
}

class Sub : NodeMessageElement("sub") {
    companion object : MessageElementContainer() {
        override operator fun invoke(element: Element) = Sub()
    }
}