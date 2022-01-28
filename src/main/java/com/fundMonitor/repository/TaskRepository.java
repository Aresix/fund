package com.fundMonitor.repository;

import com.fundMonitor.entity.Task;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {
    List<Task> findByDeleted(boolean deleted);
    List<Task> findByDeleted(boolean deleted, Sort sort);
    List<Task> findByTaskStatusAndDeleted(String taskStatus, boolean deleted, Sort sort);
    List<Task> findByCreatorAndDeleted(Long creator, boolean deleted, Sort sort);
}
