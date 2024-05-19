import {defineConfig} from 'vitepress'

export default defineConfig({
    title: "Yutori Next",
    description: "Yutori-Next's Documents",
    base: "/Yutori-Next/",
    themeConfig: {
        sidebar: [
            {
                text: "介绍",
                link: "/introduction"
            }, {
                text: "消息元素",
                link: "/elements"
            }, {
                text: "资源",
                items: [
                    {
                        text: "频道 (Channel)",
                        link: "/resources/channel"
                    }, {
                        text: "群组 (Guild)",
                        link: "/resources/guild"
                    }, {
                        text: "群组成员 (GuildMember)",
                        link: "/resources/member"
                    }, {
                        text: "群组角色 (GuildRole)",
                        link: "/resources/role"
                    }, {
                        text: "交互 (Interaction)",
                        link: "/resources/interaction"
                    }, {
                        text: "登录信息 (Login)",
                        link: "/resources/login"
                    }, {
                        text: "消息 (Message)",
                        link: "/resources/message"
                    }, {
                        text: "表态 (Reaction)",
                        link: "/resources/reaction"
                    }, {
                        text: "用户 (User)",
                        link: "/resources/user"
                    }
                ]
            }, {
                text: "进阶",
                items: [
                    {
                       text: "进阶",
                        link: "/advanced/advanced",
                    }, {
                        text: "管理接口",
                        link: "/advanced/admin"
                    }
                ]
            }, {
                text: "扩展",
                items: [
                    {
                        text: "模块(Coming soon)",
                        items: [
                            {
                                text: "动作容器 & 动作服务(Coming soon)"
                            }, {
                                text: "消息元素 & Builder(Coming soon)"
                            }, {
                                text: "监听器容器(Coming soon)"
                            }, {
                                text: "事件(Coming soon)"
                            }
                        ]
                    }, {
                        text: "适配器(Coming soon)"
                    }
                ]
            }
        ]
    }
})