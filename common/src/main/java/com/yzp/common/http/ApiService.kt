package com.yzp.common.http

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    /**
     * 获取HTML
     */
    @GET
    fun getHtml(@Url url: String): Observable<ResponseBody>
}