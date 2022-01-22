package com.fundMonitor.constants;

/**
 * @author sqmy
 * @create 2022-01-21 18:12
 */
public enum TaskPer {
    none("无权限"),
    r("读权限"),
    w("写权限"),
    x("运行权限"),
    wx("写运行权限"),
    rx("读运行权限"),
    rw("读写权限"),
    rwx("读写运行权限");
    public String per;
    TaskPer(String t){
        this.per = t;
    }
}
