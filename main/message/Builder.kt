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

package github.nyayurn.yutori_next.message

import github.nyayurn.yutori_next.BuilderMarker
import github.nyayurn.yutori_next.module.core.Core

private fun todo(): Nothing = TODO("注册消息元素构造器")

/**
 * 消息 DSL 构造器
 * @param block DSL 块
 * @return 消息段(MessageSegment)
 */
inline fun message(block: MessageDslBuilder.() -> Unit) = MessageDslBuilder().apply(block).buildMessage()

interface ChildedBuilder {
    val elements: MutableList<MessageElement>
    operator fun get(index: Int) = elements[index]
    operator fun set(index: Int, element: MessageElement) {
        elements[index] = element
    }

    fun buildMessage(): String
}

interface PropertiedBuilder : ChildedBuilder {
    val properties: MutableMap<String, Any?>
    operator fun get(key: String) = properties[key]
    operator fun set(key: String, value: Any?) {
        properties[key] = value
    }

    fun buildElement(): MessageElement
}

private fun PropertiedBuilder.buildElement(element: NodeMessageElement) = element.apply {
    properties.putAll(this@buildElement.properties)
    children.addAll(this@buildElement.elements)
}

@BuilderMarker
open class MessageDslBuilder : ChildedBuilder {
    override val elements = mutableListOf<MessageElement>()

    fun text(element: Text) = element.apply { elements += this }
    inline fun text(block: () -> String) = Text(block()).apply { elements += this }

    fun node(element: NodeMessageElement) = element.apply { elements += this }
    inline fun node(block: NodeBuilder.() -> Unit) =
        NodeBuilder().apply(block).buildElement().apply { elements += this }

    fun at(element: Core.At) = element.apply { elements += this }
    inline fun at(block: AtBuilder.() -> Unit) = AtBuilder().apply(block).buildElement().apply { elements += this }

    fun sharp(element: Core.Sharp) = element.apply { elements += this }
    inline fun sharp(block: SharpBuilder.() -> Unit) =
        SharpBuilder().apply(block).buildElement().apply { elements += this }

    fun a(element: Core.Href) = element.apply { elements += this }
    inline fun a(block: HrefBuilder.() -> Unit) = HrefBuilder().apply(block).buildElement().apply { elements += this }

    fun img(element: Core.Image) = element.apply { elements += this }
    inline fun img(block: ImageBuilder.() -> Unit) =
        ImageBuilder().apply(block).buildElement().apply { elements += this }

    fun audio(element: Core.Audio) = element.apply { elements += this }
    inline fun audio(block: AudioBuilder.() -> Unit) =
        AudioBuilder().apply(block).buildElement().apply { elements += this }

    fun video(element: Core.Video) = element.apply { elements += this }
    inline fun video(block: VideoBuilder.() -> Unit) =
        VideoBuilder().apply(block).buildElement().apply { elements += this }

    fun file(element: Core.File) = element.apply { elements += this }
    inline fun file(block: FileBuilder.() -> Unit) =
        FileBuilder().apply(block).buildElement().apply { elements += this }

    fun b(element: Core.Bold) = element.apply { elements += this }
    inline fun b(block: BoldBuilder.() -> Unit) = BoldBuilder().apply(block).buildElement().apply { elements += this }

    fun strong(element: Core.Strong) = element.apply { elements += this }
    inline fun strong(block: BoldBuilder.() -> Unit) =
        BoldBuilder().apply(block).buildElement().apply { elements += this }

    fun i(element: Core.Idiomatic) = element.apply { elements += this }
    inline fun i(block: IdiomaticBuilder.() -> Unit) =
        IdiomaticBuilder().apply(block).buildElement().apply { elements += this }

    fun em(element: Core.Em) = element.apply { elements += this }
    inline fun em(block: IdiomaticBuilder.() -> Unit) =
        IdiomaticBuilder().apply(block).buildElement().apply { elements += this }

    fun u(element: Core.Underline) = element.apply { elements += this }
    inline fun u(block: UnderlineBuilder.() -> Unit) =
        UnderlineBuilder().apply(block).buildElement().apply { elements += this }

    fun ins(element: Core.Ins) = element.apply { elements += this }
    inline fun ins(block: UnderlineBuilder.() -> Unit) =
        UnderlineBuilder().apply(block).buildElement().apply { elements += this }

    fun s(element: Core.Strikethrough) = element.apply { elements += this }
    inline fun s(block: DeleteBuilder.() -> Unit) =
        DeleteBuilder().apply(block).buildElement().apply { elements += this }

    fun del(element: Core.Delete) = element.apply { elements += this }
    inline fun del(block: DeleteBuilder.() -> Unit) =
        DeleteBuilder().apply(block).buildElement().apply { elements += this }

    fun spl(element: Core.Spl) = element.apply { elements += this }
    inline fun spl(block: SplBuilder.() -> Unit) = SplBuilder().apply(block).buildElement().apply { elements += this }

    fun code(element: Core.Code) = element.apply { elements += this }
    inline fun code(block: CodeBuilder.() -> Unit) =
        CodeBuilder().apply(block).buildElement().apply { elements += this }

    fun sup(element: Core.Sup) = element.apply { elements += this }
    inline fun sup(block: SupBuilder.() -> Unit) = SupBuilder().apply(block).buildElement().apply { elements += this }

    fun sub(element: Core.Sub) = element.apply { elements += this }
    inline fun sub(block: SubBuilder.() -> Unit) = SubBuilder().apply(block).buildElement().apply { elements += this }

    fun br(element: Core.Br) = element.apply { elements += this }
    inline fun br(block: BrBuilder.() -> Unit) = BrBuilder().apply(block).buildElement().apply { elements += this }

    fun p(element: Core.Paragraph) = element.apply { elements += this }
    inline fun p(block: ParagraphBuilder.() -> Unit) =
        ParagraphBuilder().apply(block).buildElement().apply { elements += this }

    fun message(element: Core.Message) = element.apply { elements += this }
    inline fun message(block: MessageBuilder.() -> Unit) =
        MessageBuilder().apply(block).buildElement().apply { elements += this }

    fun quote(element: Core.Quote) = element.apply { elements += this }
    inline fun quote(block: QuoteBuilder.() -> Unit) =
        QuoteBuilder().apply(block).buildElement().apply { elements += this }

    fun author(element: Core.Author) = element.apply { elements += this }
    inline fun author(block: AuthorBuilder.() -> Unit) =
        AuthorBuilder().apply(block).buildElement().apply { elements += this }

    fun button(element: Core.Button) = element.apply { elements += this }
    inline fun button(block: ButtonBuilder.() -> Unit) =
        ButtonBuilder().apply(block).buildElement().apply { elements += this }

    override fun buildMessage() = elements.joinToString("") { it.toString() }
    override fun toString() = elements.joinToString("") { it.toString() }

    @BuilderMarker
    class NodeBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        lateinit var node_name: String
        override fun buildElement() = this.buildElement(NodeMessageElement(node_name))
    }

    @BuilderMarker
    class AtBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties =
            mutableMapOf<String, Any?>("id" to null, "name" to null, "role" to null, "type" to null)
        var id: String? by properties
        var name: String? by properties
        var role: String? by properties
        var type: String? by properties
        override fun buildElement() = this.buildElement(Core.At(id, name, role, type))
    }

    @BuilderMarker
    class SharpBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to "", "name" to null)
        var id: String by properties
        var name: String? by properties
        override fun buildElement() = this.buildElement(Core.Sharp(id, name))
    }

    @BuilderMarker
    class HrefBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("href" to "")
        var href: String by properties
        override fun buildElement() = this.buildElement(Core.Href(href))
    }

    @BuilderMarker
    class ImageBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>(
            "src" to "", "title" to null, "cache" to null, "timeout" to null, "width" to null, "height" to null
        )
        var src: String by properties
        var title: String? by properties
        var cache: Boolean? by properties
        var timeout: String? by properties
        var width: Number? by properties
        var height: Number? by properties
        override fun buildElement() = this.buildElement(Core.Image(src, title, cache, timeout, width, height))
    }

    @BuilderMarker
    class AudioBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>(
            "src" to "", "title" to null, "cache" to null, "timeout" to null, "duration" to null, "poster" to null
        )
        var src: String by properties
        var title: String? by properties
        var cache: Boolean? by properties
        var timeout: String? by properties
        var duration: Number? by properties
        var poster: String? by properties
        override fun buildElement() = this.buildElement(Core.Audio(src, title, cache, timeout, duration, poster))
    }

    @BuilderMarker
    class VideoBuilder : MessageDslBuilder(), PropertiedBuilder {
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
            this.buildElement(Core.Video(src, title, cache, timeout, width, height, duration, poster))
    }

    @BuilderMarker
    class FileBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>(
            "src" to "", "title" to null, "cache" to null, "timeout" to null, "poster" to null
        )
        var src: String by properties
        var title: String? by properties
        var cache: Boolean? by properties
        var timeout: String? by properties
        var poster: String? by properties
        override fun buildElement() = this.buildElement(Core.File(src, title, cache, timeout, poster))
    }

    @BuilderMarker
    class BoldBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = this.buildElement(Core.Bold())
    }

    @BuilderMarker
    class IdiomaticBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = this.buildElement(Core.Idiomatic())
    }

    @BuilderMarker
    class UnderlineBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = this.buildElement(Core.Underline())
    }

    @BuilderMarker
    class DeleteBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = this.buildElement(Core.Strikethrough())
    }

    @BuilderMarker
    class SplBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = this.buildElement(Core.Spl())
    }

    @BuilderMarker
    class CodeBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = this.buildElement(Core.Code())
    }

    @BuilderMarker
    class SupBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = this.buildElement(Core.Sup())
    }

    @BuilderMarker
    class SubBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = this.buildElement(Core.Sub())
    }

    @BuilderMarker
    class BrBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = this.buildElement(Core.Br())
    }

    @BuilderMarker
    class ParagraphBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = this.buildElement(Core.Paragraph())
    }

    @BuilderMarker
    class MessageBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to null, "forward" to null)
        var id: String? by properties
        var forward: Boolean? by properties
        override fun buildElement() = this.buildElement(Core.Message(id, forward))
    }

    @BuilderMarker
    class QuoteBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to null, "forward" to null)
        var id: String? by properties
        var forward: Boolean? by properties
        override fun buildElement() = this.buildElement(Core.Quote(id, forward))
    }

    @BuilderMarker
    class AuthorBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to null, "name" to null, "avatar" to null)
        var id: String? by properties
        var name: String? by properties
        var avatar: String? by properties
        override fun buildElement() = this.buildElement(Core.Author(id, name, avatar))
    }

    @BuilderMarker
    class ButtonBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties =
            mutableMapOf<String, Any?>("id" to null, "type" to null, "href" to null, "text" to null, "theme" to null)
        var id: String? by properties
        var type: String? by properties
        var href: String? by properties
        var text: String? by properties
        var theme: String? by properties
        override fun buildElement() = this.buildElement(Core.Button(id, type, href, text, theme))
    }
}