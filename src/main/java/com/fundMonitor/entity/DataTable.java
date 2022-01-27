package com.fundMonitor.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;

/**
 * @author sqmy
 * @create 2022-01-26 16:01
 */
@Entity
@Data
public class DataTable extends IEntity {
    @ApiModelProperty(value = "表格名称")
    private String tableName;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "刷新频率(s)")
    private int sec;

    @ApiModelProperty(value = "日志触发逻辑")
    private String logTriggerLogic;

}
