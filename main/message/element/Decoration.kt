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

package github.nyayurn.yutori_next.message.element

import org.jsoup.nodes.Element

class Bold : NodeMessageElement("b")
class Strong : NodeMessageElement("strong")
class Idiomatic : NodeMessageElement("i")
class Em : NodeMessageElement("em")
class Underline : NodeMessageElement("u")
class Ins : NodeMessageElement("ins")
class Strikethrough : NodeMessageElement("s")
class Delete : NodeMessageElement("del")
class Spl : NodeMessageElement("spl")
class Code : NodeMessageElement("code")
class Sup : NodeMessageElement("sup")
class Sub : NodeMessageElement("sub")

object BoldContainer : MessageElementContainer() {
    override operator fun invoke(element: Element) = Bold()
}

object StrongContainer : MessageElementContainer() {
    override operator fun invoke(element: Element) = Strong()
}

object IdiomaticContainer : MessageElementContainer() {
    override operator fun invoke(element: Element) = Idiomatic()
}

object EmContainer : MessageElementContainer() {
    override operator fun invoke(element: Element) = Em()
}

object UnderlineContainer : MessageElementContainer() {
    override operator fun invoke(element: Element) = Underline()
}

object InsContainer : MessageElementContainer() {
    override operator fun invoke(element: Element) = Ins()
}

object StrikethroughContainer : MessageElementContainer() {
    override operator fun invoke(element: Element) = Strikethrough()
}

object DeleteContainer : MessageElementContainer() {
    override operator fun invoke(element: Element) = Delete()
}

object SplContainer : MessageElementContainer() {
    override operator fun invoke(element: Element) = Spl()
}

object CodeContainer : MessageElementContainer() {
    override operator fun invoke(element: Element) = Code()
}

object SupContainer : MessageElementContainer() {
    override operator fun invoke(element: Element) = Sup()
}

object SubContainer : MessageElementContainer() {
    override operator fun invoke(element: Element) = Sub()
}