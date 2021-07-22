package com.yzp.common.utils

import android.content.Context
import android.os.Parcelable
import java.io.*
import java.util.*

object CacheHelper {

    val memCacheRegion: Hashtable<String, Any> = Hashtable()
    val CACHE_TIME = 60 * 60000
    var WRITING_OR_READING_FILE_NAME = ""

    /**
     * 读取对象（Serializable）
     * @param file
     * @return
     * @throws IOException
     */
    fun readObject(context: Context, file: String): Any? {
        if (!isExistDataCache(context, file)) return null
        while (WRITING_OR_READING_FILE_NAME == file) {
            try {
                Thread.sleep(100)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        WRITING_OR_READING_FILE_NAME = file
        var fis: FileInputStream? = null
        var ois: ObjectInputStream? = null
        try {
            fis = context.openFileInput(file)
            ois = ObjectInputStream(fis)
            return ois.readObject()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            //   return null;
        } catch (e: Exception) {
            e.printStackTrace()
            //反序列化失败 - 删除缓存文件
            if (e is InvalidClassException) {
                val data: File = context.getFileStreamPath(file)
                data.delete()
            }
            //   return null;
        } finally {
            try {
                ois!!.close()
                fis!!.close()
                WRITING_OR_READING_FILE_NAME = ""
            } catch (e: Exception) {
                e.printStackTrace()
                WRITING_OR_READING_FILE_NAME = ""
            }
        }
        return null
    }

    fun deleteFile(context: Context, file: String): Boolean {
        while (WRITING_OR_READING_FILE_NAME == file) {
            try {
                Thread.sleep(100)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        WRITING_OR_READING_FILE_NAME = file
        val flag: Boolean = context.deleteFile(file)
        WRITING_OR_READING_FILE_NAME = ""
        return flag
    }

    /**
     * 保存对象
     * @param ser
     * @param file
     * @throws IOException
     */
    fun saveObject(context: Context, ser: Serializable, file: String): Boolean {
        while (WRITING_OR_READING_FILE_NAME == file) {
            try {
                Thread.sleep(100)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        WRITING_OR_READING_FILE_NAME = file
        var fos: FileOutputStream? = null
        var oos: ObjectOutputStream? = null
        return try {
            fos = context.openFileOutput(file, Context.MODE_PRIVATE)
            oos = ObjectOutputStream(fos)
            oos.writeObject(ser)
            oos.flush()
            fos.flush()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            try {
                oos!!.close()
                fos!!.close()
                WRITING_OR_READING_FILE_NAME = ""
            } catch (e: Exception) {
                e.printStackTrace()
                WRITING_OR_READING_FILE_NAME = ""
            }
        }
    }


    /**
     * 判断缓存是否存在
     * @param cachefile
     * @return
     */
    private fun isExistDataCache(context: Context, cachefile: String): Boolean {
        var exist = false
        val data: File = context.getFileStreamPath(cachefile)
        if (data.exists()) exist = true
        return exist
    }
}
