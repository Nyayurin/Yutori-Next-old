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

package github.nyayurn.yutori_next

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

abstract class ExtendedActionsContainer

/**
 * 封装所有 Action, 应通过本类对 Satori Server 发送 Satori Action
 */
class ActionsContainer private constructor(platform: String, self_id: String, service: ActionService, satori: Satori) {
    val containers = mutableMapOf<String, ExtendedActionsContainer>().apply {
        for ((key, value) in satori.actions_containers) this[key] = value(platform, self_id, service)
    }

    constructor(event: Event, service: ActionService, satori: Satori) : this(
        event.platform, event.self_id, service, satori
    )
}

/**
 * Satori Action 实现
 * @property platform 平台
 * @property self_id 自身的 ID
 * @property resource 资源路径
 * @property name 隶属哪个 Event Service
 * @property mapper JSON 反序列化
 * @property logger 日志接口
 */
class GeneralAction(
    val platform: String?,
    val self_id: String?,
    val resource: String,
    val name: String,
    val service: ActionService
) {
    val mapper: ObjectMapper = jacksonObjectMapper()
    val logger = GlobalLoggerFactory.getLogger(this::class.java)

    fun send(method: String, content: String? = null) = service.send(resource, method, platform, self_id, content)
    inline fun send(method: String, block: JsonObjectDSLBuilder.() -> Unit) = send(method, jsonObj(block))
    inline fun <reified T> sendWithParse(method: String, block: JsonObjectDSLBuilder.() -> Unit = {}): T {
        return send(method, jsonObj(block)).let { response ->
            try {
                mapper.readValue<T>(response)
            } catch (e: Exception) {
                logger.warn(name, response)
                throw ResponseParsingException(response)
            }
        }
    }
}