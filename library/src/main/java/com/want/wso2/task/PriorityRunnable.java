package com.want.wso2.task;

/**
 * Created by wisn on 2017/8/23.
 */

public class PriorityRunnable extends PriorityObject<Runnable> implements Runnable {

    public PriorityRunnable(int priority, Runnable obj) {
        super(priority, obj);
    }

    @Override
    public void run() {
        this.obj.run();
    }
}