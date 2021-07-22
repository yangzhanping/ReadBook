package com.yzp.common.db.bean;

import androidx.annotation.Nullable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 章节
 */
@Entity
public class Chapter {

    @Id
    private String id;
    private String bookId;         //章节所属书的ID
    private int chapterNumber;     //章节序号
    private String chapterTitle;   //章节标题
    private String chapterUrl;     //章节链接
    @Nullable
    private String chapterContent; //章节正文
    @Generated(hash = 1833955310)
    public Chapter(String id, String bookId, int chapterNumber, String chapterTitle,
            String chapterUrl, String chapterContent) {
        this.id = id;
        this.bookId = bookId;
        this.chapterNumber = chapterNumber;
        this.chapterTitle = chapterTitle;
        this.chapterUrl = chapterUrl;
        this.chapterContent = chapterContent;
    }
    @Generated(hash = 393170288)
    public Chapter() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBookId() {
        return this.bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public int getChapterNumber() {
        return this.chapterNumber;
    }
    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }
    public String getChapterTitle() {
        return this.chapterTitle;
    }
    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }
    public String getChapterUrl() {
        return this.chapterUrl;
    }
    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }
    public String getChapterContent() {
        return this.chapterContent;
    }
    public void setChapterContent(String chapterContent) {
        this.chapterContent = chapterContent;
    }
}
