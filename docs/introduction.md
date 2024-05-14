# 介绍

- Yutori-Next 是一个 [Satori 协议](https://satori.chat) 的 [Kotlin](https://kotlinlang.org) 多平台机器人开发框架
- Yutori-Next 的名称来源于作者 Yurn 与 Satori 名字的结合

# 快速开始

## 说在前面

- 建议拥有一定 Kotlin & Gradle 基础
- 不建议使用 Maven 作为依赖管理
- 不建议搭配 SpringFramework 使用

## 依赖引入

- 获取版本请查看 [Jitpack](https://jitpack.io/#Nyayurn/Yutori-Next)

```kotlin
maven { url = URI("https://jitpack.io") }
```

```kotlin
implementation("com.github.Nyayurn:Yutori-Next:$version")
```

## 做一个复读机

```kotlin
fun main() {
    val satori = satori {
        install(Adapter.Satori) {
            host = "127.0.0.1"
            port = 8080
            token = "token"
        }
        listening {
            message.created {
                actions.message.create {
                    channel_id = event.channel.id
                    content = event.message.content
                }
            }
        }
    }
    satori.start()
    while (readln() != "exit") continue
    satori.stop()
}
```

- 如果日志打印"无法建立事件推送服务: READY 响应超时", 可能是 Token 设置错误(Chronocat)