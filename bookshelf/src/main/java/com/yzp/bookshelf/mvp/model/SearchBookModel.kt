package com.yzp.bookshelf.mvp.model

import com.orhanobut.logger.Logger
import com.yzp.common.http.HttpUtils
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import java.net.URLEncoder

class SearchBookModel {

    fun requestData(url: String, keyWork: String): Observable<ResponseBody> {
        var key = URLEncoder.encode(keyWork, "GB2312")
        var searUrl = "$url$key"
        return HttpUtils.getHtml(searUrl)
    }
}