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
    List<EETask> findByTaskIDAndDeleted(Long taskID,boolean deleted);
    List<EETask> findByTaskPersonInChargeIDAndDeleted(Long uid,boolean deleted);
    EETask findByTaskIDAndTaskPersonInChargeIDAndDeleted(Long uid, Long tid, boolean deleted);
    Long countByTaskIDAndDeleted(Long tid,boolean deleted);
}
