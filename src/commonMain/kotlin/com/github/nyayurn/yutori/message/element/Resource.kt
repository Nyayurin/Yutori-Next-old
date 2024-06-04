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

package com.github.nyayurn.yutori.message.element

import org.jsoup.nodes.Element

class Image(
    src: String,
    title: String? = null,
    cache: Boolean? = null,
    timeout: String? = null,
    width: Number? = null,
    height: Number? = null
) : NodeMessageElement(
    "img",
    "src" to src,
    "title" to title,
    "cache" to cache,
    "timeout" to timeout,
    "width" to width,
    "height" to height
) {
    var src: String by super.properties
    var title: String? by super.properties
    var cache: Boolean? by super.properties
    var timeout: String? by super.properties
    var width: Number? by super.properties
    var height: Number? by super.properties

    companion object : MessageElementContainer(
        "src" to "",
        "title" to "",
        "cache" to false,
        "timeout" to "",
        "width" to 0,
        "height" to 0
    ) {
        override operator fun invoke(element: Element) = Image(element.attr("src"))
    }
}

class Audio(
    src: String,
    title: String? = null,
    cache: Boolean? = null,
    timeout: String? = null,
    duration: Number? = null,
    poster: String? = null
) : NodeMessageElement(
    "audio",
    "src" to src,
    "title" to title,
    "cache" to cache,
    "timeout" to timeout,
    "duration" to duration,
    "poster" to poster
) {
    var src: String by super.properties
    var title: String? by super.properties
    var cache: Boolean? by super.properties
    var timeout: String? by super.properties
    var duration: Number? by super.properties
    var poster: String? by super.properties

    companion object : MessageElementContainer(
        "src" to "",
        "title" to "",
        "cache" to false,
        "timeout" to "",
        "duration" to 0,
        "poster" to ""
    ) {
        override operator fun invoke(element: Element) = Audio(element.attr("src"))
    }
}

class Video(
    src: String,
    title: String? = null,
    cache: Boolean? = null,
    timeout: String? = null,
    width: Number? = null,
    height: Number? = null,
    duration: Number? = null,
    poster: String? = null
) : NodeMessageElement(
    "video",
    "src" to src,
    "title" to title,
    "cache" to cache,
    "timeout" to timeout,
    "width" to width,
    "height" to height,
    "duration" to duration,
    "poster" to poster
) {
    var src: String by super.properties
    var title: String? by super.properties
    var cache: Boolean? by super.properties
    var timeout: String? by super.properties
    var width: Number? by super.properties
    var height: Number? by super.properties
    var duration: Number? by super.properties
    var poster: String? by super.properties

    companion object : MessageElementContainer(
        "src" to "",
        "title" to "",
        "cache" to false,
        "timeout" to "",
        "width" to 0,
        "height" to 0,
        "duration" to 0,
        "poster" to ""
    ) {
        override operator fun invoke(element: Element) = Video(element.attr("src"))
    }
}

class File(
    src: String,
    title: String? = null,
    cache: Boolean? = null,
    timeout: String? = null,
    poster: String? = null
) : NodeMessageElement(
    "file",
    "src" to src,
    "title" to title,
    "cache" to cache,
    "timeout" to timeout,
    "poster" to poster
) {
    var src: String by super.properties
    var title: String? by super.properties
    var cache: Boolean? by super.properties
    var timeout: String? by super.properties
    var poster: String? by super.properties

    companion object : MessageElementContainer(
        "src" to "",
        "title" to "",
        "cache" to false,
        "timeout" to "",
        "poster" to ""
    ) {
        override operator fun invoke(element: Element) = File(element.attr("src"))
    }
}