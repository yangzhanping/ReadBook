package com.yzp.bookshelf.enums

import java.io.Serializable

enum class Font(var path: String) : Serializable {

    默认字体(""),
    方正楷体("fonts/fangzhengkaiti.ttf"),
    方正行楷("fonts/fangzhengxingkai.ttf"),
    经典宋体("fonts/songti.ttf"),
    迷你隶书("fonts/mini_lishu.ttf"),
    方正黄草("fonts/fangzhenghuangcao.ttf"),
    书体安景臣钢笔行书("fonts/shuti_anjingchen_gangbixingshu.ttf");

    open operator fun get(var0: Int): Font? {
        return values()[var0]
    }

    open fun fromString(string: String?): Font? {
        return valueOf(string!!)
    }
}