package com.yzp.bookshelf.mvp.contract

import com.yzp.common.db.bean.Book
import com.yzp.common.mvp.IBaseView

interface BookInfoContract {

    interface View : IBaseView {
        /**
         *设置数据
         */
        fun setData(book: Book)

        /**
         * 显示错误信息
         */
        fun showError(msg: String, errorCode: Int)
    }

    interface Presenter {
        fun requestData(book: Book)
    }
}