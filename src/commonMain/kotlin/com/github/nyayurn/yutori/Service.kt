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

package com.github.nyayurn.yutori

import io.ktor.util.reflect.*

interface ActionService {
    fun send(
        resource: String,
        method: String,
        platform: String?,
        self_id: String?,
        content: Map<String, Any?>,
        typeInfo: TypeInfo
    ): Any
    fun upload(
        resource: String,
        method: String,
        platform: String,
        self_id: String,
        content: List<FormData>
    ): Map<String, String>
}

interface EventService {
    suspend fun connect()
    suspend fun reconnect()
    fun disconnect()
}