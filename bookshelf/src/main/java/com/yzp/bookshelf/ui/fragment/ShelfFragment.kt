package com.yzp.bookshelf.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smart.refresh.header.ClassicsHeader
import com.yzp.bookshelf.R
import com.yzp.bookshelf.databinding.ShelfFragmentBinding
import com.yzp.bookshelf.mvp.contract.ShelfContract
import com.yzp.bookshelf.mvp.presenter.ShelfPresenter
import com.yzp.bookshelf.ui.activity.ShelfSearchBookActivity
import com.yzp.bookshelf.ui.adapter.ShelfAdapter
import com.yzp.common.base.BaseFragment
import com.yzp.common.db.bean.Book

@Route(path = "/shelf/ShelfFragment")
class ShelfFragment : BaseFragment(), ShelfContract.View {

    //binding
    private var _binding: ShelfFragmentBinding? = null
    private val binding get() = _binding!!

    //presenter
    private val mPresenter by lazy { ShelfPresenter() }

    //adapter
    private var shelfAdapter: ShelfAdapter? = null;

    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ShelfFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        mPresenter.attachView(this)
        initRefresh()
        initListener()
    }

    private fun initRefresh() {
        //打开下拉刷新区域块背景:
        binding.mRefreshLayout.setRefreshHeader(ClassicsHeader(activity))
        //设置下拉刷新主题颜色
        binding.mRefreshLayout.setOnRefreshListener {
            activity?.applicationContext?.let {
                mPresenter.refreshData(it)
            }
            binding.mRefreshLayout.finishRefresh()
        }
    }

    private fun initListener() {
        //search
        binding.ivSearch.setOnClickListener {
            val intent = Intent(activity, ShelfSearchBookActivity::class.java)
            startActivity(intent)
        }
    }

    override fun initData() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mPresenter.detachView()
    }

    override fun onResume() {
        super.onResume()
        activity?.applicationContext?.let { mPresenter.requestData(it, true) }
    }

    override fun setData(bookList: MutableList<Book>, isRefresh: Boolean) {
        if (bookList.size <= 0) {
            binding.multipleStatusView.showEmpty("当前无任何书籍")
            return
        }
        if (isRefresh) {
            //启动刷新
            binding.mRefreshLayout.setEnableRefresh(true)
            binding.mRefreshLayout.autoRefresh()
        }
        binding.multipleStatusView.showContent()
        //adapter
        shelfAdapter = activity?.let { ShelfAdapter(it, bookList) }
        binding.mRecyclerView.adapter = shelfAdapter
        binding.mRecyclerView.layoutManager = linearLayoutManager
        binding.mRecyclerView.itemAnimator = DefaultItemAnimator()
    }

    override fun showError(msg: String, errorCode: Int) {
        binding.multipleStatusView.showError()
    }

    /**
     * 显示 Loading （下拉刷新的时候不需要显示 Loading）
     */
    override fun showLoading() {
        binding.multipleStatusView.showLoading()
    }

    override fun dismissLoading() {
        binding.multipleStatusView.showContent()
    }
}