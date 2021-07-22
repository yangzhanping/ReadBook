package com.yzp.common.arouter

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.launcher.ARouter
import com.yzp.common.db.bean.Book
import java.util.*

object ArouterUtils {
    fun init(app: Application, isDebug: Boolean) {
        if (!isDebug) {
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(app)
    }

    /**
     *启动Activity
     */
    fun navigationActivity(path: String) {
        ARouter.getInstance().build(path).navigation()
    }

    /**
     *启动Activity
     */
    fun navigationActivity(path: String, book: Book) {
        ARouter.getInstance().build(path).withParcelable("book", book).navigation()
    }

    /**
     *启动Fragment
     */
    fun navigationFragment(path: String): Fragment? {
        if (ARouter.getInstance().build(path).navigation() == null) {
            return null
        } else {
            return ARouter.getInstance().build(path).navigation() as Fragment
        }
    }
}