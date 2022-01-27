package com.fundMonitor.service;

import com.fundMonitor.entity.MonitorGraph;
import com.fundMonitor.repository.MonitorGraphRepository;
import com.fundMonitor.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lli.chen
 */
@Service
public class MonitorGraphService extends BasicService<MonitorGraph, Long> {

    private MonitorGraphRepository monitorGraphRepository;

    @Autowired
    public MonitorGraphService(MonitorGraphRepository monitorGraphRepository) {
        super(monitorGraphRepository);
        this.monitorGraphRepository = monitorGraphRepository;
    }

    public List<MonitorGraph> getMonitorGraphs(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new MonitorGraph());
        List<MonitorGraph> result = monitorGraphRepository.findByDeleted(false,sort);
        return result;
    }
}
