package com.yzp.common.bean

class BookType {
    //分类名称
    private var typeName: String? = null

    //分类链接
    private var url: String? = null

    fun getTypeName(): String? {
        return typeName
    }

    fun setTypeName(typeName: String?) {
        this.typeName = typeName
    }

    fun getUrl(): String? {
        return url
    }

    fun setUrl(url: String?) {
        this.url = url
    }
}