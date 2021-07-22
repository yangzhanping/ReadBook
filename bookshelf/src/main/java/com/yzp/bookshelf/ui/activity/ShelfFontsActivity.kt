package com.yzp.bookshelf.ui.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.yzp.bookshelf.R
import com.yzp.bookshelf.databinding.ShelfActivityFontsBinding
import com.yzp.bookshelf.enums.Font
import com.yzp.bookshelf.mvp.contract.FontsContract
import com.yzp.bookshelf.mvp.presenter.FontsPresenter
import com.yzp.bookshelf.ui.adapter.FontsAdapter
import com.yzp.common.base.BaseActivity

@Route(path = "/bookshelf/ShelfFontsActivity")
class ShelfFontsActivity : BaseActivity(), FontsContract.View {

    val presenter = FontsPresenter()
    private lateinit var binding: ShelfActivityFontsBinding

    private var fontsAdapter: FontsAdapter? = null

    override fun initView() {
        presenter.attachView(this)
        binding = ShelfActivityFontsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.tvTitleText.text = getString(R.string.font)
        binding.title.llTitleBack.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun initData() {
        presenter.requestData()
    }

    override fun setData(fonts: MutableList<Font>) {
        fontsAdapter = FontsAdapter(this, fonts)
        binding.lvFonts.adapter = fontsAdapter
    }

    override fun showError(msg: String, errorCode: Int) {
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }
}