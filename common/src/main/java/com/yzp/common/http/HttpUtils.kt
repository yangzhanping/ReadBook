package com.yzp.common.http

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Call

object HttpUtils {

    /**
     * 获取HTML信息
     */
    fun getHtml(url: String): Observable<ResponseBody> {
        return RetrofitManager.service.getHtml(url)
    }
}