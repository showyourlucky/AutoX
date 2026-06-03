package com.stardust.autojs.core.boardcast;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Stardust on 2018/4/1.
 */

public class Broadcast {

    private static CopyOnWriteArrayList<BroadcastEmitter> sEventEmitters = new CopyOnWriteArrayList<>();

    public static void registerListener(BroadcastEmitter eventEmitter) {
        sEventEmitters.add(eventEmitter);
    }

    public static boolean unregisterListener(BroadcastEmitter eventEmitter) {
        return sEventEmitters.remove(eventEmitter);
    }

    /**
     * 清理所有监听器，防止异常路径下 BroadcastEmitter 残留在静态列表中导致内存泄漏
     */
    public static void clearAll() {
        sEventEmitters.clear();
    }

    public static void send(String eventName, Object[] args) {
        for (BroadcastEmitter emitter : sEventEmitters) {
            emitter.onBroadcast(eventName, args);
        }
    }

}
