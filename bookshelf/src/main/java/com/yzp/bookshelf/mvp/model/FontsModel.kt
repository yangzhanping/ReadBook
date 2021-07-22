package com.yzp.bookshelf.mvp.model

import com.yzp.bookshelf.enums.Font

class FontsModel {

    fun requestData(): MutableList<Font> {
        val mFonts = mutableListOf<Font>()
        mFonts.add(Font.默认字体)
        mFonts.add(Font.方正楷体)
        mFonts.add(Font.经典宋体)
        mFonts.add(Font.方正行楷)
        mFonts.add(Font.迷你隶书)
        mFonts.add(Font.方正黄草)
        mFonts.add(Font.书体安景臣钢笔行书)
        return mFonts
    }
}