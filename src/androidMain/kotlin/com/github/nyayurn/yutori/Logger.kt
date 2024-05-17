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

@file:Suppress("unused")

package com.github.nyayurn.yutori

import com.github.nyayurn.yutori.Level.*
import java.text.SimpleDateFormat
import java.util.*

actual object LoggerColor {
    actual fun blue(msg: String) = "\u001B[38;5;4m$msg\u001B[0m"
    actual fun gray(msg: String) = ""
    actual fun cyan(msg: String) = ""
}

actual class DefaultLogger actual constructor(private val clazz: Class<*>, private val useLevel: Level) : Logger {
    private infix fun String.deco(context: String) = "\u001b[${this}m$context\u001b[0m"

    override fun log(level: Level, service: String, msg: String) {
        if (level.num < useLevel.num) return
        val time = "38;5;8" deco "[${SimpleDateFormat("MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE)
            .format(Date(System.currentTimeMillis()))}]"
        val className = "38;5;2" deco "[${clazz.simpleName}]"
        val levelAndMsg = when (level) {
            ERROR -> "38;5;9"
            WARN -> "38;5;11"
            INFO -> "0"
            DEBUG -> "38;5;8"
        } deco "[${level.name}]: $msg"
        println("[$service]$time$className$levelAndMsg")
    }
}