package com.yzp.bookshelf.enums

import java.io.Serializable

enum class Language : Serializable {
    simplified,//简体中文
    traditional;//繁体中文

    open operator fun get(var0: Int): Language? {
        return values()[var0]
    }

    open fun fromString(string: String?): Language? {
        return valueOf(string!!)
    }
}