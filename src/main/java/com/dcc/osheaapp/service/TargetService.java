package com.dcc.osheaapp.service;

import static com.dcc.osheaapp.repository.BaTargetSpecifications.*;
import static org.springframework.data.jpa.domain.Specification.where;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.common.model.Constants;
import com.dcc.osheaapp.common.service.Util;
import com.dcc.osheaapp.leaderboard.domain.repository.LeaderBoardRepository;
import com.dcc.osheaapp.managerial.service.BaUnderBDEService;
import com.dcc.osheaapp.repository.*;
import com.dcc.osheaapp.common.event.AchievementsSyncedEvent;
import com.dcc.osheaapp.vo.BaTarget;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import com.dcc.osheaapp.vo.SyncAchievement;
import com.dcc.osheaapp.vo.SyncAchievementDto;
import com.dcc.osheaapp.vo.dto.BdeOutletAssociation;
import com.dcc.osheaapp.vo.dto.SearchTargetDto;
import com.dcc.osheaapp.vo.dto.SetTargetDto;
import com.dcc.osheaapp.vo.views.OutletUserMappingView;
import com.dcc.osheaapp.vo.views.PocketMISDto;
import com.dcc.osheaapp.vo.views.SyncAchievementVo;
import com.dcc.osheaapp.vo.views.TargetView;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TargetService {

  private static final Logger LOGGER = LogManager.getLogger(TargetService.class);
  @Autowired IBaTargetRepository baTargetRepository;

  @Autowired IOutletUserMappingRepository outletUserMappingRepository;

  @Autowired
  IDropdownMastereRepository dropdownMastereRepository;

  @Autowired IPocketMISRepository pocketMISRepository;

  @Autowired UserService userService;

  @Autowired OutletService outletService;
  @Autowired private ApplicationEventPublisher publisher;
  @Autowired
  LeaderBoardRepository leaderBoardRepository;

  @Autowired
IBdeOutletMappingRepository iBdeOutletMappingRepository;

  @Autowired
  ISyncAchievementDtoRepo iSyncAchievementDtoRepo;

  @Autowired
  ISyncAchievementRepo iSyncAchievementRepo;

  @Autowired
  ISyncAchievementVoRepo iSyncAchievementVoRepo;

  @Autowired
  BaUnderBDEService baUnderBDEService;

  private static CellStyle getCellStyle(SXSSFWorkbook workbook, CellStyle my_style) {
    Font headerFont = workbook.createFont();
    headerFont.setColor(IndexedColors.BLACK.index);
    headerFont.setFontName("Arial");
    // fill foreground color ...
    my_style.setFillForegroundColor(IndexedColors.YELLOW.index);
    // and solid fill pattern produces solid grey cell fill
    my_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    headerFont.setFontHeightInPoints((short) 10);
    headerFont.setBold(true);
    my_style.setFont(headerFont);
    my_style.setBorderBottom(BorderStyle.THIN);
    my_style.setBorderTop(BorderStyle.THIN);
    my_style.setBorderLeft(BorderStyle.THIN);
    my_style.setBorderRight(BorderStyle.THIN);
    return my_style;
  }

  private static CellStyle getCellStyleGray(SXSSFWorkbook workbook, CellStyle my_style) {
    Font headerFont = workbook.createFont();
    headerFont.setColor(IndexedColors.BLACK.index);
    headerFont.setFontName("Arial");
    // fill foreground color ...
    my_style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
    // and solid fill pattern produces solid grey cell fill
    my_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    headerFont.setFontHeightInPoints((short) 10);
    headerFont.setBold(true);
    my_style.setFont(headerFont);
    my_style.setBorderBottom(BorderStyle.THIN);
    my_style.setBorderTop(BorderStyle.THIN);
    my_style.setBorderLeft(BorderStyle.THIN);
    my_style.setBorderRight(BorderStyle.THIN);
    return my_style;
  }


  public BaTarget setTarget(SetTargetDto dto) {
    BaTarget newTarget = createNewTargets(dto);
    Optional<BaTarget> existingTarget =
        getTarget(dto.getBaId(), dto.getMonth(), LocalDate.now().getYear());
    existingTarget.ifPresent(baTarget -> newTarget.setId(baTarget.getId()));
    return newTarget;
  }

  public void setTarget(List<SetTargetDto> dtos) {
    if(dtos == null || dtos.isEmpty()) return;
    SetTargetDto first = dtos.get(0);
    List<Long> excelBA = dtos.stream().map(SetTargetDto::getBaId).collect(Collectors.toList());
    LOGGER.info("excelBA for delete ====>>"+excelBA.size());
    //delete existing records
    leaderBoardRepository.deleteRecordByBAId(excelBA, first.getYear()+"-"+String.format("%02d",first.getMonth().getValue()));
    baTargetRepository.deleteRecordByBAId(excelBA, first.getYear()+"", first.getMonth()+"" );
    
//    List<Long> excelOutlet = dtos.stream().map(SetTargetDto::getOutletId).collect(Collectors.toList());
//    LOGGER.info("excelOutlet for delete ====>>"+excelOutlet.size());
    //delete existing records
//    leaderBoardRepository.deleteRecordByBAId(excelBA, first.getYear()+"-"+String.format("%02d",first.getMonth().getValue()));
//    baTargetRepository.deleteRecordByOutletId(excelOutlet, first.getYear()+"", first.getMonth()+"" );

    List<BaTarget> existingTargets = baTargetRepository.findAll(
              fromMonth(first.getMonth()).and(fromYear(first.getYear()).and(hasBaId(dtos.stream().map(SetTargetDto::getBaId).collect(Collectors.toList()))))
      );
      LOGGER.info("existingTargets in DB ====>>"+existingTargets.size());
    List<BaTarget> newTargets = dtos.stream().map(this::createNewTargets).collect(Collectors.toList());
    LOGGER.info("newTargets in excel ====>>"+newTargets.size());
    newTargets.forEach(e -> {
      int index = Util.binarySearch(existingTargets.stream().map(BaTarget::getBaId).collect(Collectors.toList()), e.getBaId() );
      if(index != -1) e.setId(existingTargets.get(index).getId());
    });
    LOGGER.info("newTargets after matching ====>>"+newTargets.size());


    baTargetRepository.saveAll(newTargets);
  }
  private BaTarget createNewTargets(SetTargetDto dto) {
//    LOGGER.info("createNewTarget---->"+dto.getBaId());
    if (!userService.isActive(dto.getBaId())){
      LOGGER.info("TARGET_INVALID_ACTIVE_BA ==>>"+dto.toString());
//      throw new OjbException(ErrorCode.TARGET_INVALID_ACTIVE_BA, new Object[] {});
    }
    if (!userService.isBA(dto.getBaId())) {
      LOGGER.info("TARGET_INVALID_BA ==>>" + dto.getBaId());
      throw new OjbException(ErrorCode.TARGET_INVALID_BA, new Object[]{});
    }
    if (outletService.checkBaAssociation(dto.getOutletId(), dto.getBaId()).isEmpty()) {
      LOGGER.info("BA_NOT_MAPPED_TO_OUTLET ==>>" + dto.getBaId() + " == >>" + dto.getOutletId());
//      throw new OjbException(ErrorCode.BA_NOT_MAPPED_TO_OUTLET, new Object[]{});
    }
    BaTarget newTarget =
        new BaTarget(
            dto.getZoneId(),
            dto.getZone(),
            dto.getBaId() ,
            dto.getOutletId(),
            dto.getMonth(),
            dto.getColorTargetAmount(),
            dto.getSkinTargetAmount(),
            dto.getColorAchieved(),
            dto.getSkinAchieved(),
            dto.getYear());
    return newTarget;
  }


  public Optional<BaTarget> getTarget(Long userId, Month month, int year) {
    Optional<BaTarget> target = baTargetRepository.findByBaIdAndMonthAndYear(userId, month, year);
    target.ifPresent(BaTarget::calculateTargetsPerDay);
    target.ifPresent(BaTarget::calculatePendingAchievements);
    return target;
  }

  public Optional<SyncAchievement> getTargetV2(Long userId, Month month, int year) {
    String monthString = month.name();
    Optional<SyncAchievement> target = iSyncAchievementRepo.findByBaIdAndMonthAndYear(userId, monthString, year);
    target.ifPresent(SyncAchievement::calculateTargetsPerDay);
//    target.ifPresent(SyncAchievement::calculatePendingAchievements);
    return target;
  }

  public BaTarget getBdeTarget(Long bdeId, String month, int year) {

    List<Long> baUnderBdeIds = baUnderBDEService.baIdListUnderBde(bdeId);
    List<BaTarget> basTargets= baTargetRepository.findBdeTargetByBaidMonthYear(baUnderBdeIds, month, year);
//    basTargets.stream().collect(BaTarget::calculatePendingAchievement);
//    basTargets.ifPresent(BaTarget::calculatePendingAchievements);
//    for (BaTarget baTarget: basTargets) {
//      Optional<BaTarget> target = Optional.ofNullable(baTarget);
//      target.ifPresent(BaTarget::calculateTargetsPerDay);
//      target.ifPresent(BaTarget::calculatePendingAchievements);
//    }
    BigDecimal sumOfPendingColorAchievementAmountB = new BigDecimal(0);
    BigDecimal sumOfPendingSkinAchievementAmountB = new BigDecimal(0);

    BigDecimal getColorTargetAmount = new BigDecimal(0);
    BigDecimal getColorAchieved = new BigDecimal(0);
    BigDecimal getSkinTargetAmount = new BigDecimal(0);
    BigDecimal getSkinAchieved = new BigDecimal(0);
    for (BaTarget baTarget: basTargets){
      getColorTargetAmount = getColorTargetAmount.add(baTarget.getColorTargetAmount()) ;
      getColorAchieved = getColorAchieved.add(baTarget.getColorAchieved()) ;
      getSkinTargetAmount = getSkinTargetAmount.add(baTarget.getSkinTargetAmount()) ;
      getSkinAchieved = getSkinAchieved.add(baTarget.getSkinAchieved()) ;
    }
    sumOfPendingColorAchievementAmountB = getColorTargetAmount.subtract(getColorAchieved).abs();
    sumOfPendingSkinAchievementAmountB = getSkinTargetAmount.subtract(getSkinAchieved).abs();
    LOGGER.info("sumOfPendingSkinAchievementAmountB = ==== >>" + sumOfPendingSkinAchievementAmountB);
    LOGGER.info("sumOfPendingColorAchievementAmountB = ==== >>" + sumOfPendingColorAchievementAmountB);

    double sumOfColorTargetAmount = basTargets.stream().mapToDouble(i -> i.getColorTargetAmount().doubleValue()).sum();
    double sumOfSkinTargetAmount = basTargets.stream().mapToDouble(i -> i.getSkinTargetAmount().doubleValue()).sum();

    double sumOfColorTargetPerDay = basTargets.stream().mapToDouble(i -> i.getColorTargetPerDay().doubleValue()).sum();
    double sumOfSkinTargetPerDay = basTargets.stream().mapToDouble(i -> i.getSkinTargetPerDay().doubleValue()).sum();

    // Use for 2 digit after decimal
    sumOfColorTargetPerDay = Math.round(sumOfColorTargetPerDay * 100.0) / 100.0;
    sumOfSkinTargetPerDay = Math.round(sumOfSkinTargetPerDay * 100.0) / 100.0;

//    double sumOfPendingSkinAchievementAmount = basTargets.stream().mapToDouble(i -> i.getPendingSkinAchievementAmount().doubleValue()).sum();
//    double sumOfPendingColorAchievementAmount = basTargets.stream().mapToDouble(i -> i.getPendingColorAchievementAmount().doubleValue()).sum();


//    double sumOfPendingSkinAchievementAmount = new Double(sumOfPendingSkinAchievementAmountB.toString());
//    double sumOfPendingColorAchievementAmount = new Double(sumOfPendingColorAchievementAmountB.toString());

    /*Here Achievement till today value store in sumOfPendingSkinAchievementAmount and
    sumOfPendingColorAchievementAmount variable, variable name doesn't change for angular
     */
    double sumOfPendingSkinAchievementAmount = new Double(getSkinAchieved.toString());
    double sumOfPendingColorAchievementAmount = new Double(getColorAchieved.toString());


    //Change by tanmoy for achieve till today
//    double colorAchieveTillToday = new Double(getColorAchieved.toString());
//    double skinAchieveTillToday = new Double(getSkinAchieved.toString());

    LOGGER.info("sumOfColorTargetAmount:: " + sumOfColorTargetAmount + " sumOfSkinTargetAmount:: "+ sumOfSkinTargetAmount);
    LOGGER.info("sumOfColorTargetPerDay:: " + sumOfColorTargetPerDay + " sumOfSkinTargetPerDay:: "+ sumOfSkinTargetPerDay);

    LOGGER.info("sumOfPendingSkinAchievementAmount:: " + sumOfPendingSkinAchievementAmount + " sumOfPendingColorAchievementAmount:: "+ sumOfPendingColorAchievementAmount);
//    LOGGER.info("ColorAchieveTillToday:: " + colorAchieveTillToday + " SkinAchieveTillToday:: "+ skinAchieveTillToday);



//    basTargets.ifPresent(List<BaTarget>:: calculateTargetsPerDay);
//    basTargets.ifPresent(List<BaTarget>:: calculatePendingAchievements);
    BaTarget baTarget = new BaTarget();


    baTarget.setSkinTargetAmount(BigDecimal.valueOf(sumOfSkinTargetAmount));
    baTarget.setColorTargetAmount(BigDecimal.valueOf(sumOfColorTargetAmount));

    baTarget.setSkinTargetPerDay(BigDecimal.valueOf(sumOfSkinTargetPerDay));
    baTarget.setColorTargetPerDay(BigDecimal.valueOf(sumOfColorTargetPerDay));

    baTarget.setPendingSkinAchievementAmount(BigDecimal.valueOf(sumOfPendingSkinAchievementAmount));
    baTarget.setPendingColorAchievementAmount(BigDecimal.valueOf(sumOfPendingColorAchievementAmount));

    //Change by tanmoy for achieve till today
//    baTarget.setColorAchievementTillToday(BigDecimal.valueOf(colorAchieveTillToday));
//    baTarget.setSkinAchievementTillToday(BigDecimal.valueOf(skinAchieveTillToday));

    return baTarget;
  }

  public SyncAchievement getBdeTargetV2(Long bdeId, String month, int year) {

    List<Long> bdeOutlets = iBdeOutletMappingRepository.getOutlet(bdeId);
//    List<BaTarget> basTargets= baTargetRepository.findBdeTargetByBaidMonthYear(baUnderBdeIds, month, year);
    List<SyncAchievement> bdeTargets= iSyncAchievementRepo.findBdeTargetByOutletMonthYear(bdeOutlets, month, year);
    List<BaTarget> totalTargetOfBde =  baTargetRepository.findTotalBdeTarget(bdeOutlets, month, year);
//    basTargets.stream().collect(BaTarget::calculatePendingAchievement);
//    basTargets.ifPresent(BaTarget::calculatePendingAchievements);
//    for (BaTarget baTarget: basTargets) {
//      Optional<BaTarget> target = Optional.ofNullable(baTarget);
//      target.ifPresent(BaTarget::calculateTargetsPerDay);
//      target.ifPresent(BaTarget::calculatePendingAchievements);
//    }
    BigDecimal sumOfPendingColorAchievementAmountB = new BigDecimal(0);
    BigDecimal sumOfPendingSkinAchievementAmountB = new BigDecimal(0);

    BigDecimal getColorTargetAmount = new BigDecimal(0);
    BigDecimal getColorAchieved = new BigDecimal(0);
    BigDecimal getSkinTargetAmount = new BigDecimal(0);
    BigDecimal getSkinAchieved = new BigDecimal(0);
    for (SyncAchievement bdeTarget: bdeTargets){
      getColorTargetAmount = getColorTargetAmount.add(bdeTarget.getColorTargetAmount()) ;
      getColorAchieved = getColorAchieved.add(bdeTarget.getColorAchieved()) ;
      getSkinTargetAmount = getSkinTargetAmount.add(bdeTarget.getSkinTargetAmount()) ;
      getSkinAchieved = getSkinAchieved.add(bdeTarget.getSkinAchieved()) ;
    }
    LOGGER.info("sumOfSkinAchievementAmount = ==== >>" + getSkinAchieved);
    LOGGER.info("sumOfColorAchievementAmount = ==== >>" + getColorAchieved);
    LOGGER.info("sumOfSkinTargetAmount = ==== >>" + getSkinTargetAmount);
    LOGGER.info("sumOfColorTargetAmount = ==== >>" + getColorTargetAmount);
    sumOfPendingColorAchievementAmountB = getColorTargetAmount.subtract(getColorAchieved).abs();
    sumOfPendingSkinAchievementAmountB = getSkinTargetAmount.subtract(getSkinAchieved).abs();
    LOGGER.info("sumOfPendingSkinAchievementAmountB = ==== >>" + sumOfPendingSkinAchievementAmountB);
    LOGGER.info("sumOfPendingColorAchievementAmountB = ==== >>" + sumOfPendingColorAchievementAmountB);

    double sumOfColorTargetAmount = totalTargetOfBde.stream().mapToDouble(i -> i.getColorTargetAmount().doubleValue()).sum();
    double sumOfSkinTargetAmount = totalTargetOfBde.stream().mapToDouble(i -> i.getSkinTargetAmount().doubleValue()).sum();

    double sumOfColorTargetPerDay = bdeTargets.stream().mapToDouble(i -> i.getColorTargetPerDay().doubleValue()).sum();
    double sumOfSkinTargetPerDay = bdeTargets.stream().mapToDouble(i -> i.getSkinTargetPerDay().doubleValue()).sum();

    // Use for 2 digit after decimal
    sumOfColorTargetPerDay = Math.round(sumOfColorTargetPerDay * 100.0) / 100.0;
    sumOfSkinTargetPerDay = Math.round(sumOfSkinTargetPerDay * 100.0) / 100.0;

//    double sumOfPendingSkinAchievementAmount = basTargets.stream().mapToDouble(i -> i.getPendingSkinAchievementAmount().doubleValue()).sum();
//    double sumOfPendingColorAchievementAmount = basTargets.stream().mapToDouble(i -> i.getPendingColorAchievementAmount().doubleValue()).sum();


//    double sumOfPendingSkinAchievementAmount = new Double(sumOfPendingSkinAchievementAmountB.toString());
//    double sumOfPendingColorAchievementAmount = new Double(sumOfPendingColorAchievementAmountB.toString());

    /*Here Achievement till today value store in sumOfPendingSkinAchievementAmount and
    sumOfPendingColorAchievementAmount variable, variable name doesn't change for angular
     */
    double sumOfPendingSkinAchievementAmount = new Double(getSkinAchieved.toString());
    double sumOfPendingColorAchievementAmount = new Double(getColorAchieved.toString());


    //Change by tanmoy for achieve till today
    double colorAchieveTillToday = new Double(getColorAchieved.toString());
    double skinAchieveTillToday = new Double(getSkinAchieved.toString());
    LOGGER.info("sumOfColorAchievedTilltoday:: " + colorAchieveTillToday + " sumOfSkinAchievedTilltoday:: "+ skinAchieveTillToday);


    LOGGER.info("sumOfColorTargetAmount:: " + sumOfColorTargetAmount + " sumOfSkinTargetAmount:: "+ sumOfSkinTargetAmount);
    LOGGER.info("sumOfColorTargetPerDay:: " + sumOfColorTargetPerDay + " sumOfSkinTargetPerDay:: "+ sumOfSkinTargetPerDay);

    LOGGER.info("sumOfPendingSkinAchievementAmount:: " + sumOfPendingSkinAchievementAmount + " sumOfPendingColorAchievementAmount:: "+ sumOfPendingColorAchievementAmount);
//    LOGGER.info("ColorAchieveTillToday:: " + colorAchieveTillToday + " SkinAchieveTillToday:: "+ skinAchieveTillToday);



//    basTargets.ifPresent(List<BaTarget>:: calculateTargetsPerDay);
//    basTargets.ifPresent(List<BaTarget>:: calculatePendingAchievements);
    SyncAchievement bdeTarget = new SyncAchievement();


    bdeTarget.setSkinTargetAmount(BigDecimal.valueOf(sumOfSkinTargetAmount));
    bdeTarget.setColorTargetAmount(BigDecimal.valueOf(sumOfColorTargetAmount));

    bdeTarget.setSkinTargetPerDay(BigDecimal.valueOf(sumOfSkinTargetPerDay));
    bdeTarget.setColorTargetPerDay(BigDecimal.valueOf(sumOfColorTargetPerDay));

    bdeTarget.setPendingSkinAchievementAmount(BigDecimal.valueOf(skinAchieveTillToday));
    bdeTarget.setPendingColorAchievementAmount(BigDecimal.valueOf(colorAchieveTillToday));

//    Change by tanmoy for achieve till today
//    bdeTarget.setColorAchievementTillToday(BigDecimal.valueOf(colorAchieveTillToday));
//    bdeTarget.setSkinAchievementTillToday(BigDecimal.valueOf(skinAchieveTillToday));




    return bdeTarget;
  }

  public Object exportTargets(
      Month month, int year, Long divisionId, Long zone, HttpServletResponse response) {
    String msgResponse = null;
     SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
    SXSSFSheet sheet = workbook.createSheet("BA Target");
    CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
     getCellStyle(workbook, headerStyle);

    OutputStream out = null;

    final long startTime = System.currentTimeMillis();
    List<OutletUserMappingView> outletMapping =
        outletUserMappingRepository.findActiveOutletMappingsByDivision(divisionId, zone); // get all active outlet only with last ba association

    DropdownMasterVo zoneVo = dropdownMastereRepository.findByIdAndFieldType(zone, "zone").orElse(new DropdownMasterVo());
    String whereclause =
        " and month = '"
            + month.toString()
            + "' and year = "
            + year
            + " and divisionId = "
            + divisionId;
    if(zone != null) whereclause += " and zoneId = " + zone;
    List<TargetView> targets = baTargetRepository.findTargets(whereclause, "");
    final long elapsedTimeMillis = System.currentTimeMillis() - startTime;

    LOGGER.info("Elapased::: " + elapsedTimeMillis);

    if (outletMapping.isEmpty()) {
      try {
        out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        return out;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    final long startTime1 = System.currentTimeMillis();
    String[] headerCols =
        new String[] {
                "Zone",
          "BA ID",
          "BA Name",
          "BA Code",
          "Outlet ID",
          "Outlet Name",
          "Outlet Code",
          "Month",
          "Year",
          "Color Target Amount",
          "Skin Target Amount",
          "Colors Achieved",
          "Skin Achieved"
        };

    int rowNum = 0;
    Row headerRow = sheet.createRow(rowNum++);

    sheet.trackAllColumnsForAutoSizing();
    Map<Integer, String> colMap = createHeaderCols(headerRow, sheet, headerCols, headerStyle);


    CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
    getCellStyleGray(workbook, cellStyle);
    for (OutletUserMappingView outletUserMappingView : outletMapping) {
      Row row = sheet.createRow(rowNum++);
      for (int col = 0; col < headerCols.length; col++) {
        if(colMap.get(col).contains("Zone")) {
          Cell cell = row.createCell(col);
          cell.setCellValue(zoneVo.getFieldName());
          cell.setCellStyle(cellStyle);
        }
        if (colMap.get(col).contains("BA ID")) {
          Cell cell = row.createCell(col);
          cell.setCellValue(outletUserMappingView.getBaId().toString());
          cell.setCellStyle(cellStyle);
        }

        if (colMap.get(col).contains("BA Name")) {
          Cell cell = row.createCell(col);
          cell.setCellValue(outletUserMappingView.getBaName());
          cell.setCellStyle(cellStyle);
        }
        if (colMap.get(col).contains("BA Code")) {
          Cell cell = row.createCell(col);
          cell.setCellValue(outletUserMappingView.getBaCode().split("@")[0]);
          cell.setCellStyle(cellStyle);
        }

        if (colMap.get(col).contains("Outlet Code")) {
          Cell cell = row.createCell(col);
          cell.setCellValue(outletUserMappingView.getOutletCode());
          cell.setCellStyle(cellStyle);
        }

        if (colMap.get(col).contains("Outlet Name")) {
          Cell cell = row.createCell(col);
          cell.setCellValue(outletUserMappingView.getOutletName());
          cell.setCellStyle(cellStyle);
        }

        if (colMap.get(col).contains("Outlet ID")) {
          Cell cell = row.createCell(col);
          cell.setCellValue(outletUserMappingView.getOutletId().toString());
          cell.setCellStyle(cellStyle);
        }

        List<TargetView> target =
            targets.stream()
                .filter(
                    e ->
                        e.getYear() == year
                            && e.getMonth().equals(month)
                            && e.getBaId().equals(outletUserMappingView.getBaId())
                            && e.getOutletId().equals(outletUserMappingView.getOutletId()))
                .collect(Collectors.toList());

        if (colMap.get(col).contains("Color Target")) {
          Cell cell = row.createCell(col);
          cell.setCellValue(
              target.size() == 1 ? target.get(0).getColorTargetAmount().toString() : "0.00");
        }

        if (colMap.get(col).contains("Skin Target")) {
          Cell cell = row.createCell(col);
          cell.setCellValue(
              target.size() == 1 ? target.get(0).getSkinTargetAmount().toString() : "0.00");
        }

        if (colMap.get(col).contains("Colors Achieved")) {
          Cell cell = row.createCell(col);
          cell.setCellValue("0.00"); // did not fetch prev target or achieve that already in target table always set 0.00
//                  target.size() == 1 ? target.get(0).getColorsAchieved().toString() : "0.00");
          cell.setCellStyle(cellStyle);
        }

        if (colMap.get(col).contains("Skin Achieved")) {
          Cell cell = row.createCell(col);
          cell.setCellValue("0.00"); // did not fetch prev target or achieve that already in target table always set 0.00
                 // target.size() == 1 ? target.get(0).getSkinAchieved().toString() : "0.00");
          cell.setCellStyle(cellStyle);
        }

        if (colMap.get(col).contains("Month")) {
          Cell cell = row.createCell(col);
          cell.setCellValue(
              target.size() == 1
                  ? target.get(0).getMonth().toString()
                  : LocalDate.now().getMonth().toString());
          cell.setCellStyle(cellStyle);
        }

        if (colMap.get(col).contains("Year")) {
          Cell cell = row.createCell(col);
          cell.setCellValue(
              target.size() == 1
                  ? String.valueOf(target.get(0).getYear())
                  : String.valueOf(LocalDate.now().getYear()));
          cell.setCellStyle(cellStyle);
        }
      }
    }

    for(int i = 0; i < headerCols.length; i++) {
      sheet.autoSizeColumn(i);
    }
    final long elapsedTimeMillis1 = System.currentTimeMillis() - startTime;

    LOGGER.info("Elapsed:: Generation:: " + elapsedTimeMillis1);

    try {
      msgResponse = "Exported Successfully";
      out = response.getOutputStream();
      workbook.write(out);
      workbook.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
    return msgResponse;
  }

  private Map<Integer, String> createHeaderCols(
      Row row, SXSSFSheet sheet, String[] cols, CellStyle cellStyle) {
    Map<Integer, String> map = new HashMap<>();
    IntStream.range(0, cols.length)
        .forEach(
            i -> {
              Cell cell = row.createCell(i);
              cell.setCellValue(cols[i]);
              cell.setCellStyle(cellStyle);
              map.put(i, cols[i]);
            });

//    for (int i = 0; i < 13; i++) sheet.autoSizeColumn(i);
    return map;
  }

  public void bulkUpdate(MultipartFile excelInput) {
    //        LOGGER.info("TargetService :: saveProductFromExcel() called...");

    XSSFWorkbook workbook = null;
    XSSFSheet workSheet = null;

    try {
      workbook = new XSSFWorkbook(excelInput.getInputStream());
    } catch (Exception e) {
      throw new OjbException(e, ErrorCode.INVALID_EXCEL_FILE, new Object[] {});
    }

    workSheet = workbook.getSheetAt(0);

    String[] headerCols =
        new String[] {
          "Zone",
          "BA ID",
          "BA Name",
          "BA Code",
          "Outlet ID",
          "Outlet Name",
          "Outlet Code",
          "Month",
          "Color Target Amount",
          "Skin Target Amount",
          "Colors Achieved",
          "Skin Achieved"
        };
    Map<Integer, String> colMap = new HashMap<>();

    Row headerRow = workSheet.getRow(0);
    int noOfCols = headerRow.getPhysicalNumberOfCells();
    for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
      colMap.put(i, headerRow.getCell(i).getStringCellValue());
    }

    DataFormatter format = new DataFormatter();
    DropdownMasterVo zone = null;

    List<SetTargetDto> targets = new ArrayList<>();
    LOGGER.info("workSheet.getPhysicalNumberOfRows() === >>>"+workSheet.getPhysicalNumberOfRows());
    for (int i = 1; i < workSheet.getPhysicalNumberOfRows(); i++) {
      Row row = workSheet.getRow(i);
      SetTargetDto dto = new SetTargetDto();
      for (int j = 0; j < noOfCols; j++) {
        Cell cell = row.getCell(j);
        String cellValue = format.formatCellValue(cell);
        if (colMap.get(j).equals("Zone")) {
          if(zone == null) {
          zone =
              dropdownMastereRepository
                  .findByFieldTypeAndFieldName("zone", cellValue)
                  .orElse(new DropdownMasterVo());
          }
          dto.setZoneId(zone.getId());
          dto.setZone(zone.getFieldName());
        }
        if (colMap.get(j).equals("BA ID")) dto.setBaId(Long.parseLong(cellValue));
        if (colMap.get(j).equals("Outlet ID")) dto.setOutletId(Long.parseLong(cellValue));
        if (colMap.get(j).equals("Month")) dto.setMonth(Month.valueOf(cellValue));
        if (colMap.get(j).equals("Year")) dto.setYear(Integer.parseInt(cellValue));
        if (colMap.get(j).equals("Color Target Amount")) {
          dto.setColorTargetAmount(
              new BigDecimal((cellValue == null || cellValue.equals("")) ? "0.0" : cellValue));
        }
        if (colMap.get(j).equals("Skin Target Amount")) {
          dto.setSkinTargetAmount(
              new BigDecimal((cellValue == null || cellValue.equals("")) ? "0.0" : cellValue));
        }
        if (colMap.get(j).equals("Colors Achieved"))
          dto.setColorAchieved(
              new BigDecimal((cellValue == null || cellValue.equals("")) ? "0.0" : cellValue));
        if (colMap.get(j).equals("Skin Achieved"))
          dto.setSkinAchieved(
              new BigDecimal((cellValue == null || cellValue.equals("")) ? "0.0" : cellValue));
      }
      if (dto.getColorTargetAmount().equals(new BigDecimal("0.0"))
          && dto.getSkinTargetAmount().equals(new BigDecimal("0.0"))) continue;
      targets.add(dto);
    }
    LOGGER.info("targets list after reading excel ===>"+targets.size());
    final long startTime = System.currentTimeMillis();
    setTarget(targets);
    final long elapsedTime = System.currentTimeMillis() -  startTime;
    LOGGER.info("Elapsed::Existing target check:: "  + elapsedTime);
  }

  public List<BaTarget> searchTargetAlt(SearchTargetDto dto) {
    Specification<BaTarget> spec =
        where(BaTargetSpecifications.hasBaId(dto.getBaId()))
            .and(fromMonth(dto.getMonth()))
            .and(BaTargetSpecifications.fromYear(dto.getYear()))
            .and(BaTargetSpecifications.baIn(dto.getUsers()));
    return baTargetRepository.findAll(spec);
  }

  public ResponseEntity<ApiResponse> searchTarget(SearchTargetDto inputVo) {
    LOGGER.info("TargetService::serchTarget::Entering...");
    ApiResponse apiResponse = null;
    List<SyncAchievementVo> getDataList = null;
    String whereClause = " ";
    String limitStr = "";

    if (null != inputVo.getPage() || null != inputVo.getSize()) {
      Integer size =
          (null != inputVo.getSize()) ? inputVo.getSize() : Constants.DEFAULT_DATA_SIZE_PAGINATION;
      Integer page =
          (null != inputVo.getPage() && inputVo.getPage() > 1) ? (inputVo.getPage() - 1) * size : 0;
      limitStr = " limit " + size + " offset " + page; // order by activity_time desc
      LOGGER.info("limitStr..." + limitStr);
    }

    String orderByStr = " order by baName ASC";

    //		if (null != inputVo.getActivityType() && !inputVo.getActivityType().trim().isEmpty()) {
    //			whereClause += " and activity_type = '"+inputVo.getActivityType() + "'";
    //		}

    if (null != inputVo.getMonth()) {
      whereClause += " and month= '" + inputVo.getMonth().toString() + "'";
    }

    if (null != inputVo.getBaId()) {
      whereClause += " and baId = " + inputVo.getBaId();
    }
    if (null != inputVo.getYear()) {
      whereClause += " and year = " + inputVo.getYear();
    }

    LOGGER.info("Whereclause..." + whereClause);
    getDataList = iSyncAchievementVoRepo.findSyncTargets(whereClause, orderByStr + limitStr);

    int totalNo = baTargetRepository.countTarget(whereClause);
    LOGGER.info("TargetService::search::Exiting...");
    if (getDataList != null) {
      apiResponse = new ApiResponse(200, "success", "success", getDataList, totalNo);
      return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    } else {
      throw new OjbException(ErrorCode.NOT_FOUND, new Object[] {"UserActivity"});
    }
  }

  public void syncAchievements() {
    Month currentMonth = LocalDate.now().getMonth();
    int currentYear = LocalDate.now().getYear();
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
    String formattedDate = currentDate.format(formatter);
    List<TargetView> targets =
        baTargetRepository.findTargets(
            " and month = '" + currentMonth.toString() + "' and year = " + currentYear, "");

    LOGGER.info("Check target------>"+ targets.stream());
    List<PocketMISDto> mis =
        pocketMISRepository.findByUsersAndMonth(
                formattedDate,
            targets.stream().map(TargetView::getOutletId).collect(Collectors.toList()));
    targets.sort(Comparator.comparingLong(TargetView::getOutletId));
    mis.sort(Comparator.comparingLong(PocketMISDto::getOutlet));
    Map<Long, List<PocketMISDto>> misMap = constructMisMap(mis);
    List<SetTargetDto> setTargetDtos = setAchievements(targets, misMap);
    baTargetRepository.saveAll(setTargetDtos.stream().map(this::setTarget).collect(Collectors.toList()));
    // publishing for every zone
    List<DropdownMasterVo> zones = dropdownMastereRepository.findByFieldType("zone");
    publisher.publishEvent(new AchievementsSyncedEvent(this, LocalDateTime.now(), zones));
    LOGGER.info("Achievements synced:: " + LocalDateTime.now());
  }

  public void syncAchievementsv2() {
    Month currentMonth = LocalDate.now().getMonth();
    int currentYear = LocalDate.now().getYear();
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
    String formattedDate = currentDate.format(formatter);
    List<TargetView> targets =
            baTargetRepository.findTargets(
                    " and month = '" + currentMonth.toString() + "' and year = " + currentYear, "");

    LOGGER.info("Check target------>"+ targets.stream());
    List<SyncAchievementDto> mis =
            iSyncAchievementDtoRepo.findByUsersAndMonth(
                    formattedDate,
                    targets.stream().map(TargetView::getOutletId).collect(Collectors.toList()));

// Calculation for sale-return include

//    List<SyncAchievementDto> saleReturnMis =
//            iSyncAchievementDtoRepo.findByUsersAndMonthSaleReturn(
//                    formattedDate,
//                    targets.stream().map(TargetView::getOutletId).collect(Collectors.toList()));
//
//
////    145 -> {
////      "Skin Care" -> 245
////    }
//
//    Map<Long, Map<String, BigDecimal>> saleReturnMap = saleReturnMis.stream()
//            .collect(Collectors.groupingBy(
//                    SyncAchievementDto::getBa_Id, // group by baId
//                    Collectors.toMap(SyncAchievementDto::getCategoryName,  // key--> category name
//                                  SyncAchievementDto::getTotalSaleAmount,  // value ---> 123
//                                  BigDecimal::add)
//            ));
//
//    for (SyncAchievementDto data : mis) {
//      Map<String, BigDecimal> categoryMap = saleReturnMap.getOrDefault(data.getBa_Id(), new HashMap<>());
//      BigDecimal saleReturnAmount = categoryMap.getOrDefault(data.getCategoryName(), BigDecimal.ZERO);
//      data.setTotalSaleAmount(data.getTotalSaleAmount().subtract(saleReturnAmount));
//    }

    targets.sort(Comparator.comparingLong(TargetView::getOutletId));
    mis.sort(Comparator.comparingLong(SyncAchievementDto::getOutlet_Id));
    Map<Long, List<SyncAchievementDto>> misMap = constructMisMapv2(mis);
    List<SyncAchievement> setTargetDtos = setAchievementsv2(targets, misMap);

    if (!setTargetDtos.isEmpty()) {
      iSyncAchievementRepo.saveAll(setTargetDtos);
    }
//    baTargetRepository.saveAll(setTargetDtos.stream().map(this::setTarget).collect(Collectors.toList()));
    // publishing for every zone
    List<DropdownMasterVo> zones = dropdownMastereRepository.findByFieldType("zone");
    publisher.publishEvent(new AchievementsSyncedEvent(this, LocalDateTime.now(), zones));
    LOGGER.info("Achievements synced:: " + LocalDateTime.now());
  }
  public void syncAchievementsByBde(Long bdeId){
    Month currentMonth = LocalDate.now().getMonth();
    int currentYear = LocalDate.now().getYear();
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
    String formattedDate = currentDate.format(formatter);
    List<Long> bdeMapOutletIds = iBdeOutletMappingRepository.getOutletIds(bdeId);
    LOGGER.info("Outlet ids ----> " + bdeMapOutletIds);
    String bdeOutletsString = bdeMapOutletIds.toString().replace("[", "").replace("]", "");
    List<TargetView> targets =
            baTargetRepository.findTargetsBde(
                    " and month = '" + currentMonth.toString() + "' and year = " + currentYear +  " and outletId in (" + bdeOutletsString + ")", "");
    LOGGER.info("Check target------>"+ targets.stream());
    List<SyncAchievementDto> mis =
            iSyncAchievementDtoRepo.findByUsersAndMonth(
                    formattedDate,
                    targets.stream().map(TargetView::getOutletId).collect(Collectors.toList()));
    targets.sort(Comparator.comparingLong(TargetView::getOutletId));
    mis.sort(Comparator.comparingLong(SyncAchievementDto::getOutlet_Id));
    Map<Long, List<SyncAchievementDto>> misMap = constructMisMapv2(mis);
    List<SyncAchievement> setTargetDtos = setAchievementsv2(targets, misMap);
    if (!setTargetDtos.isEmpty()) {
      iSyncAchievementRepo.saveAll(setTargetDtos);
    }
    List<DropdownMasterVo> zones = dropdownMastereRepository.findByFieldType("zone");
    publisher.publishEvent(new AchievementsSyncedEvent(this, LocalDateTime.now(), zones));
    LOGGER.info("Achievements synced:: " + LocalDateTime.now());
  }

  /**
   * Constructs a mis map ... with a key of mis user id and mis data as value.
   *
   * @param mis
   * @param
   * @return
   */
  private static Map<Long, List<PocketMISDto>> constructMisMap(List<PocketMISDto> mis) {
    Map<Long, List<PocketMISDto>> misMap = new HashMap<>();
    int i = 0;
    if(mis.size() == 1) {
      List<PocketMISDto> misById = new ArrayList<>();
      PocketMISDto misData = mis.get(0);
      misMap.put(misData.getOutlet(), List.of(misData));
    }
    while (i < mis.size() - 1) {
      List<PocketMISDto> misById = new ArrayList<>();
      PocketMISDto misData = mis.get(i);
      if (mis.get(i).getOutlet().equals(mis.get(i + 1).getOutlet())) {
        misById = Arrays.asList(mis.get(i++), mis.get(i++));
      } else {
        misById = Collections.singletonList(mis.get(i++));
      }
      misMap.put(misData.getOutlet(), misById);
    }
    return misMap;
  }

  private static Map<Long, List<SyncAchievementDto>> constructMisMapv2(List<SyncAchievementDto> mis) {
    LOGGER.info("TargetService :: constructMisMapV2");
    return mis.stream()
            .collect(Collectors.groupingBy(SyncAchievementDto::getOutlet_Id));
  }
  private List<SetTargetDto> setAchievements(
      List<TargetView> targets, Map<Long, List<PocketMISDto>> misMap) {
    List<SetTargetDto> dtos = new ArrayList<>();
    misMap.forEach(
        (key, value) -> {
          int index = binarySearchTargets(targets, key);
          if (index == -1) return;
          TargetView target = targets.get(index);
          SetTargetDto dto =
              new SetTargetDto(
                  target.getZoneId(),
                  target.getZone(),
                  target.getBaId(),
                  target.getOutletId(),
                  target.getMonth(),
                  target.getYear(),
                  target.getColorTargetAmount(),
                  target.getSkinTargetAmount(),
                  target.getColorsAchieved(),
                  target.getSkinAchieved());
          value.forEach(
              e -> {
                BigDecimal achievement =
                    new BigDecimal(e.getSaleAmount() == null ? "0.0" : e.getSaleAmount())
                        .subtract(
                            new BigDecimal(
                                e.getSaleReturnAmount() == null ? "0.0" : e.getSaleReturnAmount()));
                LOGGER.info("Achivement--->"+achievement);
                if (e.getCategoryName().contains("Color Cosmetics")) {
                  dto.setColorAchieved(achievement);
                }
                if (e.getCategoryName().contains("Skin Care")) dto.setSkinAchieved(achievement);
              });
          dtos.add(dto);
        });

    return dtos;
  }

//  private List<SyncAchievement> setAchievementsv2(
//          List<TargetView> targets, Map<Long, List<SyncAchievementDto>> misMap) {
//    List<SyncAchievement> dtos = new ArrayList<>();
//    for(TargetView target : targets){
//      Long outletId = target.getOutletId();
//      List<SyncAchievementDto> salesData = misMap.get(outletId);
//      Map<Long, SyncAchievement> baAchievementMap = new HashMap<>();
//          for(SyncAchievementDto data : salesData){
//              Long baId =  data.getBa_Id();
//              BigDecimal saleAmount = new BigDecimal(String.valueOf(data.getTotalSaleAmount() == null ? "0.0" : data.getTotalSaleAmount()));
//            SyncAchievement achievement = baAchievementMap.computeIfAbsent(baId, k -> new SyncAchievement(
//                    null,  //Add null for id (since it's auto-generated)
//                    baId,
//                    outletId,
//                    target.getZoneId(),
//                    target.getZone(),
//                    target.getColorTargetAmount(),
//                    target.getSkinTargetAmount(),
//                    BigDecimal.ZERO, // Initialize Color Achievement
//                    BigDecimal.ZERO, // Initialize Skin Achievement
//                    target.getMonth(),
//                    target.getYear()
//            ));
//            // Update the correct achievement category
//            if (data.getCategoryName().contains("Color Cosmetics")) {
//              achievement.setColorAchieved(achievement.getColorAchieved().add(saleAmount));
//            } else if (data.getCategoryName().contains("Skin Care")) {
//              achievement.setSkinAchieved(achievement.getSkinAchieved().add(saleAmount));
//            }
//
//          }
//      // Convert the Map values to a list
//      dtos.addAll(baAchievementMap.values());
//    }
//
//  }

//  private List<SyncAchievement> setAchievementsv2(
//          List<TargetView> targets, Map<Long, List<SyncAchievementDto>> misMap) {
//
//    List<SyncAchievement> dtos = new ArrayList<>();
//    Set<Long> baIdsToDelete = new HashSet<>();
//    String monthName = null;
//    int year = 0;
//    for (TargetView target : targets) {
//      Long outletId = target.getOutletId();
//      List<SyncAchievementDto> salesData = misMap.get(outletId);
//      if (salesData == null || salesData.isEmpty()) {
//        //sales data is not available
//        continue;
//      }
//      monthName = target.getMonth().name();
//      year = target.getYear();
//      Map<Long, SyncAchievement> baAchievementMap = new HashMap<>();
//      for (SyncAchievementDto data : salesData) {
//        Long baId = data.getBa_Id();
//        BigDecimal saleAmount = data.getTotalSaleAmount() != null ? data.getTotalSaleAmount() : BigDecimal.ZERO;
//        baIdsToDelete.add(baId);
////        LOGGER.info("Check  month name and year->  " +  target.getMonth().name() +"--->"+ target.getYear());
//       // iSyncAchievementRepo.deleteByBaIdAndMonthAndYear(baId, target.getMonth().name(), target.getYear());
//        SyncAchievement achievement = baAchievementMap.computeIfAbsent(baId, k -> new SyncAchievement(
//                null,
//                baId,
//                outletId,
//                target.getZoneId(),
//                target.getZone(),
//                target.getColorTargetAmount(),
//                target.getSkinTargetAmount(),
//                BigDecimal.ZERO, // initialize with 0 Color Achievement
//                BigDecimal.ZERO, // initialize Skin  0 Achievement
//                target.getMonth().name(),
//                target.getYear()
//        ));
//        //  Update Achievements
//        if (data.getCategoryName().contains("Color Cosmetics")) {
//          achievement.setColorAchieved(achievement.getColorAchieved().add(saleAmount));
//        } else if (data.getCategoryName().contains("Skin Care")) {
//          achievement.setSkinAchieved(achievement.getSkinAchieved().add(saleAmount));
//        }
//      }
//      if (!baIdsToDelete.isEmpty()) {
//        iSyncAchievementRepo.deleteByBaIdAndMonthAndYear(baIdsToDelete, monthName, year);
//      }
//      dtos.addAll(baAchievementMap.values());
//
//    }
//
//    return dtos; //returns the list
//  }

  private List<SyncAchievement> setAchievementsv2(
          List<TargetView> targets, Map<Long, List<SyncAchievementDto>> misMap) {

    LOGGER.info("TargetService :: setAchievementsv2");

    List<SyncAchievement> dtos = new ArrayList<>();
    Map<Long, SyncAchievement> baAchievementMap = new HashMap<>();
    Set<Long> baIdsToDelete = new HashSet<>();
    String monthName = null;
    int year = 0;

    for (TargetView target : targets) {
      Long outletId = target.getOutletId(); // get  outlet from ba_target
      List<SyncAchievementDto> salesData = misMap.get(outletId); // get data of that outlet from mismap
      if (salesData == null || salesData.isEmpty()) {
        continue;
      }
      // Set month and year for deletion
      monthName = target.getMonth().name();
      year = target.getYear();
      for (SyncAchievementDto data : salesData) {
        Long baId = data.getBa_Id();
        BigDecimal saleAmount = data.getTotalSaleAmount() != null ? data.getTotalSaleAmount() : BigDecimal.ZERO;
        // collect ba id that need to delete from syncAchievement
        baIdsToDelete.add(baId);
        //  retrieve existing SyncAchievement
        SyncAchievement achievement = baAchievementMap.computeIfAbsent(baId, k -> new SyncAchievement(
                null,
                baId,
                outletId,
                target.getZoneId(),
                target.getZone(),
                target.getColorTargetAmount(),
                target.getSkinTargetAmount(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                target.getMonth().name(),
                target.getYear()
        ));
        //  update achievements with color and skin
        if (data.getCategoryName().contains("Color Cosmetics")) {
          achievement.setColorAchieved(achievement.getColorAchieved().add(saleAmount));
        } else if (data.getCategoryName().contains("Skin Care")) {
          achievement.setSkinAchieved(achievement.getSkinAchieved().add(saleAmount));
        }
      }
    }
    //  bulk Delete  those ba record already present for this month in syncAchievement table
    if (!baIdsToDelete.isEmpty()) {
      iSyncAchievementRepo.deleteByBaIdAndMonthAndYear(baIdsToDelete, monthName, year);
    }
    // Add all achievements
    dtos.addAll(baAchievementMap.values());
    return dtos;
  }


  private int binarySearchTargets(List<TargetView> list, Long target) {
    int low = 0;
    int high = list.size() - 1;
    int mid = 0;
    while (low <= high) {
      mid = (low + high) / 2;
      if (list.get(mid).getOutletId().equals(target)) {
        Long baId = list.get(mid).getOutletId();
        return mid;
      }
      if (list.get(mid).getOutletId() > target) high = mid - 1;
      else if (list.get(mid).getOutletId() < target) low = mid + 1;
    }
    return -1;
  }

}