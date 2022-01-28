package com.fundMonitor.controller;

import com.fundMonitor.entity.Account;
import com.fundMonitor.entity.TelVerifyInfo;
import com.fundMonitor.request.OrderRequest;
import com.fundMonitor.service.TelVerifyInfoService;
import com.fundMonitor.service.UserService;
import com.fundMonitor.utils.MessageUtil;
import com.fundMonitor.utils.RandomUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lli.chen
 */
@RestController
public class BaseController {
    @Value("${fundMonitor.upload.file.path}")
    String rootPath;

    public static final String Integer_MAX_VALUE = "" + Integer.MAX_VALUE;
    @Autowired
    protected UserService userService;

    @Autowired
    private TelVerifyInfoService telVerifyInfoService;

    public String getCurrentUsername() {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return username;
    }

    public Account getCurrentAccount() {
        String loginName = getCurrentUsername();
        Optional<Account> account = Optional.ofNullable(userService.getByLoginName(loginName));
        Account fakeAccount = userService.getById(0L);
        return account.orElse(fakeAccount);
    }

    public List<Sort.Order> getOrder(List<Field> fields, List<OrderRequest> order) {
        List<String> properties = fields.stream().map(Field::getName).collect(Collectors.toList());
        List<Sort.Order> orders = Lists.newArrayList();
        for (OrderRequest orderRequest : order) {
            if (!properties.contains(orderRequest.getProperty())) {
                continue;
            }
            orders.add(new Sort.Order(orderRequest.getDirection(), orderRequest.getProperty()));
        }
        return orders;
    }


    public String sendTelCode(TelVerifyInfo telVerifyInfo, String phoneNum) {
        if (!Strings.isNullOrEmpty(phoneNum) && telVerifyInfo != null && phoneNum.length() == 11) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (telVerifyInfo.getTelCodeValidTime() != null && telVerifyInfo.getTelCodeValidTime().after(timestamp)) {
                return "验证码已发送，请勿重复操作";
            }
            Timestamp telCodeValidTime = new Timestamp(System.currentTimeMillis() + 180 * 1000);
            String telCode = RandomUtils.generateNumString(6);
            telVerifyInfo.setTelCode(telCode);
            telVerifyInfo.setTelCodeValidTime(telCodeValidTime);
            telVerifyInfo = telVerifyInfoService.saveOrUpdate(telVerifyInfo);

            String content = "【fund修改手机】验证码：" + telCode + "(有效期为3分钟)，请勿泄露给他人，如非本人操作，请忽略此信息。";
            MessageUtil.request(phoneNum, content);
            return null;
        } else {
            return "手机号输入不合规";
        }
    }
}
