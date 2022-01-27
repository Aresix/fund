package com.fundMonitor.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;

/**
 * @author sqmy
 * @create 2022-01-27 10:55
 */
@Data
@Entity
public class Log extends IEntity {
    @ApiModelProperty(value = "时间戳")
    private String time;

    @ApiModelProperty(value = "级别")
    private String level;

    @ApiModelProperty(value = "打印日志的类名")
    private String loggerName;

    @ApiModelProperty(value = "日志信息")
    private String message;
}
