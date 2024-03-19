package com.github.nyayurn.qbot

import com.github.nyayurn.yutori.next.*
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

fun main() {
    GlobalLoggerFactory.factory = DefaultLoggerFactory(Level.DEBUG)
    val server = System.getenv("Satori-Server") ?: "Chronocat"
    val client = WebSocketEventService.connect(server) {
        listeners {
            message.created += CommandListener
            message.created += OpenGraphListener
            message.created += AtListener
            message.created += YzListener
//            message.created += TestListener
        }
        properties {
            when (server) {
                "Chronocat" -> {
                    token = "Chronocat"
                }
                "Koishi" -> {
                    host = "[::1]"
                    port = 5140
                    path = "/satori"
                    token = "Koishi"
                }
            }

            System.getenv("Satori-Host")?.let {
                host = it
            }
            System.getenv("Satori-Port")?.toIntOrNull()?.let {
                port = it
            }
            System.getenv("Satori-Path")?.let {
                path = it
            }
            System.getenv("Satori-Token")?.let {
                token = it
            }
        }
    }
    while (readln() != "exit") continue
    client.close()
}

val trustAllCerts: TrustManager = object : X509TrustManager {
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        // This method is empty
    }

    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        // This method is empty
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
}

class MyLogger(clazz: Class<*>, private val useLevel: Level) : Logger {
    private val defaultLogger = DefaultLogger(clazz, useLevel)
    override fun log(level: Level, service: String, msg: String) {
        defaultLogger.log(level, service, msg)
        if (level.num < useLevel.num) return
        println()
    }
}

class MyLoggerFactory(private val level: Level = Level.INFO) : LoggerFactory {
    override fun getLogger(clazz: Class<*>) = MyLogger(clazz, level)
}