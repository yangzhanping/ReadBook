package com.yzp.common.constant

enum class BookSource(var text: String) {

    tianlai("天籁小说"),
    biquge("笔趣阁");

    open operator fun get(var0: Int): BookSource? {
        return values()[var0]
    }

    open fun fromString(string: String?): BookSource? {
        return valueOf(string!!)
    }
}