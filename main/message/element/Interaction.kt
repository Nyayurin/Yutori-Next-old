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

class Button(
    id: String? = null,
    type: String? = null,
    href: String? = null,
    text: String? = null,
    theme: String? = null
) : NodeMessageElement("button", "id" to id, "type" to type, "href" to href, "text" to text, "theme" to theme) {
    var id: String? by super.properties
    var type: String? by super.properties
    var href: String? by super.properties
    var text: String? by super.properties
    var theme: String? by super.properties
}

object ButtonContainer : MessageElementContainer("id" to "", "name" to "", "role" to "", "type" to "") {
    override operator fun invoke(element: Element) = Button()
}