package com.yzp.common.db

import android.content.Context
import com.yzp.common.db.bean.SearchHistory
import com.yzp.common.db.dao.SearchHistoryDao

class SearchHistoryDaoOpe private constructor() {

    private object mHolder {
        val instance = SearchHistoryDaoOpe()
    }

    companion object {
        fun getInstance(): SearchHistoryDaoOpe {
            return mHolder.instance
        }
    }

    /**
     * 添加数据至数据库
     * @param context
     * @param searchHistory
     */
    fun insertData(context: Context, searchHistory: SearchHistory) {
        DbManager.getInstance(context)!!.getDaoSession(context)?.searchHistoryDao!!.insert(
            searchHistory
        )
    }

    /**
     *添加数据至数据库，如果存在，将原来的数据覆盖
     */
    fun saveData(context: Context, searchHistory: SearchHistory) {
        DbManager.getInstance(context)!!.getDaoSession(context)?.searchHistoryDao!!.save(
            searchHistory
        )
    }

    /**
     * 删除数据至数据库
     * @param context
     * @param searchHistory 删除具体内容
     */
    fun deleteData(context: Context, searchHistory: SearchHistory) {
        DbManager.getInstance(context!!)?.getDaoSession(context)?.searchHistoryDao?.delete(
            searchHistory
        )
    }

    /**
     * 删除全部数据
     * @param context
     */
    fun deleteAllData(context: Context?) {
        DbManager.getInstance(context!!)?.getDaoSession(context)?.searchHistoryDao?.deleteAll()
    }

    /**
     * 更新数据库
     * @param context
     * @param searchHistory
     */
    fun updateData(context: Context?, searchHistory: SearchHistory) {
        DbManager.getInstance(context!!)?.getDaoSession(context)?.searchHistoryDao?.update(
            searchHistory
        )
    }

    /**
     * 按照内容查找
     */
    fun queryKey(context: Context, keyWord: String): SearchHistory? {
        val builder = DbManager.getInstance(context!!)?.getDaoSession(context)?.searchHistoryDao
            ?.queryBuilder()!!.where(SearchHistoryDao.Properties.Content.eq(keyWord))
        return builder?.build()?.unique()
    }

    /**
     * 查询所有数据
     * @param context
     * @return
     */
    fun queryAll(context: Context?): MutableList<SearchHistory>? {
        val builder = DbManager.getInstance(context!!)?.getDaoSession(context)?.searchHistoryDao
            ?.queryBuilder()!!.orderDesc(SearchHistoryDao.Properties.CreateDate)
        return builder?.build()?.list()
    }
}