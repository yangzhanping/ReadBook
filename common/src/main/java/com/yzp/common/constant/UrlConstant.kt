package com.yzp.common.constant

object UrlConstant {

    const val BASE_URL = "https://www.tlai.cc/"

    // 命名空间
    var nameSpace_tianlai = "https://www.tlai.cc/"
    var nameSpace_biquge = "https://www.xxbqg.com/"

    fun getSearchSource(): MutableList<String> {
        val searchSources = mutableListOf<String>()
        searchSources.add("https://www.tlai.cc/search.php?searchkey=")
        searchSources.add("https://www.xxbqg.com/modules/article/search.php?searchkey=")
        return searchSources
    }
}