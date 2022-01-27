package com.fundMonitor.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.fundMonitor.entity.DataTable;
import com.fundMonitor.repository.DataTableRepository;
import com.fundMonitor.request.OrderRequest;
import com.fundMonitor.response.BaseResponse;
import com.fundMonitor.response.PageResponse;
import com.fundMonitor.response.SuccessResponse;
import com.fundMonitor.service.DataTableService;
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
@RequestMapping("/api/DataTable")
@Api(value = "/api/DataTable",tags = "数据表格相关接口")
public class DataTableController extends BaseController {
    @Autowired
    private DataTableService dataTableService;

    @Autowired
    private DataTableRepository dataTableRepository;

    @PostMapping
    @ApiOperation(value = "新建小组")
    public BaseResponse create(@RequestBody DataTable dataTable) {
        return new SuccessResponse<>(dataTableService.saveOrUpdate(dataTable));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据Id获取小组")
    public BaseResponse getOne(@PathVariable Long id) {
        DataTable dataTable = dataTableService.getById(id);
        return new SuccessResponse<>(dataTable);
    }

    @GetMapping("/list")
    @ApiOperation(value = "分页获取小组")
    public BaseResponse getList(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size,
                                @RequestParam(required = false) String searchCondition
    ) {
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
        List<DataTable> dataTables = dataTableService.getDataTables(page,size,order);
        if (!Strings.isNullOrEmpty(searchCondition)) {
            dataTables = dataTables.stream().filter(dataTable -> dataTable.toString().contains(searchCondition)).collect(Collectors.toList());
        }
        return new SuccessResponse<>(PageResponse.build(dataTables, pageable));
    }

    @PutMapping
    @ApiOperation(value = "更新小组")
    public BaseResponse update(@RequestBody DataTable dataTable) {
        DataTable old = dataTableService.getById(dataTable.getId());
        Preconditions.checkNotNull(old);
        return new SuccessResponse<>(dataTableService.saveOrUpdate(dataTable));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据Id删除小组")
    public BaseResponse delete(@PathVariable Long id) {
        return new SuccessResponse<>(dataTableService.deleteEntity(id));
    }
}
