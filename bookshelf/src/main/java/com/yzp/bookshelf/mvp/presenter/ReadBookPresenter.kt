package com.yzp.bookshelf.mvp.presenter

import android.content.Context
import android.widget.TextView
import com.orhanobut.logger.Logger
import com.yzp.bookshelf.mvp.contract.ReadBookContract
import com.yzp.bookshelf.mvp.model.ReadBookModel
import com.yzp.common.db.BookDaoOpe
import com.yzp.common.db.ChapterDaoOpe
import com.yzp.common.db.bean.Book
import com.yzp.common.db.bean.Chapter
import com.yzp.common.http.HttpUtils
import com.yzp.common.mvp.BasePresenter
import com.yzp.common.utils.StringHelper
import com.yzp.common.utils.ThreadManager
import com.yzp.common.xml.TianLaiReadUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader

class ReadBookPresenter : BasePresenter<ReadBookContract.View>(), ReadBookContract.Presenter {

    var readBookModel = ReadBookModel()

    /**
     *获取所有章节
     */
    override fun requestChaptersData(context: Context, mBook: Book) {
        readBookModel.requestData(mBook.chapterUrl).map {
            val reader = BufferedReader(InputStreamReader(it.byteStream(), "GBK"))
            val response = StringBuilder()
            var line: String? = reader.readLine()
            while (line != null) {
                response.append(line)
                line = reader.readLine()
            }

            var newChapters = TianLaiReadUtil.getChapters(response.toString(), mBook)
            var oldChapters = mutableListOf<Chapter>()

            //1、更新book库 章节字段
            if (!StringHelper.isEmpty(mBook.id)) {
                mBook.chapterTotalNum = newChapters.size
                BookDaoOpe.getInstance().saveData(context, mBook)
                //2、更新章节库
                oldChapters = updateChapter(context, newChapters, mBook)
            }

            if (!StringHelper.isEmpty(mBook.id))
                oldChapters
            else
                newChapters
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<MutableList<Chapter>?> {
                override fun onSubscribe(d: Disposable?) {
                    mRootView?.apply {
                        showLoading()
                    }
                }

                override fun onComplete() {
                    mRootView?.apply {
                        dismissLoading()
                    }
                }

                override fun onNext(it: MutableList<Chapter>?) {
                    mRootView?.apply {
                        setDrawerData(it!!)
                        setContentData(it!!)
                    }
                }

                override fun onError(e: Throwable?) {
                    mRootView?.apply {
                        dismissLoading()
                    }
                    Logger.e("error-> " + e!!.message)
                }
            })
    }

    /**
     * 缓存正本书籍
     */
    override fun downLoadAllChapters(
        context: Context,
        book: Book,
        chapters: MutableList<Chapter>,
        tvDownloadProgress: TextView
    ) {
        var downIndex = 0
        ThreadManager.createLongPool()!!.execute {
            for (chapter in chapters) {
                if (StringHelper.isEmpty(chapter.chapterContent)) {
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
                            downIndex++
                            mRootView?.apply {
                                setCacheProgress(downIndex, tvDownloadProgress)
                            }
                        }, {
                            Logger.e("error-> " + it.message)
                        })
                } else {
                    downIndex++
                    mRootView?.apply {
                        setCacheProgress(downIndex, null)
                    }
                }
            }
        }
    }

    /**
     * 更新章节
     */
    private fun updateChapter(
        context: Context, newChapters: MutableList<Chapter>,
        mBook: Book
    ): MutableList<Chapter> {
        //1、更新章节内容
        val oldChapters = ChapterDaoOpe.getInstance().queryAll(context, mBook.id)
        var i = 0
        while (i < oldChapters!!.size && i < newChapters.size) {
            val oldChapter: Chapter = oldChapters[i]
            val newChapter = newChapters[i]
            if (!oldChapter.chapterTitle.equals(newChapter.chapterTitle)) {
                oldChapter.chapterTitle = newChapter.chapterTitle
                oldChapter.chapterUrl = newChapter.chapterUrl
                oldChapter.chapterContent = null
                ChapterDaoOpe.getInstance().saveData(context, oldChapter)
            }
            i++
        }
        //2、增加章节 3、删除章节
        if (oldChapters.size < newChapters.size) {
            val start: Int = oldChapters.size
            for (j in oldChapters.size until newChapters.size) {
                newChapters[j].id = StringHelper.getStringRandom(25)
                newChapters[j].bookId = mBook.id
                oldChapters.add(newChapters[j])
            }
            ChapterDaoOpe.getInstance()
                .insertDatas(context, oldChapters.subList(start, oldChapters.size))
        } else if (oldChapters.size > newChapters.size) {
            for (j in newChapters.size until oldChapters.size) {
                ChapterDaoOpe.getInstance().deleteData(context, oldChapters[j])
            }
            oldChapters.subList(0, newChapters.size)
        }
        return oldChapters
    }
}
