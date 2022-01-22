package com.fundMonitor.repository;

import com.fundMonitor.entity.EEGroup;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface EEGroupRepository extends PagingAndSortingRepository<EEGroup, Long> {
    List<EEGroup> findByDeleted(boolean deleted);
    List<EEGroup> findByDeleted(boolean deleted, Sort sort);
}
