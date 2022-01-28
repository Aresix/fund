package com.fundMonitor.repository;

import com.fundMonitor.entity.TelVerifyInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface TelVerifyInfoRepository extends PagingAndSortingRepository<TelVerifyInfo, Long> {
    List<TelVerifyInfo> findByDeleted(boolean deleted);
    List<TelVerifyInfo> findByDeleted(boolean deleted, Sort sort);

    TelVerifyInfo findByPhoneNumAndDeleted(String phone, boolean deleted);
}
