package com.yzp.common.db

import android.content.Context
import com.yzp.common.db.bean.Chapter
import com.yzp.common.db.dao.ChapterDao
import java.util.*

class ChapterDaoOpe {

    private object mHolder {
        val instance = ChapterDaoOpe()
    }

    companion object {
        fun getInstance(): ChapterDaoOpe {
            return mHolder.instance
        }
    }

    /**
     * 添加数据至数据库
     * @param context
     * @param book
     */
    fun insertDatas(context: Context?, chapters: MutableList<Chapter>) {
        DbManager.getInstance(context!!)?.getDaoSession(context)?.chapterDao?.insertInTx(chapters)
    }

    /**
     * 添加数据至数据库
     * @param context
     * @param book
     */
    fun insertData(context: Context?, chapter: Chapter) {
        chapter.id = UUID.randomUUID().toString()
        DbManager.getInstance(context!!)?.getDaoSession(context)?.chapterDao?.insert(chapter)
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；
     * @param context
     * @param book
     */
    fun saveData(context: Context?, chapter: Chapter) {
        DbManager.getInstance(context!!)?.getDaoSession(context)?.chapterDao?.save(chapter)
    }

    /**
     * 删除数据至数据库
     * @param context
     * @param book 删除具体内容
     */
    fun deleteData(context: Context?, chapter: Chapter) {
        DbManager.getInstance(context!!)?.getDaoSession(context)?.chapterDao?.delete(chapter)
    }

    /**
     * 根据id删除数据
     */
    fun deleteData(context: Context?, id: String) {
        DbManager.getInstance(context!!)?.getDaoSession(context)?.chapterDao?.deleteByKey(id)
    }

    /**
     * 根据id删除数据
     */
    fun deleteData(context: Context?, chapters: MutableList<Chapter>) {
        DbManager.getInstance(context!!)?.getDaoSession(context)?.chapterDao?.deleteInTx(chapters)
    }

    /**
     * 删除全部数据
     * @param context
     */
    fun deleteAllData(context: Context?) {
        DbManager.getInstance(context!!)?.getDaoSession(context)?.chapterDao?.deleteAll()
    }

    /**
     * 更新数据库
     * @param context
     * @param book
     */
    fun updateData(context: Context?, chapter: Chapter) {
        DbManager.getInstance(context!!)?.getDaoSession(context)?.chapterDao?.update(chapter)
    }

    fun queryTitle(context: Context?, title: String, bookId: String): Chapter {
        val builder = DbManager.getInstance(context!!)?.getDaoSession(context)?.chapterDao
            ?.queryBuilder()!!
            .where(ChapterDao.Properties.ChapterTitle.eq(title))
            .where(ChapterDao.Properties.BookId.eq(bookId))
        return builder?.build()!!.unique()
    }

    fun queryId(context: Context?, bookId: String): MutableList<Chapter> {
        val builder = DbManager.getInstance(context!!)?.getDaoSession(context)?.chapterDao
            ?.queryBuilder()!!.where(ChapterDao.Properties.BookId.eq(bookId))
        return builder?.build()!!.list()
    }


    /**
     * 查询所有数据
     * @param context
     * @return
     */
    fun queryAll(context: Context?, bookId: String): MutableList<Chapter>? {
        val builder = DbManager.getInstance(context!!)?.getDaoSession(context)?.chapterDao
            ?.queryBuilder()!!.where(ChapterDao.Properties.BookId.eq(bookId))
        return builder?.build()?.list()
    }
}