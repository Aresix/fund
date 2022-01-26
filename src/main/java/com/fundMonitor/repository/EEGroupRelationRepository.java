package com.fundMonitor.repository;

import com.fundMonitor.entity.EEGroupRelation;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface EEGroupRelationRepository extends PagingAndSortingRepository<EEGroupRelation, Long> {
    List<EEGroupRelation> findByDeleted(boolean deleted);
    List<EEGroupRelation> findByDeleted(boolean deleted, Sort sort);

    List<EEGroupRelation> findByGroupId(Long groupID);
    EEGroupRelation findByAccountIdAndGroupIdAndDeleted(Long uid, Long gid , boolean deleted);
    List<EEGroupRelation> findByAccountId(Long uid);
}
