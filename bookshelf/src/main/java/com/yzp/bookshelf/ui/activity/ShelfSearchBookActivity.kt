package com.yzp.bookshelf.ui.activity

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import com.alibaba.android.arouter.facade.annotation.Route
import com.yzp.bookshelf.databinding.ShelfSearchBookBinding
import com.yzp.bookshelf.mvp.contract.SearchBookContract
import com.yzp.bookshelf.mvp.presenter.SearchBookPresenter
import com.yzp.bookshelf.ui.adapter.SearchBookAdapter
import com.yzp.bookshelf.ui.adapter.SearchHistoryAdapter
import com.yzp.common.base.BaseActivity
import com.yzp.common.db.SearchHistoryDaoOpe
import com.yzp.common.db.bean.Book
import com.yzp.common.utils.StringHelper

@Route(path = "/bookshelf/ShelfSearchBookActivity")
class ShelfSearchBookActivity : BaseActivity(), SearchBookContract.View {

    lateinit var binding: ShelfSearchBookBinding

    private var presenter: SearchBookPresenter = SearchBookPresenter()
    private var searchKey: String = ""

    private var historyAdapter: SearchHistoryAdapter? = null
    private var searchBookAdapter: SearchBookAdapter? = null

    private val suggestion =
        arrayOf("帝霸", "逆天邪神", "万古第一神", "九星霸体诀", "明天下", "生活系男神", "元尊", "超神机械师", "终极斗罗")

    override fun onBackPressed() {
        if (StringHelper.isEmpty(searchKey)) {
            super.onBackPressed()
        } else {
            binding.etSearchKey.setText("")
        }
    }

    override fun initView() {
        binding = ShelfSearchBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.tvTitleText.text = "搜索"
        binding.title.llTitleBack.setOnClickListener {
            if (StringHelper.isEmpty(searchKey)) {
                finish()
            } else {
                binding.etSearchKey.setText("")
            }
        }
        binding.tvSearchConform.setOnClickListener {
            search(searchKey)
        }
        binding.llClearHistory.setOnClickListener {
            SearchHistoryDaoOpe.getInstance().deleteAllData(this)
            historyAdapter!!.notifyData()
            binding.llHistoryView.visibility = View.GONE
        }
        initSuggestion()
        initSearchKey()
        initHistory()
    }

    private fun initSuggestion() {
        binding.tgSuggestBook.setTags(suggestion.toList())
        binding.tgSuggestBook.setOnTagClickListener {
            binding.etSearchKey.setText(it)
            binding.etSearchKey.setSelection(it.length)
            search(it)
        }
        //换一批
        binding.llRefreshSuggestBooks.setOnClickListener {
        }
    }

    private fun initHistory() {
        var searchHistoryList = SearchHistoryDaoOpe.getInstance().queryAll(this)
        if (searchHistoryList == null || searchHistoryList.size == 0) {
            binding.llHistoryView.visibility = View.GONE
        } else {
            historyAdapter = SearchHistoryAdapter(this, searchHistoryList)
            binding.lvHistoryList.adapter = historyAdapter
            binding.llHistoryView.visibility = View.VISIBLE
        }
        binding.lvHistoryList.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                var searchHistory = historyAdapter!!.getItem(position)
                var key = searchHistory!!.content
                binding.etSearchKey.setText(key)
                binding.etSearchKey.setSelection(key.length)
                search(key)
            }
    }

    private fun initSearchKey() {
        binding.etSearchKey.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                searchKey = editable.toString()
                if (StringHelper.isEmpty(searchKey)) {
                    search(searchKey)
                }
            }
        })
        binding.etSearchKey.setOnKeyListener { v, keyCode, event ->
            //是否是回车键
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                //隐藏键盘
                (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(
                        currentFocus?.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                search(searchKey)
            }
            false
        }
    }

    override fun initData() {
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun setSearchList(bookList: MutableList<Book>) {
        if (searchBookAdapter == null) {
            searchBookAdapter = SearchBookAdapter(this, bookList)
            binding.lvSearchBooksList.adapter = searchBookAdapter
            binding.lvSearchBooksList.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    val intent = Intent(this, ShelfBookInfoActivity::class.java)
                    intent.putExtra("book", searchBookAdapter!!.getItem(position))
                    startActivity(intent)
                }
        } else {
            searchBookAdapter!!.notfiy(bookList)
        }
    }

    override fun showError(msg: String, errorCode: Int) {
    }

    override fun showLoading() {
        binding.multipleStatusView.showLoading()
    }

    override fun dismissLoading() {
        binding.multipleStatusView.showContent()
    }

    private fun search(key: String) {
        if (StringHelper.isEmpty(key)) {
            if (searchBookAdapter != null) {
                searchBookAdapter!!.clearData()
            }
            binding.lvSearchBooksList.visibility = View.GONE
            binding.llSuggestBooksView.visibility = View.VISIBLE
            initHistory()
        } else {
            binding.lvSearchBooksList.visibility = View.VISIBLE
            binding.llSuggestBooksView.visibility = View.GONE
            binding.llHistoryView.visibility = View.GONE
            presenter.requestData(this, key)
        }
    }
}