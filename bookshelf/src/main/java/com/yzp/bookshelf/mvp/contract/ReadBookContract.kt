package com.yzp.bookshelf.mvp.contract

import android.content.Context
import android.widget.TextView
import com.yzp.common.db.bean.Book
import com.yzp.common.db.bean.Chapter
import com.yzp.common.mvp.IBaseView
import java.text.FieldPosition

interface ReadBookContract {

    interface View : IBaseView {
        /**
         *设置目录数据
         */
        fun setDrawerData(chapters: MutableList<Chapter>)

        /**
         *设置章节内容
         */
        fun setContentData(chapters: MutableList<Chapter>)

        /**
         * 更新缓存进度
         */
        fun setCacheProgress(progress: Int, tvDownloadProgress: TextView?)

        /**
         * 显示错误信息
         */
        fun showError(msg: String, errorCode: Int)
    }

    interface Presenter {
        fun requestChaptersData(context: Context, mBook: Book)
        fun downLoadAllChapters(
            context: Context,
            book: Book,
            chapters: MutableList<Chapter>,
            tvDownloadProgress: TextView
        )
    }
}