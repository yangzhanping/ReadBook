package com.yzp.common.xml

import android.text.Html
import com.yzp.common.bean.BookType
import com.yzp.common.constant.BookSource
import com.yzp.common.db.bean.Book
import com.yzp.common.utils.StringHelper
import org.jsoup.Jsoup
import java.util.*
import java.util.regex.Pattern

object BiQuGeReadUtil {

    /**
     * 获取书城小说分类列表
     * @param html
     * @return
     */
    fun getBookTypeList(html: String?): List<BookType>? {
        val bookTypes: MutableList<BookType> = ArrayList<BookType>()
        val doc = Jsoup.parse(html)
        val divs = doc.getElementsByClass("nav")
        if (divs.size > 0) {
            val uls = divs[0].getElementsByTag("ul")
            if (uls.size > 0) {
                for (li in uls[0].children()) {
                    val a = li.child(0)
                    val bookType = BookType()
                    bookType.setTypeName(a.attr("title"))
                    bookType.setUrl(a.attr("href"))
                    if (!bookType.getTypeName()!!.contains("小说") || bookType.getTypeName()!!
                            .contains("排行")
                    ) continue
                    if (StringHelper.isNotEmpty(bookType.getTypeName())) {
                        bookTypes.add(bookType)
                    }
                }
            }
        }
        return bookTypes
    }

    /**
     * 获取某一分类小说排行榜列表
     * @param html
     * @return
     */
    fun getBookRankList(html: String?): List<Book>? {
        val books: MutableList<Book> = ArrayList<Book>()
        val doc = Jsoup.parse(html)
        val divs = doc.getElementsByClass("r")
        if (divs.size > 0) {
            val uls = divs[0].getElementsByTag("ul")
            if (uls.size > 0) {
                for (li in uls[0].children()) {
                    val book = Book()
                    val scanS1 = li.getElementsByClass("s1")[0]
                    val scanS2 = li.getElementsByClass("s2")[0]
                    val scanS5 = li.getElementsByClass("s5")[0]
                    book.type = scanS1.html().replace("[", "").replace("]", "")
                    val a = scanS2.getElementsByTag("a")[0]
                    book.bookName = a.attr("title")
                    book.chapterUrl = a.attr("href")
                    book.author = scanS5.html()
                    book.source = BookSource.biquge.toString()
                    books.add(book)
                }
            }
        }
        return books
    }

    /**
     * 获取小说详细信息
     * @param html
     * @return
     */
    fun getBookInfo(html: String?, book: Book): Book? {
        //小说源
        book.setSource(BookSource.biquge.toString())
        val doc = Jsoup.parse(html)

        /* <meta property="og:novel:read_url" content="https://www.52bqg.net/book_113099/">*/
        val meta = doc.getElementsByAttributeValue("property", "og:novel:read_url")[0]
        book.chapterUrl = meta.attr("content")


        //图片url
        val divImg = doc.getElementById("fmimg")
        val img = divImg.getElementsByTag("img")[0]
        book.coverUrl = img.attr("src")
        val divInfo = doc.getElementById("info")

        //书名
        val h1 = divInfo.getElementsByTag("h1")[0]
        book.bookName = h1.html()
        val ps = divInfo.getElementsByTag("p")

        //作者
        val p0 = ps[0]
        var a = p0.getElementsByTag("a")[0]
        book.author = a.html()

        //更新时间
        val p2 = ps[2]
        val pattern = Pattern.compile("更新时间：(.*)&nbsp;")
        val matcher = pattern.matcher(p2.html())
        if (matcher.find()) {
            book.updateDate = matcher.group(1)
        }

        //最新章节
        val p3 = ps[3]
        a = p3.getElementsByTag("a")[0]
        book.newestChapterTitle = a.attr("title")
        book.newestChapterUrl = book.getChapterUrl().toString() + a.attr("href")
        //简介
        val divIntro = doc.getElementById("intro")
        book.setDesc(Html.fromHtml(divIntro.html()).toString())
        return book
    }


    /**
     * 从搜索html中得到书列表
     *
     * @param html
     * @return
     */
    fun getBooksFromSearchHtml(html: String?): ArrayList<Book>? {
        val books: ArrayList<Book> = ArrayList<Book>()
        val doc = Jsoup.parse(html)
        val divs = doc.getElementsByClass("novelslistss")
        if (divs.size != 0) {
            val div = divs[0]
            val lis = div.getElementsByTag("li")
            for (li in lis) {
                val book = Book()
                val s2a = li.getElementsByClass("s2")[0].getElementsByTag("a")[0]
                book.chapterUrl = s2a.attr("href")
                book.bookName = s2a.text()
                val s4 = li.getElementsByClass("s4")[0]
                book.author = s4.text()
                val s1 = li.getElementsByClass("s1")[0]
                book.type = s1.text()
                val s5 = li.getElementsByClass("s5")[0]
                book.updateDate = s5.text()
                val s3a = li.getElementsByClass("s3")[0].getElementsByTag("a")[0]
                book.newestChapterUrl = s3a.attr("href")
                book.newestChapterTitle = s3a.text()
                book.source = BookSource.biquge.toString()
                books.add(book)
            }
        } else {
            val book = Book()
            getBookInfo(html, book)
            books.add(book)
        }
        return books
    }
}