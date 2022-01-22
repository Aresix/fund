package com.fundMonitor.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author Aresix
 * @date 2022/1/21 17:16
 */
@Entity
@Data
public class EEGroup extends IEntity{
    @ApiModelProperty(value = "组名")
    private String groupName;

    @ApiModelProperty(value = "组员")
    @OneToMany
    @Where(clause = "deleted = 0")
    @JoinColumn(name = "groupId", referencedColumnName = "id", updatable = false, insertable = false)
    private List<Account> accounts;
}
