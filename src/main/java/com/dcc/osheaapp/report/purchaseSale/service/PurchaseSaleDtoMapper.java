package com.dcc.osheaapp.report.purchaseSale.service;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.service.Util;
import com.dcc.osheaapp.report.common.model.UserChainFlat;
import com.dcc.osheaapp.repository.IDropdownMastereRepository;
import com.dcc.osheaapp.repository.IOutletChannelRepository;
import com.dcc.osheaapp.repository.IProductRepository;
import com.dcc.osheaapp.service.*;
import com.dcc.osheaapp.vo.*;
import com.dcc.osheaapp.vo.views.UserChain;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class PurchaseSaleDtoMapper {

    @PersistenceContext
    EntityManager em;
    private static final Logger LOGGER = LogManager.getLogger(PurchaseSaleDtoMapper.class);
    private final StockService stockService;
    private final UserChainService userChainService;
    private final OutletService outletService;
    private final MasterDataService masterDataService;

    private final UserService userService;
    private final Util util;
    private final IDropdownMastereRepository dropdownMastereRepository;

    private final IProductRepository productRepository;

    @Autowired
    public PurchaseSaleDtoMapper(
            StockService stockService,
            UserChainService userChainService,
            OutletService outletService,
            MasterDataService masterDataService,
            UserService userService, Util util,
            IDropdownMastereRepository dropdownMastereRepository,
            IProductRepository productRepository) {
        this.stockService = stockService;
        this.userChainService = userChainService;
        this.outletService = outletService;
        this.masterDataService = masterDataService;
        this.userService = userService;
        this.util = util;
        this.dropdownMastereRepository = dropdownMastereRepository;

        this.productRepository = productRepository;
    }

    public List<PurchaseRecordDto> toDtoByOutletAndPurchaseDateRange(
            Date fromDate, Date toDate, Long outletId, List<String> activity, List<String> stockStatus) {
        List<StockEntryVo> purchases =
                stockService.findStockEntryByDateRangeAndActivityTypeAndStockStatus(
                        fromDate, toDate, activity, outletId, stockStatus);
        if (purchases.isEmpty()) return Collections.emptyList();
        purchases.sort(Comparator.comparingLong(e -> e.getUserId().getId()));
        List<Long> purchaseIds =
                purchases.stream().map(e -> e.getUserId().getId()).collect(Collectors.toList());

        List<Long> distinctUsers =
                purchases.stream().map(e -> e.getUserId().getId()).distinct().collect(Collectors.toList());
//    LOGGER.info("PurchaseSaleDtoMapper  === toDtoByOutletAndPurchaseDateRange distinctUsers.size ==>"+distinctUsers.size());
        List<UserChainFlat> chains = new ArrayList<>();

        OutletVo outlet =
                outletService
                        .findOutlet(outletId)
                        .orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[]{"Outlet"}));
        Map<Long, List<StockEntryVo>> purchaseMaps = new HashMap<>();

//    final long startTime = System.currentTimeMillis();
        Map<Long, UserChainFlat> chainMap = userChainService.retrieveChains(distinctUsers).stream().collect(Collectors.toMap(UserChainFlat::getBaId, e -> e));
        distinctUsers.forEach(
                user -> {
                    Pair<Integer, Integer> purchaseListRange = findListRange(purchaseIds, user);
                    List<StockEntryVo> userPurchase =
                            purchases.subList(purchaseListRange.first, purchaseListRange.second + 1);
                    purchaseMaps.put(user, userPurchase);
//          em.clear();
                    chains.add(chainMap.get(user) == null ? new UserChainFlat(): chainMap.get(user));
                });

//    final long elapsedTimeMillis = System.currentTimeMillis() - startTime;

//    LOGGER.info("Elapased::: " + elapsedTimeMillis);
        List<PurchaseRecordDto> rootList = new ArrayList<>();

        purchaseMaps.forEach(
                (key, value) -> {
//          Map<Long, UserChain> chainMap = userChainService.getAssociatedUserChainMap(key);
                    PurchaseRecordDto.PurchaseRecordDtoBuilder chainDtoBuilder =
                            new PurchaseRecordDto.PurchaseRecordDtoBuilder();
                    DropdownMasterVo zone =
                            dropdownMastereRepository
                                    .findByIdAndFieldType(outlet.getCompanyZone(), "zone")
                                    .orElse(null);
                    DropdownMasterVo outletType = dropdownMastereRepository.findByIdAndFieldType(outlet.getOutletChannel(), "outlet_channel")
                            .orElse(null);
                    PurchaseRecordDto chainDto =
                            chainDtoBuilder
                                    .zone(zone == null ? null : zone.getFieldName())
                                    .outletErpId(outlet.getOutletCode())
                                    .outletName(outlet.getOutletName())
                                    .outletType(outletType == null ? null : outletType.getFieldName())
                                    .marketName(outlet.getMarket())
                                    .city(outlet.getCity())
                                    .state(outlet.getRegionName())
                                    .build();
                    for (StockEntryVo entry : value) {
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        int index = Util.binarySearch(distinctUsers, entry.getUserId().getId());
                        UserChainFlat chainDto1 = index == -1 ? new UserChainFlat() : chains.get(index);
                        PurchaseRecordDto.PurchaseRecordDtoBuilder entryDtoBuilder =
                                PurchaseRecordDto.builder()
                                        .zone(chainDto.getZone())
                                        .baName(chainDto1.getBaName())
                                        .baCode(chainDto1.getBaCode())
                                        .so(chainDto1.getSo())
                                        .bde(chainDto1.getBde())
                                        .asm(chainDto1.getAsm())
                                        .ase(chainDto1.getAse())
                                        .bdm(chainDto1.getBdm())
                                        .zsm(chainDto1.getZsm())
                                        .nsm(chainDto1.getNsm())
                                        .md(chainDto1.getMd())
                                        .outletErpId(chainDto.getOutletErpId())
                                        .outletName(chainDto.getOutletName())
                                        .outletType(chainDto.getOutletType())
                                        .marketName(chainDto.getMarketName())
                                        .city(chainDto.getCity())
                                        .state(chainDto.getState())
                                        .month(
                                                LocalDate.ofInstant(
                                                                entry.getTransactionDate().toInstant(), ZoneId.of("Asia/Kolkata"))
                                                        .getMonth()
                                                        .getValue())
                                        .invoice(entry.getInvoiceNo())
                                        .inwardDate(dateFormat.format(entry.getTransactionDate()))
                                        .inwardTime(timeFormat.format(entry.getTransactionDate()));

//            int month =
//                LocalDate.ofInstant(entry.getUpdatedOn().toInstant(), ZoneId.of("Asia/Kolkata"))
//                    .getMonth()
//                    .getValue();
                        if (entry.getActivityType().contains("return")) {
                            entryDtoBuilder
                                    .activityType(entry.getActivityType())
                                    .stockStatus(entry.getStockStatus());
                        }
                        PurchaseRecordDto entryDto = entryDtoBuilder.build();

                        List<StockEntryDtlVo> entryDtl = stockService.findStockEntryDetails(entry.getId());
                        for (StockEntryDtlVo dtl : entryDtl) {
                            ProductVo pUnit = productRepository.pSizeUnit(dtl.getProductId().getId());
                            PurchaseRecordDto.PurchaseRecordDtoBuilder dtlDtoBuilder =
                                    PurchaseRecordDto.builder()
                                            .zone(chainDto.getZone())
                                            .baName(entryDto.getBaName())
                                            .baCode(entryDto.getBaCode())
                                            .so(entryDto.getSo())
                                            .bde(entryDto.getBde())
                                            .asm(entryDto.getAsm())
                                            .bdm(entryDto.getBdm())
                                            .zsm(entryDto.getZsm())
                                            .ase(entryDto.getAse())
                                            .nsm(entryDto.getNsm())
                                            .md(entryDto.getMd())
                                            .outletErpId(chainDto.getOutletErpId())
                                            .outletName(chainDto.getOutletName())
                                            .outletType(chainDto.getOutletType())
                                            .marketName(chainDto.getMarketName())
                                            .city(chainDto.getCity())
                                            .state(chainDto.getState())
                                            .month(
                                                    LocalDate.ofInstant(
                                                                    entry.getUpdatedOn().toInstant(), ZoneId.of("Asia/Kolkata"))
                                                            .getMonth()
                                                            .getValue())
                                            .invoice(entryDto.getInvoice())
                                            .inwardDate(entryDto.getStockInwardDate())
                                            .inwardTime(entryDto.getStockInwardTime())
                                            .primaryCategory(dtl.getCategoryName())
                                            .secondaryCategory(dtl.getSubCategoryName())
                                            .productErpId(dtl.getProductId().getProductCode())
                                            .productName(dtl.getProductId().getProductName() + " - " + pUnit.getSize() + "/" + pUnit.getUnit())
                                            .mrp(dtl.getProductId().getProductMRP())
                                            .qty(dtl.getNoOfPcs().intValue())
                                            .value(dtl.getAmount());

                            if (entryDto.getStockActivity() != null
                                    && entryDto.getStockActivity().contains("return"))
                                dtlDtoBuilder
                                        .returnQty(dtl.getNoOfPcs().toString())
                                        .qty(null)
                                        .value(null)
                                        .returnValue(dtl.getAmount())
                                        .returnDate(entryDto.getStockInwardDate());

                            rootList.add(dtlDtoBuilder.build());
                        }
                    }
                });
        return rootList;
    }


    private Pair<Integer, Integer> findListRange(List<Long> list, Long target) {

        int left = 0;
        int right = list.size() - 1;

        while ((!Objects.equals(list.get(left), list.get(right))) && (left <= right)) {
            if (target > list.get(left)) {
                left++;
            }

            if (target < list.get(right)) {
                right--;
            }
        }

        return new Pair<>(left, right);
    }

    class Pair<T, D> {
        T first;
        D second;

        public Pair(T first, D second) {
            this.first = first;
            this.second = second;
        }
    }
}
