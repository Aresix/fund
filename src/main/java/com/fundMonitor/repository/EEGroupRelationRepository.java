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

    List<EEGroupRelation> findByGroupIdAndDeleted(Long groupID, boolean deleted);
    EEGroupRelation findByAccountIdAndGroupIdAndDeleted(Long uid, Long gid , boolean deleted);
    List<EEGroupRelation> findByAccountIdAndDeleted(Long uid, boolean deleted);
    int countByGroupIdAndDeleted(Long groupID, boolean deleted);
}
