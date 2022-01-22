package com.fundMonitor.repository;

import com.fundMonitor.entity.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {
    List<Task> findByDeleted(boolean deleted);
    List<Task> findByDeleted(boolean deleted, Sort sort);
}
