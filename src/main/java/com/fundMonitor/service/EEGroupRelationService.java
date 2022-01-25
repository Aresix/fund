package com.fundMonitor.service;

import com.fundMonitor.entity.EEGroupRelation;
import com.fundMonitor.repository.EEGroupRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Aresix
 * @date 2022/1/25 12:56
 */
public class EEGroupRelationService extends BasicService<EEGroupRelation, Long> {
    private EEGroupRelationRepository eeGroupRelationRepository;

//    @Autowired
//    public EEGroupRelationService(EEGroupRelationRepository eeGroupRelationRepository){
//        super(eeGroupRelationRepository);
//        this.eeGroupRelationRepository=eeGroupRelationRepository;
//    }

    public List<EEGroupRelation> getGroupRelation(Long groupID){
        return eeGroupRelationRepository.findByGroupID(groupID);
    }
}
