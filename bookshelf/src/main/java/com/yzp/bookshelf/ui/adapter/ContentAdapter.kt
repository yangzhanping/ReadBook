package com.yzp.bookshelf.ui.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.orhanobut.logger.Logger
import com.spreada.utils.chinese.ZHConverter
import com.yzp.bookshelf.R
import com.yzp.bookshelf.app.SysManager
import com.yzp.bookshelf.enums.Font
import com.yzp.bookshelf.enums.Language
import com.yzp.common.adapter.CommonAdapter
import com.yzp.common.adapter.ViewHolder
import com.yzp.common.db.ChapterDaoOpe
import com.yzp.common.db.bean.Chapter
import com.yzp.common.http.HttpUtils
import com.yzp.common.utils.StringHelper
import com.yzp.common.xml.TianLaiReadUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader

class ContentAdapter(var context: Context, private var Chapters: MutableList<Chapter>) :
    CommonAdapter<Chapter>(context, Chapters, -1) {

    private var mTypeFace: Typeface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        initFont()
        return ViewHolder(inflaterView(R.layout.shelf_adapter_content_item, parent))
    }

    /**
     * 加载布局
     */
    private fun inflaterView(mLayoutId: Int, parent: ViewGroup): View {
        //创建view
        val view = mInflater?.inflate(mLayoutId, parent, false)
        return view ?: View(parent.context)
    }

    override fun bindData(holder: ViewHolder, chapter: Chapter, position: Int) {
        if (Chapters.size <= position) {
            return
        }

        //字体样式
        holder.getView<TextView>(R.id.tv_title).typeface = mTypeFace
        holder.getView<TextView>(R.id.tv_content).typeface = mTypeFace
        //日夜字体颜色
        val setting = SysManager.getSetting(context)!!
        if (setting.dayStyle) {
            holder.getView<TextView>(R.id.tv_title).setTextColor(
                context.resources.getColor(setting.readWordColor)
            )
            holder.getView<TextView>(R.id.tv_content).setTextColor(
                context.resources.getColor(setting.readWordColor)
            )
        } else {
            holder.getView<TextView>(R.id.tv_title).setTextColor(
                context.resources.getColor(R.color.sys_night_word)
            )
            holder.getView<TextView>(R.id.tv_content).setTextColor(
                context.resources.getColor(R.color.sys_night_word)
            )
        }
        //字体大小
        holder.getView<TextView>(R.id.tv_title).textSize = setting.readWordSize + 2
        holder.getView<TextView>(R.id.tv_content).textSize = setting.readWordSize


        holder.setText(R.id.tv_title, "【" + getLanguageContext(chapter.chapterTitle) + "】")
        if (StringHelper.isEmpty(chapter.chapterContent)) {
            getChapterContent(chapter, holder)
        } else {
            holder.setText(R.id.tv_content, getLanguageContext(chapter.chapterContent))
        }
        preLoading(position)
        lastLoading(position)
    }

    /**
     * 预加载下一章
     * @param position
     */
    private fun preLoading(position: Int) {
        if (position + 1 < itemCount) {
            val chapter: Chapter = Chapters[position + 1]
            if (StringHelper.isEmpty(chapter.chapterContent)) {
                getChapterContent(chapter, null)
            }
        }
    }

    /**
     * 预加载上一张
     * @param position
     */
    private fun lastLoading(position: Int) {
        if (position > 0) {
            val chapter: Chapter = Chapters[position - 1]
            if (StringHelper.isEmpty(chapter.chapterContent)) {
                getChapterContent(chapter, null)
            }
        }
    }

    /**
     * 获取小说内容
     */
    private fun getChapterContent(chapter: Chapter, holder: ViewHolder?) {
        if (!StringHelper.isEmpty(chapter.id)) {
            var oldChapter =
                ChapterDaoOpe.getInstance()
                    .queryTitle(context, chapter.chapterTitle, chapter.bookId)
            if (!StringHelper.isEmpty(oldChapter.chapterContent)) {
                holder?.setText(R.id.tv_content, getLanguageContext(oldChapter.chapterContent))
                return
            }
        }
        HttpUtils.getHtml(chapter.chapterUrl).map {
            val reader = BufferedReader(InputStreamReader(it.byteStream(), "GBK"))
            val response = StringBuilder()
            var line: String? = reader.readLine()
            while (!line.isNullOrEmpty()) {
                response.append(line)
                line = reader.readLine()
            }
            TianLaiReadUtil.getContent(response.toString())
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (!StringHelper.isEmpty(chapter.id)) {
                    chapter.chapterContent = it
                    ChapterDaoOpe.getInstance().saveData(context, chapter)
                }
                holder?.setText(R.id.tv_content, getLanguageContext(it))
            }, {
                Logger.e("error-> " + it.message)
            })
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
}