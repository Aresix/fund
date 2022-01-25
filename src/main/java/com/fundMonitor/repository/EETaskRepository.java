package com.fundMonitor.repository;

import com.fundMonitor.entity.EETask;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Aresix
 * @date 2022/1/25 16:53
 */
public interface EETaskRepository extends PagingAndSortingRepository<EETask, Long> {
    List<EETask> findByTaskID(Long taskID);
    List<EETask> findByTaskPersonInChargeID(Long uid);
    EETask findByTaskIDAndTaskPersonInChargeID(Long uid, Long tid);
    Long countByTaskID(Long tid);
}
