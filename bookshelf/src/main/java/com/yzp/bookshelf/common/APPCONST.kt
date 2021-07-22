package com.yzp.bookshelf.common

import android.os.Environment
import com.yzp.bookshelf.R

object APPCONST {

    //服务端公钥
    var publicKey: String? = null

    //app私钥
    var privateKey: String? = null
    val s = "11940364935628058505"

    val ALARM_SCHEDULE_MSG = "alarm_schedule_msg"

    val FILE_DIR = "MissZzzReader"
    val TEM_FILE_DIR =
        Environment.getExternalStorageDirectory().toString() + "/MissZzzReader/tem/"
    val UPDATE_APK_FILE_DIR = "MissZzzReader/apk/"
    var exitTime: Long = 0
    val exitConfirmTime = 2000

    val BOOK = "book"
    val FONT = "font"

    val READ_STYLE_NIGHT = intArrayOf(R.color.sys_night_word, R.color.sys_night_bg) //黑夜

    val READ_STYLE_PROTECTED_EYE =
        intArrayOf(R.color.sys_protect_eye_word, R.color.sys_protect_eye_bg) //护眼

    val READ_STYLE_COMMON =
        intArrayOf(R.color.sys_common_word, R.color.sys_common_bg) //普通

    val READ_STYLE_BLUE_DEEP =
        intArrayOf(R.color.sys_blue_deep_word, R.color.sys_blue_deep_bg) //深蓝

    val READ_STYLE_LEATHER =
        intArrayOf(R.color.sys_leather_word, R.mipmap.shelf_theme_leather_bg) //羊皮纸

    val READ_STYLE_BREEN_EYE =
        intArrayOf(R.color.sys_breen_word, R.color.sys_breen_bg) //棕绿色

    val FILE_NAME_SETTING = "setting"
    val FILE_NAME_UPDATE_INFO = "updateInfo"

    val REQUEST_FONT = 1001
}