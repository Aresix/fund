package com.fundMonitor.repository;

import com.fundMonitor.constants.RoleType;
import com.fundMonitor.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author lli.chen
 */
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
    Page<Account> findByDeleted(boolean deleted, Pageable pageable);
    Page<Account> findByLoginNameIsLikeAndRoleTypeInAndDeleted(String loginName,RoleType[] roleTypes,boolean deleted, Pageable pageable);
    Page<Account> findByRoleTypeInAndDeleted(RoleType[] roleTypes,boolean deleted, Pageable pageable);
    Account findByLoginNameAndDeleted(String loginName, boolean deleted);

    Account findByPhoneAndDeleted(String phone, boolean deleted);
}
