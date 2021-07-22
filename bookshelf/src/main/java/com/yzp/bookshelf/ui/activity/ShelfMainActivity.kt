package com.yzp.bookshelf.ui.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.yzp.bookshelf.R
import com.yzp.common.arouter.ArouterUtils
import com.yzp.common.base.BaseActivity

@Route(path = "/bookshelf/ShelfMainActivity")
class ShelfMainActivity : BaseActivity() {

    override fun initView() {
        setContentView(R.layout.shelf_activity_main)

        supportFragmentManager.inTransaction {
            ArouterUtils.navigationFragment("/shelf/ShelfFragment")?.let {
                add(
                    R.id.shelf_constraintLayout,
                    it
                )
            }
        }
    }

    override fun initData() {
    }
}