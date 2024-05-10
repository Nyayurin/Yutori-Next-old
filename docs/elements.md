# 标准元素

## 基础元素

### 纯文本 (Text) {#text}

| 属性   | 类型     | 范围 | 描述 |
|------|--------|----|----|
| text | String | 收发 | 文本 |

### 提及用户 (At) {#at}

| 属性   | 类型      | 范围 | 描述       |
|------|---------|----|----------|
| id   | String? | 收发 | 目标用户的 ID |
| name | String? | 收发 | 目标用户的名称  |
| role | String? | 收发 | 目标角色     |
| type | String? | 收发 | 特殊操作     |

### 提及频道 (Sharp) {#sharp}

| 属性   | 类型      | 范围 | 描述       |
|------|---------|----|----------|
| id   | String  | 收发 | 目标频道的 ID |
| name | String? | 收发 | 目标频道的名称  |

### 链接 (Href) {#a}

| 属性   | 类型     | 范围 | 描述      |
|------|--------|----|---------|
| href | String | 收发 | 链接的 URL |

## 资源元素

| 属性      | 类型       | 范围 | 描述             |
|---------|----------|----|----------------|
| src     | String   | 收发 | 资源的 URL        |
| title   | String?  | 收发 | 资源文件名称         |
| cache   | Boolean? | 发  | 是否使用已缓存的文件     |
| timeout | String?  | 发  | 下载文件的最长时间 (毫秒) |

### 图片 (Image) {#img}

| 属性     | 类型      | 范围 | 描述         |
|--------|---------|----|------------|
| width  | Number? | 收  | 图片的宽度 (像素) |
| height | Number? | 收  | 图片的高度 (像素) |

### 音频 (Audio) {#audio}

| 属性       | 类型      | 范围 | 描述       |
|----------|---------|----|----------|
| duration | Number? | 收  | 音频长度 (秒) |
| poster   | String? | 收发 | 缩略图 URL  |

### 视频 (Video) {#video}

| 属性       | 类型      | 范围 | 描述         |
|----------|---------|----|------------|
| width    | Number? | 收  | 视频的宽度 (像素) |
| height   | Number? | 收  | 视频的高度 (像素) |
| duration | Number? | 收  | 视频长度 (秒)   |
| poster   | String? | 收发 | 缩略图 URL    |

### 文件 (File) {#file}

| 属性     | 类型      | 范围 | 描述      |
|--------|---------|----|---------|
| poster | String? | 收发 | 缩略图 URL |

## 修饰元素

### 粗体 (Bold, Strong) {#b}

### 斜体 (Idiomatic, Em) {#i}

### 下划线 (Underline, Ins) {#u}

### 删除线 (Strikethrough, Delete) {#s}

### 剧透 (Spl) {#spl}

### 代码 (Code) {#code}

### 上标 (Sup) {#sup}

### 下标 (Sub) {#sub}

## 排版元素

### 换行 (Br) {#br}

### 段落 (Paragraph) {#p}

### 消息 (Message) {#message}

| 属性      | 类型       | 范围 | 描述      |
|---------|----------|----|---------|
| id      | String?  | 发  | 消息的 ID  |
| forward | Boolean? | 发  | 是否为转发消息 |

## 元信息元素

### 引用 (Quote) {#quote}

### 作者 (Author) {#author}

| 属性     | 类型      | 范围 | 描述     |
|--------|---------|----|--------|
| id     | String? | 发  | 用户 ID  |
| name   | String? | 发  | 昵称     |
| avatar | String? | 发  | 头像 URL |

## 交互元素

### 按钮 (Button) {#button}

| 属性    | 类型      | 范围 | 描述     |
|-------|---------|----|--------|
| id    | String? | 发  | 按钮的 ID |
| type  | String? | 发  | 按钮的类型  |
| href  | String? | 发  | 按钮的链接  |
| text  | String? | 发  | 待输入文本  |
| theme | String? | 发  | 按钮的样式  |