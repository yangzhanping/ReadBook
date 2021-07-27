package com.yzp.bookshelf.mvp.presenter

import android.content.Context
import android.util.Log
import com.orhanobut.logger.Logger
import com.yzp.bookshelf.mvp.contract.ShelfContract
import com.yzp.bookshelf.mvp.model.ShelfModel
import com.yzp.common.db.BookDaoOpe
import com.yzp.common.db.bean.Book
import com.yzp.common.db.bean.Chapter
import com.yzp.common.mvp.BasePresenter
import com.yzp.common.utils.StringHelper
import com.yzp.common.utils.ThreadManager
import com.yzp.common.xml.TianLaiReadUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader

class ShelfPresenter : BasePresenter<ShelfContract.View>(),
    ShelfContract.Presenter {

    private val shelfModel: ShelfModel by lazy {
        ShelfModel()
    }

    override fun requestData(context: Context, isRefresh: Boolean) {
        Single.create(SingleOnSubscribe<MutableList<Book>> {
            var bookList: MutableList<Book>? = shelfModel.requestData(context)
            it.onSuccess(bookList)
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mRootView?.apply {
                    setData(it, isRefresh)
                }
            }, {
                Logger.e("error-> " + it.message)
            })
    }

    override fun refreshData(context: Context) {
        ThreadManager.createLongPool()!!.execute {
            val bookList = shelfModel.requestData(context)
            var index = 0
            for (mBook in bookList!!) {
                shelfModel.refreshData(mBook.chapterUrl).map {
                    val reader = BufferedReader(InputStreamReader(it.byteStream(), "GBK"))
                    val response = StringBuilder()
                    var line: String? = reader.readLine()
                    while (line != null) {
                        response.append(line)
                        line = reader.readLine()
                    }
                    TianLaiReadUtil.getNewChapter(response.toString(), mBook)
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        BookDaoOpe.getInstance().saveData(context, it!!)
                        index++
                        if (index == bookList.size) {
                            requestData(context, false)
                        }
                    },
                        {
                            Logger.e("error-> " + it.message)
                        })
            }
        }
    }
}