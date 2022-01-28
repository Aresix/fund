package com.fundMonitor.service;

import com.fundMonitor.entity.TelVerifyInfo;
import com.fundMonitor.repository.TelVerifyInfoRepository;
import com.fundMonitor.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lli.chen
 */
@Service
public class TelVerifyInfoService extends BasicService<TelVerifyInfo, Long> {

    private TelVerifyInfoRepository telVerifyInfoRepository;

    @Autowired
    public TelVerifyInfoService(TelVerifyInfoRepository telVerifyInfoRepository) {
        super(telVerifyInfoRepository);
        this.telVerifyInfoRepository = telVerifyInfoRepository;
    }

    public List<TelVerifyInfo> getTelVerifyInfos(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new TelVerifyInfo());
        List<TelVerifyInfo> result = telVerifyInfoRepository.findByDeleted(false,sort);
        return result;
    }
}
