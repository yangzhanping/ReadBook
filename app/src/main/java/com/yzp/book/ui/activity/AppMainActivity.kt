package com.yzp.book.ui.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.yzp.book.R
import com.yzp.book.databinding.AppActivityMainBinding
import com.yzp.common.arouter.ArouterUtils
import com.yzp.common.base.BaseActivity

@Route(path = "/app/AppMainActivity")
class AppMainActivity : BaseActivity(), BottomNavigationBar.OnTabSelectedListener {

    lateinit var binding: AppActivityMainBinding

    override fun initView() {
        binding = AppActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
        binding.bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED)
        binding.bottomNavigationBar.setTabSelectedListener(this)
        binding.bottomNavigationBar
            //书架
            .addItem(BottomNavigationItem(R.drawable.app_shelf, R.string.app_shelf))
            .setActiveColor(R.color.colorPrimaryDark).setInActiveColor(R.color.colorMain)
            //书城
            .addItem(BottomNavigationItem(R.drawable.app_store, R.string.app_store))
            .setActiveColor(R.color.colorPrimaryDark).setInActiveColor(R.color.colorMain)
            //我的
            .addItem(BottomNavigationItem(R.drawable.app_user, R.string.app_user))
            .setActiveColor(R.color.colorPrimaryDark).setInActiveColor(R.color.colorMain)
            .setFirstSelectedPosition(0)
            .initialise()
        binding.bottomNavigationBar.selectTab(0)
    }

    override fun initData() {
    }

    override fun onTabReselected(position: Int) {
    }

    override fun onTabUnselected(position: Int) {
    }

    override fun onTabSelected(position: Int) {
        when (position) {
            0 ->
                supportFragmentManager.inTransaction {
                    ArouterUtils.navigationFragment("/shelf/ShelfFragment")?.let {
                        replace(
                            R.id.fl_layout,
                            it
                        )
                    }
                }
            1 ->
                supportFragmentManager.inTransaction {
                    ArouterUtils.navigationFragment("/store/StoreFragment")?.let {
                        replace(
                            R.id.fl_layout,
                            it
                        )
                    }
                }
            2 ->
                supportFragmentManager.inTransaction {
                    ArouterUtils.navigationFragment("/user/UserFragment")?.let {
                        replace(
                            R.id.fl_layout,
                            it
                        )
                    }
                }
        }
    }
}