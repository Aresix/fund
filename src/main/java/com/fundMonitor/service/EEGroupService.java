package com.fundMonitor.service;

import com.fundMonitor.entity.EEGroup;
import com.fundMonitor.repository.EEGroupRepository;
import com.fundMonitor.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lli.chen
 */
@Service
public class EEGroupService extends BasicService<EEGroup, Long> {

    private EEGroupRepository eEGroupRepository;

    @Autowired
    public EEGroupService(EEGroupRepository eEGroupRepository) {
        super(eEGroupRepository);
        this.eEGroupRepository = eEGroupRepository;
    }

    public List<EEGroup> getEEGroups(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new EEGroup());
        List<EEGroup> result = eEGroupRepository.findByDeleted(false,sort);
        return result;
    }
}
