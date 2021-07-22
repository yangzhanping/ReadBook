package com.yzp.bookshelf.mvp.model

import com.yzp.common.http.HttpUtils
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody

class ReadBookModel {

    fun requestData(url: String): Observable<ResponseBody> {
        return HttpUtils.getHtml(url)
    }
}