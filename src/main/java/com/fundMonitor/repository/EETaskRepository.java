package com.fundMonitor.repository;

import com.fundMonitor.entity.EETask;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface EETaskRepository extends PagingAndSortingRepository<EETask, Long> {
    List<EETask> findByDeleted(boolean deleted);
    List<EETask> findByDeleted(boolean deleted, Sort sort);
    List<EETask> findByTaskID(Long taskID);
    List<EETask> findByTaskPersonInChargeID(Long uid);
    EETask findByTaskIDAndTaskPersonInChargeID(Long uid, Long tid);
    Long countByTaskID(Long tid);
}
