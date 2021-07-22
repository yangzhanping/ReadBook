package com.yzp.common.mvp

open class BasePresenter<T : IBaseView> : IPresenter<T> {

    var mRootView: T? = null

    override fun attachView(view: T) {
        this.mRootView = view
    }

    override fun detachView() {
        mRootView = null
    }
}