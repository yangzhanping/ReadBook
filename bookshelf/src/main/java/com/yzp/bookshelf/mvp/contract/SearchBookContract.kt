package com.yzp.bookshelf.mvp.contract

import android.content.Context
import com.yzp.common.db.bean.Book
import com.yzp.common.mvp.IBaseView

interface SearchBookContract {
    interface View : IBaseView {
        /**
         * 设置第一次请求的数据
         */
        fun setSearchList(bookList: MutableList<Book>)

        /**
         * 显示错误信息
         */
        fun showError(msg: String, errorCode: Int)
    }

    interface Presenter {
        /**
         * 获取数据
         */
        fun requestData(context: Context, searchKey: String)
    }
}