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

@file:Suppress("MemberVisibilityCanBePrivate", "unused", "HttpUrlsUsage")

package github.nyayurn.yutori_next.module.adapter.satori

import github.nyayurn.yutori_next.ActionService
import github.nyayurn.yutori_next.GlobalLoggerFactory
import github.nyayurn.yutori_next.SatoriProperties
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

class SatoriActionService(val properties: SatoriProperties, val name: String) : ActionService {
    private val logger = GlobalLoggerFactory.getLogger(this::class.java)

    @Suppress("UastIncorrectHttpHeaderInspection")
    override fun send(
        resource: String,
        method: String,
        platform: String?,
        self_id: String?,
        content: String?
    ): String = runBlocking {
        HttpClient(CIO).use { client ->
            val response = client.post {
                url {
                    host = properties.host
                    port = properties.port
                    appendPathSegments(properties.path, properties.version, "$resource.$method")
                }
                contentType(ContentType.Application.Json)
                headers {
                    properties.token?.let { append(HttpHeaders.Authorization, "Bearer ${properties.token}") }
                    platform?.let { append("X-Platform", platform) }
                    self_id?.let { append("X-Self-ID", self_id) }
                }
                content?.let { setBody(content) }
                logger.debug(
                    name, """
                    Satori Action: url: ${this.url},
                        headers: ${this.headers.build()},
                        body: ${this.body}
                    """.trimIndent()
                )
            }
            logger.debug(name, "Satori Action Response: $response")
            response.body()
        }
    }
}