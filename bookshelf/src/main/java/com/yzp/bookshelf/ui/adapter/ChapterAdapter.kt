package com.yzp.bookshelf.ui.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.spreada.utils.chinese.ZHConverter
import com.yzp.bookshelf.R
import com.yzp.bookshelf.app.SysManager
import com.yzp.bookshelf.enums.Font
import com.yzp.bookshelf.enums.Language
import com.yzp.common.db.bean.Chapter
import com.yzp.common.utils.StringHelper

class ChapterAdapter(var context: Context, var chapterList: MutableList<Chapter>) : BaseAdapter() {

    private var currentPosition = -1
    private var mTypeFace: Typeface? = null

    init {
        initFont()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = null
        var viewHolder: ViewHolder? = null
        if (convertView == null) {
            view = View.inflate(context, R.layout.shelf_chapter_item, null)
            viewHolder = ViewHolder()
            viewHolder.tv_chapterName = view!!.findViewById(R.id.tv_chapterName)
            viewHolder.tv_down = view!!.findViewById(R.id.tv_down)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view?.tag as ViewHolder
        }

        //字体样式
        viewHolder.tv_chapterName.typeface = mTypeFace
        viewHolder.tv_down.typeface = mTypeFace
        //日夜字体颜色
        val setting = SysManager.getSetting(context)!!
        if (setting.dayStyle) {
            viewHolder.tv_chapterName.setTextColor(
                context.resources.getColor(setting.readWordColor)
            )
            viewHolder.tv_down.setTextColor(
                context.resources.getColor(setting.readWordColor)
            )
        } else {
            viewHolder.tv_chapterName.setTextColor(
                context.resources.getColor(R.color.sys_night_word)
            )
            viewHolder.tv_down.setTextColor(
                context.resources.getColor(R.color.sys_night_word)
            )
        }
        //选中字体颜色
        if (currentPosition == position) {
            viewHolder.tv_chapterName.setTextColor(context.resources.getColor(R.color.sys_dialog_setting_word_red));
        }

        viewHolder.tv_chapterName.text = getLanguageContext(chapterList[position].chapterTitle)
        if (StringHelper.isEmpty(chapterList[position].chapterContent)) {
            viewHolder.tv_down.visibility = View.GONE
        } else {
            viewHolder.tv_down.visibility = View.VISIBLE
        }
        return view!!
    }

    override fun getItem(position: Int): Chapter {
        return chapterList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return chapterList.size
    }

    fun setCurrentPosition(position: Int) {
        currentPosition = position
        notifyDataSetChanged()
    }

    class ViewHolder() {
        lateinit var tv_chapterName: TextView
        lateinit var tv_down: TextView
    }

    /**
     * 更新设置
     */
    fun notifyDataSetChangedBySetting() {
        initFont()
        notifyDataSetChanged()
    }

    /**
     * 字体初始化
     */
    private fun initFont() {
        val setting = SysManager.getSetting(context)!!
        mTypeFace = if (setting.font == Font.默认字体) {
            null
        } else {
            Typeface.createFromAsset(
                context.assets,
                setting.font!!.path
            )
        }
    }

    /**
     * 简繁转换
     */
    private fun getLanguageContext(content: String): String {
        val setting = SysManager.getSetting(context)!!
        return if (setting.language == Language.traditional && setting.font == Font.默认字体) {
            ZHConverter.convert(content, ZHConverter.TRADITIONAL)
        } else content
    }
}