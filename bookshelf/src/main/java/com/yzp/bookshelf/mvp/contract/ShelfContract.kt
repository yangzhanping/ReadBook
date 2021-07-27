package com.yzp.bookshelf.mvp.contract

import android.content.Context
import com.yzp.bookshelf.mvp.model.bean.ShelfBean
import com.yzp.common.db.bean.Book
import com.yzp.common.mvp.IBaseView

interface ShelfContract {

    interface View : IBaseView {
        /**
         * 设置第一次请求的数据
         */
        fun setData(bookList: MutableList<Book>, isRefresh: Boolean)

        /**
         * 显示错误信息
         */
        fun showError(msg: String, errorCode: Int)
    }

    interface Presenter {
        /**
         * 获取首页精选数据
         */
        fun requestData(context: Context, isRefresh: Boolean)

        /**
         * 更新最新章节
         */
        fun refreshData(context: Context)
    }
}