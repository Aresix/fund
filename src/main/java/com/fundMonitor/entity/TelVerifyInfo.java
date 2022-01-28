package com.fundMonitor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import java.sql.Timestamp;

/**
 * @author sqmy
 * @create 2022-01-28 12:28
 */
@Data
@Entity
public class TelVerifyInfo extends IEntity {
    @ApiModelProperty(value = "验证手机号")
    private String phoneNum;

    @JsonIgnore
    @ApiModelProperty(value = "验证码")
    private String telCode;

    @JsonIgnore
    @ApiModelProperty(value = "验证码有效时间")
    private Timestamp telCodeValidTime;
}
