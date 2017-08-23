package com.want.wso2.task;

/**
 * Created by wisn on 2017/8/23.
 */

public class PriorityObject<E> {

    public final int priority;
    public final E obj;

    public PriorityObject(int priority, E obj) {
        this.priority = priority;
        this.obj = obj;
    }
}