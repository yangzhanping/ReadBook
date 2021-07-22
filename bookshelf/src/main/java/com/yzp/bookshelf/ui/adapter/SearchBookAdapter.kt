package com.yzp.bookshelf.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.yzp.bookshelf.R
import com.yzp.common.constant.BookSource
import com.yzp.common.db.bean.Book
import com.yzp.common.image.GlideUtil

class SearchBookAdapter(var context: Context, var bookList: MutableList<Book>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return bookList.size
    }

    override fun getItem(position: Int): Book {
        return bookList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = null
        var viewHolder: ViewHolder? = null
        if (convertView == null) {
            view = View.inflate(context, R.layout.shelf_adapter_search_book_item, null)
            viewHolder = ViewHolder()
            viewHolder.iv_book_img = view!!.findViewById(R.id.iv_book_img)
            viewHolder.tv_book_name = view!!.findViewById(R.id.tv_book_name)
            viewHolder.tv_book_source = view!!.findViewById(R.id.tv_book_source)
            viewHolder.tv_book_desc = view!!.findViewById(R.id.tv_book_desc)
            viewHolder.tv_book_author = view!!.findViewById(R.id.tv_book_author)
            viewHolder.tv_book_type = view!!.findViewById(R.id.tv_book_type)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view?.tag as ViewHolder
        }

        var book = bookList[position]
        viewHolder.tv_book_name.text = book.bookName
        book.coverUrl?.let {
            GlideUtil.loadImage(context, it, viewHolder.iv_book_img)
        }
        viewHolder.tv_book_source.text = BookSource.valueOf(book.source).text
        viewHolder.tv_book_desc.text = book.desc
        viewHolder.tv_book_author.text = book.author
        viewHolder.tv_book_type.text = book.type
        return view!!
    }

    class ViewHolder() {
        lateinit var iv_book_img: ImageView
        lateinit var tv_book_name: TextView
        lateinit var tv_book_source: TextView
        lateinit var tv_book_desc: TextView
        lateinit var tv_book_author: TextView
        lateinit var tv_book_type: TextView
    }

    fun notfiy(list: MutableList<Book>) {
        bookList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearData() {
        bookList.clear()
        notifyDataSetChanged()
    }
}