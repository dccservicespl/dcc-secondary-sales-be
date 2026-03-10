package com.dcc.osheaapp.service;

import com.dcc.osheaapp.common.model.Constants;
import com.dcc.osheaapp.repository.ICounterStockManageRepository;
import com.dcc.osheaapp.vo.CounterStockManageVo;
import com.dcc.osheaapp.vo.StockEntryDtlVo;
import com.dcc.osheaapp.vo.StockEntryVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PurchaseEntry extends CounterStockEntry {

    private final Constants.BA_Activity_Enum activity = Constants.BA_Activity_Enum.purchase_entry;

    public PurchaseEntry(StockEntryVo inputEntry, Long user, ICounterStockManageRepository counterStockManageRepository) {
        super(inputEntry, user, counterStockManageRepository);
    }

    @Override
    List<CounterStockManageVo> execute() {
      //		List<CounterStockManageVo> existingData = iCounterStockManageRepository
//				.findPocketMISByDateOutlet(input.getOutlet().getId(), new Date());
//        Date transDate = input.getTransactionDate();
//        //if previous month purchase edit, then search data of previous month
//        boolean ifPrevMonthTransaction =  ifPrevMonthTransaction(transDate);
//        // get a collection of all the ids.
//        List<Long> products = input.getStockEntryDtlVo().stream()
//                .map(e -> e.getProductId().getId()).collect(Collectors.toList());
//        List<CounterStockManageVo> existingData =
//                iCounterStockManageRepository.findPocketMISByDateOutletAndProducts(
//                        input.getOutlet().getId(), transDate, products);
//
//
//        for (StockEntryDtlVo dtl : input.getStockEntryDtlVo()) {
//            boolean match = false;
//            for (CounterStockManageVo mis : existingData) {
//                if (dtl.getProductId().getId().equals(mis.getProductId().getId())) {
//                    mis.setPurchase(
//                            (null != mis.getPurchase()) ? mis.getPurchase() + dtl.getNoOfPcs() : dtl.getNoOfPcs());
//                    mis.setPurchaseAmount((null != mis.getPurchaseAmount())
//                            ? String.valueOf(
//                            Double.parseDouble(mis.getPurchaseAmount()) + Double.parseDouble(dtl.getAmount()))
//                            : dtl.getAmount());
//                    if(ifPrevMonthTransaction) {
//                        mis.setAdjustedNoOfPcs(dtl.getNoOfPcs());
//                        mis.setAdjustedAmount(Double.parseDouble(dtl.getAmount()));
//                        mis.setAdjustedBy(mis.getUpdatedBy());
//                        mis.setAdjustedOn(new Date());
//                        mis.setUpdatedOn(input.getTransactionDate()); 			//For pocket MIS calculation
//                    }
//
//                    mis.setClosingStock(mis.getClosingStockCalc());
//                    mis.setClosingStockAmount(mis.getClosingStockAmountCalc());
//                    dataList.add(mis);
//                    match = true;
//                    break;
//                }
//            }
//            if (!match) {
////				Date adjustedOn, Long adjustedBy, Long adjustedNoOfPcs, Double adjustedAmount
//                LOGGER.info(dtl.getProductId().getId());
//                if(ifPrevMonthTransaction) {
//                    CounterStockManageVo each = new CounterStockManageVo(input.getOutlet(), dtl.getProductId(), 0L,
//                            dtl.getNoOfPcsUpdated(), 0L, dtl.getNoOfPcsUpdated(), "0", dtl.getAmountUpdated(), "0", dtl.getAmountUpdated(), "user",
//                            dtl.getCategoryName(), dtl.getSubCategoryName(), new Date(), loginUserId, new Date(),
//                            loginUserId, 0L, 0L, "0", "0", new Date(), loginUserId, dtl.getNoOfPcsUpdated(),
//                            Double.parseDouble(dtl.getAmountUpdated()), input.getTransactionDate());
//                    dataList.add(each);
//                } else {
//                    CounterStockManageVo each = new CounterStockManageVo(input.getOutlet(), dtl.getProductId(), 0L,
//                            dtl.getNoOfPcs(), 0L, dtl.getNoOfPcs(), "0", dtl.getAmount(), "0", dtl.getAmount(), "user",
//                            dtl.getCategoryName(), dtl.getSubCategoryName(), new Date(), loginUserId, new Date(),
//                            loginUserId, 0L, 0L, "0", "0", input.getTransactionDate());
//                    dataList.add(each);
//                }
//
//            }
//        }
//
//        //If previous month purchase, then stock transfer needed
//        if(ifPrevMonthTransaction) {
//            // call stock update for current month
//            List<CounterStockManageVo> currentMonData = prevMonthStockAdjust( dataList, products, input.getOutlet().getId(), loginUserId);
//            dataList.addAll(currentMonData);
//        }
//        return dataList;
        return null;
    }
}
