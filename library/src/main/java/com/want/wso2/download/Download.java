package com.want.wso2.download;

import android.os.Environment;

import com.want.wso2.base.Request;
import com.want.wso2.db.DownloadManager;
import com.want.wso2.model.Progress;
import com.want.wso2.task.XExecutor;
import com.want.wso2.utils.IOUtils;
import com.want.wso2.utils.WSOLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wisn on 2017/8/23.
 */

public class Download {

    private String folder;                                      //下载的默认文件夹
    private DownloadThreadPool threadPool;                      //下载的线程池
    private ConcurrentHashMap<String, DownloadTask> taskMap;    //所有任务

    public static Download getInstance() {
        return DownloadHolder.instance;
    }

    private static class DownloadHolder {
        private static final Download instance = new Download();
    }

    private Download() {
        folder = Environment.getExternalStorageDirectory() + File.separator + "download" + File.separator;
        IOUtils.createFolder(folder);
        threadPool = new DownloadThreadPool();
        taskMap = new ConcurrentHashMap<>();

        //校验数据的有效性，防止下载过程中退出，第二次进入的时候，由于状态没有更新导致的状态错误
        List<Progress> taskList = DownloadManager.getInstance().getDownloading();
        for (Progress info : taskList) {
            if (info.status == Progress.WAITING || info.status == Progress.LOADING || info.status == Progress.PAUSE) {
                info.status = Progress.NONE;
            }
        }
        DownloadManager.getInstance().replace(taskList);
    }

    public static DownloadTask request(String tag, Request<File, ? extends Request> request) {
        Map<String, DownloadTask> taskMap = Download.getInstance().getTaskMap();
        DownloadTask task = taskMap.get(tag);
        if (task == null) {
            task = new DownloadTask(tag, request);
            taskMap.put(tag, task);
        }
        return task;
    }

    /** 从数据库中恢复任务 */
    public static DownloadTask restore(Progress progress) {
        Map<String, DownloadTask> taskMap = Download.getInstance().getTaskMap();
        DownloadTask task = taskMap.get(progress.tag);
        if (task == null) {
            task = new DownloadTask(progress);
            taskMap.put(progress.tag, task);
        }
        return task;
    }

    /** 从数据库中恢复任务 */
    public static List<DownloadTask> restore(List<Progress> progressList) {
        Map<String, DownloadTask> taskMap = Download.getInstance().getTaskMap();
        List<DownloadTask> tasks = new ArrayList<>();
        for (Progress progress : progressList) {
            DownloadTask task = taskMap.get(progress.tag);
            if (task == null) {
                task = new DownloadTask(progress);
                taskMap.put(progress.tag, task);
            }
            tasks.add(task);
        }
        return tasks;
    }

    /** 开始所有任务 */
    public void startAll() {
        for (Map.Entry<String, DownloadTask> entry : taskMap.entrySet()) {
            DownloadTask task = entry.getValue();
            if (task == null) {
                WSOLog.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            task.start();
        }
    }

    /** 暂停全部任务 */
    public void pauseAll() {
        //先停止未开始的任务
        for (Map.Entry<String, DownloadTask> entry : taskMap.entrySet()) {
            DownloadTask task = entry.getValue();
            if (task == null) {
                WSOLog.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.status != Progress.LOADING) {
                task.pause();
            }
        }
        //再停止进行中的任务
        for (Map.Entry<String, DownloadTask> entry : taskMap.entrySet()) {
            DownloadTask task = entry.getValue();
            if (task == null) {
                WSOLog.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.status == Progress.LOADING) {
                task.pause();
            }
        }
    }

    /** 删除所有任务 */
    public void removeAll() {
        removeAll(false);
    }

    /**
     * 删除所有任务
     *
     * @param isDeleteFile 删除任务是否删除文件
     */
    public void removeAll(boolean isDeleteFile) {
        Map<String, DownloadTask> map = new HashMap<>(taskMap);
        //先删除未开始的任务
        for (Map.Entry<String, DownloadTask> entry : map.entrySet()) {
            DownloadTask task = entry.getValue();
            if (task == null) {
                WSOLog.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.status != Progress.LOADING) {
                task.remove(isDeleteFile);
            }
        }
        //再删除进行中的任务
        for (Map.Entry<String, DownloadTask> entry : map.entrySet()) {
            DownloadTask task = entry.getValue();
            if (task == null) {
                WSOLog.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.status == Progress.LOADING) {
                task.remove(isDeleteFile);
            }
        }
    }

    /** 设置下载目录 */
    public String getFolder() {
        return folder;
    }

    public Download setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public DownloadThreadPool getThreadPool() {
        return threadPool;
    }

    public Map<String, DownloadTask> getTaskMap() {
        return taskMap;
    }

    public DownloadTask getTask(String tag) {
        return taskMap.get(tag);
    }

    public boolean hasTask(String tag) {
        return taskMap.containsKey(tag);
    }

    public DownloadTask removeTask(String tag) {
        return taskMap.remove(tag);
    }

    public void addOnAllTaskEndListener(XExecutor.OnAllTaskEndListener listener) {
        threadPool.getExecutor().addOnAllTaskEndListener(listener);
    }

    public void removeOnAllTaskEndListener(XExecutor.OnAllTaskEndListener listener) {
        threadPool.getExecutor().removeOnAllTaskEndListener(listener);
    }
}
