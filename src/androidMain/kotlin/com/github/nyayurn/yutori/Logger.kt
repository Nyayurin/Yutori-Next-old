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

import android.util.Log
import com.github.nyayurn.yutori.Level.*

actual object LoggerColor {
    actual fun blue(msg: String) = msg
    actual fun gray(msg: String) = msg
    actual fun cyan(msg: String) = msg
}

actual class DefaultLogger actual constructor(private val clazz: Class<*>, private val useLevel: Level) : Logger {
    override fun log(level: Level, service: String, msg: String) {
        if (level.num < useLevel.num) return
        when (level) {
            ERROR -> Log.e(clazz.simpleName, "$service | $msg")
            WARN -> Log.w(clazz.simpleName, "$service | $msg")
            INFO -> Log.i(clazz.simpleName, "$service | $msg")
            DEBUG -> Log.d(clazz.simpleName, "$service | $msg")
        }
    }
}