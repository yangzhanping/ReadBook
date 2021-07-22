package com.yzp.bookshelf.mvp.presenter

import com.yzp.bookshelf.enums.Font
import com.yzp.bookshelf.mvp.contract.FontsContract
import com.yzp.bookshelf.mvp.model.FontsModel
import com.yzp.common.db.bean.Book
import com.yzp.common.mvp.BasePresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.core.SingleOnSubscribe
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.operators.single.SingleCreate
import io.reactivex.rxjava3.schedulers.Schedulers

class FontsPresenter : BasePresenter<FontsContract.View>(), FontsContract.Presenter {

    private val fontsModel = FontsModel()

    override fun requestData() {
        Single.create(SingleOnSubscribe<MutableList<Font>> {
            var fonts: MutableList<Font>? = fontsModel.requestData()
            it.onSuccess(fonts)
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<MutableList<Font>> {
                override fun onSuccess(t: MutableList<Font>?) {
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
}