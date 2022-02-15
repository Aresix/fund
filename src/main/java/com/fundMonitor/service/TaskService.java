package com.fundMonitor.service;

import com.fundMonitor.constants.TaskPriority;
import com.fundMonitor.constants.TaskStatus;
import com.fundMonitor.constants.TaskType;
import com.fundMonitor.entity.Task;
import com.fundMonitor.repository.TaskRepository;
import com.fundMonitor.request.OrderRequest;
import com.fundMonitor.utils.DateUtil;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lli.chen
 */
@Service
public class TaskService extends BasicService<Task, Long> {

    private TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        super(taskRepository);
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new Task());
        List<Task> result = taskRepository.findByDeleted(false,sort);
        return result;
    }

    public List<Task> getSomeDayTasks(String date, int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new Task());
        List<Task> tasks =  taskRepository.findByTaskStatusAndDeleted(TaskStatus.inProcess,false,sort);
        tasks.addAll(taskRepository.findByTaskStatusAndDeleted(TaskStatus.timeExceededLimit,false,sort));
        List<Task> result = new ArrayList<>();
        for(Task task:tasks){
            if (task.getFinTime()==null) continue;
            String finTime = DateUtil.YYMMDD.format(task.getFinTime());
            if (date.equals(finTime)){
                result.add(task);
            }
        }
        return result;
//        return taskRepository.findByTaskStatusAndDeleted(TaskStatus.inProcess, false,sort);
    }

    public List<Task> getWaitingTasks(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order,new Task());
        List<Task> result = taskRepository.findByTaskStatusAndDeleted(TaskStatus.timeExceededLimit,false,sort);
        result.addAll(taskRepository.findByTaskStatusAndDeleted(TaskStatus.inProcess,false,sort));
        return result;
    }


    public List<Task> getWaitingTasks() {
        List<Task> result = taskRepository.findByTaskStatusAndDeleted(TaskStatus.timeExceededLimit, false);
        result.addAll(taskRepository.findByTaskStatusAndDeleted(TaskStatus.inProcess, false));
        return result;
    }

    public List<Task> getSomeStatusTasks(TaskStatus taskStatus, int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new Task());
        return taskRepository.findByTaskStatusAndDeleted(taskStatus,false,sort);
    }

    public List<Task> getSomeTypeTasks(TaskType taskType, int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new Task());
        return taskRepository.findByTaskTypeAndDeleted(taskType,false,sort);
    }

    public List<Task> getSomePriorityTasks(TaskPriority taskPriority, int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new Task());
        return taskRepository.findByTaskPriorityAndDeleted(taskPriority,false,sort);
    }
    /**
     * 仅测试用
     */
//    public List<Task> getTodayTasks(){
//        return taskRepository.findByTaskStatusAndDeleted(TaskStatus.inProcess,false);
//    }

    public List<Task> getMyTasks(Long creator, int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new Task());
        List<Task> result = taskRepository.findByCreatorAndDeleted(creator, false, sort);
        return result;
    }
}
