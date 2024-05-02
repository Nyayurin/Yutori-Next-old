package github.nyayurn.yutori_next.message.elements

import github.nyayurn.yutori_next.Module
import github.nyayurn.yutori_next.Satori

class ElementModule : Module {
    override fun install(satori: Satori) {
        satori.config.apply {
            elements["at"] = { At() }
            elements["sharp"] = { Sharp(it.attr("id")) }
            elements["a"] = { Href(it.attr("href")) }
            elements["img"] = { Image(it.attr("src")) }
            elements["audio"] = { Audio(it.attr("src")) }
            elements["video"] = { Video(it.attr("src")) }
            elements["file"] = { File(it.attr("src")) }
            elements["b"] = { Bold() }
            elements["strong"] = { Strong() }
            elements["i"] = { Idiomatic() }
            elements["em"] = { Em() }
            elements["u"] = { Underline() }
            elements["ins"] = { Ins() }
            elements["s"] = { Strikethrough() }
            elements["del"] = { Delete() }
            elements["spl"] = { Spl() }
            elements["code"] = { Code() }
            elements["sup"] = { Sup() }
            elements["sub"] = { Sub() }
            elements["br"] = { Br() }
            elements["p"] = { Paragraph() }
            elements["message"] = { Message() }
            elements["quote"] = { Quote() }
            elements["author"] = { Author() }
            elements["button"] = { Button() }
        }
    }

    override fun uninstall(satori: Satori) {
        satori.config.apply {
            elements.remove("at")
            elements.remove("sharp")
            elements.remove("a")
            elements.remove("img")
            elements.remove("audio")
            elements.remove("video")
            elements.remove("file")
            elements.remove("b")
            elements.remove("strong")
            elements.remove("i")
            elements.remove("em")
            elements.remove("u")
            elements.remove("ins")
            elements.remove("s")
            elements.remove("del")
            elements.remove("spl")
            elements.remove("code")
            elements.remove("sup")
            elements.remove("sub")
            elements.remove("br")
            elements.remove("p")
            elements.remove("message")
            elements.remove("quote")
            elements.remove("author")
            elements.remove("button")
        }
    }
}