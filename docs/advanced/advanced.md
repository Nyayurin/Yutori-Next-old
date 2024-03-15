# 进阶

## 注册监听器

- 通过 ListenersContainer 类的方法注册对应事件的监听器
```kotlin
val container = ListenersContainer.of {
    message.created += TestListener
    message.created { actions, event ->
        // ...
    }
}
```

- 可以通过 `container.any` 方法注册监听任意事件的监听器

## 多帐号

Yutori 支持多帐号, 只需多实例几个 EventService 并分别连接即可
```kotlin
val chronoClient = WebSocketEventService.connect("Chronocat") {
    this.container = container
    this.properties = chronoProperties
}
val koishiClient = WebSocketEventService.connect("Koishi") {
    this.container = container
    this.properties = koishiProperties
}
```

## 主动发送消息

```kotlin
fun main() {
    val actions = Actions("platform", "selfId", properties, "Satori")
    actions.message.create {
        channel_id = "channel_id"
        content = MessageSegment.of("Hello, world!")
    }
}
```

## 消息链

```kotlin
fun listen(actions: Actions, event: MessageEvent) {
    event.message.content.forEach(::println)
}
```

## WebHook

!!! warning
    本条目内容未经过测试, 可能无法正常使用

- 和 WebSocket 使用大致相同

## 内部/扩展接口

- 请自行使用 HTTP 库发送 HTTP 请求

## 消息元素扩展属性

除 `Text` 与 `Custom` 消息元素外, 所有标准元素均继承自 NodeMessageElement, 可通过操作该类的 properties 属性来添加/修改扩展属性

### DSL

```kotlin
fun main() {
    message {
        img { this["prop"] = "prop" }
        text { "扩展属性" }
    }
}
```

## 扩展消息元素

请自行创建 NodeMessageElement 子类

## 事件扩展属性

- 请自行反序列化 Event.raw(原始的 event JSON 字符串) 属性获取