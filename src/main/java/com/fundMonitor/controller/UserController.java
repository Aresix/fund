package com.fundMonitor.controller;

import com.fundMonitor.constants.RoleType;
import com.fundMonitor.entity.Account;
import com.fundMonitor.entity.EEGroup;
import com.fundMonitor.entity.EEGroupRelation;
import com.fundMonitor.entity.TelVerifyInfo;
import com.fundMonitor.repository.AccountRepository;
import com.fundMonitor.repository.EEGroupRelationRepository;
import com.fundMonitor.repository.EEGroupRepository;
import com.fundMonitor.repository.TelVerifyInfoRepository;
import com.fundMonitor.request.LoginRequest;
import com.fundMonitor.request.OrderRequest;
import com.fundMonitor.response.BaseResponse;
import com.fundMonitor.response.ErrorResponse;
import com.fundMonitor.response.SuccessResponse;
import com.fundMonitor.security.UserAuthenticationProvider;
import com.fundMonitor.service.UserService;
import com.fundMonitor.utils.EntityUtils;
import com.fundMonitor.utils.MailUtils;
import com.fundMonitor.utils.UserUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author lli.chen
 */
@RestController
@RequestMapping("api/users")
@Api(value = "/api/users", tags = "用户相关接口")
public class UserController extends BaseController {
    @Autowired
    private UserAuthenticationProvider authenticationProvider;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EEGroupRelationRepository eeGroupRelationRepository;

    @Autowired
    private TelVerifyInfoRepository telVerifyInfoRepository;

    @Autowired
    private EEGroupRepository eeGroupRepository;

    /**
     * 登陆？
     */
    @PostMapping("/login")
    @ApiOperation(value = "登陆")
    public void login(@RequestBody LoginRequest request) {
        Preconditions.checkNotNull(request.getUsername(), "登陆账号不能为空。");
        Preconditions.checkNotNull(request.getPassword(), "密码不能为空。");
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authenticationProvider.authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @PostMapping("create")
    @ApiOperation(value = "新建用户")
    public BaseResponse create(@RequestBody Account account) {
        Preconditions.checkNotNull(account.getLoginName(), "用户名不能为空。");
        Account old = accountRepository.findByLoginNameAndDeleted(account.getLoginName(), false);
        if (old != null) {
            return new ErrorResponse("用户名不能重复。");
        }
        return new SuccessResponse<>(userService.saveOrUpdate(account));
    }

    @GetMapping("/me")
    @ApiOperation(value = "获取个人信息相关")
    public BaseResponse me() {
        Account account = getCurrentAccount();
        if (account.isDeleted()) {
            return new ErrorResponse("该账户已被停用。");
        }

        return new SuccessResponse<>(account);

    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据 id 获取用户")
    public BaseResponse getOne(@PathVariable Long id) {
        Account account = userService.getById(id);
        return new SuccessResponse<>(account);
    }

    @GetMapping("/list")
    @ApiOperation(value = "分页获取用户")
    public BaseResponse getList(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size,
                                @RequestParam(required = false) String loginName,
                                @RequestParam(required = false) RoleType roleType) {
        List<OrderRequest> order = null;
        Page<Account> customerinfos = userService.getAccountInfos(loginName, roleType, page, size, order);
        return new SuccessResponse<>(customerinfos);
    }

    @PutMapping("/password")
    @ApiOperation(value = "修改密码")
    public BaseResponse changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        Account account = getCurrentAccount();
        if (oldPassword.equals(account.getPassword())) {
            account.setPassword(newPassword);
            return new SuccessResponse<>(userService.saveOrUpdate(account));
        } else {
            return new ErrorResponse<>("旧密码输入错误。");
        }
    }

    @PutMapping
    @ApiOperation(value = "更新用户信息")
    public BaseResponse updateInfo(@RequestBody Account account) {
        Account currentAccount = userService.getById(account.getId());
        Account old = accountRepository.findByLoginNameAndDeleted(account.getLoginName(), false);
        if (old != null && old.getId() != account.getId()) {
            return new ErrorResponse("登陆账号不能重复。");
        }
        EntityUtils.copyProperties(account, currentAccount);

        return new SuccessResponse<>(userService.saveOrUpdate(currentAccount));
    }

    @PutMapping("/reset_password")
    @ApiOperation(value = "重置密码")
    public BaseResponse updatePassword(@RequestParam Long id) {
        Account old = userService.getById(id);
        Preconditions.checkNotNull(old);
        old.setPassword("123456");
        return new SuccessResponse<>(userService.saveOrUpdate(old));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据 id 删除用户")
    public BaseResponse deleteOne(@PathVariable Long id) {
        userService.deleteEntity(id);
        return new SuccessResponse<>();
    }

    /**
     * Added by Aresix
     * @return All groups where the user belongs to.
     */
    @GetMapping("/{id}/all_groups")
    @ApiOperation(value = "用户所在的组")
    public BaseResponse getGroups(@PathVariable Long id) {
        List<EEGroupRelation> relations = eeGroupRelationRepository.findByAccountIdAndDeleted(id, false);
        List<Optional<EEGroup>> groups = new ArrayList<>();
        for(EEGroupRelation relation : relations){
            System.out.println("group信息为："+relation.getGroupId()+"\t\t123\n");
            Optional<EEGroup> group = eeGroupRepository.findById(relation.getGroupId());
            groups.add(group);
        }
        return new SuccessResponse<>(groups);
    }

    @ApiOperation(value = "获取验证码")
    @GetMapping("/telCode")
    public BaseResponse getCode(@RequestParam String phoneNum) {
        TelVerifyInfo telVerifyInfo = telVerifyInfoRepository.findByPhoneNumAndDeleted(phoneNum, false);
        if (telVerifyInfo == null) {
            telVerifyInfo = new TelVerifyInfo();
            telVerifyInfo.setPhoneNum(phoneNum);
        }
        String errorMessage = sendTelCode(telVerifyInfo, phoneNum);
        if (!Strings.isNullOrEmpty(errorMessage)) {
            return new ErrorResponse(errorMessage);
        }
        return new SuccessResponse();
    }

    @ApiOperation(value = "验证验证码")
    @PostMapping("/verCode")
    public BaseResponse verCode(@RequestParam String phoneNum,
                                @RequestParam String code) {
        TelVerifyInfo telVerifyInfo = telVerifyInfoRepository.findByPhoneNumAndDeleted(phoneNum, false);
        System.out.println(telVerifyInfo);
        if(telVerifyInfo == null || telVerifyInfo.getTelCodeValidTime().getTime() < System.currentTimeMillis()){
            return new ErrorResponse("验证码已过期,请重新获取验证码");
        }else {
            if (!telVerifyInfo.getTelCode().equals(code)) {
                return new ErrorResponse("验证码错误");
            }
        }
        return new SuccessResponse();
    }

    @ApiOperation(value = "测试邮箱")
    @PostMapping("/testMail")
    public BaseResponse testMail(@RequestParam String mail) {
        MailUtils.send("测试","这是一个测试邮件","hejinotify",mail);
        return new SuccessResponse();
    }

    @ApiOperation(value = "设置邮箱告警")
    @PostMapping("/email")
    public BaseResponse email() {
        Account account = UserUtil.currentUser();
        account.setEmailNotification(true);
        userService.saveOrUpdate(account);
        return new SuccessResponse();
    }

    @ApiOperation(value = "设置电话告警")
    @PostMapping("/phone")
    public BaseResponse phone() {
        Account account = UserUtil.currentUser();
        account.setPhoneNotification(true);
        userService.saveOrUpdate(account);
        return new SuccessResponse();
    }
}
