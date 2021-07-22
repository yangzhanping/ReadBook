package com.yzp.bookshelf.mvp.presenter

import android.content.Context
import android.util.Log
import com.yzp.bookshelf.mvp.contract.ShelfContract
import com.yzp.bookshelf.mvp.model.ShelfModel
import com.yzp.common.db.bean.Book
import com.yzp.common.mvp.BasePresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ShelfPresenter : BasePresenter<ShelfContract.View>(),
    ShelfContract.Presenter {

    private val shelfModel: ShelfModel by lazy {
        ShelfModel()
    }

    override fun requestData(context: Context) {
        Single.create(SingleOnSubscribe<MutableList<Book>> {
            var bookList: MutableList<Book>? = shelfModel.requestData(context)
            it.onSuccess(bookList)
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<MutableList<Book>> {
                override fun onSuccess(t: MutableList<Book>?) {
                    mRootView?.apply {
                        setData(t!!)
                    }
                }

                override fun onSubscribe(d: Disposable?) {
                }

                override fun onError(e: Throwable?) {
                }
            })
    }

    override fun loadMoreData() {
    }
}