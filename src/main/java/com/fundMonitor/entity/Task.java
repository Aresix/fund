package com.fundMonitor.entity;

import com.fundMonitor.constants.TaskPriority;
import com.fundMonitor.constants.TaskStatus;
import com.fundMonitor.constants.TaskType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

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
    private Long creator;

    @ApiModelProperty(value = "项目名")
    private String projectName;

//    @ApiModelProperty(value = "负责人")
//    @OneToMany
//    @Where(clause = "deleted = 0")
//    @JoinColumn(name = "taskId", referencedColumnName = "id", updatable = false, insertable = false)
//    private List<Account> accounts;

    @ApiModelProperty(value = "任务日志")
    @OneToMany
    @Where(clause = "deleted = 0")
    @JoinColumn(name = "taskId", referencedColumnName = "id", updatable = false, insertable = false)
    private List<Log> logs;

    @ApiModelProperty(value = "紧急程度")
    @Enumerated(EnumType.STRING)
    private TaskPriority taskPriority = TaskPriority.common;

    @ApiModelProperty(value = "目标完成时间")
    private Timestamp finTime;

    @ApiModelProperty(value = "完成情况")
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus = TaskStatus.inProcess;

    @ApiModelProperty(value = "类型")
    @Enumerated(EnumType.STRING)
    private TaskType taskType;

//    @ApiModelProperty(value = "责任人权限")
//    @Enumerated(EnumType.STRING)
//    private TaskPer principalPer;
//
//    @ApiModelProperty(value = "责任组权限")
//    @Enumerated(EnumType.STRING)
//    private TaskPer groupPer;
//
//    @ApiModelProperty(value = "其他人权限")
//    @Enumerated(EnumType.STRING)
//    private TaskPer OtherPer;

    @ApiModelProperty(value = "任务链接")
    private String taskURL;

    @ApiModelProperty(value = "任务完成逻辑/文件存在逻辑")
    private String taskComLogic;

    @ApiModelProperty(value = "r")
    private int r;

    @ApiModelProperty(value = "w")
    private int w;

    @ApiModelProperty(value = "x")
    private int x;

}
