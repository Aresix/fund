package com.fundMonitor.repository;

import com.fundMonitor.entity.MonitorGraph;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface MonitorGraphRepository extends PagingAndSortingRepository<MonitorGraph, Long> {
    List<MonitorGraph> findByDeleted(boolean deleted);
    List<MonitorGraph> findByDeleted(boolean deleted, Sort sort);
}
