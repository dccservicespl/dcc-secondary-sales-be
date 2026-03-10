package com.dcc.osheaapp.report.purchaseSale.controller;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.report.common.ReportWSHandler;
import com.dcc.osheaapp.report.common.ReportWSPayload;
import com.dcc.osheaapp.report.common.controller.ReportInputDto;
import com.dcc.osheaapp.report.purchaseSale.service.ExportService;
import com.dcc.osheaapp.report.purchaseSale.service.PurchaseRecordDto;
import com.dcc.osheaapp.report.purchaseSale.service.PurchaseSaleDtoMapper;
import com.dcc.osheaapp.repository.IOutletRepository;
import com.dcc.osheaapp.service.UserService;
import com.dcc.osheaapp.vo.OutletVo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import static com.dcc.osheaapp.common.service.Util.LOGGER;

@Service
public final class PurchaseSaleReqHandler {

  private static final Logger LOGGER = LogManager.getLogger(PurchaseSaleReqHandler.class);
  private final PurchaseSaleDtoMapper _mapper;
  private final ExportService _PurchaseSale_exportService;
  private final IOutletRepository _outletRepository;
  private final ReportWSHandler wsHandler;

  public PurchaseSaleReqHandler(
      PurchaseSaleDtoMapper _mapper,
      ExportService _PurchaseSale_exportService,
      IOutletRepository _outletRepository,
      ReportWSHandler wsHandler) {
    this._mapper = _mapper;
    this._PurchaseSale_exportService = _PurchaseSale_exportService;
    this._outletRepository = _outletRepository;
    this.wsHandler = wsHandler;
  }

  public void exportReportThroughWs(
      PurchaseRecordExcelExportInputDto dto) {

    LOGGER.info("Initiating Report Generation:: " + dto.getActivity() + " Report");
    CompletableFuture.supplyAsync(
            () -> {
              try {
             byte[] data =    ((ByteArrayOutputStream)
                        exportPurchases(
                                dto,
                                new ByteArrayOutputStream()))
                        .toByteArray();
             LOGGER.info("[" + dto.getActivity() + "]" + " Report:: Generation Complete.");
                return new ReportWSPayload(
                    dto.getActivity() + "_" + dto.getFromDate().toString()+ "-" + dto.getToDate().toString() + "_",data
                    );
              } catch (IOException e) {
                LOGGER.info("[" + dto.getActivity() + "]" + " Report:: Generation Failure.");
                throw new RuntimeException(e);
              }
            })
        .thenAccept(wsHandler::sendReportReadyNotification)
        .exceptionally(
            (e) -> {
              e.printStackTrace();
              wsHandler.sendReportErrNotification(
                  new ApiResponse(
                      500, "INTERNAL_SERVER_ERROR", "Error while generating report", null, 0));
              return null;
            });
  }

  public Object exportPurchases(
      PurchaseRecordExcelExportInputDto dto,
      OutputStream os)
      throws IOException {
    // export purchases for all active outlets

      final long startTime = System.currentTimeMillis();
    if (dto.getOutlet() == null) {
      List<Long> activeOutlets =
          _outletRepository.findActiveOutletsZone1(dto.getZone()).stream()//active inactive all outlet get from this repo
              .map(OutletVo::getId)
              .collect(Collectors.toList());

        LOGGER.info("activeOutlets ::sale_purchase Report :: Entering....");
//      List<PurchaseRecordDto> records =
//          activeOutlets.parallelStream()
//              .map(
//                  outlt ->
//                      _mapper.toDtoByOutletAndPurchaseDateRange(
//                          dto.getFromDate(), dto.getToDate(), outlt, Arrays.asList(dto.getActivity(), dto.getActivity().toLowerCase()+ "_return"), dto.getStockStatus()))
//              .flatMap(List::stream)
//              .collect(Collectors.toList());

        int batchSize = 100;
        List<PurchaseRecordDto> records = new ArrayList<>();

        for (int i = 0; i < activeOutlets.size(); i += batchSize) {
            List<Long> batch = activeOutlets.subList(i, Math.min(i + batchSize, activeOutlets.size()));
            List<PurchaseRecordDto> batchRecords = batch.parallelStream()
                    .map(outlt ->
                            _mapper.toDtoByOutletAndPurchaseDateRange(
                          dto.getFromDate(), dto.getToDate(), outlt, Arrays.asList(dto.getActivity(), dto.getActivity().toLowerCase()+ "_return"), dto.getStockStatus()))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            records.addAll(batchRecords);
        }

        LOGGER.info("records ::sale_purchase Report :: Entering...." + records.size());
     final long elapsedTimeMillis = System.currentTimeMillis() - startTime;
        LOGGER.info("Elapased Data Creation::: " + elapsedTimeMillis);
      return dto.getActivity().equalsIgnoreCase("purchase") ? 
    		  _PurchaseSale_exportService.exportPurchases(records, os) : _PurchaseSale_exportService.exportSales(records, os) ;
    }
    List<PurchaseRecordDto> records =
        _mapper.toDtoByOutletAndPurchaseDateRange(dto.getFromDate(), dto.getToDate(), dto.getOutlet(), Arrays.asList(dto.getActivity(), dto.getActivity()+ "_return"), dto.getStockStatus());
      return dto.getActivity().equalsIgnoreCase("purchase") ? _PurchaseSale_exportService.exportPurchases(records, os) : _PurchaseSale_exportService.exportSales(records, os) ;
  }
  
  public String getSalePurchaseFilename(PurchaseRecordExcelExportInputDto dto) {
//		DropdownMasterVo dropdownMaster = dropdownMastereRepository.findByIdAndFieldType(8L, "zone").orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[]{"Zone"}));
      return dto.getActivity()+ "_summary_" + ".xlsx";
	}


}
