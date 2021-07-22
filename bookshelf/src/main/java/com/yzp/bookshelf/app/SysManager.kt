package com.yzp.bookshelf.app

import android.content.Context
import com.yzp.bookshelf.R
import com.yzp.bookshelf.bean.Setting
import com.yzp.bookshelf.common.APPCONST
import com.yzp.bookshelf.enums.Font
import com.yzp.bookshelf.enums.Language
import com.yzp.bookshelf.enums.ReadStyle
import com.yzp.common.utils.CacheHelper

object SysManager {

    /**
     * 获取设置
     * @return
     */
    fun getSetting(context: Context): Setting? {
        var setting: Setting? = null
        CacheHelper.readObject(context, APPCONST.FILE_NAME_SETTING)?.let {
            setting = it as Setting
        }
        if (setting == null) {
            setting = getDefaultSetting()
            saveSetting(context, setting!!)
        }
        return setting
    }

    /**
     * 保存设置
     * @param setting
     */
    fun saveSetting(context: Context, setting: Setting) {
        CacheHelper.saveObject(context, setting, APPCONST.FILE_NAME_SETTING)
    }

    /**
     * 默认设置
     * @return
     */
    private fun getDefaultSetting(): Setting {
        return Setting(
            R.color.sys_protect_eye_word,
            R.color.sys_protect_eye_bg,
            20f,
            ReadStyle.protectedEye,
            true,
            50,
            true,
            Language.simplified,
            Font.默认字体, 50
        )
    }
}