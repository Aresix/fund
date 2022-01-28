package com.fundMonitor.service;

import com.fundMonitor.constants.TaskStatus;
import com.fundMonitor.entity.Task;
import com.fundMonitor.repository.TaskRepository;
import com.fundMonitor.request.OrderRequest;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public List<Task> getTodayTasks(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new Task());
        return taskRepository.findByTaskStatusAndDeleted(TaskStatus.inProcess, false,sort);
    }

    public List<Task> getWaitingTasks(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order,new Task());
        List<Task> result = taskRepository.findByTaskStatusAndDeleted(TaskStatus.timeExceededLimit,false,sort);
        result.addAll(taskRepository.findByTaskStatusAndDeleted(TaskStatus.inProcess,false,sort));
        return result;
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
