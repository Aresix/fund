package com.fundMonitor.controller;

import com.fundMonitor.constants.RoleType;
import com.fundMonitor.entity.Account;
import com.fundMonitor.entity.EEGroup;
import com.fundMonitor.entity.EEGroupRelation;
import com.fundMonitor.repository.AccountRepository;
import com.fundMonitor.repository.EEGroupRelationRepository;
import com.fundMonitor.repository.EEGroupRepository;
import com.fundMonitor.request.LoginRequest;
import com.fundMonitor.request.OrderRequest;
import com.fundMonitor.response.BaseResponse;
import com.fundMonitor.response.ErrorResponse;
import com.fundMonitor.response.SuccessResponse;
import com.fundMonitor.security.UserAuthenticationProvider;
import com.fundMonitor.utils.EntityUtils;
import com.google.common.base.Preconditions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private EEGroupRelationRepository eeGroupRelationRepository;
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
        Account old = accountRepository.findByLoginName(account.getLoginName());
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
        Account old = accountRepository.findByLoginName(account.getLoginName());
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
}
