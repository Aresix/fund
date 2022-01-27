package com.fundMonitor.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;

/**
 * @author sqmy
 * @create 2022-01-26 15:48
 */
@Entity
@Data
public class MonitorGraph extends IEntity {
    @ApiModelProperty(value = "图表名称")
    private String graphName;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "刷新频率(s)")
    private int sec;

    @ApiModelProperty(value = "输出文件名")
    private String outFileName;

    @ApiModelProperty(value = "绘图python指令")
    private String PlotIns;
}
