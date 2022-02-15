package com.fundMonitor.controller;

import com.fundMonitor.constants.TaskPriority;
import com.fundMonitor.constants.TaskStatus;
import com.fundMonitor.constants.TaskType;
import com.fundMonitor.entity.Account;
import com.fundMonitor.entity.EETask;
import com.fundMonitor.repository.AccountRepository;
import com.fundMonitor.repository.EETaskRepository;
import com.fundMonitor.response.ErrorResponse;
import com.fundMonitor.service.EETaskService;
import com.fundMonitor.utils.DateUtil;
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
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @Autowired
    private EETaskService eeTaskService;
    @Autowired
    private EETaskRepository eeTaskRepository;
    @Autowired
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
        Pair<Task, List<Optional<Account>>> taskListPair = new Pair<>(
                task, getChargeListViaTask(task)
        );
        return new SuccessResponse<>(taskListPair);
    }

    @GetMapping("/list")
    @ApiOperation(value = "分页获取任务")
    public BaseResponse getList(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size

    ) {
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
        // 同时返回负责人
        List<Pair<Task,List<Optional<Account>>>> result = new ArrayList<>();
        List<Task> tasks = taskService.getTasks(page,size,order);
        // 根据task,找到责任人
        for (Task task : tasks){
            List<Optional<Account>> accounts = getChargeListViaTask(task);
            result.add(new Pair<>(task,accounts));
        }
//        if (!Strings.isNullOrEmpty(searchCondition)) {
//            tasks = tasks.stream().filter(task -> task.toString().contains(searchCondition)).collect(Collectors.toList());
//        }
        return new SuccessResponse<>(PageResponse.build(result, pageable));
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
    /**
     * All the functions below are added by Aresix
     * @return Operations about the person in charge and the corresponding group.
     */
    @GetMapping("/{id}/in_charge")
    @ApiOperation(value = "根据id获取该task的责任人")
    public BaseResponse getInChargeList(@PathVariable Long id){
        List<EETask> eeTasks = eeTaskRepository.findByTaskIDAndDeleted(id,false);
//        System.out.println("许墨\t"+eeTasks.size());
        List<Optional<Account>> accounts = new ArrayList<>();
        for(EETask task : eeTasks){
            accounts.add(accountRepository.findById(task.getTaskPersonInChargeID()));
        }
        return new SuccessResponse<>(accounts);
    }

    @PutMapping("assign")
    @ApiOperation(value = "分配责任人")
    public BaseResponse assign(@RequestBody EETask eeTask){
//        Preconditions.checkNotNull(eeTask.getTaskPersonInChargeID(),"未选中任何人");
        EETask task = eeTaskRepository.
                findByTaskIDAndTaskPersonInChargeIDAndDeleted
                        (eeTask.getTaskPersonInChargeID(), eeTask.getTaskID(), false);
        if (task != null) return new ErrorResponse("任务已分配");
        return new SuccessResponse<>(eeTaskService.saveOrUpdate(eeTask));
    }

    @DeleteMapping
    @ApiOperation(value = "删除责任人")
    public BaseResponse deleteOnePIC(@RequestBody EETask eeTask){
        if (eeTaskRepository.countByTaskIDAndDeleted(eeTask.getTaskID(),false)<=1)
            return new ErrorResponse("每个任务至少得有一位负责人");
        eeTaskService.deleteEntity(eeTask.getId());
        return new SuccessResponse<>();
    }

    @GetMapping("today")
    @ApiOperation(value = "分页获取只看今日")
    public BaseResponse getTodayList(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size

    ) {
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
//        List<Task> tasks = taskService.getTodayTasks(page,size,order);
        List<Task> tasks = taskService.getSomeDayTasks(
                DateUtil.YYMMDD.format(new Date()),page,size,order);
        return getListBaseResponse(pageable, tasks);
//    public BaseResponse getTodayList(){
//        return new SuccessResponse<>(taskService.getTodayTasks());
    }

    @GetMapping("tomorrow")
    @ApiOperation(value = "分页获取只看明日")
    public BaseResponse getTomorrowList(@RequestParam(required = false,defaultValue = "0") int page,
                                        @RequestParam(required = false,defaultValue = Integer_MAX_VALUE) int size){
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
        // 获取明日日期
        Date tomorrow = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tomorrow);
        calendar.add(Calendar.DATE,1);
        tomorrow=calendar.getTime();
        List<Task> tasks = taskService.getSomeDayTasks(
                DateUtil.YYMMDD.format(tomorrow),page,size,order);
        return getListBaseResponse(pageable, tasks);
    }

    @GetMapping("waitingList")
    @ApiOperation(value = "分页获取“隐藏已取消已完成”")
    public BaseResponse getWaitingList(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size

    ) {
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
        List<Task> tasks = taskService.getWaitingTasks(page,size,order);
        return getListBaseResponse(pageable, tasks);
    }

    @GetMapping("/{creatorID}/myList")
    @ApiOperation(value = "根据id分页获取“我发布的“")
    public BaseResponse getMyList(@RequestParam(required = false, defaultValue = "0") int page,
                                  @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size,
                                  @PathVariable Long creatorID

    ) {
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
        List<Task> tasks = taskService.getMyTasks(creatorID,page,size,order);
        return getListBaseResponse(pageable, tasks);
    }

    @GetMapping("/taskStatus/list")
    @ApiOperation(value = "分页获取：根据【任务状态】筛选的任务")
    public BaseResponse getSomeList(@RequestParam(required = false, defaultValue = "0") int page,
                                  @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size,
                                  TaskStatus taskStatus
                                  ) {
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
        List<Task> tasks = taskService.getSomeStatusTasks(taskStatus, page,size, order);
        return getListBaseResponse(pageable, tasks);
    }

    @GetMapping("/taskType/list")
    @ApiOperation(value = "分页获取：根据【任务类型】筛选的任务")
    public BaseResponse getSomeList(@RequestParam(required = false, defaultValue = "0") int page,
                                  @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size,
                                  TaskType taskType
                                  ) {
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
        List<Task> tasks = taskService.getSomeTypeTasks(taskType, page,size, order);
        return getListBaseResponse(pageable, tasks);
    }

    @GetMapping("/taskPriority/list")
    @ApiOperation(value = "分页获取：根据【任务重要性】筛选的任务")
    public BaseResponse getSomeList(@RequestParam(required = false, defaultValue = "0") int page,
                                  @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size,
                                  TaskPriority taskPriority
                                  ) {
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
        List<Task> tasks = taskService.getSomePriorityTasks(taskPriority, page,size, order);
        return getListBaseResponse(pageable, tasks);
    }

    /**
     * Return the list of person in charge based on some task.
     * @param task
     * @return The list of person in charge of this task.
     */
    private List<Optional<Account>> getChargeListViaTask(Task task){
        List<EETask> eeTasks = eeTaskRepository.findByTaskIDAndDeleted(task.getId(),false);
        List<Optional<Account>> accounts = new ArrayList<>();
        for (EETask eeTask : eeTasks){
            accounts.add(accountRepository.findById(eeTask.getTaskPersonInChargeID()));
        }
        return accounts;
    }

    private BaseResponse getListBaseResponse(Pageable pageable, List<Task> tasks) {
        List<Pair<Task,List<Optional<Account>>>> result = new ArrayList<>();
        for (Task task:tasks){
            List<Optional<Account>> accounts = getChargeListViaTask(task);
            result.add(new Pair<>(task,accounts));
        }
        return new SuccessResponse<>(PageResponse.build(result, pageable));
    }
}
