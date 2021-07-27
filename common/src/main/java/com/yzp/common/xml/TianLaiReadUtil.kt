package com.yzp.common.xml

import android.text.Html
import com.yzp.common.constant.BookSource
import com.yzp.common.constant.UrlConstant
import com.yzp.common.db.bean.Book
import com.yzp.common.db.bean.Chapter
import com.yzp.common.utils.StringHelper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

object TianLaiReadUtil {

    fun getNewChapter(html: String, book: Book): Book {
        val doc: Document = Jsoup.parse(html)
        val head: Element = doc.head()
        val updateTime = head.select("meta[property=og:novel:update_time]").attr("content")
        val newChapterTitle =
            head.select("meta[property=og:novel:latest_chapter_name]").attr("content")
        val newChapterUrl =
            head.select("meta[property=og:novel:latest_chapter_url]").attr("content")

        book.newestChapterTitle = newChapterTitle
        book.newestChapterUrl = newChapterUrl
        book.updateDate = updateTime
        return book
    }

    /**
     *  章节解析
     */
    fun getChapters(html: String, book: Book): MutableList<Chapter> {
        val chapters = mutableListOf<Chapter>()
        val doc: Document = Jsoup.parse(html)
        val divList: Element = doc.getElementById("list")
        val dl: Element = divList.getElementsByTag("dl")[0]

        var lastTile: String? = null
        var index = 0
        var dtFirst = 1

        for (dd in dl.allElements) {
            if (dd.tagName().equals("dt")) {
                dtFirst++
                continue
            }
            if (dtFirst > 2) {
                val tagA: Elements = dd.getElementsByTag("a")
                if (tagA.size > 0) {
                    val a: Element = tagA[0]
                    val title: String = a.html()
                    if (!StringHelper.isEmpty(lastTile) && title == lastTile) {
                        continue
                    }
                    var chapter = Chapter()
                    chapter.chapterNumber = index++
                    chapter.chapterTitle = title
                    var url: String = a.attr("href")
                    if (StringHelper.isEmpty(book.source) || BookSource.tianlai.toString()
                            .equals(book.source)
                    ) {
                        url = UrlConstant.nameSpace_tianlai.toString() + url
                    } else if (BookSource.biquge.toString().equals(book.source)) {
                        url = book.getChapterUrl().toString() + url
                    }
                    chapter.chapterUrl = url
                    chapters.add(chapter)
                    lastTile = title
                }
            }
        }
        return chapters
    }

    /**
     * 从html中获取章节正文
     *
     * @param html
     * @return
     */
    fun getContent(html: String): String {
        val doc = Jsoup.parse(html)
        val divContent = doc.getElementById("content")
        return if (divContent != null) {
            var content = Html.fromHtml(divContent.html()).toString()
            val c = 160.toChar()
            val spaec = "" + c
            content = content.replace(spaec, "  ")
            content
        } else {
            ""
        }
    }

    /**
     * 从搜索html中得到书列表
     * @param html
     * @return
     */
    fun getBooks(html: String?): MutableList<Book>? {
        val books = mutableListOf<Book>()
        val doc = Jsoup.parse(html)
        val div = doc.getElementsByClass("details").first()
        for (element in div.getElementsByClass("item-pic")) {
            val book = Book()
            val img = element.getElementsByTag("img").first()
            book.coverUrl = UrlConstant.nameSpace_tianlai + img.attr("src")
            val info = element.getElementsByClass("result-game-item-detail").first()
            for (el in info.children()) {
                var infoStr = el.text()
                if (el.tagName() == "h3") {
                    val a = el.getElementsByTag("a").first()
                    book.chapterUrl = UrlConstant.nameSpace_tianlai.toString() + a.attr("href")
                    book.bookName = a.text()
                } else if (el.className() == "intro") {
                    book.desc = el.text()
                } else if (infoStr.contains("作者：")) {
                    infoStr = infoStr.substring(0, infoStr.indexOf("状态"))
                    book.author = infoStr.replace("作者：", "").replace(" ", "")
                } else if (infoStr.contains("类型：")) {
                    book.type = infoStr.replace("类型：", "").replace(" ", "")
                } else if (infoStr.contains("更新时间：")) {
                    book.updateDate = infoStr.replace("更新时间：", "").replace(" ", "") + "00:00:00"
                } else if (infoStr.contains("最新章节")) {
                    val newChapter = el.getElementsByTag("a").first()
                    book.newestChapterUrl =
                        UrlConstant.nameSpace_tianlai.toString() + newChapter.attr("href")
                    book.newestChapterTitle = newChapter.text()
                }
            }
            book.source = BookSource.tianlai.toString()
            books.add(book)
        }
        return books
    }
}