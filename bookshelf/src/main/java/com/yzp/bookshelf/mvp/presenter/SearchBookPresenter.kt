package com.yzp.bookshelf.mvp.presenter

import android.content.Context
import com.orhanobut.logger.Logger
import com.yzp.bookshelf.mvp.contract.SearchBookContract
import com.yzp.bookshelf.mvp.model.SearchBookModel
import com.yzp.common.constant.UrlConstant
import com.yzp.common.db.SearchHistoryDaoOpe
import com.yzp.common.db.bean.Book
import com.yzp.common.db.bean.SearchHistory
import com.yzp.common.mvp.BasePresenter
import com.yzp.common.utils.DateHelper
import com.yzp.common.xml.BiQuGeReadUtil
import com.yzp.common.xml.TianLaiReadUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class SearchBookPresenter : BasePresenter<SearchBookContract.View>(),
    SearchBookContract.Presenter {

    private val searchBookModel: SearchBookModel by lazy {
        SearchBookModel()
    }

    override fun requestData(context: Context, searchKey: String) {
        var searchHistory = SearchHistoryDaoOpe.getInstance().queryKey(context, searchKey)
        if (searchHistory == null) {
            searchHistory = SearchHistory()
            searchHistory.id = UUID.randomUUID().toString()
            searchHistory.content = searchKey
            searchHistory.createDate = DateHelper.longToTime(Date().time)
            SearchHistoryDaoOpe.getInstance().insertData(context, searchHistory)
        } else {
            searchHistory.createDate = DateHelper.longToTime(Date().time)
            SearchHistoryDaoOpe.getInstance().saveData(context, searchHistory)
        }
        searchBook(searchKey)
    }

    private fun searchBook(searchKey: String) {
        for (url in UrlConstant.getSearchSource()) {
            searchBookModel.requestData(url, searchKey).map {
                val reader = BufferedReader(InputStreamReader(it.byteStream(), "GBK"))
                val response = StringBuilder()
                var line: String? = reader.readLine()
                while (line != null) {
                    response.append(line)
                    line = reader.readLine()
                }
                if (url.contains(UrlConstant.nameSpace_tianlai)) {
                    TianLaiReadUtil.getBooks(response.toString())
                } else {
                    BiQuGeReadUtil.getBooksFromSearchHtml(response.toString())
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MutableList<Book>?> {
                    override fun onSubscribe(d: Disposable) {
                        mRootView?.apply {
                            showLoading()
                        }
                    }

                    override fun onComplete() {
                        mRootView?.apply {
                            dismissLoading()
                        }
                    }

                    override fun onNext(it: MutableList<Book>?) {
                        mRootView?.apply {
                            setSearchList(it!!)
                        }
                    }

                    override fun onError(e: Throwable) {
                        Logger.e("error-> " + e.message)
                    }
                })
        }
    }
}