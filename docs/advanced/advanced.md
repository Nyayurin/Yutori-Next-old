# 进阶

## 为何要有多余的 core 前缀

- 目前仍在 0.2 开发阶段, 分离出 Core 模块仅是为了框架开发方便, 待后续模块相关机制的文档及 Chronocat 适配器完善后自会将 Core 模块移回本体

## 注册监听器

- 可以通过 `container.any` 方法注册监听任意事件的监听器

## 多连接

```kotlin
satori {
    install(Adapter.Satori) {
        host = "websocket-host"
        port = 8080
        token = "websocket"
    }
    install(Adapter.Satori) {
        host = "webhook-host"
        port = 8080
        token = "webhook"
        useWebHook { }
    }
}.start()
```

## 多实例

```kotlin
satori {
    // ...
}.start()
satori {
    // ...
}.sart()
```

## 主动发送消息

```kotlin
val actions = Actions("platform", "selfId", "Satori", SatoriActionService(properties, "Satori"))
actions.core.message.create {
    channel_id = "channel_id"
    content = "Hello, world!"
}
```

## 消息链

```kotlin
val chain = MessageUtil.parse(context.satori, context.event.message.content)
chain.forEach(::println)
```

## WebHook

!!! warning
    本条目内容未经过测试, 可能无法正常使用

```kotlin
satori {
    install(Adapter.Satori) {
        // ...
        useWebHook {
            listen = "0.0.0.0" // 监听 IPV6: "::"
            port = 8080
            path = "/"
        }
    }
}
```

## 内部/扩展接口

- 有对应的模块包请直接使用模块提供的接口
- 没有请自行使用 HTTP 库发送 HTTP 请求

## 扩展消息元素

- 有对应的模块包请直接使用模块提供的元素类型
- 没有可使用 NodeMessageElement:

```kotlin
message {
    node("chronocat:markdown") { 
        text { "这是一条 Chronocat 的 Markdown 消息" }
    }
}
```

## 消息元素扩展属性

除 `Text` 消息元素外, 其余所有元素应该都继承自 NodeMessageElement, 可通过操作该类的 properties 属性来添加/修改扩展属性

### DSL

```kotlin
message {
    core.img { this["prop"] = "prop" } 
}
```

## 事件扩展属性

- Event 类 properties 属性中包含