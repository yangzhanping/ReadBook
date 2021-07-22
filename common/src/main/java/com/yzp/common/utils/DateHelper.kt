package com.yzp.common.utils

import android.annotation.SuppressLint
import java.sql.Timestamp
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    @SuppressLint("SimpleDateFormat")
    val yyyymmddhhmm = SimpleDateFormat("yyyy-MM-dd HH:mm")

    /**
     * @param
     * @return
     * ==>1471941967364
     * 2147483647
     */
    fun getLongDate(): Long {
        return System.currentTimeMillis()
    }

    /**
     *
     * @param date==>2016/05/02 09:10:46
     * @return ==>2016/05/02 09:10
     */
    fun formatDate_3(date: String): String? {
        var res = date
        res = res.substring(0, res.length - 3)
        return res
    }

    /**
     * 截断字符串末尾num个字符
     */
    fun formatDateByTailNum(date: String, num: Int): String? {
        var res = date
        res = res.substring(0, res.length - num)
        return res
    }

    /**
     *
     * @param date==>2016/05/02 09:10:46
     * @return ==>2016/05/02
     */
    fun formatDate3(date: String): String? {
        var res = date
        res = res.substring(0, res.length - 9)
        return res
    }


    fun getStrLongDate(): String? {
        return getLongDate().toString()
    }

    fun longToDate(longDate: Long): Date? {
        return Date(longDate)
    }

    fun strLongToDate(strLongDate: String): Date? {
        var date: Date? = null
        date = try {
            Date(strLongDate.toLong())
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return date
    }

    /**
     *
     * @param longDate ==>1471941967364
     * @return ==>2016/05/02 09:10:46
     */
    fun longToTime(longDate: Long): String? {
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return df.format(longToDate(longDate))
    }

    /**
     *
     * @param longDate ==>1471941967364
     * @return ==>2016/05/02 09:10
     */
    fun longToTime2(longDate: Long): String? {
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return df.format(longToDate(longDate))
    }

    /**
     *
     * @param longDate ==>1471941967364
     * @return ==>2016/05/02
     */
    fun longToTime3(longDate: Long): String? {
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        return df.format(longToDate(longDate))
    }

    /**
     *
     * @param longDate ==>1471941967364
     * @return ==>09:10
     */
    fun longToDayTime(longDate: Long): String? {
        val df: DateFormat = SimpleDateFormat("HH:mm")
        return df.format(longToDate(longDate))
    }

    /**
     *
     * @param strLongDate  ==>1471941967364
     * @return   ==>2016/05/02 09:10:46
     */
    fun strLongToTime(strLongDate: String): String? {
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return df.format(strLongToDate(strLongDate))
    }

    /**
     *
     * @param longDate ==>1471941967364
     * @return ==>2016年05月02日 09:10
     */
    fun strLongToScheduleTime(longDate: Long): String? {
        val df: DateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm")
        return df.format(longToDate(longDate))
    }


    /**
     * @param
     * @return
     * ==>Wed May 02 09:10:46 CST 2012
     */
    fun getTime1(): String? {
        val date1 = Date()
        return date1.toString()
    }

    /**
     * @param
     * @return
     * ==>2012-05-02
     */
    fun getYearMonthDay1(): String? {
        val date2 = java.sql.Date(System.currentTimeMillis())
        return date2.toString()
    }

    /**
     * @param
     * @return
     * ==>2012-05-02
     */
    fun getYearMonthDay2(): String? {
        val date1 = Date()
        val date3 = java.sql.Date(date1.time)
        return date3.toString()
    }

    /**
     * @param
     * @return
     * ==>2012-05-02 09:10:46.436
     */
    fun getSeconds1(): String? {
        val stamp1 = Timestamp(System.currentTimeMillis())
        return stamp1.toString()
    }

    /**
     * @param
     * @return
     * ==>2012-05-02 09:10:46.436
     */
    fun getSeconds2(): String? {
        val date1 = Date()
        val stamp2 = Timestamp(date1.time)
        return stamp2.toString()
    }

    /**
     * @param
     * @return
     * ==>2012/05/02 09:10:46
     */
    fun getYear_Second1(): String? {
        val stamp1 = Timestamp(System.currentTimeMillis())
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sdf.format(stamp1)
    }

    /**
     * @param
     * @return
     * ==>2012/05/02 09:10:46
     */
    fun getYear_Second2(): String? {
        val date1 = Date()
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return df.format(date1)
    }

    /**
     * �
     * @param str
     * ==>yyyy-MM-dd HH:mm:ss
     * @param
     * @return
     * ==>Thu Dec 10 05:12:02 CST 2009
     */
    fun changeStringToDate1(str: String?): Date? {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date: Date? = null
        return try {
            date = sdf.parse(str)
            date
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    /**
     *
     *
     * @param str
     * ==>yyyy-MM-dd HH:mm:ss
     * @param
     * @return
     * ==>2009-12-10
     */
    fun changeStringToDate2(str: String?): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date6: Date? = null
        return try {
            date6 = sdf.parse(str)
            val date7 = java.sql.Date(date6.time)
            date7.toString()
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     *
     *
     * @param str
     * ==>yyyy-MM-dd HH:mm:ss
     * @param
     * @return
     * ==>2009-12-10 05:12:02.0
     */
    fun changeStringToDate3(str: String?): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date6: Date? = null
        return try {
            date6 = sdf.parse(str)
            val date7 = java.sql.Date(date6.time)
            val stamp9 = Timestamp(date7.time)
            stamp9.toString()
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     *
     * @param str ==>2016/05/02 09:10:46
     * @return 1471941967364
     */
    fun strDateToLong(str: String?): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date6: Date? = null
        return try {
            date6 = sdf.parse(str)
            date6.time
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }
}