package com.dcc.osheaapp.service.upload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.report.common.model.UserChainFlat;
import com.dcc.osheaapp.repository.IBeatNameRepository;
import com.dcc.osheaapp.repository.ICompanyZoneRepository;
import com.dcc.osheaapp.repository.IDistributorRepository;
import com.dcc.osheaapp.repository.IDropdownMastereRepository;
import com.dcc.osheaapp.repository.IOutletChannelRepository;
import com.dcc.osheaapp.repository.IOutletRepository;
import com.dcc.osheaapp.repository.IOutletUserMappingRepository;
import com.dcc.osheaapp.repository.IProductDivisionRepository;
import com.dcc.osheaapp.repository.IUserDetailsRepository;
import com.dcc.osheaapp.service.UserChainService;
import com.dcc.osheaapp.service.UserService;
import com.dcc.osheaapp.vo.BeatName;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import com.dcc.osheaapp.vo.OutletVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import com.dcc.osheaapp.vo.UserTypeVo;
import com.dcc.osheaapp.vo.views.OutletUserMappingView;

@Service
public class OutletUploadService {

  private static final Logger LOGGER = LogManager.getLogger(OutletUploadService.class);

  @Autowired IOutletChannelRepository outletChannelRepository;

  @Autowired IBeatNameRepository beatNameRepository;

  @Autowired IDropdownMastereRepository dropdownMastereRepository;

  @Autowired IDistributorRepository distributorRepository;

  @Autowired IProductDivisionRepository productDivisionRepository;

  @Autowired ICompanyZoneRepository companyZoneRepository;

  @Autowired IOutletRepository outletRepository;
  @Autowired

  IOutletUserMappingRepository iOutletUserMappingRepository;
  
 @Autowired IUserDetailsRepository iUserDetailsRepository;
  
  @Autowired UserService userService;

  @Autowired
  UserChainService userChainService;

  @PersistenceContext
  EntityManager em;

//  @Autowired
//  IUserChainRepository userChainRepository;



  public void upload(MultipartFile excelInput) {
    LOGGER.info("OutletService :: saveOutletFromExcel() called...");
    //		ResponseEntity<ApiResponse> responseEntity = null;
    //		ApiResponse apiResponse = null;
    try {
      XSSFWorkbook workbook = new XSSFWorkbook(excelInput.getInputStream());
      XSSFSheet worksheet = workbook.getSheetAt(0);
      //				XSSFSheet worksheet = workbook.getSheet("outlets");
      int rows = worksheet.getLastRowNum();
      LOGGER.info("OutletService :: saveOutletFromExcel() :: Rows in WorkSheet :: " + rows);
      LOGGER.info(
          "OutletService :: saveOutletFromExcel() :: Defined Rows in WorkSheet :: "
              + worksheet.getPhysicalNumberOfRows());
      DataFormatter format = new DataFormatter();
      //				StringBuilder msg = new StringBuilder();

      List<OutletVo> dataList = new ArrayList<>();

      int count = 0;
      for (int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) {
        //				for (int i = 1; i <= rows; i++) {
        XSSFRow row = worksheet.getRow(i);
        OutletVo vo = null;
        //					LOGGER.info("OutletService :: saveOutletFromExcel() :: row.getCell(0)) ::
        // "+row.getCell(0));
        //					try {
        if (null != row) {
        LOGGER.info("format :: " + format.toString());
    	LOGGER.info("outletCode :: " + format.formatCellValue(row.getCell(1)));
    	LOGGER.info("outletName :: " + format.formatCellValue(row.getCell(2)));
    	LOGGER.info("outletTypeCellValue :: " + format.formatCellValue(row.getCell(3)));
    	LOGGER.info("outletChannelCellValue :: " + format.formatCellValue(row.getCell(4)));
    	LOGGER.info("beat :: " + format.formatCellValue(row.getCell(5)));
    	LOGGER.info("companyZoneCellValue :: " + format.formatCellValue(row.getCell(6)));
    	LOGGER.info("region :: " + format.formatCellValue(row.getCell(7)));
    	LOGGER.info("market :: " + format.formatCellValue(row.getCell(8)));
          String outletCode = format.formatCellValue(row.getCell(1));
          LOGGER.info("OutletService :: saveOutletFromExcel() :: outletCode :: " + outletCode);
          String outletName = format.formatCellValue(row.getCell(2));
          
          String outletTypeCellValue = format.formatCellValue(row.getCell(3));
          LOGGER.info("OutletService :: saveOutletFromExcel() :: outletTypeCellValue :: " + outletTypeCellValue);
          Long outletType = Long.parseLong(outletTypeCellValue.trim());
          LOGGER.info("OutletService :: saveOutletFromExcel() :: outletType :: " + outletType);
          
          String outletChannelCellValue = format.formatCellValue(row.getCell(4));
          Long outletChannel = Long.parseLong(outletChannelCellValue);
          
          Long beat = beatNameRepository.getIDByBeatName(format.formatCellValue(row.getCell(5)));
          
          String companyZoneCellValue = format.formatCellValue(row.getCell(6));
          Long companyZone = Long.parseLong(companyZoneCellValue);
          
          String region = format.formatCellValue(row.getCell(7));
          String market = format.formatCellValue(row.getCell(8));
                   
//          Long soName =
//              Long.valueOf(
//                  format.formatCellValue(
//                      row.getCell(
//                          7))); // clarify this : Long in OutletVo > which Vo is referred? DD in
          
          // NOT in Excel but in VO
          String address = "";
          String city = "";
          
          Long distributor = 1L;
          
          Long productDivision = dropdownMastereRepository.getIDByFieldName("Oshea");

          String latitude = "22.580664781364284";
          String longitude = "88.43723466902894";
          Long state = Long.valueOf("1");
          Long country = Long.valueOf("1");
          Long pin = Long.valueOf("999999");
          Long createdBy = Long.valueOf("2");

          vo = new OutletVo();

          vo.setOutletCode(outletCode);
          vo.setOutletName(outletName);
          vo.setOutletChannel(outletChannel);
          vo.setOutletType(outletType);
          vo.setMarket(market);
          vo.setBeat(beat);
          vo.setCompanyZone(companyZone);
          vo.setRegionName(region);
          vo.setLatitude(latitude); 
          vo.setLongitude(longitude);
          vo.setAddress(address);
          vo.setCity(city);
          vo.setState(state);
          vo.setCountry(country);
          vo.setPin(pin);
          vo.setIsActive(true);
          vo.setCreatedOn(new Date());
          vo.setCreatedBy(createdBy);
          vo.setUpdatedOn(new Date());
          vo.setUpdatedBy(createdBy);
//        vo.setSoUserId(soName);

          // Present in Excel but not in Vo
          vo.setDistributor(distributor);
          vo.setProductDivision(productDivision);

          OutletVo resultVo = outletRepository.save(vo);
          LOGGER.info("resultVo ----------------:" + resultVo);
          dataList.add(resultVo);
          if (resultVo != null) {
            count++;
            LOGGER.info("COUNT --------------------------" + count);
            LOGGER.info("Outlet Data saved in DB -----------");
          } else {
            LOGGER.info("Outlet Data NOT saved in DB -----------" + vo);
            //							dataNotSaveList.add(i);
          }
          //						apiResponse = new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Outlet(s)
          // created successfully.", resultVo), HttpStatus.OK);
        } else {
          break;
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error(
          "OutletService :: saveOutletFromExcel() :: ERROR :: Exception 3 -------> "
              + e.getMessage());
    }
  }

//public Object export(List<OutletVo> records, OutputStream outputStream){
//    String msgResponse = "Outlet Report Exported";
//  BiFunction<String, Counter>
//
//}
  
  public void uploadOutletAndBa(MultipartFile excelInput) {

	  LOGGER.info("OutletService :: saveOutletFromExcel() called...");
	    //		ResponseEntity<ApiResponse> responseEntity = null;
	    //		ApiResponse apiResponse = null;
	    try {
	      XSSFWorkbook workbook = new XSSFWorkbook(excelInput.getInputStream());
	      XSSFSheet worksheet = workbook.getSheetAt(0);
	      int rows = worksheet.getLastRowNum();
	      LOGGER.info("OutletService :: saveOutletFromExcel() :: Rows in WorkSheet :: " + rows);
	      LOGGER.info(
	          "OutletService :: saveOutletFromExcel() :: Defined Rows in WorkSheet :: "
	              + worksheet.getPhysicalNumberOfRows());
	      DataFormatter format = new DataFormatter();
	    
	      LOGGER.info("No of rows--> :: " + worksheet.getPhysicalNumberOfRows());
	      int count = 0;
	      for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
	        XSSFRow row = worksheet.getRow(i);
	        
//	        String employeeTypeXL = format.formatCellValue(row.getCell(13));
//	        LOGGER.info("employeeTypeXL :: " + employeeTypeXL);
	      //for emp_type 
//        	if(!employeeTypeXL.toLowerCase().trim().equals("payroll")) {
//        		employeeTypeXL = employeeTypeXL.replace(" ", "-");
//        	}else {
//        		employeeTypeXL = "Payroll";
//        	}
//        	LOGGER.info("employeeTypeXL after IF :: " + employeeTypeXL);
//        	Long empTypeIdFromDB = dropdownMastereRepository.getIDByFieldName(employeeTypeXL.toLowerCase(), "salary");
//        	
//        	LOGGER.info("empTypeIdFromDB :: " + empTypeIdFromDB);
//	        
	        if (null != row) {
	        
	        // First Check BeatName if present in DB then set otherwise save in DB.
	        LOGGER.info("beatname :: " + format.formatCellValue(row.getCell(18)));
	    	
	    	String beatName = format.formatCellValue(row.getCell(18));
	    	BeatName beatnameResult = null;
	    	if(null != beatName) {
	    		 beatnameResult = beatNameRepository.fetchByBeatName(beatName);
	    		 LOGGER.info("beatnameResult :: "+ beatnameResult);
	    		if(null == beatnameResult) {
	    			BeatName beatnameObj = new BeatName();
	    			beatnameObj.setBeatName(beatName);
	    			
	    			beatnameResult = beatNameRepository.save(beatnameObj);
	    		}
	    	}
	    	
	    	// Second Check Company Zone from DB then set.
	    	String companyZoneName = format.formatCellValue(row.getCell(1));
	    	LOGGER.info("companyZoneName :: " + companyZoneName);
	    	Long companyZoneId = dropdownMastereRepository.getIDByFieldName(companyZoneName.toLowerCase(), "zone");
//	    	Long companyZoneId = companyZoneRepository.getIDByCompanyZoneName(companyZoneName);
	    	
	    	LOGGER.info("BeatName & Company Zone-->>  :: " + beatnameResult.toString() + " -->> "+ companyZoneId);
	        
	    	
	    	// Save to Outlet Master
	    	OutletVo outletVo = new OutletVo();
	    	
	    	String outletName = format.formatCellValue(row.getCell(16));
	    	String outletCode = format.formatCellValue(row.getCell(15));
	    	String outletMarket = format.formatCellValue(row.getCell(17));
	    	String productDivisionXL = format.formatCellValue(row.getCell(12));
	    	String regionName = format.formatCellValue(row.getCell(2));
	    	
	    	
	    	outletVo.setRegionName(regionName);
	    	outletVo.setOutletName(outletName);
	    	outletVo.setOutletCode(outletCode);
	    	outletVo.setLatitude(null);
	    	outletVo.setLongitude(null);
	    	
	    	//** Set Default value of Outlet Channel
	    	outletVo.setOutletChannel(2L);
	    	//** Set Default value of Outlet Type
	    	outletVo.setOutletType(2L);
	    	
	    	outletVo.setMarket(outletMarket);
	    	outletVo.setBeat(beatnameResult.getId());
	    	outletVo.setCompanyZone(companyZoneId);
	    	outletVo.setDistributor(null);
	    	
	    	Long productDivisionDB = dropdownMastereRepository.getIDByFieldName(productDivisionXL.toLowerCase(), "division");
	    	outletVo.setProductDivision(productDivisionDB);
	    	
	    	outletVo.setOwnerFullName(null);
	    	outletVo.setOwnerContactNumber(null);
	    	outletVo.setAddress(null);
	    	outletVo.setCity(outletMarket);
	    	outletVo.setCountry(null);
	    	outletVo.setPin(null);
	    	outletVo.setIsActive(true);
	    	outletVo.setCreatedBy(1L);
	    	outletVo.setCreatedOn(new Date());
	    	outletVo.setUpdatedBy(1L);
	    	outletVo.setUpdatedOn(new Date());
	    	
	        OutletVo resultOutletVo = outletRepository.save(outletVo);
	        
	        String baCodeXL = format.formatCellValue(row.getCell(9));
	        
	        if((!baCodeXL.isEmpty()) && (null != resultOutletVo)) {
	        	
	        	// If outlet data saved then processed to User Register Method -->> 
	        	UserDetailsVo input = new UserDetailsVo();
	        	
	        	String fullName = format.formatCellValue(row.getCell(10));
	        	String contactNumber = format.formatCellValue(row.getCell(14));
	        	String employeeTypeXL = format.formatCellValue(row.getCell(13));
	        	
	        	String state = format.formatCellValue(row.getCell(2));
	        	String dateOfJoining = format.formatCellValue(row.getCell(11));
	        	String baCode = format.formatCellValue(row.getCell(9));
	        	String bdeName = format.formatCellValue(row.getCell(8));
	        	
	        	input.setFullName(fullName);
	        	
	        	UserTypeVo userType = new UserTypeVo();
	        	userType.setId(4L);
	        	userType.setUserType("BA");
	        	input.setUserType(userType);
	        	
	        	input.setContactNumber(contactNumber);
	        	input.setAddress(null);
	        	input.setPan(null);
	        	input.setBankName(null);
	        	input.setBankBranchName(null);
	        	input.setBankAccountNo(null);
	        	input.setIfscCode(null);
	        	
	        	//for emp_type 
	        	if(!employeeTypeXL.toLowerCase().trim().equals("payroll")) {
	        		employeeTypeXL = employeeTypeXL.replace(" ", "-");
	        	}else {
	        		employeeTypeXL = "Payroll";
	        	}
	        	Long empTypeIdFromDB = dropdownMastereRepository.getIDByFieldName(employeeTypeXL.toLowerCase(), "salary");
	        	DropdownMasterVo employeeType = new DropdownMasterVo();
	        	employeeType.setId(empTypeIdFromDB);
	        	
	        	input.setEmployeeType(employeeType);
	        	
	        	//For company Zone
	        	DropdownMasterVo companyZone = new DropdownMasterVo();
	        	companyZone.setId(companyZoneId);
	        	
	        	input.setCompanyZone(companyZone);
	        	
	        	//For Product division
	        	DropdownMasterVo productDivision = new DropdownMasterVo();
	        	productDivision.setId(productDivisionDB);
	        	
	        	input.setProductDivision(productDivision);
	        	
	        	//For associate Outlet
	        	List<OutletVo> assotiateOutlet = new ArrayList<>();
	        	OutletVo userOutlet = new OutletVo();
	        	userOutlet.setId(resultOutletVo.getId());
	        	assotiateOutlet.add(userOutlet);
	        	input.setAssotiateOutlet(assotiateOutlet);
	        	
	        	//Reporting To Name
//	        	UserDetailsVo reportingTo = new UserDetailsVo();
	        	
	        	UserDetailsVo reportingTo = iUserDetailsRepository.findById(Long.parseLong(bdeName)).orElse(null);
//	        			(bdeName.toUpperCase(), 12L, true);
	        	input.setReportingTo(reportingTo);								 
	        	
	        	//Reporting To Designation
	        	
	        	input.setState(state);
	        	
	        	//For Date of Joining...
	        	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

	            // Convert from String to Date
	            Date dojDate = df.parse(dateOfJoining);
	            System.out.println(dojDate);
	            
	            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//	            System.out.println(simpleDateFormat.format(dojDate).toString());
	            
	            String dateOfJoiningFormatted = simpleDateFormat.format(dojDate);
	            input.setDateOfJoining(simpleDateFormat.parse(dateOfJoiningFormatted));
	            
	            input.setBaCodeXl(baCode);
	            
	            input.setSalary(null);
	            input.setCreatedBy(1L);
	            input.setCreatedOn(new Date());
	            
	            
	            ResponseEntity<ApiResponse> apiresponse = userService.registerUpload(input);
	            if(apiresponse.getStatusCode().equals("200")) {
	            	LOGGER.info("Data inserted for outlet --->>> "+ outletCode);
	            }else {
	            	LOGGER.info("Data not inserted for  --->>> "+ outletCode);
	            }
	        		
	        	}
	        }
	      } 
	      LOGGER.info("<<<--- Upload process completed --->>> ");

	    } catch (Exception e) {
	      e.printStackTrace();
	      LOGGER.error(
	          "OutletService :: saveOutletFromExcel() :: ERROR :: Exception 3 -------> "
	              + e.getMessage());
	    }
	
  }


  public OutputStream export( Long id,OutputStream os){
    LOGGER.info("UploadService :: Entering exportOutlet method");
    String msgResponse = null;

    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("Outlet Details");
    int rowNum = 0;

    byte[] bytes = null;

    CellStyle my_style = getHeaderCellStyle(workbook, sheet);
    OutputStream out = null;

    List<OutletVo> listofOutlet = outletRepository.findActiveOutletsZone(id,true);
    LOGGER.info("OutletDetail :: exportOutlet() :: listOfOutlet :: " + listofOutlet);

//    UserChain user = userChainRepository.findByoutletId(Long.valueOf(99));
//    LOGGER.info("User Details " +user);
    if(null != listofOutlet){
      Row row = sheet.createRow(rowNum++);

      Cell cell0 = row.createCell(0);
      cell0.setCellValue("Zone");
      cell0.setCellStyle(my_style);

      Cell cell1 = row.createCell(1);
      cell1.setCellValue("BDE Name");
      cell1.setCellStyle(my_style);

      Cell cell2 = row.createCell(2);
      cell2.setCellValue("So Name");
      cell2.setCellStyle(my_style);

      Cell cell3 = row.createCell(3);
      cell3.setCellValue("BA Code");
      cell3.setCellStyle(my_style);

      Cell cell4 = row.createCell(4);
      cell4.setCellValue("BA Name");
      cell4.setCellStyle(my_style);

      Cell cell5 = row.createCell(5);
      cell5.setCellValue("Beat Name");
      cell5.setCellStyle(my_style);

      Cell cell6 = row.createCell(6);
      cell6.setCellValue("Outlet Erp Id");
      cell6.setCellStyle(my_style);

      Cell cell7 = row.createCell(7);
      cell7.setCellValue("Outlets Name");
      cell7.setCellStyle(my_style);



      Cell cell8 = row.createCell(8);
      cell8.setCellValue("Latitude");
      cell8.setCellStyle(my_style);

      Cell cell9 = row.createCell(9);
      cell9.setCellValue("Longitude");
      cell9.setCellStyle(my_style);

      Cell cell10 = row.createCell(10);
      cell10.setCellValue("Owners Name");
      cell10.setCellStyle(my_style);

      Cell cell11 = row.createCell(11);
      cell11.setCellValue("Owners Number");
      cell11.setCellStyle(my_style);

      Cell cell12 = row.createCell(12);
      cell12.setCellValue("Outlets Type");
      cell12.setCellStyle(my_style);

      Cell cell13 = row.createCell(13);
      cell13.setCellValue("Address");
      cell13.setCellStyle(my_style);

      Cell cell14 = row.createCell(14);
      cell14.setCellValue("Market");
      cell14.setCellStyle(my_style);

      Cell cell15 = row.createCell(15);
      cell15.setCellValue("Region");
      cell15.setCellStyle(my_style);

      Cell cell16 = row.createCell(16);
      cell16.setCellValue("Pincode");
      cell16.setCellStyle(my_style);

      Cell cell17 = row.createCell(17);
      cell17.setCellValue("isActive");
      cell17.setCellStyle(my_style);

      Cell cell18 = row.createCell(18);
      cell18.setCellValue("Outlet Creation Date");
      cell18.setCellStyle(my_style);



      for(OutletVo outletVo : listofOutlet){
        row = sheet.createRow(rowNum++);


        String baCode = "";
        String baName = "";
        String beatName = "";
        String outletErpId = "";
        String outletName = "";
        String latitude = "";
        String longitude = "";
        String ownerName = "";
        String ownersNumber = "";
        String outletsChannel = "";
        String address = "";
        String market = "";
        String region = "";
        String pincode = "";
        String isActive = "";
        String outletCreationDate="";


        String zoneName = "";
        Long zoneCode = null;
        if(null != outletVo.getCompanyZone()){
          zoneName = dropdownMastereRepository.getName(outletVo.getCompanyZone().toString());
        }else{
          zoneName = "NA";
        }

        LOGGER.info("zone Name -----------------------------> " +zoneName);
        cell0 = row.createCell(0);
        cell0.setCellValue(zoneName);

        Long outletId = outletVo.getId();

        List<OutletUserMappingView> baDetailsList = iOutletUserMappingRepository.findByoutletId(outletId);

        if (!baDetailsList.isEmpty()) {

          OutletUserMappingView baDetails = baDetailsList.get(0);



          cell3 = row.createCell(3);
          baCode = baDetails.getBaCode();
          cell3.setCellValue(baCode);

          cell4 = row.createCell(4);
          baName = baDetails.getBaName();
          cell4.setCellValue(baName);

          Long baId =baDetails.getBaId();
          if (baId != null ) {
            em.clear();
            UserChainFlat userChainFlat = userChainService.generateChainFlattened(baId);

            String bdeValue = userChainFlat.getBde();
            String soValue = userChainFlat.getSo();

            cell1 = row.createCell(1);
            cell1.setCellValue(bdeValue);

            cell2 = row.createCell(2);
            cell2.setCellValue(soValue);
          } else {
            cell2 = row.createCell(2);
            cell2.setCellValue("NA");

            cell3 = row.createCell(3);
            cell3.setCellValue("NA");
          }
        }

        if(null != outletVo.getBeat()){
          beatName = beatNameRepository.getName(outletVo.getBeat().toString());
        }else{
          beatName = "NA";
        }
        LOGGER.info("Beat Name -----------------------------> " +beatName);
        cell0 = row.createCell(5);
        cell0.setCellValue(beatName);

//        cell5 = row.createCell(5);
//        beatName = (outletVo.getBeat().toString());
//        cell5.setCellValue(beatName);

        cell6 = row.createCell(6);
        outletErpId = (outletVo.getOutletCode());
        cell6.setCellValue(outletErpId);

        cell7 = row.createCell(7);
        outletName = (outletVo.getOutletName());
        cell7.setCellValue(outletName);

        cell8 = row.createCell(8);
        latitude = (outletVo.getLatitude() != null) ? outletVo.getLatitude() : "N/A";
        cell8.setCellValue(latitude);

        cell9 = row.createCell(9);
        longitude = (outletVo.getLongitude()!= null) ? outletVo.getLongitude() : "N/A";
        cell9.setCellValue(longitude);

        cell10 = row.createCell(10);
        ownerName = (outletVo.getOwnerFullName()!= null) ? outletVo.getOwnerFullName() : "N/A";
        cell10.setCellValue(ownerName);

        cell11 = row.createCell(11);
        ownersNumber = (outletVo.getOwnerContactNumber() != null) ? outletVo.getOwnerContactNumber() : "N/A";
        cell11.setCellValue(ownersNumber);



        if(null != outletVo.getOutletChannel()){
//          outletsChannel = outletChannelRepository.getName(outletVo.getOutletChannel().toString());
            outletsChannel = dropdownMastereRepository.getName(outletVo.getOutletChannel().toString());
        }else{
          outletsChannel = "NA";
        }

        LOGGER.info("outlet channel Name -----------------------------> " +outletsChannel);
        cell0 = row.createCell(12);
        cell0.setCellValue(outletsChannel);

//        cell12 = row.createCell(12);
//        outletsChannel = (outletVo.getOutletChannel() != null) ? outletVo.getOutletChannel().toString(): "N/A";
//        cell12.setCellValue(outletsChannel);

        cell13 = row.createCell(13);
        address = (outletVo.getAddress() != null) ? outletVo.getAddress() : "N/A";
        cell13.setCellValue(address);

        cell14 = row.createCell(14);
        market = (outletVo.getMarket());
        cell14.setCellValue(market);

        cell15 = row.createCell(15);
        region = (outletVo.getRegionName());
        cell15.setCellValue(region);

        cell16 = row.createCell(16);
        pincode = (outletVo.getPin() !=null) ? outletVo.getPin().toString() : "N/A";
        cell16.setCellValue(pincode);

        cell17 = row.createCell(17);
        isActive = (outletVo.getIsActive().toString());
        cell17.setCellValue(isActive);

        cell18 = row.createCell(18);
        outletCreationDate = (outletVo.getCreatedOn().toString());
        cell18.setCellValue(outletCreationDate);

      }
        for (int i = 0; i < 21; i++)
            sheet.autoSizeColumn(i); // To expand the column



    } else{
      msgResponse = "Outlet Details Not Found";
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

  CellStyle getHeaderCellStyle(XSSFWorkbook workbook, XSSFSheet sheet){
    Font headerFont = workbook.createFont();
    headerFont.setColor(IndexedColors.BLACK.index);
    headerFont.setFontName("Arial");
    CellStyle my_style = sheet.getWorkbook().createCellStyle();
    my_style.setFillForegroundColor(IndexedColors.YELLOW.index);
    my_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    headerFont.setFontHeightInPoints((short)10);
    headerFont.setBold(true);
    my_style.setFont(headerFont);
    my_style.setBorderBottom(BorderStyle.THIN);
    my_style.setBorderTop(BorderStyle.THIN);
    my_style.setBorderLeft(BorderStyle.THIN);
    my_style.setBorderRight(BorderStyle.THIN);
    return my_style;
  }
}
