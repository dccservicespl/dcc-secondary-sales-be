package com.dcc.osheaapp.managerial.service;


import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.repository.*;
import com.dcc.osheaapp.service.UserService;
import com.dcc.osheaapp.vo.*;
import com.dcc.osheaapp.vo.views.Outlet;
import com.dcc.osheaapp.vo.views.PocketMISDto;
import com.dcc.osheaapp.vo.views.PurchaseSaleInputVo;
import com.dcc.osheaapp.vo.views.PurchaseSaleOutputVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Service
public class SaleAndPurchaseService {

    private static final Logger LOGGER = LogManager.getLogger(SaleAndPurchaseService.class);
    private final IUserCredRepository userCredRepository;
    private final ISumOfPurchaseAndSaleOfABa iSumOfPurchaseAndSaleOfABa;
    private final IUserAssotoationRepository iUserAssotoationRepository;

    private final ICounterStockManageRepository iCounterStockManageRepository;
    private final IOutletUserMappingRepository iOutletUserMappingRepository;

    private final ISalePurchaseSumOfBA iSalePurchaseSumOfBA;

    public  SaleAndPurchaseService(
            IUserCredRepository userCredRepository,
            ISumOfPurchaseAndSaleOfABa iSumOfPurchaseAndSaleOfABa,
            IUserAssotoationRepository iUserAssotoationRepository,
            ICounterStockManageRepository iCounterStockManageRepository,
            ISalePurchaseSumOfBA iSalePurchaseSumOfBA,
            IOutletUserMappingRepository iOutletUserMappingRepository
    ){
        this.userCredRepository = userCredRepository;
        this.iSumOfPurchaseAndSaleOfABa = iSumOfPurchaseAndSaleOfABa;
        this.iUserAssotoationRepository = iUserAssotoationRepository;
        this.iCounterStockManageRepository = iCounterStockManageRepository;
        this.iSalePurchaseSumOfBA = iSalePurchaseSumOfBA;
        this.iOutletUserMappingRepository = iOutletUserMappingRepository;
    }
@Autowired
IStockEntryRepository iStockEntryRepository;


    public ResponseEntity<ApiResponse> saleAndPurchase(PurchaseSaleInputVo input) {

//
        PurchaseSaleOutputVo  purchaseSale  =  iSalePurchaseSumOfBA.getSalePurchaseSum(input.fromDate,input.toDate,input.outletId);
        if (purchaseSale != null) {
            return new ResponseEntity<>(
                    new ApiResponse(200, "SUCCESS", "Data Found", purchaseSale), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new ApiResponse(204, "NOT_FOUND", "No Data Found", 0),
                    HttpStatus.OK);
        }

    }

    public ResponseEntity<ApiResponse> totalSumOfSaleAndPurchase(SumOfPurchaseAndSaleInputVo input){
        LOGGER.info("User Service ::Total  Sum of purchase and sale :: Entering....");


        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        Long loginUserId = userCredRepository.loginUserId(username);

        LOGGER.info("userid :: "+loginUserId);

//        List<OutletUserMappingVo> outletsUserRes = iOutletUserMappingRepository.getOutletMappingResult(input.getBdeId());
//        LOGGER.info("uom size() :: " +outletsUserRes.size());
//        Map<Long, OutletUserMappingVo> groupedObjects = outletsUserRes.stream()
//                .filter(o -> o.getOutlet() != null)
//                .collect(Collectors.toMap(o -> o.getOutlet().getId(), o -> o, BinaryOperator.minBy(Comparator.comparing(OutletUserMappingVo::getId))));
//
//
//        LOGGER.info("groupedObjects :: " +groupedObjects.keySet());
//        List<Long> uniqueOutletIds = new ArrayList<>(groupedObjects.keySet());
        // Sort and deduplicate by outlet_id with minimum id

//        Map<Long, Long> maxIdsByOutletId = outletsUserRes.stream()
//                .collect(Collectors.groupingBy(
//                        entry -> (Long) entry.getOutlet().getId(),
//                        Collectors.maxBy(Comparator.comparing(entry -> (Long) entry.getId()))
//                ))
//                .entrySet().stream()
//                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get().getId()));
//
//        // Extract unique outlet IDs
//        List<Long> uniqueOutletIds = new ArrayList<>(maxIdsByOutletId.keySet()).stream()
//                .sorted()
//                .collect(Collectors.toList());

        SumOfTotalPurchaseAndSaleOutputVo getDataList = iSumOfPurchaseAndSaleOfABa.getTotalPurchaseAndSaleOfBa( input.getBdeId(),input.getStartDate(), input.getEndDate());

        if (null != getDataList) {
            return new ResponseEntity<>(
                    new ApiResponse(200, "SUCCESS", "Data Found", getDataList),
                    HttpStatus.OK
            );
        } else if(null == getDataList) {
            return new ResponseEntity<>(
                    new ApiResponse(204, "SUCCESS", "No Content"),
                    HttpStatus.NO_CONTENT
            );
        }
        return new ResponseEntity<>(
                new ApiResponse(404, "NOT_FOUND", "Date Wise Purchase and Sale Total Not found"),
                HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<ApiResponse> fetchEditRequestCount(Long id){

        LOGGER.info("User Service :: Edit Request count :: Enter");
        int editCount = iUserAssotoationRepository.editRequestCount(id);

        if (editCount > 0) {
            LOGGER.info("EditCount :: " + editCount);

            return new ResponseEntity<>(
                    new ApiResponse(200, "SUCCESS", "Data Found", editCount), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new ApiResponse(204, "NOT_FOUND", "No Data Found"),
                    HttpStatus.OK);
        }

    }

    public ResponseEntity<ApiResponse> fetchEditRequestCountOfBas(Long id) {
        LOGGER.info("User Service :: Edit Request count :: Enter");
        int editCount = iUserAssotoationRepository.editRequestCountByBde(id);

        if (editCount > 0) {
            LOGGER.info("EditCount :: " + editCount);

            return new ResponseEntity<>(
                    new ApiResponse(200, "SUCCESS", "Data Found", editCount), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new ApiResponse(204, "NOT_FOUND", "No Data Found"),
                    HttpStatus.OK);
        }

    }
}
