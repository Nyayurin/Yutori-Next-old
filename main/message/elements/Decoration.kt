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

package com.github.nyayurn.yutori.next.message.elements

/**
 * 修饰元素
 */
abstract class DecorationElement(name: String, vararg pairs: Pair<String, Any?>) : NodeMessageElement(name, *pairs)

/**
 * 粗体
 */
class Bold : DecorationElement("b")

/**
 * 粗体
 */
class Strong : DecorationElement("strong")

/**
 * 斜体
 */
class Idiomatic : DecorationElement("i")

/**
 * 斜体
 */
class Em : DecorationElement("em")

/**
 * 下划线
 */
class Underline : DecorationElement("u")

/**
 * 下划线
 */
class Ins : DecorationElement("ins")

/**
 * 删除线
 */
class Strikethrough : DecorationElement("s")

/**
 * 删除线
 */
class Delete : DecorationElement("del")

/**
 * 剧透
 */
class Spl : DecorationElement("spl")

/**
 * 代码
 */
class Code : DecorationElement("code")

/**
 * 上标
 */
class Sup : DecorationElement("sup")

/**
 * 下标
 */
class Sub : DecorationElement("sub")