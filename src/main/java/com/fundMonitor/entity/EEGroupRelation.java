package com.fundMonitor.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Aresix
 * @date 2022/1/21 13:18
 */
@Entity
@Data
public class EEGroupRelation extends IEntity {
    @ApiModelProperty(value = "所属分组")
    @Column(nullable = false)
    private String groupID;

    @ApiModelProperty(value = "组内成员")
    @Column(nullable = false)
    private String accountID;
}
