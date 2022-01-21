package com.fundMonitor.constants;

/**
 * @author Aresix
 * @date 2022/1/21 15:56
 */
public enum TaskStatus {
    cancelled("已取消"),
    inProcess("待完成"),
    finished("已完成"),
    timeExceededLimit("已超时");
    private String status;

    TaskStatus(String s) {
        this.status=s;
    }
}
