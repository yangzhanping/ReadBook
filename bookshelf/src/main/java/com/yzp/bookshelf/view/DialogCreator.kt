package com.yzp.bookshelf.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import com.yzp.bookshelf.R
import com.yzp.bookshelf.app.SysManager
import com.yzp.bookshelf.app.SysManager.saveSetting
import com.yzp.bookshelf.bean.Setting
import com.yzp.bookshelf.enums.Language
import com.yzp.bookshelf.enums.ReadStyle
import com.yzp.common.utils.BrightUtil.followSystemBright
import com.yzp.common.utils.BrightUtil.progressToBright
import com.yzp.common.utils.BrightUtil.setBrightness
import com.yzp.common.utils.StringHelper

@SuppressLint("StaticFieldLeak")
object DialogCreator {
    var ivLastSelectd: ImageView? = null

    /**
     * 阅读详细设置对话框
     *
     * @param context
     * @param setting
     * @param onReadStyleChangeListener
     * @param reduceSizeListener
     * @param increaseSizeListener
     * @param languageChangeListener
     * @param onFontClickListener
     * @return
     */
    @SuppressLint("ClickableViewAccessibility")
    fun createReadDetailSetting(
        context: Context?, setting: Setting,
        onReadStyleChangeListener: OnReadStyleChangeListener?,
        reduceSizeListener: View.OnClickListener?,
        increaseSizeListener: View.OnClickListener?,
        languageChangeListener: View.OnClickListener?,
        onFontClickListener: View.OnClickListener?,
        autoScrollListener: View.OnClickListener?
    ): Dialog? {
        val dialog =
            Dialog(context!!, R.style.jmui_default_dialog_style)
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.shelf_dialog_read_setting_detail, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        //触摸外部关闭
        view.findViewById<View>(R.id.ll_bottom_view).setOnClickListener(null)
        view.setOnTouchListener { view, motionEvent ->
            dialog.dismiss()
            false
        }
        //阅读背景风格
        val ivCommonStyle =
            view.findViewById<View>(R.id.iv_common_style) as ImageView
        val ivLeatherStyle =
            view.findViewById<View>(R.id.iv_leather_style) as ImageView
        val ivProtectEyeStyle =
            view.findViewById<View>(R.id.iv_protect_eye_style) as ImageView
        val ivBreenStyle =
            view.findViewById<View>(R.id.iv_breen_style) as ImageView
        val ivBlueDeepStyle =
            view.findViewById<View>(R.id.iv_blue_deep_style) as ImageView
        when (setting.readStyle) {
            ReadStyle.common -> {
                ivCommonStyle.isSelected = true
                ivLastSelectd = ivCommonStyle
            }
            ReadStyle.leather -> {
                ivLeatherStyle.isSelected = true
                ivLastSelectd = ivLeatherStyle
            }
            ReadStyle.protectedEye -> {
                ivProtectEyeStyle.isSelected = true
                ivLastSelectd = ivProtectEyeStyle
            }
            ReadStyle.breen -> {
                ivBreenStyle.isSelected = true
                ivLastSelectd = ivBreenStyle
            }
            ReadStyle.blueDeep -> {
                ivBlueDeepStyle.isSelected = true
                ivLastSelectd = ivBlueDeepStyle
            }
        }
        ivCommonStyle.setOnClickListener {
            selectedStyle(
                ivCommonStyle,
                ReadStyle.common,
                onReadStyleChangeListener
            )
        }
        ivLeatherStyle.setOnClickListener {
            selectedStyle(
                ivLeatherStyle,
                ReadStyle.leather,
                onReadStyleChangeListener
            )
        }
        ivProtectEyeStyle.setOnClickListener {
            selectedStyle(
                ivProtectEyeStyle,
                ReadStyle.protectedEye,
                onReadStyleChangeListener
            )
        }
        ivBlueDeepStyle.setOnClickListener {
            selectedStyle(
                ivBlueDeepStyle,
                ReadStyle.blueDeep,
                onReadStyleChangeListener
            )
        }
        ivBreenStyle.setOnClickListener {
            selectedStyle(
                ivBreenStyle,
                ReadStyle.breen,
                onReadStyleChangeListener
            )
        }

        //字体大小
        val tvSizeReduce =
            view.findViewById<View>(R.id.tv_reduce_text_size) as TextView
        val tvSizeIncrease =
            view.findViewById<View>(R.id.tv_increase_text_size) as TextView
        val tvSize = view.findViewById<View>(R.id.tv_text_size) as TextView
        tvSize.text = setting.readWordSize.toInt().toString()
        tvSizeReduce.setOnClickListener { v ->
            if (setting.readWordSize > 1) {
                tvSize.text = (SysManager.getSetting(context)!!.readWordSize.toInt() - 1).toString()
                reduceSizeListener?.onClick(v)
            }
        }
        tvSizeIncrease.setOnClickListener { v ->
            if (setting.readWordSize < 40) {
                tvSize.text = (SysManager.getSetting(context)!!.readWordSize.toInt() + 1).toString()
                increaseSizeListener?.onClick(v)
            }
        }

        //亮度调节
        val seekBar =
            view.findViewById<View>(R.id.sb_brightness_progress) as SeekBar
        val tvBrightFollowSystem =
            view.findViewById<View>(R.id.tv_system_brightness) as TextView
        seekBar.progress = setting.brightProgress
        tvBrightFollowSystem.isSelected = setting.brightFollowSystem
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                setBrightness(
                    (context as Activity?)!!,
                    progressToBright(progress)
                )
                tvBrightFollowSystem.isSelected = false
                setting.brightProgress = progress
                setting.brightFollowSystem = false
                saveSetting(context, setting)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        //亮度跟随系统
        tvBrightFollowSystem.setOnClickListener {
            tvBrightFollowSystem.isSelected = !tvBrightFollowSystem.isSelected
            if (tvBrightFollowSystem.isSelected) {
                followSystemBright((context as Activity?)!!)
                setting.brightFollowSystem = true
                saveSetting(context, setting)
            } else {
                setBrightness(
                    (context as Activity?)!!,
                    progressToBright(setting.brightProgress)
                )
                setting.brightFollowSystem = false
                saveSetting(context, setting)
            }
        }
        //简繁体
        val tvSimplifiedAndTraditional =
            view.findViewById<View>(R.id.tv_simplified_and_traditional) as TextView
        if (setting.language == Language.simplified) {
            tvSimplifiedAndTraditional.text = "繁"
        } else if (setting.language == Language.traditional) {
            tvSimplifiedAndTraditional.text = "简"
        }
        tvSimplifiedAndTraditional.setOnClickListener { v ->
            if (tvSimplifiedAndTraditional.text.toString() == "简") {
                tvSimplifiedAndTraditional.text = "繁"
            } else {
                tvSimplifiedAndTraditional.text = "简"
            }
            languageChangeListener?.onClick(v)
        }

        //选择字体
        val tvFont = view.findViewById<View>(R.id.tv_text_font) as TextView
        tvFont.setOnClickListener(onFontClickListener)

        //自动滚屏速度
        val sbScrollSpeed =
            view.findViewById<View>(R.id.sb_auto_scroll_progress) as SeekBar
        val tvAutoScroll =
            view.findViewById<View>(R.id.tv_auto_scroll) as TextView
        sbScrollSpeed.progress = 100 - setting.autoScrollSpeed
        sbScrollSpeed.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                setting.autoScrollSpeed = (100 - progress)
                saveSetting(context, setting)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        tvAutoScroll.setOnClickListener(autoScrollListener)
        dialog.show()
        return dialog
    }

    private fun selectedStyle(
        curSelected: ImageView,
        readStyle: ReadStyle,
        listener: OnReadStyleChangeListener?
    ) {
        ivLastSelectd!!.isSelected = false
        ivLastSelectd = curSelected
        curSelected.isSelected = true
        listener?.onChange(readStyle)
    }


    /**
     * 阅读设置对话框
     *
     * @param context
     * @param isDayStyle
     * @param chapterProgress
     * @param backListener
     * @param lastChapterListener
     * @param nextChapterListener
     * @param chapterListListener
     * @param onClickNightAndDayListener
     * @param settingListener
     * @return
     */
    fun createReadSetting(
        context: Context, isDayStyle: Boolean, chapterProgress: Int,
        backListener: View.OnClickListener?,
        lastChapterListener: View.OnClickListener,
        nextChapterListener: View.OnClickListener,
        chapterListListener: View.OnClickListener,
        onClickNightAndDayListener: OnClickNightAndDayListener,
        settingListener: View.OnClickListener,
        onSeekBarChangeListener: OnSeekBarChangeListener,
        voiceOnClickListener: View.OnClickListener?,
        onClickDownloadAllChapterListener: OnClickDownloadAllChapterListener
    ): Dialog? {
        val dialog =
            Dialog(context, R.style.jmui_default_dialog_style)
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.shelf_dialog_read_setting, null)
        dialog.setContentView(view)
        val llBack =
            view.findViewById<View>(R.id.ll_title_back) as LinearLayout
        val tvLastChapter =
            view.findViewById<View>(R.id.tv_last_chapter) as TextView
        val tvNextChapter =
            view.findViewById<View>(R.id.tv_next_chapter) as TextView
        val sbChapterProgress =
            view.findViewById<View>(R.id.sb_read_chapter_progress) as SeekBar
        val llChapterList =
            view.findViewById<View>(R.id.ll_chapter_list) as LinearLayout
        val llNightAndDay =
            view.findViewById<View>(R.id.ll_night_and_day) as LinearLayout
        val llSetting =
            view.findViewById<View>(R.id.ll_setting) as LinearLayout
        val ivNightAndDay =
            view.findViewById<View>(R.id.iv_night_and_day) as ImageView
        val tvNightAndDay =
            view.findViewById<View>(R.id.tv_night_and_day) as TextView
        val ivVoice =
            view.findViewById<View>(R.id.iv_voice_read) as ImageView
        view.findViewById<View>(R.id.rl_title_view).setOnClickListener(null)
        view.findViewById<View>(R.id.ll_bottom_view).setOnClickListener(null)
        val window = dialog.window
        window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = dialog.context.resources.getColor(R.color.sys_dialog_setting_bg)
        }
        view.setOnTouchListener { view, motionEvent ->
            dialog.dismiss()
            false
        }
        if (!isDayStyle) {
            ivNightAndDay.setImageResource(R.mipmap.shelf_ic_day)
            tvNightAndDay.text = context.getString(R.string.set_day)
        }
        llBack.setOnClickListener(backListener)
        tvLastChapter.setOnClickListener(lastChapterListener)
        tvNextChapter.setOnClickListener(nextChapterListener)
        sbChapterProgress.progress = chapterProgress
        llChapterList.setOnClickListener(chapterListListener)
        llSetting.setOnClickListener(settingListener)
        sbChapterProgress.setOnSeekBarChangeListener(onSeekBarChangeListener)
        ivVoice.setOnClickListener(voiceOnClickListener)
        //日夜切换
        llNightAndDay.setOnClickListener { view ->
            val isDay: Boolean
            if (tvNightAndDay.text.toString() == context.getString(R.string.set_day)) {
                isDay = false
                ivNightAndDay.setImageResource(R.mipmap.shelf_ic_nighttime)
                tvNightAndDay.text = context.getString(R.string.set_night)
            } else {
                isDay = true
                ivNightAndDay.setImageResource(R.mipmap.shelf_ic_day)
                tvNightAndDay.text = context.getString(R.string.set_day)
            }
            onClickNightAndDayListener?.onClick(dialog, view, isDay)
        }

        //缓存全部章节
        val tvDownloadProgress =
            view.findViewById<View>(R.id.tv_download_progress) as TextView
        val llDonwloadCache =
            view.findViewById<View>(R.id.ll_download_cache) as LinearLayout
        llDonwloadCache.setOnClickListener { v ->
            onClickDownloadAllChapterListener?.onClick(
                dialog,
                v,
                tvDownloadProgress
            )
        }
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        return dialog
    }


    /**
     * 创建一个普通对话框（包含确定、取消按键）
     *
     * @param context
     * @param title
     * @param mesage
     * @param cancelable       是否允许返回键取消
     * @param positiveListener 确定按键动作
     * @param negativeListener 取消按键动作
     * @return
     */
    fun createCommonDialog(
        context: Context?,
        title: String?,
        mesage: String?,
        cancelable: Boolean,
        positiveListener: DialogInterface.OnClickListener?,
        negativeListener: DialogInterface.OnClickListener?
    ): AlertDialog? {
        val normalDialog = AlertDialog.Builder(context)
        //        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle(title)
        normalDialog.setCancelable(cancelable)
        normalDialog.setMessage(mesage)
        normalDialog.setPositiveButton("确定", positiveListener)
        normalDialog.setNegativeButton("取消", negativeListener)
        // 显示
        val alertDialog = normalDialog.create()
        alertDialog.show()
        return alertDialog
    }

    /**
     * 创建一个普通对话框（包含key1、key2按键）
     *
     * @param context
     * @param title
     * @param mesage
     * @param key1
     * @param key2
     * @param positiveListener key1按键动作
     * @param negativeListener key2按键动作
     */
    fun createCommonDialog(
        context: Context?, title: String?, mesage: String?,
        key1: String?, key2: String?,
        positiveListener: DialogInterface.OnClickListener?,
        negativeListener: DialogInterface.OnClickListener?
    ) {
        try {
            val normalDialog =
                AlertDialog.Builder(context)
            //        normalDialog.setIcon(R.drawable.icon_dialog);
            normalDialog.setTitle(title)
            normalDialog.setCancelable(true)
            if (mesage != null) {
                normalDialog.setMessage(mesage)
            }
            normalDialog.setPositiveButton(key1, positiveListener)
            normalDialog.setNegativeButton(key2, negativeListener)
            // 显示
            normalDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 单按键对话框
     *
     * @param context
     * @param title
     * @param mesage
     * @param key
     * @param positiveListener
     */
    fun createCommonDialog(
        context: Context?, title: String?, mesage: String?,
        key: String?, positiveListener: DialogInterface.OnClickListener?
    ) {
        try {
            val normalDialog =
                AlertDialog.Builder(context)
            //        normalDialog.setIcon(R.drawable.icon_dialog);
            normalDialog.setTitle(title)
            normalDialog.setCancelable(false)
            if (mesage != null) {
                normalDialog.setMessage(mesage)
            }
            normalDialog.setPositiveButton(key, positiveListener)

            // 显示
            normalDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 创建一个进度对话框（圆形、旋转）
     *
     * @param context
     * @param title
     * @param message
     * @return
     */
    fun createProgressDialog(
        context: Context?, title: String?, message: String? /*,
             DialogInterface.OnClickListener positiveListener,DialogInterface.OnClickListener negativeListener*/
    ): ProgressDialog? {
        val progressDialog = ProgressDialog(context)
        //        normalDialog.setIcon(R.drawable.icon_dialog);
        if (!StringHelper.isEmpty(title)) {
            progressDialog.setTitle(title)
        }
        if (!StringHelper.isEmpty(message)) {
            progressDialog.setMessage(message)
        }
        progressDialog.setCancelable(false)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        /*  progressDialog.setPositiveButton("确定",positiveListener);
        progressDialog.setNegativeButton("取消",negativeListener);*/
        // 显示
        progressDialog.show()
        return progressDialog
    }


    /**
     * 三按键对话框
     *
     * @param context
     * @param title
     * @param msg
     * @param btnText1
     * @param btnText2
     * @param btnText3
     * @param positiveListener
     * @param neutralListener
     * @param negativeListener
     * @return
     */
    fun createThreeButtonDialog(
        context: Context?, title: String?, msg: String?,
        btnText1: String?, btnText2: String?, btnText3: String?,
        neutralListener: DialogInterface.OnClickListener?,
        negativeListener: DialogInterface.OnClickListener?,
        positiveListener: DialogInterface.OnClickListener?
    ): AlertDialog? {
        /*  final EditText et = new EditText(context);*/
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        if (!StringHelper.isEmpty(msg)) {
            builder.setMessage(msg)
        }
        //  第一个按钮
        builder.setNeutralButton(btnText1, neutralListener)
        //  中间的按钮
        builder.setNegativeButton(btnText2, negativeListener)
        //  第三个按钮
        builder.setPositiveButton(btnText3, positiveListener)
        val dialog = builder.show()
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        //  Diglog的显示
        return dialog
    }


    interface OnClickPositiveListener {
        fun onClick(dialog: Dialog, view: View)
    }

    interface OnClickNegativeListener {
        fun onClick(dialog: Dialog, view: View)
    }

    interface OnClickNightAndDayListener {
        fun onClick(
            dialog: Dialog,
            view: View,
            isDayStyle: Boolean
        )
    }

    interface OnReadStyleChangeListener {
        fun onChange(readStyle: ReadStyle?)
    }

    interface OnBrightFollowSystemChangeListener {
        fun onChange(isFollowSystem: Boolean)
    }

    interface OnClickDownloadAllChapterListener {
        fun onClick(
            dialog: Dialog,
            view: View,
            tvDownloadProgress: TextView?
        )
    }
}