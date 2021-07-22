package com.yzp.bookshelf.mvp.presenter

import com.orhanobut.logger.Logger
import com.yzp.bookshelf.mvp.contract.BookInfoContract
import com.yzp.bookshelf.mvp.model.BookInfoModel
import com.yzp.common.db.bean.Book
import com.yzp.common.mvp.BasePresenter
import com.yzp.common.xml.BiQuGeReadUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader

class BookInfoPresenter : BasePresenter<BookInfoContract.View>(), BookInfoContract.Presenter {

    var bookInfoModel = BookInfoModel()

    override fun requestData(book: Book) {
        bookInfoModel.requestData(book!!.chapterUrl).map {
            val reader = BufferedReader(InputStreamReader(it.byteStream(), "GBK"))
            val response = StringBuilder()
            var line: String? = reader.readLine()
            while (line != null) {
                response.append(line)
                line = reader.readLine()
            }
            BiQuGeReadUtil.getBookInfo(response.toString(), book)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Book?> {
                override fun onSubscribe(d: Disposable?) {
                    mRootView?.apply {
                        showLoading()
                    }
                }

                override fun onComplete() {
                    mRootView?.apply {
                        dismissLoading()
                    }
                }

                override fun onNext(it: Book?) {
                    mRootView?.apply {
                        setData(it!!)
                    }
                }

                override fun onError(e: Throwable?) {
                    Logger.e("error-> " + e!!.message)
                }
            })
    }
}