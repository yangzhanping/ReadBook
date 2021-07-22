package com.yzp.common.db.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Book implements Parcelable {

    @Id
    private String id;
    private String bookName;    //书名
    private String chapterUrl;  //书目Url
    private String coverUrl;    //封面图片url
    private String desc;        //简介
    private String author;      //作者
    @Nullable
    private String type;        //类型
    private String updateDate;  //更新时间
    @Nullable
    private String newestChapterId;     //最新章节id
    @Nullable
    private String newestChapterTitle;  //最新章节标题
    @Nullable
    private String newestChapterUrl;    //最新章节url
    @Nullable
    private String historyChapterId;    //上次关闭时的章节ID
    @Nullable
    private int historyChapterNum;      //上次关闭时的章节数
    private int sortCode;               //排序编码
    private int noReadNum;              //未读章数量
    private int chapterTotalNum;        //总章节数
    private int lastReadPosition;       //上次阅读到的章节的位置
    @Nullable
    private String source;              //书籍来源

    @Generated(hash = 1834694140)
    public Book(String id, String bookName, String chapterUrl, String coverUrl,
                String desc, String author, String type, String updateDate,
                String newestChapterId, String newestChapterTitle,
                String newestChapterUrl, String historyChapterId, int historyChapterNum,
                int sortCode, int noReadNum, int chapterTotalNum, int lastReadPosition,
                String source) {
        this.id = id;
        this.bookName = bookName;
        this.chapterUrl = chapterUrl;
        this.coverUrl = coverUrl;
        this.desc = desc;
        this.author = author;
        this.type = type;
        this.updateDate = updateDate;
        this.newestChapterId = newestChapterId;
        this.newestChapterTitle = newestChapterTitle;
        this.newestChapterUrl = newestChapterUrl;
        this.historyChapterId = historyChapterId;
        this.historyChapterNum = historyChapterNum;
        this.sortCode = sortCode;
        this.noReadNum = noReadNum;
        this.chapterTotalNum = chapterTotalNum;
        this.lastReadPosition = lastReadPosition;
        this.source = source;
    }

    @Generated(hash = 1839243756)
    public Book() {
    }

    protected Book(Parcel in) {
        id = in.readString();
        bookName = in.readString();
        chapterUrl = in.readString();
        coverUrl = in.readString();
        desc = in.readString();
        author = in.readString();
        type = in.readString();
        updateDate = in.readString();
        newestChapterId = in.readString();
        newestChapterTitle = in.readString();
        newestChapterUrl = in.readString();
        historyChapterId = in.readString();
        historyChapterNum = in.readInt();
        sortCode = in.readInt();
        noReadNum = in.readInt();
        chapterTotalNum = in.readInt();
        lastReadPosition = in.readInt();
        source = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(bookName);
        dest.writeString(chapterUrl);
        dest.writeString(coverUrl);
        dest.writeString(desc);
        dest.writeString(author);
        dest.writeString(type);
        dest.writeString(updateDate);
        dest.writeString(newestChapterId);
        dest.writeString(newestChapterTitle);
        dest.writeString(newestChapterUrl);
        dest.writeString(historyChapterId);
        dest.writeInt(historyChapterNum);
        dest.writeInt(sortCode);
        dest.writeInt(noReadNum);
        dest.writeInt(chapterTotalNum);
        dest.writeInt(lastReadPosition);
        dest.writeString(source);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookName() {
        return this.bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getChapterUrl() {
        return this.chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getNewestChapterId() {
        return this.newestChapterId;
    }

    public void setNewestChapterId(String newestChapterId) {
        this.newestChapterId = newestChapterId;
    }

    public String getNewestChapterTitle() {
        return this.newestChapterTitle;
    }

    public void setNewestChapterTitle(String newestChapterTitle) {
        this.newestChapterTitle = newestChapterTitle;
    }

    public String getNewestChapterUrl() {
        return this.newestChapterUrl;
    }

    public void setNewestChapterUrl(String newestChapterUrl) {
        this.newestChapterUrl = newestChapterUrl;
    }

    public String getHistoryChapterId() {
        return this.historyChapterId;
    }

    public void setHistoryChapterId(String historyChapterId) {
        this.historyChapterId = historyChapterId;
    }

    public int getHistoryChapterNum() {
        return this.historyChapterNum;
    }

    public void setHistoryChapterNum(int historyChapterNum) {
        this.historyChapterNum = historyChapterNum;
    }

    public int getSortCode() {
        return this.sortCode;
    }

    public void setSortCode(int sortCode) {
        this.sortCode = sortCode;
    }

    public int getNoReadNum() {
        return this.noReadNum;
    }

    public void setNoReadNum(int noReadNum) {
        this.noReadNum = noReadNum;
    }

    public int getChapterTotalNum() {
        return this.chapterTotalNum;
    }

    public void setChapterTotalNum(int chapterTotalNum) {
        this.chapterTotalNum = chapterTotalNum;
    }

    public int getLastReadPosition() {
        return this.lastReadPosition;
    }

    public void setLastReadPosition(int lastReadPosition) {
        this.lastReadPosition = lastReadPosition;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
