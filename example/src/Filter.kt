package github.nyayurn.qbot

import github.nyayurn.yutori_next.MessageEvent

val qqHelperFilter = { event: MessageEvent -> event.platform == "chronocat" && event.user.id == "2854196310" }