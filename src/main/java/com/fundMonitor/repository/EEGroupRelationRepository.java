package com.fundMonitor.repository;

import com.fundMonitor.entity.Account;
import com.fundMonitor.entity.EEGroupRelation;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Aresix
 * @date 2022/1/25 12:48
 */
public interface EEGroupRelationRepository extends PagingAndSortingRepository<EEGroupRelation, Long> {
    List<EEGroupRelation> findByGroupID(Long groupID);
    EEGroupRelation findByAccountIDAndGroupID(Long uid, Long gid);
    List<EEGroupRelation> findByAccountID(Long uid);
}
