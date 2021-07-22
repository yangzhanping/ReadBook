package com.yzp.bookstore.ui.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.yzp.bookstore.R
import com.yzp.common.arouter.ArouterUtils
import com.yzp.common.base.BaseActivity

@Route(path = "/bookstore/StoreMainActivity")
class StoreMainActivity : BaseActivity() {

    override fun initView() {
        setContentView(R.layout.store_activity_main)

        supportFragmentManager.inTransaction {
            ArouterUtils.navigationFragment("/store/StoreFragment")?.let {
                add(
                    R.id.store_constraintLayout,
                    it
                )
            }
        }
    }

    override fun initData() {
    }
}