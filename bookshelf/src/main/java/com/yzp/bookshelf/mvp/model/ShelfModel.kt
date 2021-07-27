package com.yzp.bookshelf.mvp.model

import android.content.Context
import com.yzp.common.db.BookDaoOpe
import com.yzp.common.db.bean.Book
import com.yzp.common.http.HttpUtils
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody

class ShelfModel {

    fun requestData(context: Context): MutableList<Book>? {
        return BookDaoOpe.getInstance().queryAll(context);
    }

    fun refreshData(url: String): Observable<ResponseBody> {
        return HttpUtils.getHtml(url)
    }
}