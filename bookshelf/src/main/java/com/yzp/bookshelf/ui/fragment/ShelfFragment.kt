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
import com.scwang.smartrefresh.header.MaterialHeader
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

    //refresh
    private var isRefresh = false
    private var mMaterialHeader: MaterialHeader? = null
    private var loadingMore = false

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
        mMaterialHeader = binding.mRefreshLayout.refreshHeader as MaterialHeader?
        mMaterialHeader?.setShowBezierWave(true)
        //设置下拉刷新主题颜色
        binding.mRefreshLayout.setPrimaryColorsId(R.color.color_light_black, R.color.color_title_bg)
        binding.mRefreshLayout.setEnableHeaderTranslationContent(true)
        binding.mRefreshLayout.setOnRefreshListener {
            isRefresh = true
            activity?.applicationContext?.let { mPresenter.requestData(it) }
        }
    }

    private fun initListener() {
        //search
        binding.ivSearch.setOnClickListener {
            val intent = Intent(activity, ShelfSearchBookActivity::class.java)
            startActivity(intent)
        }
        //recyclerview
        binding.mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val childCount = binding.mRecyclerView.childCount
                    val itemCount = binding.mRecyclerView.layoutManager?.itemCount
                    val firstVisibleItem =
                        (binding.mRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (firstVisibleItem + childCount == itemCount) {
                        if (!loadingMore) {
                            loadingMore = true
                            mPresenter.loadMoreData()
                        }
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
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
        activity?.applicationContext?.let { mPresenter.requestData(it) }
    }

    override fun setData(bookList: MutableList<Book>) {
        if (bookList.size <= 0) {
            binding.multipleStatusView.showEmpty("当前无任何书籍")
            return
        }
        binding.multipleStatusView.showContent()
        //adapter
        shelfAdapter = activity?.let { ShelfAdapter(it, bookList) }

        binding.mRecyclerView.adapter = shelfAdapter
        binding.mRecyclerView.layoutManager = linearLayoutManager
        binding.mRecyclerView.itemAnimator = DefaultItemAnimator()
    }

    override fun setMoreData(bookList: MutableList<Book>) {
        loadingMore = false
        shelfAdapter?.addItemData(bookList)
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