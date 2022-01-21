package com.fundMonitor.entity;

import com.fundMonitor.constants.TaskPriority;
import com.fundMonitor.constants.TaskStatus;
import com.fundMonitor.constants.TaskType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Timestamp;

/**
 * @author Aresix
 * @date 2022/1/21 14:39
 */
@Entity
@Data
public class Task extends IEntity{
    @ApiModelProperty(value = "任务标题")
    private String taskTitle;

    @ApiModelProperty(value = "任务描述")
    private String taskDescription;

    @ApiModelProperty(value = "发布者")
    private String creator;

    @ApiModelProperty(value = "项目名")
    private String projectName;

    @ApiModelProperty(value = "负责人")
    private String principal;

    @ApiModelProperty(value = "紧急程度")
    @Enumerated(EnumType.STRING)
    private TaskPriority taskPriority;

    @ApiModelProperty(value = "目标完成时间")
    private Timestamp finTime;

    @ApiModelProperty(value = "完成情况")
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @ApiModelProperty(value = "类型")
    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @ApiModelProperty(value = "读权限")
    private int r;

    @ApiModelProperty(value = "写权限")
    private int w;

    @ApiModelProperty(value = "执行权限")
    private int x;

    @ApiModelProperty(value = "任务链接")
    private String taskURL;
}
