package com.yzp.user.ui.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.yzp.common.arouter.ArouterUtils
import com.yzp.common.base.BaseActivity
import com.yzp.user.R

@Route(path = "/user/UserMainActivity")
class UserMainActivity : BaseActivity() {

    override fun initView() {
        setContentView(R.layout.user_activity_main)

        supportFragmentManager.inTransaction {
            ArouterUtils.navigationFragment("/user/UserFragment")?.let {
                add(
                    R.id.user_constraintLayout,
                    it
                )
            }
        }
    }

    override fun initData() {
    }
}