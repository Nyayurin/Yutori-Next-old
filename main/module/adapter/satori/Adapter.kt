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

import github.nyayurn.yutori_next.*
import github.nyayurn.yutori_next.module.core.Core

val Adapter.Companion.Satori: SatoriAdapter
    get() = SatoriAdapter()

@BuilderMarker
class SatoriAdapter : Adapter {
    var host: String = "127.0.0.1"
    var port: Int = 5500
    var path: String = ""
    var token: String? = null
    var version: String = "v1"
    var webhook: WebHook? = null
    private var service: EventService? = null

    fun useWebHook(block: WebHook.() -> Unit) {
        webhook = WebHook().apply(block)
    }

    override fun install(satori: Satori) {
        if (Module.Core !in satori.modules) {
            Module.Core.install(satori)
        }
    }

    override fun uninstall(satori: Satori) {
        if (Module.Core !in satori.modules) {
            Module.Core.uninstall(satori)
        }
    }

    override fun start(satori: Satori) {
        val properties = SatoriProperties(host, port, path, token, version)
        service = webhook?.run { WebhookEventService(listen, port, path, properties, satori) }
                  ?: WebSocketEventService(properties, satori)
        service!!.connect()
    }

    override fun stop(satori: Satori) {
        service?.close()
        service = null
    }

    @BuilderMarker
    class WebHook {
        var listen: String = "0.0.0.0"
        var port: Int = 8080
        var path: String = "/"
    }
}