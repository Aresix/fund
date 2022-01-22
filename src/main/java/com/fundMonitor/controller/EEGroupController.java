package com.fundMonitor.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
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
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        return new SuccessResponse<>(eEGroupService.deleteEntity(id));
    }
}
