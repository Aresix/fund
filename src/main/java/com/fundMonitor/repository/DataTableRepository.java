package com.fundMonitor.repository;

import com.fundMonitor.entity.DataTable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface DataTableRepository extends PagingAndSortingRepository<DataTable, Long> {
    List<DataTable> findByDeleted(boolean deleted);
    List<DataTable> findByDeleted(boolean deleted, Sort sort);
}
