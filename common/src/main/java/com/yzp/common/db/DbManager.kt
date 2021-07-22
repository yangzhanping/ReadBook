package com.yzp.common.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.yzp.common.db.dao.DaoMaster
import com.yzp.common.db.dao.DaoSession

class DbManager private constructor(mContext: Context) {

    private val DB_NAME = "book.db"
    private var mDevOpenHelper: DaoMaster.DevOpenHelper? = null
    private var mDaoMaster: DaoMaster? = null
    private var mDaoSession: DaoSession? = null

    init {
        // 初始化数据库信息
        mDevOpenHelper = DaoMaster.DevOpenHelper(mContext, DB_NAME)
        getDaoMaster(mContext)
        getDaoSession(mContext)
    }

    companion object {
        @Volatile
        var instance: DbManager? = null

        fun getInstance(mContext: Context): DbManager? {
            if (instance == null) {
                synchronized(DbManager::class) {
                    if (instance == null) {
                        instance = DbManager(mContext)
                    }
                }
            }
            return instance
        }
    }

    fun getDaoMaster(context: Context): DaoMaster? {
        if (null == mDaoMaster) {
            synchronized(DbManager::class.java) {
                if (null == mDaoMaster) {
                    mDaoMaster = DaoMaster(getWritableDatabase(context))
                }
            }
        }
        return mDaoMaster
    }

    fun getDaoSession(context: Context): DaoSession? {
        if (null == mDaoSession) {
            synchronized(DbManager::class.java) {
                mDaoSession = getDaoMaster(context)?.newSession()
            }
        }
        return mDaoSession
    }

    fun getWritableDatabase(context: Context): SQLiteDatabase? {
        if (null == mDevOpenHelper) {
            getInstance(context)
        }
        return mDevOpenHelper?.getWritableDatabase()
    }

    fun getReadableDatabase(context: Context): SQLiteDatabase? {
        if (null == mDevOpenHelper) {
            getInstance(context)
        }
        return mDevOpenHelper?.getReadableDatabase()
    }
}