package com.yzp.common.utils

import android.app.Activity
import android.provider.Settings
import android.view.WindowManager

object BrightUtil {

    /**
     * 获取屏幕的亮度
     * @param activity
     * @return
     */
    fun getScreenBrightness(activity: Activity): Int {
        var nowBrightnessValue = 0
        val resolver = activity.contentResolver
        try {
            nowBrightnessValue = Settings.System.getInt(
                resolver, Settings.System.SCREEN_BRIGHTNESS
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return nowBrightnessValue
    }

    /**
     * 设置亮度
     * @param activity
     * @param brightness
     */
    fun setBrightness(activity: Activity, brightness: Int) {
        val lp = activity.window.attributes
        lp.screenBrightness = java.lang.Float.valueOf(brightness.toFloat()) * (1f / 255f)
        activity.window.attributes = lp
    }

    fun brightToProgress(brightness: Int): Int {
        return (java.lang.Float.valueOf(brightness.toFloat()) * (1f / 255f) * 100).toInt()
    }

    fun progressToBright(progress: Int): Int {
        return progress * 255 / 100
    }

    /**
     * 亮度跟随系统
     * @param activity
     */
    fun followSystemBright(activity: Activity) {
        val lp = activity.window.attributes
        lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        activity.window.attributes = lp
    }
}