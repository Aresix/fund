package com.fundMonitor.service;

import com.fundMonitor.entity.DataTable;
import com.fundMonitor.repository.DataTableRepository;
import com.fundMonitor.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lli.chen
 */
@Service
public class DataTableService extends BasicService<DataTable, Long> {

    private DataTableRepository dataTableRepository;

    @Autowired
    public DataTableService(DataTableRepository dataTableRepository) {
        super(dataTableRepository);
        this.dataTableRepository = dataTableRepository;
    }

    public List<DataTable> getDataTables(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new DataTable());
        List<DataTable> result = dataTableRepository.findByDeleted(false,sort);
        return result;
    }
}
