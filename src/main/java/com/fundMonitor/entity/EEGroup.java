package com.fundMonitor.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;

/**
 * @author Aresix
 * @date 2022/1/21 17:16
 */
@Entity
@Data
public class EEGroup extends IEntity{
    @ApiModelProperty(value = "组名")
    private String groupName;
}
