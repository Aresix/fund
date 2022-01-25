package com.fundMonitor.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Aresix
 * @date 2022/1/21 17:45
 */
@Entity
@Data
public class EETask extends IEntity{
    @ApiModelProperty(value = "任务编号")
    @Column(nullable = false)
    private Long taskID;

    @ApiModelProperty(value = "责任人")
    @Column(nullable = false)
    private Long taskPersonInChargeID;

}
