package com.fundMonitor.constants;

/**
 * @author Aresix
 * @date 2022/1/21 15:41
 */
public enum TaskPriority {
    common("一般"),
    urgent("紧急"),
    important("重要");
    public String priority;

    TaskPriority(String p) {this.priority = p;}
}
