package com.stardust.autojs.core.looper;

import android.os.Looper;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Stardust on 2017/12/27.
 */

public class LooperHelper {

    private static volatile ConcurrentHashMap<Thread, Looper> sLoopers = new ConcurrentHashMap<>();

    public static void prepare() {
        if (Looper.myLooper() == Looper.getMainLooper())
            return;
        if (Looper.myLooper() == null)
            Looper.prepare();
        Looper l = Looper.myLooper();
        if (l != null)
            sLoopers.put(Thread.currentThread(), l);
    }

    public static void quitForThread(Thread thread) {
        Looper looper = sLoopers.remove(thread);
        if (looper != null && looper != Looper.getMainLooper())
            looper.quit();
    }

    /**
     * 仅从映射中移除指定线程的 Looper 记录，不退出 Looper
     * 用于 Loopers.recycle() 中清理静态 Map 残留，避免内存泄漏
     */
    public static void removeForThread(Thread thread) {
        sLoopers.remove(thread);
    }
}
