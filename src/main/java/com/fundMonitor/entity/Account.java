package com.fundMonitor.entity;

import com.fundMonitor.constants.RoleType;
import com.fundMonitor.security.JPACryptoConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @author lli.chen
 */
@Entity
@Data
public class Account extends IEntity {

    @ApiModelProperty(value = "邮箱")
    @Column(unique = true, nullable = false)
    private String email;

    @ApiModelProperty(value = "电话")
    @Column(unique = true, nullable = false)
    private String phone;

    @ApiModelProperty(value = "用户名")
    @Column(unique = true, nullable = false)
    private String loginName;

    @ApiModelProperty(value = "密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Convert(converter = JPACryptoConverter.class)
    private String password = "123456";

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @JsonIgnore
    public String getAuthority() {
        return "ROLE_ACCOUNT";
    }


    @ApiModelProperty(value = "电话通知")
    private Boolean phoneNotification;

    @ApiModelProperty(value = "邮件通知")
    private Boolean emailNotification;
}
