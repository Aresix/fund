package com.fundMonitor.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Aresix
 * @date 2022/1/21 13:02
 */

@Entity
@Data
public class Group extends IEntity {
    @ApiModelProperty(value = "组名")
    private String groupName;



}
