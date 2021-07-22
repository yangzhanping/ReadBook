package com.yzp.bookshelf.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.yzp.bookshelf.R
import com.yzp.bookshelf.app.SysManager
import com.yzp.bookshelf.common.APPCONST
import com.yzp.bookshelf.enums.Font

class FontsAdapter(var context: Context, var fonts: MutableList<Font>) : BaseAdapter() {

    private var mTypefaceMap = mutableMapOf<Font, Typeface>()

    override fun getCount(): Int {
        return fonts.size
    }

    override fun getItem(position: Int): Font {
        return fonts[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = null
        var viewHolder: ViewHolder? = null
        if (convertView == null) {
            view = View.inflate(context, R.layout.shelf_adapter_fonts_item, null)
            viewHolder = ViewHolder()
            viewHolder.tvExample = view!!.findViewById(R.id.tv_font_example)
            viewHolder.tvFontName = view!!.findViewById(R.id.tv_font_name)
            viewHolder.btnFontUse = view!!.findViewById(R.id.btn_font_use)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view?.tag as ViewHolder
        }
        initView(position, viewHolder)
        return view!!
    }

    class ViewHolder() {
        lateinit var tvExample: TextView
        lateinit var tvFontName: TextView
        lateinit var btnFontUse: Button
    }

    private fun initView(position: Int, viewHolder: ViewHolder) {
        val font: Font = getItem(position)
        var typeFace: Typeface? = null
        if (font !== Font.默认字体) {
            if (!mTypefaceMap.containsKey(font)) {
                typeFace = Typeface.createFromAsset(context.assets, font.path)
                mTypefaceMap[font] = typeFace
            } else {
                typeFace = mTypefaceMap[font]
            }
        }
        viewHolder.tvExample.setTypeface(typeFace)
        viewHolder.tvFontName.text = font.toString()
        if (SysManager.getSetting(context)!!.font == font) {
            viewHolder.btnFontUse.setBackgroundResource(R.drawable.font_using_btn_bg)
            viewHolder.btnFontUse.setTextColor(
                context.resources.getColor(R.color.sys_font_using_btn)
            )
            viewHolder.btnFontUse.text = context.resources.getString(R.string.font_using)
            viewHolder.btnFontUse.setOnClickListener(null)
        } else {
            viewHolder.btnFontUse.setBackgroundResource(R.drawable.font_use_btn_bg)
            viewHolder.btnFontUse.setTextColor(
                context.resources.getColor(R.color.sys_font_use_btn)
            )
            viewHolder.btnFontUse.text = context.resources.getString(R.string.font_use)

            viewHolder.btnFontUse.setOnClickListener(View.OnClickListener {
                val setting = SysManager.getSetting(context)
                setting!!.font = font
                SysManager.saveSetting(context, setting!!)
                notifyDataSetChanged()
                val intent = Intent()
                intent.putExtra(APPCONST.FONT, font)
                (context as Activity).setResult(Activity.RESULT_OK, intent)
            })
        }
    }
}