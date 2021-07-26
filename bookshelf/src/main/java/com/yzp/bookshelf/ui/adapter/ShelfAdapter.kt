package com.yzp.bookshelf.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.yzp.bookshelf.R
import com.yzp.bookshelf.mvp.model.bean.ShelfBean
import com.yzp.common.adapter.CommonAdapter
import com.yzp.common.adapter.OnItemClickListener
import com.yzp.common.adapter.ViewHolder
import com.yzp.common.arouter.ArouterUtils
import com.yzp.common.db.BookDaoOpe
import com.yzp.common.db.bean.Book
import com.yzp.common.image.GlideUtil

class ShelfAdapter(var context: Context, private var bookList: MutableList<Book>) :
    CommonAdapter<Book>(context, bookList, -1) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflaterView(R.layout.shelf_adapter_item, parent))
    }

    /**
     * 加载布局
     */
    private fun inflaterView(mLayoutId: Int, parent: ViewGroup): View {
        //创建view
        val view = mInflater?.inflate(mLayoutId, parent, false)
        return view ?: View(parent.context)
    }

    override fun bindData(holder: ViewHolder, itemData: Book, position: Int) {
        holder.setText(R.id.shelf_bookName, itemData?.bookName ?: "")
        holder.setText(R.id.shelf_newestChapterTitle, itemData?.newestChapterTitle ?: "")
        holder.setText(R.id.shelf_updateDate, itemData?.updateDate ?: "")
        GlideUtil.loadImage(context, itemData?.coverUrl, holder.getView(R.id.shelf_bookCover))
        //item click
        holder.setOnItemClickListener(listener = View.OnClickListener {
            BookDaoOpe.getInstance().updateSort(context, itemData)
            ArouterUtils.navigationActivity("/bookshelf/ShelfReadBookActivity", itemData)
        })
    }

    /**
     * 添加更多数据
     */
    fun addItemData(bookList: MutableList<Book>) {
        this.bookList.addAll(bookList)
        notifyDataSetChanged()
    }
}