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

import github.nyayurn.yutori_next.message.elements.*

/**
 * 消息 DSL 构造器
 * @param block DSL 块
 * @return 消息段(MessageSegment)
 */
inline fun message(block: MessageDslBuilder.() -> Unit) = MessageDslBuilder().apply(block).buildMessage()

@DslMarker
annotation class MessageDSL

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

@MessageDSL
open class MessageDslBuilder : ChildedBuilder {
    override val elements = mutableListOf<MessageElement>()

    fun text(element: Text) = element.apply { elements += this }
    inline fun text(block: () -> String) = Text(block()).apply { elements += this }

    fun node(element: NodeMessageElement) = element.apply { elements += this }
    inline fun node(block: NodeBuilder.() -> Unit) =
        NodeBuilder().apply(block).buildElement().apply { elements += this }

    fun at(element: At) = element.apply { elements += this }
    inline fun at(block: AtBuilder.() -> Unit) = AtBuilder().apply(block).buildElement().apply { elements += this }

    fun sharp(element: Sharp) = element.apply { elements += this }
    inline fun sharp(block: SharpBuilder.() -> Unit) =
        SharpBuilder().apply(block).buildElement().apply { elements += this }

    fun a(element: Href) = element.apply { elements += this }
    inline fun a(block: HrefBuilder.() -> Unit) = HrefBuilder().apply(block).buildElement().apply { elements += this }

    fun img(element: Image) = element.apply { elements += this }
    inline fun img(block: ImageBuilder.() -> Unit) =
        ImageBuilder().apply(block).buildElement().apply { elements += this }

    fun audio(element: Audio) = element.apply { elements += this }
    inline fun audio(block: AudioBuilder.() -> Unit) =
        AudioBuilder().apply(block).buildElement().apply { elements += this }

    fun video(element: Video) = element.apply { elements += this }
    inline fun video(block: VideoBuilder.() -> Unit) =
        VideoBuilder().apply(block).buildElement().apply { elements += this }

    fun file(element: File) = element.apply { elements += this }
    inline fun file(block: FileBuilder.() -> Unit) =
        FileBuilder().apply(block).buildElement().apply { elements += this }

    fun b(element: Bold) = element.apply { elements += this }
    inline fun b(block: BoldBuilder.() -> Unit) = BoldBuilder().apply(block).buildElement().apply { elements += this }

    fun strong(element: Strong) = element.apply { elements += this }
    inline fun strong(block: BoldBuilder.() -> Unit) =
        BoldBuilder().apply(block).buildElement().apply { elements += this }

    fun i(element: Idiomatic) = element.apply { elements += this }
    inline fun i(block: IdiomaticBuilder.() -> Unit) =
        IdiomaticBuilder().apply(block).buildElement().apply { elements += this }

    fun em(element: Em) = element.apply { elements += this }
    inline fun em(block: IdiomaticBuilder.() -> Unit) =
        IdiomaticBuilder().apply(block).buildElement().apply { elements += this }

    fun u(element: Underline) = element.apply { elements += this }
    inline fun u(block: UnderlineBuilder.() -> Unit) =
        UnderlineBuilder().apply(block).buildElement().apply { elements += this }

    fun ins(element: Ins) = element.apply { elements += this }
    inline fun ins(block: UnderlineBuilder.() -> Unit) =
        UnderlineBuilder().apply(block).buildElement().apply { elements += this }

    fun s(element: Strikethrough) = element.apply { elements += this }
    inline fun s(block: DeleteBuilder.() -> Unit) =
        DeleteBuilder().apply(block).buildElement().apply { elements += this }

    fun del(element: Delete) = element.apply { elements += this }
    inline fun del(block: DeleteBuilder.() -> Unit) =
        DeleteBuilder().apply(block).buildElement().apply { elements += this }

    fun spl(element: Spl) = element.apply { elements += this }
    inline fun spl(block: SplBuilder.() -> Unit) = SplBuilder().apply(block).buildElement().apply { elements += this }

    fun code(element: Code) = element.apply { elements += this }
    inline fun code(block: CodeBuilder.() -> Unit) =
        CodeBuilder().apply(block).buildElement().apply { elements += this }

    fun sup(element: Sup) = element.apply { elements += this }
    inline fun sup(block: SupBuilder.() -> Unit) = SupBuilder().apply(block).buildElement().apply { elements += this }

    fun sub(element: Sub) = element.apply { elements += this }
    inline fun sub(block: SubBuilder.() -> Unit) = SubBuilder().apply(block).buildElement().apply { elements += this }

    fun br(element: Br) = element.apply { elements += this }
    inline fun br(block: BrBuilder.() -> Unit) = BrBuilder().apply(block).buildElement().apply { elements += this }

    fun p(element: Paragraph) = element.apply { elements += this }
    inline fun p(block: ParagraphBuilder.() -> Unit) =
        ParagraphBuilder().apply(block).buildElement().apply { elements += this }

    fun message(element: Message) = element.apply { elements += this }
    inline fun message(block: MessageBuilder.() -> Unit) =
        MessageBuilder().apply(block).buildElement().apply { elements += this }

    fun quote(element: Quote) = element.apply { elements += this }
    inline fun quote(block: QuoteBuilder.() -> Unit) =
        QuoteBuilder().apply(block).buildElement().apply { elements += this }

    fun author(element: Author) = element.apply { elements += this }
    inline fun author(block: AuthorBuilder.() -> Unit) =
        AuthorBuilder().apply(block).buildElement().apply { elements += this }

    fun button(element: Button) = element.apply { elements += this }
    inline fun button(block: ButtonBuilder.() -> Unit) =
        ButtonBuilder().apply(block).buildElement().apply { elements += this }

    override fun buildMessage() = elements.joinToString("") { it.toString() }
    override fun toString() = elements.joinToString("") { it.toString() }
}

@MessageDSL
class NodeBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties = mutableMapOf<String, Any?>()
    lateinit var node_name: String
    override fun buildElement() = this.buildElement(NodeMessageElement(node_name))
}

@MessageDSL
class AtBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties =
        mutableMapOf<String, Any?>("id" to null, "name" to null, "role" to null, "type" to null)
    var id: String? by properties
    var name: String? by properties
    var role: String? by properties
    var type: String? by properties
    override fun buildElement() = this.buildElement(At(id, name, role, type))
}

@MessageDSL
class SharpBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties = mutableMapOf<String, Any?>("id" to "", "name" to null)
    var id: String by properties
    var name: String? by properties
    override fun buildElement() = this.buildElement(Sharp(id, name))
}

@MessageDSL
class HrefBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties = mutableMapOf<String, Any?>("href" to "")
    var href: String by properties
    override fun buildElement() = this.buildElement(Href(href))
}

@MessageDSL
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
    override fun buildElement() = this.buildElement(Image(src, title, cache, timeout, width, height))
}

@MessageDSL
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
    override fun buildElement() = this.buildElement(Audio(src, title, cache, timeout, duration, poster))
}

@MessageDSL
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
        this.buildElement(Video(src, title, cache, timeout, width, height, duration, poster))
}

@MessageDSL
class FileBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties = mutableMapOf<String, Any?>(
        "src" to "", "title" to null, "cache" to null, "timeout" to null, "poster" to null
    )
    var src: String by properties
    var title: String? by properties
    var cache: Boolean? by properties
    var timeout: String? by properties
    var poster: String? by properties
    override fun buildElement() = this.buildElement(File(src, title, cache, timeout, poster))
}

@MessageDSL
class BoldBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties = mutableMapOf<String, Any?>()
    override fun buildElement() = this.buildElement(Bold())
}

@MessageDSL
class IdiomaticBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties = mutableMapOf<String, Any?>()
    override fun buildElement() = this.buildElement(Idiomatic())
}

@MessageDSL
class UnderlineBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties = mutableMapOf<String, Any?>()
    override fun buildElement() = this.buildElement(Underline())
}

@MessageDSL
class DeleteBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties = mutableMapOf<String, Any?>()
    override fun buildElement() = this.buildElement(Strikethrough())
}

@MessageDSL
class SplBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties = mutableMapOf<String, Any?>()
    override fun buildElement() = this.buildElement(Spl())
}

@MessageDSL
class CodeBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties = mutableMapOf<String, Any?>()
    override fun buildElement() = this.buildElement(Code())
}

@MessageDSL
class SupBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties = mutableMapOf<String, Any?>()
    override fun buildElement() = this.buildElement(Sup())
}

@MessageDSL
class SubBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties = mutableMapOf<String, Any?>()
    override fun buildElement() = this.buildElement(Sub())
}

@MessageDSL
class BrBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties = mutableMapOf<String, Any?>()
    override fun buildElement() = this.buildElement(Br())
}

@MessageDSL
class ParagraphBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties = mutableMapOf<String, Any?>()
    override fun buildElement() = this.buildElement(Paragraph())
}

@MessageDSL
class MessageBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties = mutableMapOf<String, Any?>("id" to null, "forward" to null)
    var id: String? by properties
    var forward: Boolean? by properties
    override fun buildElement() = this.buildElement(Message(id, forward))
}

@MessageDSL
class QuoteBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties = mutableMapOf<String, Any?>("id" to null, "forward" to null)
    var id: String? by properties
    var forward: Boolean? by properties
    override fun buildElement() = this.buildElement(Quote(id, forward))
}

@MessageDSL
class AuthorBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties = mutableMapOf<String, Any?>("id" to null, "name" to null, "avatar" to null)
    var id: String? by properties
    var name: String? by properties
    var avatar: String? by properties
    override fun buildElement() = this.buildElement(Author(id, name, avatar))
}

@MessageDSL
class ButtonBuilder : MessageDslBuilder(), PropertiedBuilder {
    override val properties =
        mutableMapOf<String, Any?>("id" to null, "type" to null, "href" to null, "text" to null, "theme" to null)
    var id: String? by properties
    var type: String? by properties
    var href: String? by properties
    var text: String? by properties
    var theme: String? by properties
    override fun buildElement() = this.buildElement(Button(id, type, href, text, theme))
}