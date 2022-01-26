package com.fundMonitor.service;

import com.fundMonitor.entity.EETask;
import com.fundMonitor.repository.EETaskRepository;
import com.fundMonitor.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lli.chen
 */
@Service
public class EETaskService extends BasicService<EETask, Long> {

    private EETaskRepository eETaskRepository;

    @Autowired
    public EETaskService(EETaskRepository eETaskRepository) {
        super(eETaskRepository);
        this.eETaskRepository = eETaskRepository;
    }

    public List<EETask> getEETasks(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new EETask());
        List<EETask> result = eETaskRepository.findByDeleted(false,sort);
        return result;
    }

    public List<EETask> getAllPersonInCharge(Long tid){
        return eETaskRepository.findByTaskID(tid);
    }
}
