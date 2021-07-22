package com.yzp.bookshelf.bean

import com.yzp.bookshelf.enums.Font
import com.yzp.bookshelf.enums.Language
import com.yzp.bookshelf.enums.ReadStyle
import java.io.Serializable

data class Setting(
    //阅读字体颜色1
    var readWordColor: Int,
    //阅读背景颜色
    var readBgColor: Int,
    //阅读字体大小
    var readWordSize: Float,
    //阅读模式
    var readStyle: ReadStyle?,
    //是否日间模式
    var dayStyle: Boolean,
    //亮度 1- 100
    var brightProgress: Int,
    //亮度跟随系统
    var brightFollowSystem: Boolean,
    //简繁体
    var language: Language?,
    //字体
    var font: Font?,
    //自动滑屏速度
    var autoScrollSpeed: Int
) : Serializable