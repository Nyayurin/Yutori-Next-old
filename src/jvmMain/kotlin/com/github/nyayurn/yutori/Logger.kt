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

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextColors.gray
import com.github.ajalt.mordant.rendering.TextColors.green
import com.github.ajalt.mordant.terminal.Terminal
import com.github.nyayurn.yutori.Level.*
import kotlinx.datetime.Clock
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char

actual object LoggerColor {
    actual fun blue(msg: String) = TextColors.blue(msg)
    actual fun gray(msg: String) = TextColors.gray(msg)
    actual fun cyan(msg: String) = TextColors.cyan(msg)
}

actual class DefaultLogger actual constructor(private val clazz: Class<*>, private val useLevel: Level) : Logger {
    private val terminal = Terminal()
    private val format = DateTimeComponents.Format {
        monthNumber()
        char('-')
        dayOfMonth()
        char(' ')
        year()
        char(':')
        minute()
        char(':')
        second()
    }
    override fun log(level: Level, service: String, msg: String) {
        if (level.num < useLevel.num) return
        val time = gray(Clock.System.now().format(format))
        val leveled_msg = when (level) {
            ERROR -> terminal.theme.danger("${level.name} | $msg")
            WARN -> terminal.theme.warning("${level.name} | $msg")
            INFO -> "${level.name} | $msg"
            DEBUG -> terminal.theme.muted("${level.name} | $msg")
        }
        println("$service | $time | ${green(clazz.simpleName)} | $leveled_msg")
    }
}