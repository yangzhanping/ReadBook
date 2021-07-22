package com.yzp.common.utils

import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object StringHelper {

    /**
     * 是否是Emoji表情符
     * @param string
     * @return
     */
    fun isEmoji(string: String?): Boolean {
        val p: Pattern = Pattern.compile(
            "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE
        )
        val m: Matcher = p.matcher(string)
        return m.find()
    }

    /**
     * 字符集编码
     * @param encoded
     * @return
     */
    fun encode(encoded: String?): String? {
        var res = encoded
        try {
            res = URLEncoder.encode(encoded, "UTF-8")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return res
    }

    /**
     * 字符集编码
     * @param encoded
     * @return
     */
    fun encode(encoded: String?, charsetName: String?): String? {
        var res = encoded
        try {
            res = URLEncoder.encode(encoded, charsetName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return res
    }

    /**
     * 字符集解码
     * @param decoded
     * @return
     */
    fun decode(decoded: String?, charsetName: String?): String? {
        var res = decoded
        try {
            res = URLDecoder.decode(decoded, charsetName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return res
    }

    /**
     * 字符集解码
     * @param decoded
     * @return
     */
    fun decode(decoded: String?): String? {
        var res = decoded
        try {
            res = URLDecoder.decode(decoded, "UTF-8")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return res
    }

    //生成随机数字和字母,
    fun getStringRandom(length: Int): String? {
        var str = ""
        val random = Random()
        //参数length，表示生成几位随机数
        for (i in 0 until length) {
            val charOrNum = if (random.nextInt(2) % 2 == 0) "char" else "num"
            //输出字母还是数字
            if ("char".equals(charOrNum, ignoreCase = true)) {
                //输出是大写字母还是小写字母
                val temp = if (random.nextInt(2) % 2 == 0) 65 else 97
                str += (random.nextInt(26) + temp).toString()
            } else if ("num".equals(charOrNum, ignoreCase = true)) {
                str += java.lang.String.valueOf(random.nextInt(10))
            }
        }
        return str
    }

    fun jidToUsername(jid: String?): String? {
        return if (jid != null) {
            if (jid.contains("@")) {
                jid.substring(0, jid.indexOf("@"))
            } else {
                jid
            }
        } else ""
    }

    fun isEmpty(str: String?): Boolean {
        var str = str
        if (str != null) {
            str = str.replace(" ", "")
        }
        return str == null || str == ""
    }

    fun isNotEmpty(str: String?): Boolean {
        return !isEmpty(str)
    }

    /**
     * 缩减字符串
     * @param strlocation
     * @param maxLength
     * @return
     */
    fun reduceString(strlocation: String?, maxLength: Int): String? {
        return if (strlocation != null) {
            var res: String = strlocation
            if (strlocation.length > maxLength) {
                val tem = res.toCharArray()
                res = String(tem, 0, maxLength)
                res += "..."
            }
            res
        } else {
            null
        }
    }

    /**
     * 两字符串是否相等或者都为空
     * @param str1
     * @param str2
     * @return
     */
    fun isEquals(str1: String, str2: String): Boolean {
        return if (isEmpty(str1) && isEmpty(str2)) {
            true
        } else !isEmpty(str1) && !isEmpty(str2) && str1 == str2
    }

    fun formatText(text: String?): String? {
        return if (isEmpty(text)) "" else text
    }
}