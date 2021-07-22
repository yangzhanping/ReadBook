package com.yzp.bookshelf.mvp.model

import android.content.Context
import com.yzp.common.db.BookDaoOpe
import com.yzp.common.db.bean.Book

class ShelfModel {

    fun requestData(context: Context): MutableList<Book>? {
        return BookDaoOpe.getInstance().queryAll(context);
    }

    fun loadMoreData() {
    }
}