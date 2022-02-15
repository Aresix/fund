package com.fundMonitor.repository;

import com.fundMonitor.constants.TaskPriority;
import com.fundMonitor.constants.TaskStatus;
import com.fundMonitor.constants.TaskType;
import com.fundMonitor.entity.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author lli.chen
 */
public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {
    List<Task> findByDeleted(boolean deleted);
    List<Task> findByDeleted(boolean deleted, Sort sort);
    List<Task> findByTaskStatusAndDeleted(TaskStatus taskStatus, boolean deleted, Sort sort);
    List<Task> findByTaskStatusAndDeleted(TaskStatus taskStatus, boolean deleted);
    List<Task> findByTaskTypeAndDeleted(TaskType taskType, boolean deleted, Sort sort);
    List<Task> findByTaskPriorityAndDeleted(TaskPriority taskPriority, boolean deleted, Sort sort);
//    List<Task> findByFinTimeAndDeleted(Timestamp finTime, boolean deleted, Sort sort);
    List<Task> findByCreatorAndDeleted(Long creator, boolean deleted, Sort sort);
//    List<Task> findByTaskStatusAndDeleted(TaskStatus taskStatus, boolean deleted);
}
