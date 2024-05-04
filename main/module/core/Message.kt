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

import github.nyayurn.yutori_next.BuilderMarker
import github.nyayurn.yutori_next.Satori
import github.nyayurn.yutori_next.message.ExtendedMessageBuilder
import github.nyayurn.yutori_next.message.MessageBuilder
import github.nyayurn.yutori_next.message.PropertiedMessageBuilder

@BuilderMarker
open class CoreMessageBuilder(builder: MessageBuilder) : ExtendedMessageBuilder(builder) {
    inline fun at(block: At.() -> Unit) =
        At(satori).apply(block).buildElement().apply { elements += this }

    inline fun sharp(block: Sharp.() -> Unit) =
        Sharp(satori).apply(block).buildElement().apply { elements += this }

    inline fun a(block: Href.() -> Unit) =
        Href(satori).apply(block).buildElement().apply { elements += this }

    inline fun img(block: Image.() -> Unit) =
        Image(satori).apply(block).buildElement().apply { elements += this }

    inline fun audio(block: Audio.() -> Unit) =
        Audio(satori).apply(block).buildElement().apply { elements += this }

    inline fun video(block: Video.() -> Unit) =
        Video(satori).apply(block).buildElement().apply { elements += this }

    inline fun file(block: File.() -> Unit) =
        File(satori).apply(block).buildElement().apply { elements += this }

    inline fun b(block: Bold.() -> Unit) =
        Bold(satori).apply(block).buildElement().apply { elements += this }

    inline fun strong(block: Bold.() -> Unit) =
        Bold(satori).apply(block).buildElement().apply { elements += this }

    inline fun i(block: Idiomatic.() -> Unit) =
        Idiomatic(satori).apply(block).buildElement().apply { elements += this }

    inline fun em(block: Idiomatic.() -> Unit) =
        Idiomatic(satori).apply(block).buildElement().apply { elements += this }

    inline fun u(block: Underline.() -> Unit) =
        Underline(satori).apply(block).buildElement().apply { elements += this }

    inline fun ins(block: Underline.() -> Unit) =
        Underline(satori).apply(block).buildElement().apply { elements += this }

    inline fun s(block: Delete.() -> Unit) =
        Delete(satori).apply(block).buildElement().apply { elements += this }

    inline fun del(block: Delete.() -> Unit) =
        Delete(satori).apply(block).buildElement().apply { elements += this }

    inline fun spl(block: Spl.() -> Unit) =
        Spl(satori).apply(block).buildElement().apply { elements += this }

    inline fun code(block: Code.() -> Unit) =
        Code(satori).apply(block).buildElement().apply { elements += this }

    inline fun sup(block: Sup.() -> Unit) =
        Sup(satori).apply(block).buildElement().apply { elements += this }

    inline fun sub(block: Sub.() -> Unit) =
        Sub(satori).apply(block).buildElement().apply { elements += this }

    inline fun br(block: Br.() -> Unit) =
        Br(satori).apply(block).buildElement().apply { elements += this }

    inline fun p(block: Paragraph.() -> Unit) =
        Paragraph(satori).apply(block).buildElement().apply { elements += this }

    inline fun message(block: Message.() -> Unit) =
        Message(satori).apply(block).buildElement().apply { elements += this }

    inline fun quote(block: Quote.() -> Unit) =
        Quote(satori).apply(block).buildElement().apply { elements += this }

    inline fun author(block: Author.() -> Unit) =
        Author(satori).apply(block).buildElement().apply { elements += this }

    inline fun button(block: Button.() -> Unit) =
        Button(satori).apply(block).buildElement().apply { elements += this }

    @BuilderMarker
    class At(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties =
            mutableMapOf<String, Any?>("id" to null, "name" to null, "role" to null, "type" to null)
        var id: String? by properties
        var name: String? by properties
        var role: String? by properties
        var type: String? by properties
        override fun buildElement() = buildElement(Core.At(id, name, role, type))
    }

    @BuilderMarker
    class Sharp(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to "", "name" to null)
        var id: String by properties
        var name: String? by properties
        override fun buildElement() = buildElement(Core.Sharp(id, name))
    }

    @BuilderMarker
    class Href(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>("href" to "")
        var href: String by properties
        override fun buildElement() = buildElement(Core.Href(href))
    }

    @BuilderMarker
    class Image(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>(
            "src" to "", "title" to null, "cache" to null, "timeout" to null, "width" to null, "height" to null
        )
        var src: String by properties
        var title: String? by properties
        var cache: Boolean? by properties
        var timeout: String? by properties
        var width: Number? by properties
        var height: Number? by properties
        override fun buildElement() = buildElement(Core.Image(src, title, cache, timeout, width, height))
    }

    @BuilderMarker
    class Audio(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>(
            "src" to "", "title" to null, "cache" to null, "timeout" to null, "duration" to null, "poster" to null
        )
        var src: String by properties
        var title: String? by properties
        var cache: Boolean? by properties
        var timeout: String? by properties
        var duration: Number? by properties
        var poster: String? by properties
        override fun buildElement() = buildElement(Core.Audio(src, title, cache, timeout, duration, poster))
    }

    @BuilderMarker
    class Video(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>(
            "src" to "", "title" to null, "cache" to null, "timeout" to null, "width" to null, "height" to null,
            "duration" to null, "poster" to null
        )
        var src: String by properties
        var title: String? by properties
        var cache: Boolean? by properties
        var timeout: String? by properties
        var width: Number? by properties
        var height: Number? by properties
        var duration: Number? by properties
        var poster: String? by properties
        override fun buildElement() =
            buildElement(Core.Video(src, title, cache, timeout, width, height, duration, poster))
    }

    @BuilderMarker
    class File(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>(
            "src" to "", "title" to null, "cache" to null, "timeout" to null, "poster" to null
        )
        var src: String by properties
        var title: String? by properties
        var cache: Boolean? by properties
        var timeout: String? by properties
        var poster: String? by properties
        override fun buildElement() = buildElement(Core.File(src, title, cache, timeout, poster))
    }

    @BuilderMarker
    class Bold(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Core.Bold())
    }

    @BuilderMarker
    class Idiomatic(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Core.Idiomatic())
    }

    @BuilderMarker
    class Underline(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Core.Underline())
    }

    @BuilderMarker
    class Delete(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Core.Strikethrough())
    }

    @BuilderMarker
    class Spl(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Core.Spl())
    }

    @BuilderMarker
    class Code(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Core.Code())
    }

    @BuilderMarker
    class Sup(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Core.Sup())
    }

    @BuilderMarker
    class Sub(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Core.Sub())
    }

    @BuilderMarker
    class Br(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Core.Br())
    }

    @BuilderMarker
    class Paragraph(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Core.Paragraph())
    }

    @BuilderMarker
    class Message(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to null, "forward" to null)
        var id: String? by properties
        var forward: Boolean? by properties
        override fun buildElement() = buildElement(Core.Message(id, forward))
    }

    @BuilderMarker
    class Quote(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to null, "forward" to null)
        var id: String? by properties
        var forward: Boolean? by properties
        override fun buildElement() = buildElement(Core.Quote(id, forward))
    }

    @BuilderMarker
    class Author(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to null, "name" to null, "avatar" to null)
        var id: String? by properties
        var name: String? by properties
        var avatar: String? by properties
        override fun buildElement() = buildElement(Core.Author(id, name, avatar))
    }

    @BuilderMarker
    class Button(satori: Satori) : MessageBuilder(satori), PropertiedMessageBuilder {
        override val properties =
            mutableMapOf<String, Any?>("id" to null, "type" to null, "href" to null, "text" to null, "theme" to null)
        var id: String? by properties
        var type: String? by properties
        var href: String? by properties
        var text: String? by properties
        var theme: String? by properties
        override fun buildElement() = buildElement(Core.Button(id, type, href, text, theme))
    }
}