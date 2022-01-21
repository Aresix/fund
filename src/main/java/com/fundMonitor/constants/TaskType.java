package com.fundMonitor.constants;

/**
 * @author Aresix
 * @date 2022/1/21 15:59
 */
public enum TaskType {
    log("日志触发逻辑"),
    file("文件触发逻辑");
    public String type;
    TaskType(String t){
        this.type=t;
    }
}
