package com.fundMonitor.service;

import com.fundMonitor.entity.Log;
import com.fundMonitor.repository.LogRepository;
import com.fundMonitor.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lli.chen
 */
@Service
public class LogService extends BasicService<Log, Long> {

    private LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        super(logRepository);
        this.logRepository = logRepository;
    }

    public List<Log> getLogs(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new Log());
        List<Log> result = logRepository.findByDeleted(false,sort);
        return result;
    }
}
