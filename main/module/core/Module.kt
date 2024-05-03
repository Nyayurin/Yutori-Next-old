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

package github.nyayurn.yutori_next.module.core

import github.nyayurn.yutori_next.*

val Module.Companion.Core: CoreModule
    get() = CoreModule

object CoreModule : Module {
    override fun install(satori: Satori) {
        satori.actions["core"] = { platform, id, service -> CoreAction(satori, platform, id, service) }
        satori.message_builders["core"] = { CoreDslBuilder(it) }
        ElementModule.install(satori)
    }

    override fun uninstall(satori: Satori) {
        satori.actions.remove("core")
        satori.message_builders.remove("core")
        ElementModule.uninstall(satori)
    }

    private object ElementModule : Module {
        override fun install(satori: Satori) {
            satori.elements["at"] = { Core.At() }
            satori.elements["sharp"] = { Core.Sharp(it.attr("id")) }
            satori.elements["a"] = { Core.Href(it.attr("href")) }
            satori.elements["img"] = { Core.Image(it.attr("src")) }
            satori.elements["audio"] = { Core.Audio(it.attr("src")) }
            satori.elements["video"] = { Core.Video(it.attr("src")) }
            satori.elements["file"] = { Core.File(it.attr("src")) }
            satori.elements["b"] = { Core.Bold() }
            satori.elements["strong"] = { Core.Strong() }
            satori.elements["i"] = { Core.Idiomatic() }
            satori.elements["em"] = { Core.Em() }
            satori.elements["u"] = { Core.Underline() }
            satori.elements["ins"] = { Core.Ins() }
            satori.elements["s"] = { Core.Strikethrough() }
            satori.elements["del"] = { Core.Delete() }
            satori.elements["spl"] = { Core.Spl() }
            satori.elements["code"] = { Core.Code() }
            satori.elements["sup"] = { Core.Sup() }
            satori.elements["sub"] = { Core.Sub() }
            satori.elements["br"] = { Core.Br() }
            satori.elements["p"] = { Core.Paragraph() }
            satori.elements["message"] = { Core.Message() }
            satori.elements["quote"] = { Core.Quote() }
            satori.elements["author"] = { Core.Author() }
            satori.elements["button"] = { Core.Button() }
        }

        override fun uninstall(satori: Satori) {
            satori.elements.remove("at")
            satori.elements.remove("sharp")
            satori.elements.remove("a")
            satori.elements.remove("img")
            satori.elements.remove("audio")
            satori.elements.remove("video")
            satori.elements.remove("file")
            satori.elements.remove("b")
            satori.elements.remove("strong")
            satori.elements.remove("i")
            satori.elements.remove("em")
            satori.elements.remove("u")
            satori.elements.remove("ins")
            satori.elements.remove("s")
            satori.elements.remove("del")
            satori.elements.remove("spl")
            satori.elements.remove("code")
            satori.elements.remove("sup")
            satori.elements.remove("sub")
            satori.elements.remove("br")
            satori.elements.remove("p")
            satori.elements.remove("message")
            satori.elements.remove("quote")
            satori.elements.remove("author")
            satori.elements.remove("button")
        }
    }
}