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
import github.nyayurn.yutori_next.message.ExtendedDslBuilder
import github.nyayurn.yutori_next.message.MessageDslBuilder
import github.nyayurn.yutori_next.message.NodeMessageElement
import github.nyayurn.yutori_next.message.PropertiedBuilder
import github.nyayurn.yutori_next.module.core.Core.At
import github.nyayurn.yutori_next.module.core.Core.Audio
import github.nyayurn.yutori_next.module.core.Core.Author
import github.nyayurn.yutori_next.module.core.Core.Bold
import github.nyayurn.yutori_next.module.core.Core.Br
import github.nyayurn.yutori_next.module.core.Core.Button
import github.nyayurn.yutori_next.module.core.Core.Code
import github.nyayurn.yutori_next.module.core.Core.File
import github.nyayurn.yutori_next.module.core.Core.Href
import github.nyayurn.yutori_next.module.core.Core.Idiomatic
import github.nyayurn.yutori_next.module.core.Core.Image
import github.nyayurn.yutori_next.module.core.Core.Message
import github.nyayurn.yutori_next.module.core.Core.Paragraph
import github.nyayurn.yutori_next.module.core.Core.Quote
import github.nyayurn.yutori_next.module.core.Core.Sharp
import github.nyayurn.yutori_next.module.core.Core.Spl
import github.nyayurn.yutori_next.module.core.Core.Strikethrough
import github.nyayurn.yutori_next.module.core.Core.Sub
import github.nyayurn.yutori_next.module.core.Core.Sup
import github.nyayurn.yutori_next.module.core.Core.Underline
import github.nyayurn.yutori_next.module.core.Core.Video

val MessageDslBuilder.core: CoreDslBuilder
    get() = this.builders["core"] as CoreDslBuilder

object Core {
    /**
     * 提及用户
     * @property id 目标用户的 ID
     * @property name 目标用户的名称
     * @property role 目标角色
     * @property type 特殊操作，例如 all 表示 @全体成员，here 表示 @在线成员
     */
    class At(
        id: String? = null,
        name: String? = null,
        role: String? = null,
        type: String? = null
    ) : NodeMessageElement("at", "id" to id, "name" to name, "role" to role, "type" to type) {
        var id: String? by super.properties
        var name: String? by super.properties
        var role: String? by super.properties
        var type: String? by super.properties
    }

    /**
     * 提及频道
     * @property id 目标频道的 ID
     * @property name 目标频道的名称
     */
    class Sharp(id: String, name: String? = null) : NodeMessageElement("sharp", "id" to id, "name" to name) {
        var id: String by super.properties
        var name: String? by super.properties
    }

    /**
     * 链接
     * @property href 链接的 URL
     */
    class Href(href: String) : NodeMessageElement("a", "href" to href) {
        var href: String by super.properties
    }

    /**
     * 资源元素
     * @property src 资源的 URL
     * @property cache 是否使用已缓存的文件
     * @property timeout 下载文件的最长时间 (毫秒)
     */
    abstract class ResourceElement(
        name: String,
        src: String,
        title: String?,
        cache: Boolean?,
        timeout: String?,
        vararg pairs: Pair<String, Any?>
    ) : NodeMessageElement(name, "src" to src, "title" to title, "cache" to cache, "timeout" to timeout, *pairs) {
        var src: String by super.properties
        var title: String? by super.properties
        var cache: Boolean? by super.properties
        var timeout: String? by super.properties
    }

    /**
     * 图片
     * @property width 图片的宽度
     * @property height 图片的高度
     */
    class Image(
        src: String,
        title: String? = null,
        cache: Boolean? = null,
        timeout: String? = null,
        width: Number? = null,
        height: Number? = null
    ) : ResourceElement("img", src, title, cache, timeout, "width" to width, "height" to height) {
        var width: Number? by super.properties
        var height: Number? by super.properties
    }

    /**
     * 语音
     */
    class Audio(
        src: String,
        title: String? = null,
        cache: Boolean? = null,
        timeout: String? = null,
        duration: Number? = null,
        poster: String? = null
    ) : ResourceElement("audio", src, title, cache, timeout, "duration" to duration, "poster" to poster) {
        var duration: Number? by super.properties
        var poster: String? by super.properties
    }

    /**
     * 视频
     */
    class Video(
        src: String,
        title: String? = null,
        cache: Boolean? = null,
        timeout: String? = null,
        width: Number? = null,
        height: Number? = null,
        duration: Number? = null,
        poster: String? = null
    ) : ResourceElement(
        "video", src, title, cache, timeout,
        "width" to width,
        "height" to height,
        "duration" to duration,
        "poster" to poster
    ) {
        var width: Number? by super.properties
        var height: Number? by super.properties
        var duration: Number? by super.properties
        var poster: String? by super.properties
    }

    /**
     * 文件
     */
    class File(
        src: String,
        title: String? = null,
        cache: Boolean? = null,
        timeout: String? = null,
        poster: String? = null
    ) : ResourceElement("file", src, title, cache, timeout, "poster" to poster) {
        var poster: String? by super.properties
    }

    /**
     * 修饰元素
     */
    abstract class DecorationElement(name: String, vararg pairs: Pair<String, Any?>) : NodeMessageElement(name, *pairs)

    /**
     * 粗体
     */
    class Bold : DecorationElement("b")

    /**
     * 粗体
     */
    class Strong : DecorationElement("strong")

    /**
     * 斜体
     */
    class Idiomatic : DecorationElement("i")

    /**
     * 斜体
     */
    class Em : DecorationElement("em")

    /**
     * 下划线
     */
    class Underline : DecorationElement("u")

    /**
     * 下划线
     */
    class Ins : DecorationElement("ins")

    /**
     * 删除线
     */
    class Strikethrough : DecorationElement("s")

    /**
     * 删除线
     */
    class Delete : DecorationElement("del")

    /**
     * 剧透
     */
    class Spl : DecorationElement("spl")

    /**
     * 代码
     */
    class Code : DecorationElement("code")

    /**
     * 上标
     */
    class Sup : DecorationElement("sup")

    /**
     * 下标
     */
    class Sub : DecorationElement("sub")

    /**
     * 换行
     */
    class Br : NodeMessageElement("br")

    /**
     * 段落
     */
    class Paragraph : NodeMessageElement("p")

    /**
     * 消息
     * @property id 消息的 ID
     * @property forward 是否为转发消息
     */
    class Message(
        id: String? = null,
        forward: Boolean? = null
    ) : NodeMessageElement("message", "id" to id, "forward" to forward) {
        var id: String? by super.properties
        var forward: Boolean? by super.properties
    }

    /**
     * 引用
     */
    class Quote(
        id: String? = null,
        forward: Boolean? = null
    ) : NodeMessageElement("quote", "id" to id, "forward" to forward) {
        var id: String? by super.properties
        var forward: Boolean? by super.properties
    }

    /**
     * 作者
     * @property id 用户 ID
     * @property name 昵称
     * @property avatar 头像 URL
     */
    class Author(
        id: String? = null,
        name: String? = null,
        avatar: String? = null
    ) : NodeMessageElement("author", "id" to id, "name" to name, "avatar" to avatar) {
        var id: String? by super.properties
        var name: String? by super.properties
        var avatar: String? by super.properties
    }

    /**
     * 按钮
     * @property id 按钮的 ID
     * @property type 按钮的类型
     * @property href 按钮的链接
     * @property text 待输入文本
     * @property theme 按钮的样式
     */
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
}

@BuilderMarker
open class CoreDslBuilder(builder: MessageDslBuilder) : ExtendedDslBuilder(builder) {
    inline fun at(block: AtBuilder.() -> Unit) =
        AtBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun sharp(block: SharpBuilder.() -> Unit) =
        SharpBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun a(block: HrefBuilder.() -> Unit) =
        HrefBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun img(block: ImageBuilder.() -> Unit) =
        ImageBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun audio(block: AudioBuilder.() -> Unit) =
        AudioBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun video(block: VideoBuilder.() -> Unit) =
        VideoBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun file(block: FileBuilder.() -> Unit) =
        FileBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun b(block: BoldBuilder.() -> Unit) =
        BoldBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun strong(block: BoldBuilder.() -> Unit) =
        BoldBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun i(block: IdiomaticBuilder.() -> Unit) =
        IdiomaticBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun em(block: IdiomaticBuilder.() -> Unit) =
        IdiomaticBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun u(block: UnderlineBuilder.() -> Unit) =
        UnderlineBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun ins(block: UnderlineBuilder.() -> Unit) =
        UnderlineBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun s(block: DeleteBuilder.() -> Unit) =
        DeleteBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun del(block: DeleteBuilder.() -> Unit) =
        DeleteBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun spl(block: SplBuilder.() -> Unit) =
        SplBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun code(block: CodeBuilder.() -> Unit) =
        CodeBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun sup(block: SupBuilder.() -> Unit) =
        SupBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun sub(block: SubBuilder.() -> Unit) =
        SubBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun br(block: BrBuilder.() -> Unit) =
        BrBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun p(block: ParagraphBuilder.() -> Unit) =
        ParagraphBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun message(block: MessageBuilder.() -> Unit) =
        MessageBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun quote(block: QuoteBuilder.() -> Unit) =
        QuoteBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun author(block: AuthorBuilder.() -> Unit) =
        AuthorBuilder(satori).apply(block).buildElement().apply { elements += this }

    inline fun button(block: ButtonBuilder.() -> Unit) =
        ButtonBuilder(satori).apply(block).buildElement().apply { elements += this }

    @BuilderMarker
    class AtBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties =
            mutableMapOf<String, Any?>("id" to null, "name" to null, "role" to null, "type" to null)
        var id: String? by properties
        var name: String? by properties
        var role: String? by properties
        var type: String? by properties
        override fun buildElement() = buildElement(At(id, name, role, type))
    }

    @BuilderMarker
    class SharpBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to "", "name" to null)
        var id: String by properties
        var name: String? by properties
        override fun buildElement() = buildElement(Sharp(id, name))
    }

    @BuilderMarker
    class HrefBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("href" to "")
        var href: String by properties
        override fun buildElement() = buildElement(Href(href))
    }

    @BuilderMarker
    class ImageBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>(
            "src" to "", "title" to null, "cache" to null, "timeout" to null, "width" to null, "height" to null
        )
        var src: String by properties
        var title: String? by properties
        var cache: Boolean? by properties
        var timeout: String? by properties
        var width: Number? by properties
        var height: Number? by properties
        override fun buildElement() = buildElement(Image(src, title, cache, timeout, width, height))
    }

    @BuilderMarker
    class AudioBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>(
            "src" to "", "title" to null, "cache" to null, "timeout" to null, "duration" to null, "poster" to null
        )
        var src: String by properties
        var title: String? by properties
        var cache: Boolean? by properties
        var timeout: String? by properties
        var duration: Number? by properties
        var poster: String? by properties
        override fun buildElement() = buildElement(Audio(src, title, cache, timeout, duration, poster))
    }

    @BuilderMarker
    class VideoBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
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
            buildElement(Video(src, title, cache, timeout, width, height, duration, poster))
    }

    @BuilderMarker
    class FileBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>(
            "src" to "", "title" to null, "cache" to null, "timeout" to null, "poster" to null
        )
        var src: String by properties
        var title: String? by properties
        var cache: Boolean? by properties
        var timeout: String? by properties
        var poster: String? by properties
        override fun buildElement() = buildElement(File(src, title, cache, timeout, poster))
    }

    @BuilderMarker
    class BoldBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Bold())
    }

    @BuilderMarker
    class IdiomaticBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Idiomatic())
    }

    @BuilderMarker
    class UnderlineBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Underline())
    }

    @BuilderMarker
    class DeleteBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Strikethrough())
    }

    @BuilderMarker
    class SplBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Spl())
    }

    @BuilderMarker
    class CodeBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Code())
    }

    @BuilderMarker
    class SupBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Sup())
    }

    @BuilderMarker
    class SubBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Sub())
    }

    @BuilderMarker
    class BrBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Br())
    }

    @BuilderMarker
    class ParagraphBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Paragraph())
    }

    @BuilderMarker
    class MessageBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to null, "forward" to null)
        var id: String? by properties
        var forward: Boolean? by properties
        override fun buildElement() = buildElement(Message(id, forward))
    }

    @BuilderMarker
    class QuoteBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to null, "forward" to null)
        var id: String? by properties
        var forward: Boolean? by properties
        override fun buildElement() = buildElement(Quote(id, forward))
    }

    @BuilderMarker
    class AuthorBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to null, "name" to null, "avatar" to null)
        var id: String? by properties
        var name: String? by properties
        var avatar: String? by properties
        override fun buildElement() = buildElement(Author(id, name, avatar))
    }

    @BuilderMarker
    class ButtonBuilder(satori: Satori) : MessageDslBuilder(satori), PropertiedBuilder {
        override val properties =
            mutableMapOf<String, Any?>("id" to null, "type" to null, "href" to null, "text" to null, "theme" to null)
        var id: String? by properties
        var type: String? by properties
        var href: String? by properties
        var text: String? by properties
        var theme: String? by properties
        override fun buildElement() = buildElement(Button(id, type, href, text, theme))
    }
}