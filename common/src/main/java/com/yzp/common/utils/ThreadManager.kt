package com.yzp.common.utils

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object ThreadManager {

    private var longPool: ThreadPoolProxy? = null

    // 联网比较耗时
    // cpu的核数*2+1
    @Synchronized
    fun createLongPool(): ThreadPoolProxy? {
        if (longPool == null) {
            longPool = ThreadPoolProxy(5, 10, 5000L)
        }
        return longPool
    }

    class ThreadPoolProxy(
        private val corePoolSize: Int,
        private val maximumPoolSize: Int,
        private val time: Long
    ) {
        private var pool: ThreadPoolExecutor? = null

        /**
         * 执行任务
         * @param runnable
         */
        fun execute(runnable: Runnable?) {
            if (pool == null) {
                /*
                 * 1. 线程池里面管理多少个线程
                 * 2. 如果排队满了, 额外的开的线程数
                 * 3. 如果线程池没有要执行的任务 存活多久
                 * 4. 时间的单位
                 * 5. 如果 线程池里管理的线程都已经用了,剩下的任务 临时存到LinkedBlockingQueue对象中 排队
                 */
                pool = ThreadPoolExecutor(
                    corePoolSize, maximumPoolSize,
                    time, TimeUnit.MILLISECONDS,
                    LinkedBlockingQueue<Runnable>(10)
                )
            }
            pool!!.execute(runnable) // 调用线程池 执行异步任务
        }

        /**
         * 取消任务
         * @param runnable
         */
        fun cancel(runnable: Runnable?) {
            if (pool != null && !pool!!.isShutdown() && !pool!!.isTerminated()) {
                pool!!.remove(runnable)
            }
        }
    }
}