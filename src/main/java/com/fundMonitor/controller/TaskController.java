package com.fundMonitor.controller;

import com.fundMonitor.entity.Account;
import com.fundMonitor.entity.EETask;
import com.fundMonitor.repository.AccountRepository;
import com.fundMonitor.repository.EETaskRepository;
import com.fundMonitor.response.ErrorResponse;
import com.fundMonitor.service.EETaskService;
import com.google.common.base.Preconditions;
import com.fundMonitor.entity.Task;
import com.fundMonitor.repository.TaskRepository;
import com.fundMonitor.request.OrderRequest;
import com.fundMonitor.response.BaseResponse;
import com.fundMonitor.response.PageResponse;
import com.fundMonitor.response.SuccessResponse;
import com.fundMonitor.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author lli.chen
 */
@RestController
@RequestMapping("/api/Task")
@Api(value = "/api/Task",tags = "任务相关接口")
public class TaskController extends BaseController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    private EETaskService eeTaskService;
    private EETaskRepository eeTaskRepository;
    private AccountRepository accountRepository;

    @PostMapping
    @ApiOperation(value = "新建任务")
    public BaseResponse create(@RequestBody Task task) {
        return new SuccessResponse<>(taskService.saveOrUpdate(task));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据Id获取任务")
    public BaseResponse getOne(@PathVariable Long id) {
        Task task = taskService.getById(id);
        return new SuccessResponse<>(task);
    }

    @GetMapping("/list")
    @ApiOperation(value = "分页获取任务")
    public BaseResponse getList(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size

    ) {
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
        List<Task> tasks = taskService.getTasks(page,size,order);
//        if (!Strings.isNullOrEmpty(searchCondition)) {
//            tasks = tasks.stream().filter(task -> task.toString().contains(searchCondition)).collect(Collectors.toList());
//        }
        return new SuccessResponse<>(PageResponse.build(tasks, pageable));
    }

    @PutMapping
    @ApiOperation(value = "更新任务")
    public BaseResponse update(@RequestBody Task task) {
        Task old = taskService.getById(task.getId());
        Preconditions.checkNotNull(old);
        return new SuccessResponse<>(taskService.saveOrUpdate(task));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据Id删除任务")
    public BaseResponse delete(@PathVariable Long id) {
        return new SuccessResponse<>(taskService.deleteEntity(id));
    }

    //===================责任人====================
    @GetMapping("/{id}/in_charge")
    @ApiOperation(value = "根据id获取该task的责任人")
    public BaseResponse getInChargeList(@PathVariable Long id){
        List<EETask> eeTasks = eeTaskService.getAllPersonInCharge(id);
        List<Optional<Account>> accounts = new ArrayList<>();
        for(EETask task : eeTasks){
            accounts.add(accountRepository.findById(task.getTaskPersonInChargeID()));
        }
        return new BaseResponse<>(accounts);
    }

    @PutMapping("assign")
    @ApiOperation(value = "分配责任人")
    public BaseResponse assign(@RequestBody EETask eeTask){
        Preconditions.checkNotNull(eeTask.getTaskPersonInChargeID(),"未选中任何人");
        EETask task = eeTaskRepository.
                findByTaskIDAndTaskPersonInChargeID(eeTask.getTaskPersonInChargeID(), eeTask.getTaskID());
        if (task != null) return new ErrorResponse("任务已分配");
        return new SuccessResponse<>(eeTaskService.saveOrUpdate(eeTask));
    }

    @DeleteMapping
    @ApiOperation(value = "删除责任人")
    public BaseResponse deleteOnePIC(@RequestBody EETask eeTask){
        if (eeTaskRepository.countByTaskID(eeTask.getTaskID())==1)
            return new ErrorResponse("每个任务至少得有一位负责人");
        eeTaskService.deleteEntity(eeTask.getId());
        return new SuccessResponse<>();
    }
}