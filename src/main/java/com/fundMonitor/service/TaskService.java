package com.fundMonitor.service;

import com.fundMonitor.entity.Task;
import com.fundMonitor.repository.TaskRepository;
import com.fundMonitor.request.OrderRequest;
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
}
