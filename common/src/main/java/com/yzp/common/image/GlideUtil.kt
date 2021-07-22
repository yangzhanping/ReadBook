package com.yzp.common.image

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.yzp.common.R
import java.io.File


object GlideUtil {

    /*** 占位图  */
    var placeholderImage: Int = R.mipmap.no_image

    /*** 错误图  */
    var errorImage: Int = R.mipmap.no_image

    fun loadImage(context: Context, url: String, imageView: ImageView) {
        val options: RequestOptions = RequestOptions()
            .placeholder(placeholderImage) //占位图
            .error(errorImage) //错误图
        Glide.with(context).load(url).apply(options).into(imageView)
    }

    /**
     * 指定图片大小;使用override()方法指定了一个图片的尺寸。
     * Glide现在只会将图片加载成width*height像素的尺寸，而不会管你的ImageView的大小是多少了。
     * 如果你想加载一张图片的原始尺寸的话，可以使用Target.SIZE_ORIGINAL关键字----override(Target.SIZE_ORIGINAL)
     *
     * @param context   上下文
     * @param url       链接
     * @param imageView ImageView
     * @param width     图片宽度
     * @param height    图片高度
     */
    fun loadImageSize(
        context: Context, url: String, imageView: ImageView, width: Int, height: Int
    ) {
        val options = RequestOptions()
            .placeholder(placeholderImage) //占位图
            .error(errorImage) //错误图
            .override(width, height)
            .priority(Priority.HIGH)
        Glide.with(context).load(url).apply(options).into(imageView)
    }

    /**
     * 禁用内存缓存功能
     * diskCacheStrategy()方法基本上就是Glide硬盘缓存功能的一切，它可以接收五种参数：
     *
     * DiskCacheStrategy.NONE： 表示不缓存任何内容。
     * DiskCacheStrategy.DATA： 表示只缓存原始图片。
     * DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片。
     * DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
     * DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）。
     *
     * @param context   上下文
     * @param url       链接
     * @param imageView ImageView
     */
    fun loadImageSizeKipMemoryCache(
        context: Context, url: String, imageView: ImageView
    ) {
        val options = RequestOptions()
            .placeholder(placeholderImage) //占位图
            .error(errorImage) //错误图
            .skipMemoryCache(true) //禁用掉Glide的内存缓存功能
        Glide.with(context).load(url).apply(options).into(imageView)
    }

    /**
     * 预先加载图片
     * 在使用图片之前，预先把图片加载到缓存，调用了预加载之后，我们以后想再去加载这张图片就会非常快了，
     * 因为Glide会直接从缓存当中去读取图片并显示出来
     *
     * @param context 上下文
     * @param url     链接
     */
    fun preloadImage(context: Context, url: String) {
        Glide.with(context).load(url).preload()
    }

    /**
     * 加载圆形图片
     *
     * @param context   上下文
     * @param url       链接
     * @param imageView ImageView
     */
    fun loadCircleImage(
        context: Context, url: String, imageView: ImageView
    ) {
        val options = RequestOptions()
            .centerCrop()
            .circleCrop() //设置圆形
            .placeholder(placeholderImage) //占位图
            .error(errorImage) //错误图
            .priority(Priority.HIGH)
        Glide.with(context).load(url).apply(options).into(imageView)
    }

    /**
     * 下载图片
     * 在RequestListener的onResourceReady方法里面获取下载File图片
     * new RequestListener<File>() {
     * *@Override
     * public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
     * return false;
     * }
     *
     *
     * *@Override
     * public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
     * //resource即为下载取得的图片File
     * return false;
     * }
     * }
     *
     * @param context         上下文
     * @param url             下载链接
     * @param requestListener 下载监听
    </File></File></File> */
    fun downloadImage(
        context: Context?, url: String?, requestListener: RequestListener<File?>?
    ) {
        Glide.with(context!!)
            .downloadOnly()
            .load(url)
            .addListener(requestListener)
            .preload()
    }
}