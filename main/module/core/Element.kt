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

import github.nyayurn.yutori_next.message.MessageBuilder
import github.nyayurn.yutori_next.message.NodeMessageElement

val MessageBuilder.core: CoreMessageBuilder
    get() = this.builders["core"] as CoreMessageBuilder

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