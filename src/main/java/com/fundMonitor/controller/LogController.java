package com.fundMonitor.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.fundMonitor.entity.Log;
import com.fundMonitor.repository.LogRepository;
import com.fundMonitor.request.OrderRequest;
import com.fundMonitor.response.BaseResponse;
import com.fundMonitor.response.PageResponse;
import com.fundMonitor.response.SuccessResponse;
import com.fundMonitor.service.LogService;
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
@RequestMapping("/api/Log")
@Api(value = "/api/Log",tags = "监控图形相关接口")
public class LogController extends BaseController {
    @Autowired
    private LogService logService;

    @Autowired
    private LogRepository logRepository;

    @PostMapping
    @ApiOperation(value = "新建监控图形")
    public BaseResponse create(@RequestBody Log log) {
        return new SuccessResponse<>(logService.saveOrUpdate(log));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据Id获取监控图形")
    public BaseResponse getOne(@PathVariable Long id) {
        Log log = logService.getById(id);
        return new SuccessResponse<>(log);
    }

    @GetMapping("/list")
    @ApiOperation(value = "分页获取监控图形")
    public BaseResponse getList(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size,
                                @RequestParam(required = false) String searchCondition
    ) {
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
        List<Log> logs = logService.getLogs(page,size,order);
        if (!Strings.isNullOrEmpty(searchCondition)) {
            logs = logs.stream().filter(log -> log.toString().contains(searchCondition)).collect(Collectors.toList());
        }
        return new SuccessResponse<>(PageResponse.build(logs, pageable));
    }

    @PutMapping
    @ApiOperation(value = "更新监控图形")
    public BaseResponse update(@RequestBody Log log) {
        Log old = logService.getById(log.getId());
        Preconditions.checkNotNull(old);
        return new SuccessResponse<>(logService.saveOrUpdate(log));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据Id删除监控图形")
    public BaseResponse delete(@PathVariable Long id) {
        return new SuccessResponse<>(logService.deleteEntity(id));
    }
}
