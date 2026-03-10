package com.dcc.osheaapp.ojbso.service;

import com.dcc.osheaapp.ojbso.repo.ISODailyActivityMstRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class OfficialWorkService {

    private static final Logger LOGGER = LogManager.getLogger(OfficialWorkService.class);

    private ISODailyActivityMstRepository iOfficialWorkMstRepository;

    public OfficialWorkService(
            ISODailyActivityMstRepository iOfficialWorkMstRepository
    ){
        this.iOfficialWorkMstRepository = iOfficialWorkMstRepository;

    }
//    public List<OfficialWorkMstVo> fetchAllOfficialWorkType(){
//        LOGGER.info("OfficialWorkService :: fetchAllOfficialWorkType() ::called() ");
//        List<OfficialWorkMstVo> dataList =iOfficialWorkMstRepository.findAll();
//        if(dataList != null){
//            return dataList;
//        }else{
//            throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "OfficialWorkType" });
//        }
//    }
}
