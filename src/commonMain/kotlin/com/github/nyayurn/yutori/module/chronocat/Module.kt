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

package com.github.nyayurn.yutori.module.chronocat

import com.github.nyayurn.yutori.*

val Module.Companion.Chronocat: ChronocatModule
    get() = ChronocatModule

object ChronocatModule : Module {
    override fun install(satori: Satori) {
        satori.actions_containers["chronocat"] = { platform, self_id, service ->
            ChronocatActions(platform, self_id, service)
        }
        satori.message_builders["chronocat"] = { ChronocatMessageBuilder(it) }
        Element.install(satori)
    }

    override fun uninstall(satori: Satori) {
        satori.actions_containers.remove("chronocat")
        satori.message_builders.remove("chronocat")
        Element.uninstall(satori)
    }

    private object Element : Module {
        override fun install(satori: Satori) {
            satori.elements["chronocat:pcpoke"] = Chronocat.PcPoke
            satori.elements["chronocat:face"] = Chronocat.Face
            satori.elements["chronocat:marketface"] = Chronocat.MarketFace
            satori.elements["chronocat:facebubble"] = Chronocat.FaceBubble
        }

        override fun uninstall(satori: Satori) {
            satori.elements.remove("chronocat:pcpoke")
            satori.elements.remove("chronocat:face")
            satori.elements.remove("chronocat:marketface")
            satori.elements.remove("chronocat:facebubble")
        }
    }
}