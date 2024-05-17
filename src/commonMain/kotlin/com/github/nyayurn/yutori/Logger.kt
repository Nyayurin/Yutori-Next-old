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

enum class Level(val num: Int) { ERROR(3), WARN(2), INFO(1), DEBUG(0) }

expect object LoggerColor {
    fun blue(msg: String): String
    fun gray(msg: String): String
    fun cyan(msg: String): String
}

interface Logger {
    fun log(level: Level, service: String, msg: String)
    fun error(name: String, msg: String) = log(ERROR, name, msg)
    fun warn(name: String, msg: String) = log(WARN, name, msg)
    fun info(name: String, msg: String) = log(INFO, name, msg)
    fun debug(name: String, msg: String) = log(DEBUG, name, msg)
}

fun interface LoggerFactory {
    fun getLogger(clazz: Class<*>): Logger
}

expect class DefaultLogger(clazz: Class<*>, useLevel: Level) : Logger

class DefaultLoggerFactory(private val level: Level = INFO) : LoggerFactory {
    override fun getLogger(clazz: Class<*>): Logger = DefaultLogger(clazz, level)
}

object GlobalLoggerFactory : LoggerFactory {
    @Suppress("MemberVisibilityCanBePrivate")
    var factory: LoggerFactory = DefaultLoggerFactory()
        set(value) {
            field = if (value == GlobalLoggerFactory) DefaultLoggerFactory() else value
        }
    override fun getLogger(clazz: Class<*>) = factory.getLogger(clazz)
}