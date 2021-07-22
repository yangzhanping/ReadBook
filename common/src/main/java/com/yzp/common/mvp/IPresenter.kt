package com.yzp.common.mvp

interface IPresenter<in V : IBaseView> {

    fun attachView(view: V)

    fun detachView()
}