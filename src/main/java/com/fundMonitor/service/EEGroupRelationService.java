package com.fundMonitor.service;

import com.fundMonitor.entity.EEGroupRelation;
import com.fundMonitor.repository.EEGroupRelationRepository;
import com.fundMonitor.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lli.chen
 */
@Service
public class EEGroupRelationService extends BasicService<EEGroupRelation, Long> {

    private EEGroupRelationRepository eEGroupRelationRepository;

    @Autowired
    public EEGroupRelationService(EEGroupRelationRepository eEGroupRelationRepository) {
        super(eEGroupRelationRepository);
        this.eEGroupRelationRepository = eEGroupRelationRepository;
    }

    public List<EEGroupRelation> getEEGroupRelations(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new EEGroupRelation());
        List<EEGroupRelation> result = eEGroupRelationRepository.findByDeleted(false,sort);
        return result;
    }

    public List<EEGroupRelation> getGroupRelation(Long groupID){
        return eEGroupRelationRepository.findByGroupId(groupID);
    }
}
