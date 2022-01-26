package com.fundMonitor.controller;

import com.fundMonitor.entity.Account;
import com.fundMonitor.entity.EEGroupRelation;
import com.fundMonitor.repository.EEGroupRelationRepository;
import com.fundMonitor.service.EEGroupRelationService;
import com.fundMonitor.service.UserService;
import com.google.common.base.Preconditions;
import com.fundMonitor.entity.EEGroup;
import com.fundMonitor.repository.EEGroupRepository;
import com.fundMonitor.request.OrderRequest;
import com.fundMonitor.response.BaseResponse;
import com.fundMonitor.response.PageResponse;
import com.fundMonitor.response.SuccessResponse;
import com.fundMonitor.service.EEGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lli.chen
 */
@RestController
@RequestMapping("/api/EEGroup")
@Api(value = "/api/EEGroup",tags = "小组相关接口")
public class EEGroupController extends BaseController {
    @Autowired
    private EEGroupService eEGroupService;

    @Autowired
    private EEGroupRepository eEGroupRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EEGroupRelationService eeGroupRelationService;

    @Autowired
    private EEGroupRelationRepository eeGroupRelationRepository;

    @PostMapping
    @ApiOperation(value = "新建小组")
    public BaseResponse create(@RequestBody EEGroup eEGroup) {
        return new SuccessResponse<>(eEGroupService.saveOrUpdate(eEGroup));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据Id获取小组")
    public BaseResponse getOne(@PathVariable Long id) {
        EEGroup eEGroup = eEGroupService.getById(id);
        return new SuccessResponse<>(eEGroup);
    }

    @GetMapping("/list")
    @ApiOperation(value = "分页获取小组")
    public BaseResponse getList(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size
    ) {
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
        List<EEGroup> eEGroups = eEGroupService.getEEGroups(page,size,order);
        return new SuccessResponse<>(PageResponse.build(eEGroups, pageable));
    }

    @PutMapping
    @ApiOperation(value = "更新小组")
    public BaseResponse update(@RequestBody EEGroup eEGroup) {
        EEGroup old = eEGroupService.getById(eEGroup.getId());
        Preconditions.checkNotNull(old);
            return new SuccessResponse<>(eEGroupService.saveOrUpdate(eEGroup));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据Id删除小组")
    public BaseResponse delete(@PathVariable Long id) {
        List<EEGroupRelation> relations = eeGroupRelationService.getGroupRelation(id);
        // 删除relation table中和与该组有关的所有数据
        for(EEGroupRelation relation : relations) {
            Preconditions.checkNotNull(relation);
            // TODO: 这里的SR怎么处理
            eeGroupRelationService.delete(relation);
        }
        return new SuccessResponse<>(eEGroupService.deleteEntity(id));
    }

    //===================组员======================
    /**
     * The functions below are added by Aresix
     * @return Display/Add/Delete the members in some group.
     */
    @GetMapping("/{id}/member_list")
    @ApiOperation(value = "根据Id获取组内成员")
    public BaseResponse getMembers(@PathVariable Long id){
        List<EEGroupRelation> relations = eeGroupRelationRepository.findByGroupIdAndDeleted(id,false);
        List<Account> accounts = new ArrayList<>();
        for(EEGroupRelation relation : relations){
            Account account = userService.getById(relation.getAccountId());
            accounts.add(account);
        }
        return new SuccessResponse<>(accounts);
    }

    @PutMapping("add_member")
    @ApiOperation(value = "在组内添加新成员")
    public BaseResponse addMember(@RequestBody EEGroupRelation relation){
        EEGroupRelation old = eeGroupRelationRepository.
                findByAccountIdAndGroupIdAndDeleted(relation.getAccountId(), relation.getGroupId(), false);
        if(old != null){
            return new BaseResponse<>("用户已在组内");
        }
        return new SuccessResponse<>(eeGroupRelationService.saveOrUpdate(relation));
    }

    @DeleteMapping("remove_member")
    @ApiOperation(value = "在组内删除成员")
    public BaseResponse deleteMember(@RequestBody EEGroupRelation relation){
        EEGroupRelation del = eeGroupRelationRepository.
                findByAccountIdAndGroupIdAndDeleted(relation.getAccountId(), relation.getGroupId(), false);
        eeGroupRelationService.delete(del);
        return new SuccessResponse<>();
    }

    @GetMapping("/{gid}/count")
    @ApiOperation(value = "组内人数")
    public BaseResponse countMember(@PathVariable Long gid){
        return new BaseResponse<>(eeGroupRelationRepository.countByGroupIdAndDeleted(gid,false));
    }
}
