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
import github.nyayurn.yutori_next.message.MessageDslBuilder
import github.nyayurn.yutori_next.message.NodeMessageElement
import github.nyayurn.yutori_next.message.PropertiedBuilder

inline fun MessageDslBuilder.at(block: Core.AtBuilder.() -> Unit) =
    Core.AtBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.sharp(block: Core.SharpBuilder.() -> Unit) =
    Core.SharpBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.a(block: Core.HrefBuilder.() -> Unit) =
    Core.HrefBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.img(block: Core.ImageBuilder.() -> Unit) =
    Core.ImageBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.audio(block: Core.AudioBuilder.() -> Unit) =
    Core.AudioBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.video(block: Core.VideoBuilder.() -> Unit) =
    Core.VideoBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.file(block: Core.FileBuilder.() -> Unit) =
    Core.FileBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.b(block: Core.BoldBuilder.() -> Unit) =
    Core.BoldBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.strong(block: Core.BoldBuilder.() -> Unit) =
    Core.BoldBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.i(block: Core.IdiomaticBuilder.() -> Unit) =
    Core.IdiomaticBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.em(block: Core.IdiomaticBuilder.() -> Unit) =
    Core.IdiomaticBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.u(block: Core.UnderlineBuilder.() -> Unit) =
    Core.UnderlineBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.ins(block: Core.UnderlineBuilder.() -> Unit) =
    Core.UnderlineBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.s(block: Core.DeleteBuilder.() -> Unit) =
    Core.DeleteBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.del(block: Core.DeleteBuilder.() -> Unit) =
    Core.DeleteBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.spl(block: Core.SplBuilder.() -> Unit) =
    Core.SplBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.code(block: Core.CodeBuilder.() -> Unit) =
    Core.CodeBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.sup(block: Core.SupBuilder.() -> Unit) =
    Core.SupBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.sub(block: Core.SubBuilder.() -> Unit) =
    Core.SubBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.br(block: Core.BrBuilder.() -> Unit) =
    Core.BrBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.p(block: Core.ParagraphBuilder.() -> Unit) =
    Core.ParagraphBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.message(block: Core.MessageBuilder.() -> Unit) =
    Core.MessageBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.quote(block: Core.QuoteBuilder.() -> Unit) =
    Core.QuoteBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.author(block: Core.AuthorBuilder.() -> Unit) =
    Core.AuthorBuilder().apply(block).buildElement().apply { elements += this }

inline fun MessageDslBuilder.button(block: Core.ButtonBuilder.() -> Unit) =
    Core.ButtonBuilder().apply(block).buildElement().apply { elements += this }

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

    @BuilderMarker
    class AtBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties =
            mutableMapOf<String, Any?>("id" to null, "name" to null, "role" to null, "type" to null)
        var id: String? by properties
        var name: String? by properties
        var role: String? by properties
        var type: String? by properties
        override fun buildElement() = buildElement(At(id, name, role, type))
    }

    @BuilderMarker
    class SharpBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to "", "name" to null)
        var id: String by properties
        var name: String? by properties
        override fun buildElement() = buildElement(Sharp(id, name))
    }

    @BuilderMarker
    class HrefBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("href" to "")
        var href: String by properties
        override fun buildElement() = buildElement(Href(href))
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
        override fun buildElement() = buildElement(Image(src, title, cache, timeout, width, height))
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
        override fun buildElement() = buildElement(Audio(src, title, cache, timeout, duration, poster))
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
            buildElement(Video(src, title, cache, timeout, width, height, duration, poster))
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
        override fun buildElement() = buildElement(File(src, title, cache, timeout, poster))
    }

    @BuilderMarker
    class BoldBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Bold())
    }

    @BuilderMarker
    class IdiomaticBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Idiomatic())
    }

    @BuilderMarker
    class UnderlineBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Underline())
    }

    @BuilderMarker
    class DeleteBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Strikethrough())
    }

    @BuilderMarker
    class SplBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Spl())
    }

    @BuilderMarker
    class CodeBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Code())
    }

    @BuilderMarker
    class SupBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Sup())
    }

    @BuilderMarker
    class SubBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Sub())
    }

    @BuilderMarker
    class BrBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Br())
    }

    @BuilderMarker
    class ParagraphBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>()
        override fun buildElement() = buildElement(Paragraph())
    }

    @BuilderMarker
    class MessageBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to null, "forward" to null)
        var id: String? by properties
        var forward: Boolean? by properties
        override fun buildElement() = buildElement(Message(id, forward))
    }

    @BuilderMarker
    class QuoteBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to null, "forward" to null)
        var id: String? by properties
        var forward: Boolean? by properties
        override fun buildElement() = buildElement(Quote(id, forward))
    }

    @BuilderMarker
    class AuthorBuilder : MessageDslBuilder(), PropertiedBuilder {
        override val properties = mutableMapOf<String, Any?>("id" to null, "name" to null, "avatar" to null)
        var id: String? by properties
        var name: String? by properties
        var avatar: String? by properties
        override fun buildElement() = buildElement(Author(id, name, avatar))
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
        override fun buildElement() = buildElement(Button(id, type, href, text, theme))
    }
}