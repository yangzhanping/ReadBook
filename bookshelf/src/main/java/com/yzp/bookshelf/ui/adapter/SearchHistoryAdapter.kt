package com.yzp.bookshelf.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.yzp.bookshelf.R
import com.yzp.common.db.bean.SearchHistory


class SearchHistoryAdapter(
    var context: Context,
    var searchHistoryList: MutableList<SearchHistory>
) :
    BaseAdapter() {

    override fun getCount(): Int {
        return searchHistoryList.size
    }

    override fun getItem(position: Int): SearchHistory {
        return searchHistoryList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = null
        var viewHolder: ViewHolder? = null
        if (convertView == null) {
            view = View.inflate(context, R.layout.shelf_adapter_search_history_item, null)
            viewHolder = ViewHolder()
            viewHolder.tv_history_content = view!!.findViewById(R.id.tv_history_content)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view?.tag as ViewHolder
        }
        viewHolder.tv_history_content.text = searchHistoryList[position].content
        return view!!
    }

    class ViewHolder() {
        lateinit var tv_history_content: TextView
    }

    fun notifyData() {
        searchHistoryList.clear()
        notifyDataSetChanged()
    }
}

