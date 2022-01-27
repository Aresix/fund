package com.fundMonitor.repository;

import com.fundMonitor.entity.Log;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface LogRepository extends PagingAndSortingRepository<Log, Long> {
    List<Log> findByDeleted(boolean deleted);
    List<Log> findByDeleted(boolean deleted, Sort sort);
}
