package com.yzp.bookshelf.enums

import java.io.Serializable


enum class ReadStyle : Serializable {

    protectedEye,//护眼
    common,//普通
    blueDeep,//深蓝
    leather,//羊皮纸
    breen;//

    open operator fun get(var0: Int): ReadStyle? {
        return values()[var0]
    }

    open fun fromString(string: String?): ReadStyle? {
        return valueOf(string!!)
    }
}