package com.yzp.bookshelf.ui.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.yzp.bookshelf.databinding.ShelfActivityBookInfoBinding
import com.yzp.bookshelf.mvp.contract.BookInfoContract
import com.yzp.bookshelf.mvp.presenter.BookInfoPresenter
import com.yzp.common.arouter.ArouterUtils
import com.yzp.common.base.BaseActivity
import com.yzp.common.constant.BookSource
import com.yzp.common.db.BookDaoOpe
import com.yzp.common.db.ChapterDaoOpe
import com.yzp.common.db.bean.Book
import com.yzp.common.image.GlideUtil
import com.yzp.common.utils.StringHelper

@Route(path = "/bookshelf/ShelfBookInfoActivity")
class ShelfBookInfoActivity : BaseActivity(), BookInfoContract.View {

    lateinit var binding: ShelfActivityBookInfoBinding
    var book: Book? = null
    val presenter = BookInfoPresenter()

    override fun initView() {
        binding = ShelfActivityBookInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.llTitleBack.setOnClickListener {
            finish()
        }

        binding.btnAddBookcase.setOnClickListener {
            if (StringHelper.isEmpty(book!!.id)) {
                BookDaoOpe.getInstance().insertData(this, book!!)
                binding.btnAddBookcase.text = "移出书架"
                showToast("已添加到书架")
            } else {
                val chapters = ChapterDaoOpe.getInstance().queryId(this, book!!.id)
                ChapterDaoOpe.getInstance().deleteData(this, chapters)
                BookDaoOpe.getInstance().deleteData(this, book!!.id)
                book!!.id = null
                binding.btnAddBookcase.text = "加入书架"
                showToast("移出成功")
            }
        }
        binding.btnReadBook.setOnClickListener {
            ArouterUtils.navigationActivity("/bookshelf/ShelfReadBookActivity", book!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    private fun initView(book: Book) {
        binding.title.tvTitleText.text = book!!.bookName
        GlideUtil.loadImage(this, book!!.coverUrl, binding.ivBookImg)
        binding.tvBookName.text = book!!.bookName
        binding.tvBookAuthor.text = book!!.author
        binding.tvBookType.text = book!!.type
        binding.tvBookDesc.text = book!!.desc
    }

    override fun initData() {
        presenter.attachView(this)
        book = intent.getParcelableExtra("book")
        book?.let {
            if (BookSource.biquge.toString() == book!!.source) {
                initBiquge(book!!)
            } else {
                initView(book!!)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        book?.let {
            if (isBookCollected(book!!)) {
                binding.btnAddBookcase.text = "移出书架"
            } else {
                binding.btnAddBookcase.text = "加入书架"
            }
        }
    }

    private fun initBiquge(book: Book) {
        presenter.requestData(book!!)
    }

    override fun setData(book: Book) {
        book?.let {
            initView(book)
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

    private fun isBookCollected(book: Book): Boolean {
        val mBook = BookDaoOpe.getInstance().query(this, book)
        return if (mBook != null) {
            this.book = mBook
            true
        } else {
            false
        }
    }
}