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

package github.nyayurn.yutori_next.module.chronocat

import github.nyayurn.yutori_next.*

val Module.Companion.Chronocat: ChronocatModule
    get() = ChronocatModule

object ChronocatModule : Module {
    override fun install(satori: Satori) {
        Action.install(satori)
        Element.install(satori)
        MessageBuilder.install(satori)
    }

    override fun uninstall(satori: Satori) {
        Action.uninstall(satori)
        Element.uninstall(satori)
        MessageBuilder.uninstall(satori)
    }

    private object Action : Module {
        override fun install(satori: Satori) {
            satori.actions["chronocat"] = { platform, id, service -> ChronocatAction(platform, id, satori.name, service) }
        }

        override fun uninstall(satori: Satori) {
            satori.actions.remove("chronocat")
        }
    }

    private object Element : Module {
        override fun install(satori: Satori) {
            satori.elements["chronocat:poke"] = { Chronocat.Poke() }
        }

        override fun uninstall(satori: Satori) {
            satori.elements.remove("chronocat:poke")
        }
    }

    private object MessageBuilder : Module {
        override fun install(satori: Satori) {
            satori.message_builders["chronocat"] = { ChronocatDslBuilder(it) }
        }

        override fun uninstall(satori: Satori) {
            satori.message_builders.remove("chronocat")
        }
    }
}