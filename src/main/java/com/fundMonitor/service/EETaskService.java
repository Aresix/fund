package com.fundMonitor.service;

import com.fundMonitor.entity.Account;
import com.fundMonitor.entity.EETask;
import com.fundMonitor.entity.Task;
import com.fundMonitor.repository.EETaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aresix
 * @date 2022/1/25 17:10
 */
public class EETaskService extends BasicService<EETask, Long> {
    private EETaskRepository eeTaskRepository;

//    @Autowired
//    public EETaskService(EETaskRepository eeTaskRepository){
//        super(eeTaskRepository);
//        this.eeTaskRepository = eeTaskRepository;
//    }

    public List<EETask> getAllPersonInCharge(Long tid){
        return eeTaskRepository.findByTaskID(tid);
    }
}
