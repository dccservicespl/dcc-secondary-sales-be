package com.dcc.osheaapp.service.upload;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.leaderboard.domain.model.Leaderboard;
import com.dcc.osheaapp.leaderboard.domain.repository.LeaderBoardRepository;
import com.dcc.osheaapp.leaderboard.domain.service.LeaderboardService;
import com.dcc.osheaapp.report.common.model.UserChainFlat;
import com.dcc.osheaapp.common.model.Password;
import com.dcc.osheaapp.repository.*;
import com.dcc.osheaapp.service.UserChainService;
import com.dcc.osheaapp.vo.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.function.Function;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.dcc.osheaapp.vo.views.UserView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserUploadService {
	private static final Logger LOGGER = LogManager.getLogger(UserUploadService.class);


	String[] HEADER_COLS = { "BA NAME", "Contact Number", "BA CODE" };

	/**
	 * Contains a mapping from Excel column names to product view fields. Also,
	 * setter has been given for setting view fields with appropriate column values.
	 * Enum value contains relevant Product View field value.
	 */
	enum ColFieldEnum {
		BA_Name(UserDetailsVo::getFullName), Contact_Number(UserDetailsVo::getContactNumber),
		BA_Code(e -> e.getUserCred().getCode());

		private final Function<UserDetailsVo, Object> user;

		ColFieldEnum(Function<UserDetailsVo, Object> user) {
			this.user = user;
		}

		public Object getValue(UserDetailsVo view) {
			return this.user.apply(view);
		}

		public void setValue(UserDetailsVo view, Object value) {
			String val = (String) value;
			switch (this) {
			case BA_Name:
				view.setFullName(val);
				break;

			case Contact_Number:
				view.setContactNumber(val);
				break;
			case BA_Code:
				view.getUserCred().setCode(val, view.getContactNumber());
				break;
			}
		}
	}

	@Autowired
	IUserCredRepository userCredRepository;

	@Autowired
	IDropdownMastereRepository dropdownMastereRepository;

	@Autowired
	IOutletRepository outletRepository;

	@Autowired
	IUserDetailsRepository userDetailsRepository;

	@Autowired
	UserViewRepository userViewRepository;


	@Autowired
	IUserTypeRepository userTypeRepository;

	@Autowired
	IOutletUserMappingRepository outletUserMappingRepository;

	@Autowired
	IUserAssotoationRepository userAssotoationRepository;
	@Autowired
	UserChainService userChainService;
	@Autowired
	IDistributorRepository distributorRepository;

	@PersistenceContext
	EntityManager em;
	
	@Autowired
	IUserDetailsRepository iUserDetailsRepository;
	@Autowired
	LeaderboardService leaderboardService;

	@Autowired
	LeaderBoardRepository _leaderboardRepository;

	@Autowired
	LeaderBoardRepository leaderBoardRepository;

	public void uploadOld(MultipartFile excelInput) {
		XSSFWorkbook workbook = null;
		XSSFSheet worksheet = null;

		try {
			workbook = new XSSFWorkbook(excelInput.getInputStream());
			worksheet = workbook.getSheetAt(0);
		} catch (IOException e) {
			throw new OjbException(ErrorCode.INVALID_EXCEL_FILE, new Object[] {});
		}

		DataFormatter formatter = new DataFormatter();
		for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
			ExcelUploadService<UserDetailsVo> uploadService = new ExcelUploadService<>(workbook, worksheet,
					HEADER_COLS);
			Map<Integer, String> getColMapInverted = uploadService.getColMapInverted();
			String baCode = getColMapInverted.get("BA CODE");
			Row row = worksheet.getRow(i);
			if (null != baCode && !baCode.isEmpty()) {
				// existing user
			} else {
				// new ba
				UserDetailsVo user = new UserDetailsVo();
				user.setUserCred(new UserCredVo());
				user.setAssotiateOutlet(new ArrayList<>());
				user.setUserType(new UserTypeVo());
				uploadService.getColMapInverted().forEach((key, value) -> {
					Cell cell = row.getCell(key);
					String cellValue = formatter.formatCellValue(cell);
					getEnumFromHeader(value).ifPresent(e -> e.setValue(user, cellValue));
				});

				user.setIsActive(true);
			}
		}
	}

	/**
	 * get the enum value from the header string. e.g., if header is "header name"
	 * the respective enum value returned header_name.
	 *
	 * @param header
	 * @return Optional<ColFieldEnum>
	 */
	private Optional<ColFieldEnum> getEnumFromHeader(String header) {
		String headerEnumName = header.replace(" ", "_");
		return Arrays.stream(ColFieldEnum.values()).filter(e -> e.name().equals(headerEnumName)).findAny();
	}

	private String getFieldValue(String header, UserDetailsVo view) {
		return getEnumFromHeader(header).map(e -> (String) e.getValue(view)).orElse(null);
	}

	public OutputStream export(Long zoneId, OutputStream os) {
		LOGGER.info("UploadService :: Entering exportUser method");
		// ResponseEntity<ApiResponse> apiResponse = null;
		String msgResponse = null;

		// String FILE_NAME = "BAUsers.xlsx";
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("BA Details");
		int rowNum = 0;

		// Excel Download
		byte[] bytes = null;

		// response.setContentType("application/vnd.ms-excel");
		// response.setHeader("Content-Disposition", "attachment;filename=" +
		// FILE_NAME);

		CellStyle my_style = getHeaderCellStyle(workbook, sheet);

		OutputStream out = null;
		// Only Active Users
		List<UserDetailsVo> listOfUsers = userDetailsRepository.findByZone(zoneId,4);
		LOGGER.info("UploadService :: exportUser() :: listOfEntries :: " + listOfUsers);

		if (null != listOfUsers) {
			Row row = sheet.createRow(rowNum++);

			Cell cell0 = row.createCell(0);
			cell0.setCellValue("Zone");
			cell0.setCellStyle(my_style);

			Cell cell1 = row.createCell(1);
			cell1.setCellValue("MD");
			cell1.setCellStyle(my_style);

			Cell cell2 = row.createCell(2);
			cell2.setCellValue("ZSM");
			cell2.setCellStyle(my_style);

			Cell cell3 = row.createCell(3);
			cell3.setCellValue("NSM");
			cell3.setCellStyle(my_style);

			Cell cell4 = row.createCell(4);
			cell4.setCellValue("ASM");
			cell4.setCellStyle(my_style);

			Cell cell5 = row.createCell(5);
			cell5.setCellValue("BDE");
			cell5.setCellStyle(my_style);

			Cell cell6 = row.createCell(6);
			cell6.setCellValue("SO");
			cell6.setCellStyle(my_style);

			Cell cell7 = row.createCell(7);
			cell7.setCellValue("REGION");
			cell7.setCellStyle(my_style);

			Cell cell8 = row.createCell(8);
			cell8.setCellValue("Distributor Code");
			cell8.setCellStyle(my_style);

			Cell cell9 = row.createCell(9);
			cell9.setCellValue("Distributor Name");
			cell9.setCellStyle(my_style);

			Cell cell10 = row.createCell(10);
			cell10.setCellValue("Outlet Code");
			cell10.setCellStyle(my_style);

			Cell cell11 = row.createCell(11);
			cell11.setCellValue("Outlet Name");
			cell11.setCellStyle(my_style);

			Cell cell12 = row.createCell(12);
			cell12.setCellValue("Outlet Type");
			cell12.setCellStyle(my_style);

			Cell cell13 = row.createCell(13);
			cell13.setCellValue("BA Code");
			cell13.setCellStyle(my_style);

			Cell cell14 = row.createCell(14);
			cell14.setCellValue("BA Name");
			cell14.setCellStyle(my_style);

			Cell cell15 = row.createCell(15);
			cell15.setCellValue("Contact Number");
			cell15.setCellStyle(my_style);

			Cell cell16 = row.createCell(16);
			cell16.setCellValue("Is Active");
			cell16.setCellStyle(my_style);


			Cell cell17 = row.createCell(17);
			cell17.setCellValue("DOJ");
			cell17.setCellStyle(my_style);

			Cell cell18 = row.createCell(18);
			cell18.setCellValue("DOL");
			cell18.setCellStyle(my_style);

			//is_active

			Cell cell19 = row.createCell(19);
			cell19.setCellValue("SALARY");
			cell19.setCellStyle(my_style);

			Cell cell20 = row.createCell(20);
			cell20.setCellValue("PAN CARD");
			cell20.setCellStyle(my_style);

			Cell cell21 = row.createCell(21);
			cell21.setCellValue("AADHAR CARD");
			cell21.setCellStyle(my_style);

			Cell cell22= row.createCell(22);
			cell22.setCellValue("BANK ACCOUNT");
			cell22.setCellStyle(my_style);


					for (UserDetailsVo userDetailsVo : listOfUsers) {
				if (userDetailsVo.getUserType().getId().equals(Long.valueOf("4"))) {
					row = sheet.createRow(rowNum++);



					String BAUserName = userDetailsVo.getUserCred().getUsername();
					String BACode = "";
					if (BAUserName.contains("BA")) {
						int endIndex = BAUserName.indexOf('@'); // Find the index of the '@' character
						if (endIndex != -1) {
							BACode = BAUserName.substring(0, endIndex); // Extract the desired substring
						} else {
							// Handle the case where '@' is not found in the username
							LOGGER.info("UploadService :: exportUser() :: BAUserName :: Invalid username format :: "
									+ BAUserName);
							BACode = BAUserName;
						}
					} else if (BAUserName.contains("FLOATER")) {
						BACode = BAUserName;
					} else {
						LOGGER.info("UploadService :: exportUser() :: UserName :: Invalid username format :: "
								+ BAUserName);
						BACode = "NA";
					}


					String zoneName = "";
					Long zoneCode = null;
					if (null != userDetailsVo.getCompanyZone()) {
						zoneName = dropdownMastereRepository.getName(userDetailsVo.getCompanyZone().getId().toString());
						zoneCode = userDetailsVo.getCompanyZone().getId();
					} else {
						zoneName = "NA";
						zoneCode = Long.valueOf("0");
					}
					cell0 = row.createCell(0);
					cell0.setCellValue(zoneName);

					String salary = "";
					String pan = "";
					String aadhar = "";
					String bankAc = "";
					String doj = "";
					String dol = "";
					String state = "";
					String distributorCode = "NA";
					String distributorName = "NA";

					Long userId = userDetailsVo.getId();

					em.clear();
					UserChainFlat userChainFlat = userChainService.generateChainFlattened(userId);

					String mdValue = userChainFlat.getMd();
					String zsmValue = userChainFlat.getZsm();
					String nsmValue = userChainFlat.getNsm();
					String asmValue = userChainFlat.getAsm();
					String bdeValue = userChainFlat.getBde();
					String soValue = userChainFlat.getSo();


					cell1 = row.createCell(1);
					cell1.setCellValue(mdValue);

					cell2 = row.createCell(2);
					cell2.setCellValue(zsmValue);

					cell3 = row.createCell(3);
					cell3.setCellValue(nsmValue);

					cell4 = row.createCell(4);
					cell4.setCellValue(asmValue);

					cell5 = row.createCell(5);
					cell5.setCellValue(bdeValue);

					cell6 = row.createCell(6);
					cell6.setCellValue(soValue);

					cell7 = row.createCell(7);
					state = userDetailsVo.getState();
					cell7.setCellValue(state);

					cell8 = row.createCell(8);
					cell8.setCellValue(distributorCode);

					cell9 = row.createCell(9);
					cell9.setCellValue(distributorName);

					List<OutletVo> OutletDetails = outletRepository.findAllAssotiatedOutlet(userDetailsVo.getId()); // Both
					// Active
					// &
					// Inactive
					// AssotiatedOutlets

					String outletName = "";
					String outletCode = "";
					String outletType ="";
					String outletStatus = "";

					StringBuilder concatenatedOutletNames = new StringBuilder();
					StringBuilder concatenatedOutletCodes = new StringBuilder();

					if (OutletDetails != null && !OutletDetails.isEmpty()) {
						for (OutletVo OutletDetail : OutletDetails) {

							if (OutletDetails.size() > 1) {
								outletName = OutletDetail.getOutletName();
								outletCode = OutletDetail.getOutletCode();
								outletType = dropdownMastereRepository.getName(OutletDetail.getOutletChannel().toString());

								concatenatedOutletNames.append(outletName);
								concatenatedOutletCodes.append(outletCode);

							} else {
								outletName = OutletDetail.getOutletName();
								outletCode = OutletDetail.getOutletCode();
								outletType = dropdownMastereRepository.getName(OutletDetail.getOutletChannel().toString());
							}
							if (OutletDetail.getIsActive()) {
								outletStatus = "Active";
							} else {
								outletStatus = "Inactive";
							}
						}
					} else {
						outletName = "NA";
						outletCode = "NA";
						outletType ="NA";
						outletStatus = "NA";
					}

					cell10 = row.createCell(10);
					cell10.setCellValue(outletCode);

					cell11 = row.createCell(11);
					cell11.setCellValue(outletName);

					cell12 = row.createCell(12);
					cell12.setCellValue(outletType);

					cell13 = row.createCell(13);
					cell13.setCellValue(BACode);

					cell14 = row.createCell(14);
					cell14.setCellValue(userDetailsVo.getFullName());

					cell15 = row.createCell(15);
					cell15.setCellValue(userDetailsVo.getContactNumber());

					cell16 = row.createCell(16);
					cell16.setCellValue(userDetailsVo.getIsActive() ? "Active" : "Inactive");

					cell17 = row.createCell(17);
					doj = (userDetailsVo.getDateOfJoining() != null) ? userDetailsVo.getDateOfJoining().toString() : "";
					cell17.setCellValue(doj);

					cell18 = row.createCell(18);
					dol = (userDetailsVo.getReleaseDate() != null) ? userDetailsVo.getReleaseDate().toString() : "";
					if(dol != null && !dol.isEmpty()){
						cell18.setCellValue(dol);

					}else{
						cell18.setCellValue("");
					}


					cell19 = row.createCell(19);
					salary = userDetailsVo.getSalary();
					cell19.setCellValue(salary);

					cell20 = row.createCell(20);
					pan = userDetailsVo.getPan();
					cell20.setCellValue(pan);

					cell21 = row.createCell(21);
					aadhar = userDetailsVo.getAadhar();
					cell21.setCellValue(aadhar);

					cell22 = row.createCell(22);
					bankAc = userDetailsVo.getBankAccountNo();
					cell22.setCellValue(bankAc);
					String productDivisonName = "";
					Long productDivisonCode = null;
					if (null != userDetailsVo.getProductDivision()) {
						productDivisonName = dropdownMastereRepository
								.getName(userDetailsVo.getProductDivision().getId().toString());
						productDivisonCode = userDetailsVo.getProductDivision().getId();
					} else {
						productDivisonName = "NA";
						productDivisonCode = Long.valueOf("0");
					}


				} else {
					LOGGER.info("UploadService :: exportUser() :: Not a BA user :: " + userDetailsVo.getFullName());
				}
			}
			for (int i = 0; i < 21; i++)
				sheet.autoSizeColumn(i); // To expand the column


			msgResponse = "BA Details Exported Successfully.";
			// apiResponse = new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "BA
			// Details Exported
			// Successfully."),HttpStatus.OK);
		} else {
			msgResponse = "BA Details Not Found.";
			// apiResponse = new ResponseEntity<>(new ApiResponse(404, "NOT FOUND", "BA
			// Details Not
			// Found."),HttpStatus.NOT_FOUND);
		}

		try {

			out = os;
			workbook.write(out);
			workbook.close();

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
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

	// First have to create/ upload users hierarchy
	// Then need to check if BA already exist as active user
	// If new BA, then check if outlet exists
	// It outlet exists then, check if another BA is associated, then VALIDATION
	// data for user outlet mapping 
	// else create new outlet , then map in outlet mapping
	// Create user association mapping
	// create user credential data

	public OutputStream exportBARank(String yearMonth, Long zoneId , OutputStream os){
		LOGGER.info("UploadService :: Entering exportBaRank method");

		String msgResponse = null;

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("BARank Details");

		int rowNum = 0;
		byte[] bytes = null;

		CellStyle my_style = getHeaderCellStyle(workbook,sheet);

		OutputStream out = null;

		List<UserDetailsVo> listOfUsers = userDetailsRepository.findByIsActive(true);
		List<Leaderboard> leaderboard = leaderBoardRepository.findBaRank(yearMonth,zoneId);
		if(null != listOfUsers){
			Row row = sheet.createRow(rowNum++);

			Cell cell0 = row.createCell(0);
			cell0.setCellValue("Zone");
			cell0.setCellStyle(my_style);

			Cell cell1 = row.createCell(1);
			cell1.setCellValue("MD");
			cell1.setCellStyle(my_style);

			Cell cell2 = row.createCell(2);
			cell2.setCellValue("ZSM");
			cell2.setCellStyle(my_style);

			Cell cell3 = row.createCell(3);
			cell3.setCellValue("NSM");
			cell3.setCellStyle(my_style);

			Cell cell4 = row.createCell(4);
			cell4.setCellValue("ASM");
			cell4.setCellStyle(my_style);

			Cell cell5 = row.createCell(5);
			cell5.setCellValue("BDE");
			cell5.setCellStyle(my_style);

			Cell cell6 = row.createCell(6);
			cell6.setCellValue("SO");
			cell6.setCellStyle(my_style);

			Cell cell7 = row.createCell(7);
			cell7.setCellValue("REGION");
			cell7.setCellStyle(my_style);

			Cell cell8 = row.createCell(8);
			cell8.setCellValue("BA Code");
			cell8.setCellStyle(my_style);

			Cell cell9 = row.createCell(9);
			cell9.setCellValue("BA Name");
			cell9.setCellStyle(my_style);

			Cell cell10 = row.createCell(10);
			cell10.setCellValue("BA Rank");
			cell10.setCellStyle(my_style);

			Cell cell11 = row.createCell(11);
			cell11.setCellValue("MONTH");
			cell11.setCellStyle(my_style);

			for( int i = 0; i<12; i++){
				sheet.autoSizeColumn(i);
			}

			for(UserDetailsVo userDetailsVo : listOfUsers){
				if(userDetailsVo.getUserType().getId().equals(Long.valueOf("4"))){
					row = sheet.createRow((rowNum++));
					String zoneName = "";
					if (null != userDetailsVo.getCompanyZone()) {
						zoneName = dropdownMastereRepository.getName(userDetailsVo.getCompanyZone().getId().toString());
					} else {
						zoneName = "NA";
					}

					cell0 = row.createCell(0);
					cell0.setCellValue(zoneName);

					String BAUserName = userDetailsVo.getUserCred().getUsername();
					String BACode = "";
					if (BAUserName.contains("BA")) {
						int endIndex = BAUserName.indexOf('@'); // Find the index of the '@' character
						if (endIndex != -1) {
							BACode = BAUserName.substring(0, endIndex); // Extract the desired substring
						} else {
							// Handle the case where '@' is not found in the username
							LOGGER.info("UploadService :: exportUser() :: BAUserName :: Invalid username format :: "
									+ BAUserName);
							BACode = BAUserName;
						}
					} else if (BAUserName.contains("FLOATER")) {
						BACode = BAUserName;
					} else {
						LOGGER.info("UploadService :: exportUser() :: UserName :: Invalid username format :: "
								+ BAUserName);
						BACode = "NA";
					}



					Long userId = userDetailsVo.getId();


//					String state = "";
//					Long userId = userDetailsVo.getId();
//
//					em.clear();
//					UserChainDto userChainDto = userChainService.generateChainFlattened(userId);
//
//					String mdValue = userChainDto.getMd();
//					String zsmValue = userChainDto.getZsm();
//					String nsmValue = userChainDto.getNsm();
//					String asmValue = userChainDto.getAsm();
//					String bdeValue = userChainDto.getBde();
//					String soValue = userChainDto.getSo();
//
//
//					cell1 = row.createCell(1);
//					cell1.setCellValue(mdValue);
//
//					cell2 = row.createCell(2);
//					cell2.setCellValue(zsmValue);
//
//					cell3 = row.createCell(3);
//					cell3.setCellValue(nsmValue);
//
//					cell4 = row.createCell(4);
//					cell4.setCellValue(asmValue);
//
//					cell5 = row.createCell(5);
//					cell5.setCellValue(bdeValue);
//
//					cell6 = row.createCell(6);
//					cell6.setCellValue(soValue);
//
//					cell7 = row.createCell(7);
//					state = userDetailsVo.getState();
//					cell7.setCellValue(state);
//
//					cell8 = row.createCell(8);
//					cell8.setCellValue(BACode);
//
//					cell9 = row.createCell(9);
//					cell9.setCellValue(userDetailsVo.getFullName());

//
//					Leaderboard leaderboard = leaderBoardRepository.findBaRank(yearMonth,zoneId,userId);
//
//					String rank;
//					cell10 = row.createCell(10);
//					 rank = (leaderboard!= null && leaderboard.getRank() != null)? leaderboard.getRank().toString() : "N/A";
//					 cell10.setCellValue(rank);
//
//					 String month;
//					 cell11 = row.createCell(11);
//					 month = (leaderboard!= null &&leaderboard.getYearMonth()!= null) ? leaderboard.getYearMonth().toString() : "N/A";
//					 cell11.setCellValue(month);

				}else{
					LOGGER.info("UploadService :: exportBARank() :: Not a BA User :: " + userDetailsVo.getFullName());
				}
			}
			msgResponse = "BA Details Exported Successfully";

		}else{
			msgResponse = "BA Details Not Found";
		}

		if(null != leaderboard){
			for(Leaderboard leaderboard1 : leaderboard){

				Row row = sheet.createRow(rowNum++);
				LOGGER.info("Leaderboard User ===> lead" +leaderboard1.getUser().getId());
//							if(leaderboard1.getUser().getId().equals(userId)){

				em.clear();

				UserChainFlat userChainFlat = userChainService.generateChainFlattened(leaderboard1.getUser().getId());

				String mdValue = userChainFlat.getMd();
				String zsmValue = userChainFlat.getZsm();
				String nsmValue = userChainFlat.getNsm();
				String asmValue = userChainFlat.getAsm();
				String bdeValue = userChainFlat.getBde();
				String soValue = userChainFlat.getSo();


				UserView view = userViewRepository.findById(leaderboard1.getUser().getId()).orElse(null);

				Cell cell1 = row.createCell(1);
				cell1.setCellValue(mdValue);

				Cell cell2 = row.createCell(2);
				cell2.setCellValue(zsmValue);

				Cell cell3 = row.createCell(3);
				cell3.setCellValue(nsmValue);

				Cell cell4 = row.createCell(4);
				cell4.setCellValue(asmValue);

				Cell cell5 = row.createCell(5);
				cell5.setCellValue(bdeValue);

				Cell cell6 = row.createCell(6);
				cell6.setCellValue(soValue);

				String state = "";
				Cell cell7 = row.createCell(7);
				state = view.getUserState();
				cell7.setCellValue(state);

				UserCredVo cred = userCredRepository.findById(view.getCredId()).orElse(null);
				Cell cell8 = row.createCell(8);
				cell8.setCellValue(cred.getCode().split("@")[0]);

				Cell cell9 = row.createCell(9);
				cell9.setCellValue(view.getFullName());


				String rank;
				Cell cell10 = row.createCell(10);
				rank = (leaderboard1.getRank() != null)? leaderboard1.getRank().toString() : "N/A";
				cell10.setCellValue(rank);

				String month;
				Cell cell11 = row.createCell(11);
				month = (leaderboard1.getYearMonth()!= null) ? leaderboard1.getYearMonth().toString() : "N/A";
				cell11.setCellValue(month);


//							}
			}

		}


		LOGGER.info("UploadService :: exportUser() :: listOfEntries :: " + listOfUsers);



		try{
			out =os;
			workbook.write(out);
			workbook.close();

		}catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os;

	}
	
	public void upload(MultipartFile excelInput) {
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(excelInput.getInputStream());

			XSSFSheet worksheet = workbook.getSheetAt(0);

			DataFormatter format = new DataFormatter();
			StringBuilder msg = new StringBuilder();
			// msg.append("Line No: ");

			// to get the empty list
			List<String> outletNotFound = new ArrayList<>();
			List<String> blankListDoj = new ArrayList<>();
			List<String> blankListPD = new ArrayList<>();
			List<Integer> blankListOutlet = new ArrayList<>();
			List<String> blankListBACode = new ArrayList<>();
			List<String> blankListBAName = new ArrayList<>();
			List<String> blankListEmloyeeType = new ArrayList<>();
			List<String> blankListBAPhNo = new ArrayList<>();
			List<UserCredVo> blankListBACred = new ArrayList<>();
			List<String> blankListRegion = new ArrayList<>();
			List<String> blankListZone = new ArrayList<>();
			List<Integer> blankListContName = new ArrayList<>();
			List<Integer> blankListPhone = new ArrayList<>();
			List<Integer> blankListEmail = new ArrayList<>();
			List<Integer> uniqueListPhone = new ArrayList<>();
			List<Integer> uniqueListEmail = new ArrayList<>();

			/*
			 * int rowTotal = worksheet.getRow(0).getLastCellNum();
			 * //worksheet.getLastRowNum();
			 * LOGGER.info("rowTotal -------------- >"+rowTotal); boolean isEmpty = false;
			 */
			for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
				LOGGER.info("Index :::::::" + i);
				UserDetailsVo user = new UserDetailsVo();
				UserCredVo userCred = new UserCredVo();
				
				XSSFRow row = worksheet.getRow(i);

				try {
					String slNo = format.formatCellValue(row.getCell(0));
					if (slNo.length() == 0 && !slNo.equalsIgnoreCase("null")) {
						continue;
					}
					LOGGER.info("slNo ========> "+slNo);
					
					String baCode = format.formatCellValue(row.getCell(9));
					if (baCode.length() == 0 && !baCode.equalsIgnoreCase("null")) {
						blankListBACode.add(baCode);
						continue;
					} else {
						// Then need to check if BA already exist as active user
						Long existingUser = userDetailsRepository.ifUserExists(baCode, true);
						if(null != existingUser) {
							LOGGER.info("BA already exists block ========> "+baCode);
							continue;
						}
						String zoneName = format.formatCellValue(row.getCell(1));
						if (zoneName.length() == 0 && !zoneName.equalsIgnoreCase("null")) {
							blankListZone.add(zoneName);
							continue;
						}
						
						String regionName = format.formatCellValue(row.getCell(2));
						if (regionName.length() == 0 && !regionName.equalsIgnoreCase("null")) {
							blankListRegion.add(regionName);
							continue;
						}
						
						String baName = format.formatCellValue(row.getCell(10));
						if (baName.length() == 0 && !baName.equalsIgnoreCase("null")) {
							blankListBAName.add(baName);
							continue;
						}
						LOGGER.info("baName ========> "+baName);
						
						//here get other values for user details
						String doj = format.formatCellValue(row.getCell(11));
						if (doj.length() == 0 && !doj.equalsIgnoreCase("null")) {
							blankListDoj.add(doj);
							continue;
						}
						
						String productDivision = format.formatCellValue(row.getCell(12));
						if (productDivision.length() == 0 && !productDivision.equalsIgnoreCase("null")) {
							blankListPD.add(productDivision);
							continue;
						}
						
						String employeeTypeName = format.formatCellValue(row.getCell(13));
						if (employeeTypeName.length() == 0 && !employeeTypeName.equalsIgnoreCase("null")) {
							blankListEmloyeeType.add(employeeTypeName);
							continue;
						}
						
						String baPhNo = format.formatCellValue(row.getCell(14));
						// Contact number can't be duplicate
					      String findExixtingContactNoOfUser =
					          iUserDetailsRepository.findExistingContactNoOfUser(baPhNo).getContactNumber();
					      if (null != findExixtingContactNoOfUser) {
					    	  LOGGER.info("BA Contact Number already exists block ========> "+ baPhNo);
					    	  continue;
					      }else {
					    	  if (baPhNo.length() == 0 && !baPhNo.equalsIgnoreCase("null")) {
									blankListBAPhNo.add(baPhNo);
									continue;
					    	  }
					    	  
					    	  String username = baCode + "@" + baPhNo;
					    	  String base64passworddecrypt = baPhNo;
					          String encryptedPassword = Password.getSaltedHash(base64passworddecrypt);
					          userCred = new UserCredVo(encryptedPassword, username, true);
					          if (userCred != null) {
					        	  blankListBACred.add(userCred);
									continue;
					    	  }
					      }
					      Long userType = 4L; //BA userType > 4
//					      user = new UserDetailsVo(baName, baPhNo, userType, userCred, zoneName, doj, productDivision, true, new Date(), new Date());
					}
					LOGGER.info("baCode ========> "+baCode);
					
					//Now check if outlet is present
					String outlet = format.formatCellValue(row.getCell(15));
					if (outlet.length() == 0 && !outlet.equalsIgnoreCase("null")) {
						blankListOutlet.add(i);
						continue;
						
						//if necessary, here outlet entry code 
						/*String zone = format.formatCellValue(row.getCell(1));
						if (zone.length() == 0 && !zone.equalsIgnoreCase("null")) {
							blankListCustName.add(i);
							continue;
						}
						LOGGER.info("zone ========> "+zone);

						String region = row.getCell(2).toString();
						if (region.length() == 0 && !region.equalsIgnoreCase("null")) {
							blankListAdds1.add(i);
//							continue;
						}
						LOGGER.info("region ========> "+region);*/
					} else {
						//Now check if outlet is present
						Long existingOutlet = outletRepository.findIfExists(outlet);
						if(null == existingOutlet) {
							LOGGER.info("Outlet Not exists block ========> "+baCode);
							outletNotFound.add(outlet);
							continue;
						} else {
							//Outlet user mapping will be done after adding the user
						}
					}
					

					

					

					

					UserDetailsVo resultVo = null;

//					boolean dupCheck = false;
//					long tempCustId = 0;
//					List<CustomerMasterVo> entry = customerMasterService.getAllEntries(userVo.getUserId());
//					for (CustomerMasterVo res : entry) {
//						if (res.getCustomerId().equals(custId)) {
//							tempCustId = res.getId();
//							dupCheck = true;
//							dupCheckStatus = true;
//							break;
//						} else {
//							dupCheck = false;
//							dupCheckStatus = false;
//						}
//					}
//					LOGGER.info("cstGrpDesc : dupCheck ================= " + dupCheck);
//					if (dupCheck == false) {
//						vo.setCreatedBy(userVo.getUserId());
//						vo.setCustomerId(custId);
//						vo.setName(custName);
//						vo.setStreetAddress(adds1);
//						vo.setState(region);
//						vo.setPostalCode(Long.parseLong(postal));
//						vo.setContactName(contName);
//						vo.setPhone(Long.parseLong(phone));
//						vo.setFax(!fax.isEmpty() ? Long.parseLong(fax) : 0);
//						vo.setEmail(email);
//
//						LOGGER.info("============================= dupCheck : index : " + i + " : vo : " + vo);
//
//						resultVo = customerMasterService.save(vo, userVo);
//
//						if (resultVo != null) {
//							if (null != resultVo.getName() && resultVo.getName().equals("DUPLICATE_ENTRY")) {
//								if (resultVo.getErrMsg().contains("phone")) {
//									uniqueListPhone.add(i);
//								} else {
//									uniqueListEmail.add(i);
//								}
//							}
//							apiResponse = new ApiResponse(200, "Success", "Saved successfull", "");
//							responseEntity = new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
//
//						} else {
//							apiResponse = new ApiResponse(401, "ERROR", "");
//							responseEntity = new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
//						}
//					} else {
//						vo.setId(tempCustId);
//						vo.setCreatedBy(userVo.getUserId());
//						vo.setCustomerId(custId);
//						vo.setName(custName);
//						vo.setStreetAddress(adds1);
//						vo.setState(region);
//						vo.setPostalCode(Long.parseLong(postal));
//						vo.setContactName(contName);
//						vo.setPhone(Long.parseLong(phone));
//						vo.setFax(!fax.isEmpty() ? Long.parseLong(fax) : 0);
//						vo.setEmail(email);
//
//						LOGGER.info("vo========================index : " + i + ", " + vo);
//
//						resultVo = customerMasterService.editEntryById(tempCustId, vo, userVo);
//
//						if (resultVo != null) {
//							if (null != resultVo.getName() && resultVo.getName().equals("DUPLICATE_ENTRY")) {
//								if (resultVo.getErrMsg().contains("phone")) {
//									uniqueListPhone.add(i);
//								} else {
//									uniqueListEmail.add(i);
//								}
//							}
//							apiResponse = new ApiResponse(200, "Success",
//									"Saved successfull & Duplicate account numbers are updated.", "");
//							responseEntity = new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
//
//						} else {
//							apiResponse = new ApiResponse(401, "ERROR", "");
//							responseEntity = new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
//						}
//
//					}
				} catch (Exception e) {
					e.printStackTrace();
//					apiResponse = new ApiResponse(401, "ERROR", "Please Correct Your Exel File Column Type.", "");
//					responseEntity = new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
//					return responseEntity;
				}
			}
			/*LOGGER.info("Empty row nos are :::::::::: " + blankListCustId);
			LOGGER.info("Duplicate Phone number :::::::::: " + uniqueListPhone);
			msg.append((blankListCustId.size() > 0) ? "<br>Cust Id missing in rows :" + blankListCustId : " ");
			msg.append(
					(blankListCustName.size() > 0) ? "<br>Customer name missing in rows : " + blankListCustName : " ");

			msg.append((blankListAdds1.size() > 0) ? "<br>Address missing in rows : " + blankListAdds1 : " ");
			msg.append((blankListPostal.size() > 0) ? "<br>Postal code missing in rows : " + blankListPostal : " ");
			msg.append((blankListRegion.size() > 0) ? "<br>Region missing in rows : " + blankListRegion : " ");
			msg.append(
					(blankListContName.size() > 0) ? "<br>Contact name missing in rows : " + blankListContName : " ");
			msg.append((blankListPhone.size() > 0) ? "<br>Phone number missing in rows : " + blankListPhone : " ");
			msg.append((blankListEmail.size() > 0) ? "<br>Email missing in rows : " + blankListEmail : " ");
			msg.append((uniqueListPhone.size() > 0) ? "<br>Duplicate Phone number found in rows : " + uniqueListPhone
					: " ");
			msg.append((uniqueListEmail.size() > 0) ? "<br>Duplicate Email found in rows : " + uniqueListEmail : " ");
			if (dupCheckStatus) {
				apiResponse = new ApiResponse(200, "SUCCESS",
						msg.toString().trim() + " Saved successfully & Duplicate customers are updated", "");
				responseEntity = new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
				return responseEntity;
			} else {
				apiResponse = new ApiResponse(200, "SUCCESS", msg.toString().trim(), "");
				responseEntity = new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
				return responseEntity;
			}*/

		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
