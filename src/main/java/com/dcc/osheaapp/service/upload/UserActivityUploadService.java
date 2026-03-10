package com.dcc.osheaapp.service.upload;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.Constants;
import com.dcc.osheaapp.repository.IOutletRepository;
import com.dcc.osheaapp.repository.IUserActivityRepository;
import com.dcc.osheaapp.repository.IUserDetailsRepository;
import com.dcc.osheaapp.vo.OutletVo;
import com.dcc.osheaapp.vo.UserActivityRegisterVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import com.dcc.osheaapp.vo.dto.UserActivityInputDto;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserActivityUploadService {

  private static final Logger LOGGER = LogManager.getLogger(UserActivityUploadService.class);

  @Autowired IUserActivityRepository userActivityRepository;

  @Autowired IOutletRepository outletRepository;
  @Autowired IUserDetailsRepository userDetailsRepository;

  CellStyle getHeaderCellStyle(XSSFWorkbook workbook, XSSFSheet sheet) {
    Font headerFont = workbook.createFont();
    headerFont.setColor(IndexedColors.BLACK.index);
    headerFont.setFontName("Arial");
    CellStyle my_style = sheet.getWorkbook().createCellStyle();
    my_style.setFillForegroundColor(IndexedColors.YELLOW.index);
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

  public Object exportUserActivity(UserActivityInputDto inputVo, HttpServletResponse response) {
    LOGGER.info("UploadService :: Entering exportUserActivity method");
    //		ResponseEntity<ApiResponse> apiResponse = null;
    //        String msgResponse = null;

    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("User Activity Details");
    //        int rowNum = 0;

    getHeaderCellStyle(workbook, sheet);

    //        OutputStream out = null;

    List<UserActivityRegisterVo> getDataList = null;
    String whereClause = " ";
    String limitStr = "";

    LOGGER.info(
        "UploadService :: exportUserActivity :: UserActivityInputDto inputVo :: " + inputVo);

    if (null == inputVo.getPage() && null == inputVo.getSize()) {
      // do nothing
    } else {
      Integer size =
          (null != inputVo.getSize()) ? inputVo.getSize() : Constants.DEFAULT_DATA_SIZE_PAGINATION;
      Integer page =
          (null != inputVo.getPage() && inputVo.getPage() > 1) ? (inputVo.getPage() - 1) * size : 0;
      limitStr = " limit " + size + " offset " + page;
      LOGGER.info("limitStr..." + limitStr);
    }

    String fromDate = inputVo.getFromDate();
    String toDate = inputVo.getToDate();

    if (null != inputVo.getUserID()) {
      whereClause += " and userID = " + inputVo.getUserID();
    }

    if (null != inputVo.getOutletID()) {
      whereClause += " and outletID = " + inputVo.getOutletID();
    }

    LOGGER.info("Whereclause..." + whereClause);
    LOGGER.info("limitStr..." + limitStr);
    getDataList =
        userActivityRepository.searchUserActivity(whereClause, fromDate, toDate, limitStr);
    LOGGER.info("UploadService :: exportUserActivity() :: getDataList :: " + getDataList);
    // User activity details
    //		List<UserDetailsVo> listOfUsers = iUserDetailsRepository.findByIsActive(true);
    //		List<UserActivityRegisterVo> getDataList =
    // userActivityRepository.findUserActivityDetails(fromDate, toDate);
    //		LOGGER.info("UploadService :: exportUserActivity() :: getDataList :: " + getDataList);
    //
    //		if(null != getDataList) {
    //			Row row = sheet.createRow(rowNum++);
    //
    //			Cell cell0 = row.createCell(0);
    //			cell0.setCellValue("Date");
    //			cell0.setCellStyle(my_style);
    //
    //			Cell cell1 = row.createCell(1);
    //			cell1.setCellValue("BA Name");
    //			cell1.setCellStyle(my_style);
    //
    //			Cell cell2 = row.createCell(2);
    //			cell2.setCellValue("Checked In Time");
    //			cell2.setCellStyle(my_style);
    //
    //			Cell cell3 = row.createCell(3);
    //			cell3.setCellValue("Checked Out Time");
    //			cell3.setCellStyle(my_style);
    //
    //			Cell cell4 = row.createCell(4);
    //			cell4.setCellValue("Working Hours");
    //			cell4.setCellStyle(my_style);
    //
    //			Cell cell5 = row.createCell(5);
    //			cell5.setCellValue("Activity"); //In store, Office work, Leave, Week off, Holiday
    //			cell5.setCellStyle(my_style);
    //
    //			for (int i=0; i<6; i++)
    //				sheet.autoSizeColumn(i);			//To expand the column
    //
    //			for (UserActivityRegisterVo userActivityDetailsVo : getDataList) {}
    //			msgResponse = "BA Activity Details Exported Successfully.";
    ////			apiResponse = new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "BA Details Exported
    // Successfully."),HttpStatus.OK);
    //		}else {
    //			msgResponse = "BA Activity Details Not Found.";
    ////			apiResponse = new ResponseEntity<>(new ApiResponse(404, "NOT FOUND", "BA Details Not
    // Found."),HttpStatus.NOT_FOUND);
    //		}
    //
    //		try {
    //
    //			out = response.getOutputStream();
    //			workbook.write(out);
    //			workbook.close();
    //
    //		} catch (FileNotFoundException e1) {
    //			e1.printStackTrace();
    //		} catch (IOException e) {
    //			e.printStackTrace();
    //		}
    return getDataList;
    //		return null;
  }

  public Object exportUserAllActivity(UserActivityInputDto inputVo, HttpServletResponse response) {
    LOGGER.info("UploadService :: Entering exportUserActivity method");
    //		ResponseEntity<ApiResponse> apiResponse = null;
    String msgResponse = null;

    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("User Activity Details");
    int rowNum = 0;

    CellStyle my_style = getHeaderCellStyle(workbook, sheet);

    OutputStream out = null;
    List<String> activity = new ArrayList<String>();
    activity.add("store_login");
    activity.add("store_logout");
    activity.add("office_work");
    activity.add("comp_off");
    activity.add("leave");
    activity.add("holiday");
    activity.add("purchase_entry");
    activity.add("sale_entry");
    activity.add("damage_entry");
    activity.add("stock_entry");
    activity.add("attendance");
    activity.add("week_off");
    activity.add("day_in");
    activity.add("day_out");
    activity.add("stock_edit");
    activity.add("purchase_draft");
    activity.add("purchase_edit");
    activity.add("sale_edit");
    activity.add("purchase_return");
    activity.add("sale_return");

    List<UserActivityRegisterVo> getDataList = null;

    LOGGER.info(
        "UploadService :: exportUserAllActivity :: UserActivityInputDto inputVo :: " + inputVo);
    LOGGER.info(
        "UploadService :: exportUserAllActivity :: UserActivityInputDto :: activityType :: "
            + inputVo.getActivityType());

    if (inputVo.getActivityType() != null) {
      getDataList =
          userActivityRepository.searchUserActivityByActivityTypeAndUserId(
              inputVo.getActivityType(),
              inputVo.getFromDate(),
              inputVo.getToDate(),
              inputVo.getUserID());
    }
    if (inputVo.getActivityType() == null) {
      getDataList =
          userActivityRepository.searchUserActivityByActivityTypeAndUserId(
              activity, inputVo.getFromDate(), inputVo.getToDate(), inputVo.getUserID());
    }

    LOGGER.info("UploadService :: exportUserAllActivity() :: getDataList :: " + getDataList);
    int totalNo = getDataList.size();
    LOGGER.info("UploadService :: exportUserAllActivity() :: getDataList totalNo :: " + totalNo);

    if (null != getDataList) {
      Row row = sheet.createRow(rowNum++);

      Cell cell0 = row.createCell(0);
      cell0.setCellValue("Activity Date");
      cell0.setCellStyle(my_style);

      Cell cell1 = row.createCell(1);
      cell1.setCellValue("BA Code");
      cell1.setCellStyle(my_style);

      Cell cell2 = row.createCell(2);
      cell2.setCellValue("BA Name");
      cell2.setCellStyle(my_style);

      Cell cell3 = row.createCell(3);
      cell3.setCellValue("Outlet Code");
      cell3.setCellStyle(my_style);

      Cell cell4 = row.createCell(4);
      cell4.setCellValue("Outlet Name");
      cell4.setCellStyle(my_style);

      // Store Login, Store Logout, Office work, Leave, Week off, Holiday
      // Purchase entry, Purchase edit, Sale entry, Sale edit, Stock entry
      Cell cell5 = row.createCell(5);
      cell5.setCellValue("Activity Type");
      cell5.setCellStyle(my_style);

      Cell cell6 = row.createCell(6);
      cell6.setCellValue("Cause of Leave");
      cell6.setCellStyle(my_style);

      Cell cell7 = row.createCell(7);
      cell7.setCellValue("Activity Time");
      cell7.setCellStyle(my_style);

      Cell cell8 = row.createCell(8);
      cell8.setCellValue("Working Hours");
      cell8.setCellStyle(my_style);

      Cell cell9 = row.createCell(9);
      cell9.setCellValue("Sale/Purchase Amount");
      cell9.setCellStyle(my_style);

      Cell cell10 = row.createCell(10);
      cell10.setCellValue("Salary");
      cell10.setCellStyle(my_style);

      for (int i = 0; i < 11; i++) sheet.autoSizeColumn(i); // To expand the column

      for (UserActivityRegisterVo userActivityDetailsVo : getDataList) {
        LOGGER.info(
            "UploadService :: exportUserAllActivity() :: userActivityDetailsVo :: "
                + userActivityDetailsVo);

        row = sheet.createRow(rowNum++);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedActivityTime = formatter.format(userActivityDetailsVo.getActivityTime());
        String[] tempActivityTime = formattedActivityTime.split(" ");
        String activityDate = "";
        String activityTime = "";
        if (tempActivityTime.length >= 2) {
          activityDate = tempActivityTime[0];
          activityTime = tempActivityTime[1];
        } else {
          LOGGER.info(
              "UploadService :: exportUserAllActivity :: Invalid Activity Time"
                  + userActivityDetailsVo.getActivityTime());
        }

        UserDetailsVo user =
            userDetailsRepository
                .findById(userActivityDetailsVo.getCreatedBy())
                .orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] {"User"}));
        String BAUserName = user.getUserCred().getUsername();
        String BACode = "";
        if (BAUserName.contains("BA")) {
          int endIndex = BAUserName.indexOf('@'); // Find the index of the '@' character
          if (endIndex != -1) {
            BACode = BAUserName.substring(0, endIndex); // Extract the desired substring
          } else {
            // Handle the case where '@' is not found in the username
            LOGGER.info(
                "UploadService :: exportUserAllActivity() :: BAUserName :: Invalid username format :: "
                    + BAUserName);
            BACode = BAUserName;
          }
        }

        // Outlet Details
        OutletVo OutletDetails =
            outletRepository
                .findById(userActivityDetailsVo.getOutlet().getId())
                .orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] {"Outlet"}));
        // Only Active AssotiatedOutlets
        String outletName = "";
        String outletCode = "";

        if (null != OutletDetails) {
          outletName = OutletDetails.getOutletName();
          outletCode = OutletDetails.getOutletCode();
        } else {
          outletName = "NA";
          outletCode = "NA";
        }

        //				LOGGER.info("UploadService :: exportUserAllActivity() :: Working Hours :: " +
        // userActivityDetailsVo.getWorkingHours());
        String workingHours = "";
        String causeOfLeave = "";
        if (null != userActivityDetailsVo.getWorkingHours()) {
          if (userActivityDetailsVo.getActivityType().name().equals("store_logout")) {
            Long workingHoursInMinutes = userActivityDetailsVo.getWorkingHours();
            LOGGER.info(
                "UploadService :: exportUserAllActivity() :: store_logout :: workingHoursInMinutes :: "
                    + workingHoursInMinutes);
            long diffMinutes = workingHoursInMinutes % 60;
            long diffHours = workingHoursInMinutes / 60;

            String finalWorkingHours =
                ((diffHours > 0) ? (diffHours + " Hrs ") : "") + diffMinutes + " Min ";
            LOGGER.info(
                "UploadService :: exportUserAllActivity() :: store_logout :: finalWorkingHours :: "
                    + finalWorkingHours);
            workingHours = finalWorkingHours;
          }
        } else {
          if (null == userActivityDetailsVo.getActivityType()) {
            workingHours = "NULL";
          } else if (userActivityDetailsVo.getActivityType().name().equals("store_login")
              || userActivityDetailsVo.getActivityType().name().equals("leave")
              || userActivityDetailsVo.getActivityType().name().equals("office_work")) {
            workingHours = "0";
          } else if (userActivityDetailsVo.getActivityType().name().equals("purchase_entry")
              || userActivityDetailsVo.getActivityType().name().equals("purchase_edit")
              || userActivityDetailsVo.getActivityType().name().equals("sale_entry")
              || userActivityDetailsVo.getActivityType().name().equals("sale_edit")
              || userActivityDetailsVo.getActivityType().name().equals("stock_entry")
              || userActivityDetailsVo.getActivityType().name().equals("damage_entry")
              || userActivityDetailsVo.getActivityType().name().equals("purchase_return")
              || userActivityDetailsVo.getActivityType().name().equals("sale_return")) {
            workingHours = "NA";
          } else if (userActivityDetailsVo.getActivityType().name().equals("leave")) {
            if (userActivityDetailsVo.getLeaveType().contentEquals("week_off")) {
              causeOfLeave = "Week Off";
            }
          } else {
            causeOfLeave = "NA";
          }
        }

        String salePurchaseAmount = "";
        String salary = "";
        if (null == userActivityDetailsVo.getActivityType()) {
          workingHours = "NULL AMOUNT";
        } else if (userActivityDetailsVo.getActivityType().name().equals("purchase_entry")
            || userActivityDetailsVo.getActivityType().name().equals("purchase_edit")
            || userActivityDetailsVo.getActivityType().name().equals("sale_entry")
            || userActivityDetailsVo.getActivityType().name().equals("sale_edit")
            || userActivityDetailsVo.getActivityType().name().equals("stock_entry")
            || userActivityDetailsVo.getActivityType().name().equals("damage_entry")
            || userActivityDetailsVo.getActivityType().name().equals("purchase_return")
            || userActivityDetailsVo.getActivityType().name().equals("sale_return")) {
          salePurchaseAmount = userActivityDetailsVo.getSalePurchaseAmount();
        } else if (userActivityDetailsVo.getActivityType().name().equals("store_login")
            || userActivityDetailsVo.getActivityType().name().equals("store_logout")
            || userActivityDetailsVo.getActivityType().name().equals("leave")
            || userActivityDetailsVo.getActivityType().name().equals("office_work")) {
          salePurchaseAmount = "NA";
        }
        cell0 = row.createCell(0);
        cell0.setCellValue(activityDate);

        cell1 = row.createCell(1);
        cell1.setCellValue(BACode);

        cell2 = row.createCell(2);
        cell2.setCellValue(user.getFullName());

        cell3 = row.createCell(3);
        cell3.setCellValue(outletCode);

        cell4 = row.createCell(4);
        cell4.setCellValue(outletName);

        cell5 = row.createCell(5);
        cell5.setCellValue(
            (null != userActivityDetailsVo.getActivityType())
                ? userActivityDetailsVo.getActivityType().toTitleCase()
                : "NULL ACTIVITY");

        cell6 = row.createCell(6);
        cell6.setCellValue(causeOfLeave);

        cell7 = row.createCell(7);
        cell7.setCellValue(activityTime);

        cell8 = row.createCell(8);
        cell8.setCellValue(workingHours);

        cell9 = row.createCell(9);
        cell9.setCellValue(salePurchaseAmount);

        cell10 = row.createCell(10);
        salary = user.getSalary() != null ? user.getSalary() : "";
        cell10.setCellValue(salary);
      }
      msgResponse = "BA All Activity Details Exported Successfully.";
    }

    try {

      out = response.getOutputStream();
      workbook.write(out);
      workbook.close();

    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    //		return getDataList;
    return msgResponse;
  }
}
