package com.dcc.osheaapp.service.upload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.identitymanagement.model.UserDetail;
import com.dcc.osheaapp.repository.IDistributorRepository;
import com.dcc.osheaapp.repository.IDropdownMastereRepository;
import com.dcc.osheaapp.repository.IUserDetailsRepository;
import com.dcc.osheaapp.repository.IUserTypeRepository;
import com.dcc.osheaapp.vo.DistributorVo;
import com.dcc.osheaapp.vo.UserDetailsVo;

@Service
public class DistributorExportService {
    private static final Logger LOGGER = LogManager.getLogger(DistributorExportService.class);

    @Autowired
    IDistributorRepository iDistributorRepository;
    @Autowired
    IDropdownMastereRepository dropdownMastereRepository;
    
    @Autowired
    IUserDetailsRepository iUserDetailsRepository;

    @Autowired
    IUserTypeRepository iUserTypeRepository;

    

    public OutputStream export(Long id, OutputStream os) {
        LOGGER.info("DistributorService :: Entering exportOutlet method");
        LOGGER.info("Zone id---->"+id);
        String msgResponse = null;

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Distributor Details");

        int rowNum = 0;
        byte[] bytes = null;

        CellStyle my_style = getHeaderCellStyle(workbook, sheet);
        OutputStream out = null;

        List<DistributorVo> listofDistributor = iDistributorRepository.findActiveDistributorZone(id, true);
        LOGGER.info("DistributorDetails :: ExportDistributor() :: listOfDistributor :: " + listofDistributor);


        if(null != listofDistributor){
            Row row = sheet.createRow(rowNum++);

            Cell cell0 = row.createCell(0);
            cell0.setCellValue("Zone");
            cell0.setCellStyle(my_style);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue("Region");
            cell1.setCellStyle(my_style);

            Cell cell2 = row.createCell(2);
            cell2.setCellValue("Distributor_Name");
            cell2.setCellStyle(my_style);

            Cell cell3 = row.createCell(3);
            cell3.setCellValue("Associated_Name");
            cell3.setCellStyle(my_style);

            Cell cell4 = row.createCell(4);
            cell4.setCellValue("Associated_type");
            cell4.setCellStyle(my_style);

            Cell cell5 = row.createCell(5);
            cell5.setCellValue("Address");
            cell5.setCellStyle(my_style);

            Cell cell6 = row.createCell(6);
            cell6.setCellValue("Contact_Number");
            cell6.setCellStyle(my_style);

            Cell cell7 = row.createCell(7);
            cell7.setCellValue("Credit_Duration");
            cell7.setCellStyle(my_style);

            Cell cell8 = row.createCell(8);
            cell8.setCellValue("GSTIN");
            cell8.setCellStyle(my_style);

            Cell cell9 = row.createCell(9);
            cell9.setCellValue("Pan_No");
            cell9.setCellStyle(my_style);

            Cell cell10 = row.createCell(10);
            cell10.setCellValue("Stockist_Type");
            cell10.setCellStyle(my_style);
        
            String zoneName = dropdownMastereRepository.getName(id+"");
            LOGGER.info("zoneName ============ >> "+zoneName);
                    
            for ( DistributorVo distributorVo : listofDistributor){
                row = sheet.createRow(rowNum++);

                String distributorName;
                String associatedName;
                String associatedType;
                String address;
                String contactNo;
                String creditDuration;
                String gstin;
                String panNo;
                String region;
                String zone;
                String stockistType;


                LOGGER.info("Zone Name ------->" +zoneName);
                cell0 = row.createCell(0);
                cell0.setCellValue(zoneName);

                cell1 = row.createCell(1);
                region = (distributorVo.getCompanyZone() != null) ? distributorVo.getRegion() : "N/A";
                LOGGER.info("Region ------->" +region);
                cell1.setCellValue(region);

                cell2 = row.createCell(2);
                distributorName =distributorVo.getDistributorName();
                LOGGER.info("Distributor Name ------->" +distributorName);
                cell2.setCellValue(distributorName);

                if(null != distributorVo.getUserId()){
                    associatedName = iUserDetailsRepository.getFullName(distributorVo.getUserId().getId().toString());
                }else{
                    associatedName = "N/A";

                }
                LOGGER.info("Associated Name ------->" +associatedName);
                cell3 = row.createCell(3);
                cell3.setCellValue(associatedName);

                if(null != distributorVo.getUserType())
                associatedType= iUserTypeRepository.userType(distributorVo.getUserType().getId().toString());
                else
                associatedType = "N/A";
                LOGGER.info("Associated Type ------->" +associatedType);
                cell4 = row.createCell(4);
                cell4.setCellValue(associatedType);

                cell5 = row.createCell(5);
                address = (distributorVo.getAddress() != null) ? distributorVo.getAddress() : "N/A";
                LOGGER.info("Address ------->" +address);
                cell5.setCellValue(address);

                cell6 = row.createCell(6);
                contactNo =(distributorVo.getContactNumber() != null) ? distributorVo.getContactNumber() : "N/A";
                LOGGER.info("Contact No ------->" +contactNo);
                cell6.setCellValue(contactNo);

                cell7 = row.createCell(7);
                creditDuration = (distributorVo.getCreditDu() != null) ? distributorVo.getCreditDu().toString() : "N/A";
                LOGGER.info("Credit duration ------->" +creditDuration);
                cell7.setCellValue(creditDuration);

                cell8 = row.createCell(8);
                gstin = (distributorVo.getGstin() != null) ? distributorVo.getGstin() : "N/A";
                LOGGER.info("Gstin ------->" +gstin);
                cell8.setCellValue(gstin);

                cell9 = row.createCell(9);
                panNo = (distributorVo.getPan() != null) ? distributorVo.getPan() : "N/A";
                LOGGER.info("pan No ------->" +panNo);
                cell9.setCellValue(panNo);

                cell10 =  row.createCell(10);
                stockistType = (distributorVo.getStockist() != null) ? distributorVo.getStockist() : "N/A";
                LOGGER.info("Stockist Type ------->" +stockistType);
                cell10.setCellValue(stockistType);
 
            }
            for (int i = 0; i < 21; i++)
            sheet.autoSizeColumn(i); // To expand the column
        } else {
            msgResponse = "Distributor Details Not Found";

        }

        try{
            out = os;
            workbook.write(out);
            workbook.close();
          }catch(FileNotFoundException e1){
            e1.printStackTrace();
          }catch (IOException e){
            e.printStackTrace();
          }
        
            return os;
    }



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

}
