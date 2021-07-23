package com.yzp.bookshelf.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.yzp.bookshelf.R
import com.yzp.bookshelf.app.SysManager
import com.yzp.bookshelf.common.APPCONST
import com.yzp.bookshelf.databinding.ShelfReadbookBinding
import com.yzp.bookshelf.enums.Language
import com.yzp.bookshelf.enums.ReadStyle
import com.yzp.bookshelf.mvp.contract.ReadBookContract
import com.yzp.bookshelf.mvp.presenter.ReadBookPresenter
import com.yzp.bookshelf.ui.adapter.ChapterAdapter
import com.yzp.bookshelf.ui.adapter.ContentAdapter
import com.yzp.bookshelf.view.DialogCreator
import com.yzp.bookshelf.view.DialogCreator.OnReadStyleChangeListener
import com.yzp.bookshelf.view.DialogCreator.createReadDetailSetting
import com.yzp.common.adapter.OnItemClickListener
import com.yzp.common.base.BaseActivity
import com.yzp.common.db.BookDaoOpe
import com.yzp.common.db.ChapterDaoOpe
import com.yzp.common.db.bean.Book
import com.yzp.common.db.bean.Chapter
import com.yzp.common.utils.DateHelper
import com.yzp.common.utils.StringHelper
import com.yzp.common.utils.ThreadManager
import java.util.*

@Route(path = "/bookshelf/ShelfReadBookActivity")
class ShelfReadBookActivity : BaseActivity(), ReadBookContract.View {

    lateinit var binding: ShelfReadbookBinding
    private var book: Book? = null
    private var presenter: ReadBookPresenter = ReadBookPresenter()

    private var chapterAdapter: ChapterAdapter? = null
    private var contentAdapter: ContentAdapter? = null
    private var mLinearLayoutManager: LinearLayoutManager? = null

    private var mSettingDialog: Dialog? = null
    private var mSettingDetailDialog: Dialog? = null

    private var isFirstInit: Boolean = true

    private var autoScrollOpening: Boolean = false

    private var mHandler = Handler()

    private var chapterList: MutableList<Chapter>? = null

    //上次点击时间
    private val lastOnClickTime: Long = 0

    //双击确认时间
    private val doubleOnClickConfirmTime: Long = 200

    private var currentY = 0f

    override fun initView() {
        binding = ShelfReadbookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.attachView(this)

        //init view
        initDrawer()
        initRecyclerView()
        initChapterSort()
        initViewSet(SysManager.getSetting(this)!!.dayStyle)
    }

    /**
     * 初始化目录排序功能
     */
    private fun initChapterSort() {
        binding.shelfIvSort.setOnClickListener {
            if (binding.shelfIvSort.tag.equals("降序")) {
                binding.shelfIvSort.setBackgroundResource(R.mipmap.shelf_ic_sort_asc)
                chapterList!!.reverse()
                if (chapterAdapter != null) {
                    chapterAdapter!!.setCurrentPosition(chapterList!!.size - book!!.historyChapterNum - 1)
                    binding.shelfLvChapterList.setSelectionFromTop(
                        chapterList!!.size - book!!.historyChapterNum - 1,
                        binding.shelfLvChapterList.height / 2
                    )
                }
                binding.shelfIvSort.tag = "升序"
            } else {
                binding.shelfIvSort.setBackgroundResource(R.mipmap.shelf_ic_sort_dsc)
                chapterList!!.reverse()
                if (chapterAdapter != null) {
                    chapterAdapter!!.setCurrentPosition(book!!.historyChapterNum)
                    binding.shelfLvChapterList.setSelectionFromTop(
                        book!!.historyChapterNum,
                        binding.shelfLvChapterList.height / 2
                    )
                }
                binding.shelfIvSort.tag = "降序"
            }
        }
    }

    /**
     * 抽屉布局-目录
     */
    private fun initDrawer() {
        binding.shelfIvBack.setOnClickListener {
            binding.shelfReadDl.closeDrawer(GravityCompat.START)
        }
        binding.shelfReadDl.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerClosed(drawerView: View) {
                binding.shelfReadDl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            override fun onDrawerOpened(drawerView: View) {
                binding.shelfReadDl.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        })
    }

    /**
     * 小说内容
     */
    private fun initRecyclerView() {
        //设置布局管理器
        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager!!.orientation = LinearLayoutManager.VERTICAL
        binding.shelfRvContent.layoutManager = mLinearLayoutManager
        //
        binding.shelfRvContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!isFirstInit) {
                    //保存历史位置
                    if (mLinearLayoutManager!!.findFirstVisibleItemPosition() != mLinearLayoutManager!!.findLastVisibleItemPosition()
                        || dy == 0
                    ) {
                        book!!.lastReadPosition = 0
                    } else {
                        book!!.lastReadPosition = book!!.lastReadPosition + dy
                    }
                    book!!.historyChapterNum = mLinearLayoutManager!!.findLastVisibleItemPosition()
                    if (!StringHelper.isEmpty(book!!.id)) {
                        book?.let {
                            BookDaoOpe.getInstance().saveData(this@ShelfReadBookActivity, it)
                        }
                    }
                    //更新章节列表选中位置
                    if (chapterAdapter != null) {
                        chapterAdapter!!.setCurrentPosition(book!!.historyChapterNum)
                        var tagY = binding.shelfLvChapterList.height / 2
                        binding.shelfLvChapterList.setSelectionFromTop(
                            book!!.historyChapterNum,
                            tagY
                        )
                    }
                } else {
                    isFirstInit = false
                }
            }
        })
    }

    override fun initData() {
        book = intent.getParcelableExtra("book")
        //
        book?.let {
            binding.shelfTvBookName.text = book!!.bookName
            presenter.requestChaptersData(this, it)
            mLinearLayoutManager!!.scrollToPositionWithOffset(it.historyChapterNum, 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    /**
     * 设置目录数据
     */
    override fun setDrawerData(chapterList: MutableList<Chapter>) {
        this.chapterList = chapterList
        chapterAdapter = ChapterAdapter(this, chapterList)
        binding.shelfLvChapterList.adapter = chapterAdapter

        chapterAdapter!!.setCurrentPosition(book!!.historyChapterNum)
        var tagY = binding.shelfLvChapterList.height / 2
        binding.shelfLvChapterList.setSelectionFromTop(
            book!!.historyChapterNum,
            tagY
        )

        binding.shelfLvChapterList.setOnItemClickListener { parent, view, position, id ->
            mLinearLayoutManager!!.scrollToPositionWithOffset(position, 0)
            binding.shelfReadDl.closeDrawer(GravityCompat.START)
        }
    }

    /**
     * 设置内容数据
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun setContentData(chapterList: MutableList<Chapter>) {
        contentAdapter = ContentAdapter(this, chapterList)
        binding.shelfRvContent.adapter = contentAdapter

        contentAdapter!!.setmOnTouchListener { v, event ->
            currentY = event.rawY
            false
        }

        contentAdapter!!.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                if (currentY > settingOnClickValidFrom && currentY < settingOnClickValidTo) {
                    val curOnClickTime: Long = DateHelper.getLongDate()
                    if (curOnClickTime - lastOnClickTime < doubleOnClickConfirmTime) {
                        autoScrollOpening = false
                        autoScroll()
                    } else {
                        showSetting()
                    }
                }
            }
        })
    }

    /**
     * 更新章节进度
     */
    override fun setCacheProgress(progress: Int, tvDownloadProgress: TextView?) {
        if (tvDownloadProgress != null) {
            tvDownloadProgress.text = (progress * 100 / chapterList!!.size).toString() + " %"
        }
        if (chapterList!!.size == progress) {
            runOnUiThread {
                showToast(getString(R.string.download_already_all_tips))
            }
        }
    }

    override fun showError(msg: String, errorCode: Int) {
    }

    override fun showLoading() {
        binding.multipleStatusView.showLoading()
    }

    override fun dismissLoading() {
        binding.multipleStatusView.showContent()
    }

    /**
     * 显示设置
     */
    private fun showSetting() {
        if (mSettingDialog != null) {
            mSettingDialog!!.show()
        } else {
            var progress = 100
            if (mLinearLayoutManager!!.itemCount > 1) {
                progress =
                    mLinearLayoutManager!!.findLastVisibleItemPosition() * 100 / (mLinearLayoutManager!!.itemCount - 1)
            }
            mSettingDialog = DialogCreator.createReadSetting(
                this,
                SysManager.getSetting(this)!!.dayStyle,
                progress,
                {
                    //返回
                    if (StringHelper.isEmpty(book!!.id)) {
                        DialogCreator.createCommonDialog(this,
                            getString(R.string.tip),
                            getString(R.string.no_add_tips),
                            true,
                            { dialog, which ->
                                book!!.historyChapterNum =
                                    mLinearLayoutManager!!.findLastVisibleItemPosition()
                                BookDaoOpe.getInstance().insertData(this, book!!)
                                for (chapter in chapterList!!) {
                                    chapter.id = UUID.randomUUID().toString()
                                    chapter.bookId = book!!.id
                                }
                                ChapterDaoOpe.getInstance().insertDatas(this, chapterList!!)
                                finish()
                            }
                        ) { dialog, which ->
                            finish()
                            dialog.dismiss()
                        }
                    } else {
                        finish()
                    }
                },
                {
                    //上一章
                    mLinearLayoutManager!!.scrollToPositionWithOffset(
                        book!!.historyChapterNum - 1,
                        0
                    )
                },
                {
                    //下一章
                    mLinearLayoutManager!!.scrollToPositionWithOffset(
                        book!!.historyChapterNum + 1,
                        0
                    )
                },
                {
                    //目录
                    binding.shelfReadDl.openDrawer(GravityCompat.START)
                    mSettingDialog!!.dismiss()
                },
                object : DialogCreator.OnClickNightAndDayListener {
                    override fun onClick(dialog: Dialog, view: View, isDayStyle: Boolean) {
                        //日夜切换
                        changeNightAndDaySetting(isDayStyle)
                    }
                },
                {
                    //设置
                    mSettingDialog!!.dismiss()
                    showSettingDetailView()
                },
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
//                        //阅读进度
                        val chapterNum: Int =
                            (mLinearLayoutManager!!.itemCount - 1) * progress / 100
                        mLinearLayoutManager!!.scrollToPositionWithOffset(
                            chapterNum,
                            0
                        )
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    }
                },
                {
                    //语音
                },
                object : DialogCreator.OnClickDownloadAllChapterListener {
                    override fun onClick(
                        dialog: Dialog,
                        view: View,
                        tvDownloadProgress: TextView?
                    ) {
                        //缓存
                        if (tvDownloadProgress!!.text.equals(getString(R.string.set_cache))) {
                            if (StringHelper.isEmpty(book!!.id)) {
                                addBookToCaseAndDownload(tvDownloadProgress!!)
                            } else {
                                getAllChapterData(tvDownloadProgress!!)
                            }
                        } else {
                            showToast(getString(R.string.download_tips))
                        }
                    }
                }
            )
        }
    }

    /**
     *具体设置
     */
    private fun showSettingDetailView() {
        if (mSettingDetailDialog != null) {
            mSettingDetailDialog!!.show()
        } else {
            mSettingDetailDialog = createReadDetailSetting(
                this,
                SysManager.getSetting(this)!!,
                object : OnReadStyleChangeListener {
                    override fun onChange(readStyle: ReadStyle?) {
                        changeStyle(readStyle!!)
                    }
                },
                View.OnClickListener {
                    reduceTextSize()
                    if (contentAdapter != null) {
                        contentAdapter!!.notifyDataSetChangedBySetting()
                    }
                },
                View.OnClickListener {
                    increaseTextSize()
                    if (contentAdapter != null) {
                        contentAdapter!!.notifyDataSetChangedBySetting()
                    }
                },
                View.OnClickListener {
                    setLanguage()
                    if (contentAdapter != null) {
                        contentAdapter!!.notifyDataSetChangedBySetting()
                    }
                    if (chapterAdapter != null) {
                        chapterAdapter!!.notifyDataSetChangedBySetting()
                    }
                },
                View.OnClickListener {
                    val intent = Intent(this, ShelfFontsActivity::class.java)
                    startActivityForResult(intent, APPCONST.REQUEST_FONT)
                },
                View.OnClickListener {
                    autoScroll()
                    mSettingDetailDialog!!.dismiss()
                }
            )
        }
    }

    /**
     * 日夜切换
     */
    private fun changeNightAndDaySetting(isDayStyle: Boolean) {
        val setting = SysManager.getSetting(this)
        setting!!.dayStyle = !isDayStyle
        SysManager.saveSetting(this, setting)
        initViewSet(!isDayStyle)
    }

    /**
     * 改变阅读风格
     *
     * @param readStyle
     */
    private fun changeStyle(readStyle: ReadStyle) {
        val setting = SysManager.getSetting(this)!!
        if (!setting.dayStyle) {
            setting.dayStyle = true
        }
        setting.readStyle = readStyle
        when (readStyle) {
            ReadStyle.common -> {
                setting.readBgColor = R.color.sys_common_bg
                setting.readWordColor = R.color.sys_common_word
            }
            ReadStyle.leather -> {
                setting.readBgColor = R.mipmap.shelf_theme_leather_bg
                setting.readWordColor = R.color.sys_leather_word
            }
            ReadStyle.protectedEye -> {
                setting.readBgColor = R.color.sys_protect_eye_bg
                setting.readWordColor = R.color.sys_protect_eye_word
            }
            ReadStyle.breen -> {
                setting.readBgColor = R.color.sys_breen_bg
                setting.readWordColor = R.color.sys_breen_word
            }
            ReadStyle.blueDeep -> {
                setting.readBgColor = R.color.sys_blue_deep_bg
                setting.readWordColor = R.color.sys_blue_deep_word
            }
        }
        SysManager.saveSetting(this, setting)
        initViewSet(setting.dayStyle)
    }

    /**
     * 缩小字体
     */
    private fun reduceTextSize() {
        val setting = SysManager.getSetting(this)!!
        if (setting.readWordSize > 1) {
            setting.readWordSize = SysManager.getSetting(this)!!.readWordSize - 1
            SysManager.saveSetting(this, setting)
        }
    }

    /**
     * 增大字体
     */
    private fun increaseTextSize() {
        val setting = SysManager.getSetting(this)!!
        if (setting.readWordSize < 40) {
            setting.readWordSize = SysManager.getSetting(this)!!.readWordSize + 1
            SysManager.saveSetting(this, setting)
        }
    }

    /**
     * 设置语言
     */
    private fun setLanguage() {
        val setting = SysManager.getSetting(this)!!
        if (setting.language == Language.simplified) {
            setting.language = Language.traditional
        } else {
            setting.language = Language.simplified
        }
        SysManager.saveSetting(this, setting)
    }

    /**
     * 字体界面回调
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            APPCONST.REQUEST_FONT ->
                if (resultCode == RESULT_OK) {
                    if (contentAdapter != null) {
                        contentAdapter!!.notifyDataSetChangedBySetting()
                    }
                    if (chapterAdapter != null) {
                        chapterAdapter!!.notifyDataSetChangedBySetting()
                    }
                }
        }
    }

    /**
     * 更改设置刷新view
     */
    private fun initViewSet(isDayStyle: Boolean) {
        if (contentAdapter != null) {
            contentAdapter!!.notifyDataSetChangedBySetting()
        }
        if (chapterAdapter != null) {
            chapterAdapter!!.notifyDataSetChangedBySetting()
        }
        if (isDayStyle) {
            binding.shelfReadDl
                .setBackgroundResource(
                    SysManager.getSetting(this@ShelfReadBookActivity)!!
                        .readBgColor
                )
            binding.shelfTvBookName.setTextColor(
                resources.getColor(
                    SysManager.getSetting(this@ShelfReadBookActivity)!!
                        .readWordColor
                )
            )
            binding.shelfChapterListView
                .setBackgroundResource(
                    SysManager.getSetting(this@ShelfReadBookActivity)!!
                        .readBgColor
                )
        } else {
            binding.shelfReadDl
                .setBackgroundResource(R.color.sys_night_bg)

            binding.shelfTvBookName.setTextColor(
                resources.getColor(R.color.sys_night_word)
            )
            binding.shelfChapterListView
                .setBackgroundResource(R.color.sys_night_bg)
        }
    }

    /**
     * 自动滚动
     */
    private fun autoScroll() {
        autoScrollOpening = true
        ThreadManager.createLongPool()!!.execute {
            while (autoScrollOpening) {
                try {
                    Thread.sleep(
                        (SysManager.getSetting(this@ShelfReadBookActivity)!!.autoScrollSpeed + 1).toLong()
                    )
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                mHandler.post {
                    binding.shelfRvContent.scrollBy(0, 2)
                }
            }
        }
    }

    /**
     * 先增加书架后缓存书籍
     */
    private fun addBookToCaseAndDownload(tvDownloadProgress: TextView) {
        DialogCreator.createCommonDialog(this,
            getString(R.string.tip),
            getString(R.string.download_no_add_tips),
            true,
            { dialog, which ->
                BookDaoOpe.getInstance().insertData(this, book!!)
                for (chapter in chapterList!!) {
                    chapter.id = UUID.randomUUID().toString()
                    chapter.bookId = book!!.id
                }
                ChapterDaoOpe.getInstance().insertDatas(this, chapterList!!)
                getAllChapterData(tvDownloadProgress)
            }
        ) { dialog, which -> dialog.dismiss() }
    }

    /**
     * 缓存所有章节
     */
    private fun getAllChapterData(tvDownloadProgress: TextView) {
        presenter.downLoadAllChapters(this, book!!, chapterList!!, tvDownloadProgress)
    }
}