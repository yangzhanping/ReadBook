package com.yzp.common.db

import android.content.Context
import com.orhanobut.logger.Logger
import com.yzp.common.db.bean.Book
import com.yzp.common.db.dao.BookDao
import java.util.*

class BookDaoOpe private constructor() {

    private object mHolder {
        val instance = BookDaoOpe()
    }

    companion object {
        fun getInstance(): BookDaoOpe {
            return mHolder.instance
        }
    }

    /**
     * 添加数据至数据库
     * @param context
     * @param book
     */
    fun insertData(context: Context?, book: Book) {
        book.id = UUID.randomUUID().toString()
        book.sortCode = countBookTotalNum(context!!).toInt() + 1
        DbManager.getInstance(context!!)?.getDaoSession(context)?.bookDao?.insert(book)
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；
     * @param context
     * @param book
     */
    fun saveData(context: Context?, book: Book) {
        DbManager.getInstance(context!!)?.getDaoSession(context)?.bookDao?.save(book)
    }

    /**
     * 删除数据至数据库
     * @param context
     * @param book 删除具体内容
     */
    fun deleteData(context: Context?, book: Book) {
        DbManager.getInstance(context!!)?.getDaoSession(context)?.bookDao?.delete(book)
    }

    /**
     * 根据id删除数据
     */
    fun deleteData(context: Context?, id: String) {
        DbManager.getInstance(context!!)?.getDaoSession(context)?.bookDao?.deleteByKey(id)
    }

    /**
     * 删除全部数据
     * @param context
     */
    fun deleteAllData(context: Context?) {
        DbManager.getInstance(context!!)?.getDaoSession(context)?.bookDao?.deleteAll()
    }

    /**
     * 更新数据库
     * @param context
     * @param book
     */
    fun updateData(context: Context?, book: Book) {
        DbManager.getInstance(context!!)?.getDaoSession(context)?.bookDao?.update(book)
    }

    /**
     * 查询所有数据
     * @param context
     * @return
     */
    fun queryAll(context: Context?): MutableList<Book>? {
        val builder = DbManager.getInstance(context!!)?.getDaoSession(context)?.bookDao
            ?.queryBuilder()!!.orderDesc(BookDao.Properties.SortCode)
        return builder?.build()?.list()
    }

    /**
     *
     */
    fun query(context: Context?, book: Book): Book? {
        val builder =
            DbManager.getInstance(context!!)?.getDaoSession(context)?.bookDao?.queryBuilder()!!
                .where(
                    BookDao.Properties.BookName.eq(book.bookName),
                    BookDao.Properties.Author.eq(book.author),
                    BookDao.Properties.Source.eq(book.source)
                )
        return builder?.build()?.unique()
    }

    fun countBookTotalNum(context: Context): Long {
        return DbManager.getInstance(context!!)?.getDaoSession(context)?.bookDao
            ?.queryBuilder()!!.count()
    }
}