package com.yzp.common.base

import android.app.Application
import androidx.multidex.MultiDex
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.yzp.common.BuildConfig
import com.yzp.common.arouter.ArouterUtils


open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //解决方法数过多
        MultiDex.install(this);
        //Arouter初始化
        ArouterUtils.init(this, BuildConfig.isRelease)
        //logger
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
//            .showThreadInfo(false)
//            .methodCount(0)
//            .methodOffset(7)
//            .logStrategy(customLog)
            .tag("book")
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
}