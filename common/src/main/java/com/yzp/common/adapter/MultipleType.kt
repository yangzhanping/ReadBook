package com.yzp.common.adapter

interface MultipleType<in T> {
    fun getLayoutId(item: T, position: Int): Int
}
