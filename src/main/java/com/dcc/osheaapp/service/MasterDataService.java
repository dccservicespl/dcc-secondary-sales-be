package com.dcc.osheaapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.dcc.osheaapp.ojbso.repo.IBeatTypeMstRepo;
import com.dcc.osheaapp.ojbso.repo.ISoBeatNTypeMappingRepo;
import com.dcc.osheaapp.ojbso.vo.SoBeatNTypeMappingVo;
import com.dcc.osheaapp.vo.dto.CreateBeatInputDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.common.model.Constants;
import com.dcc.osheaapp.ojbso.repo.IUserDistributorMappingRepository;
import com.dcc.osheaapp.ojbso.vo.UserDistributorMappingVo;
import com.dcc.osheaapp.repository.IBeatNameRepository;
import com.dcc.osheaapp.repository.ICompanyZoneRepository;
import com.dcc.osheaapp.repository.IDistributorRepository;
import com.dcc.osheaapp.repository.IDistributorSerchOutputRepo;
import com.dcc.osheaapp.repository.IDropdownMastereRepository;
import com.dcc.osheaapp.repository.IOutletChannelRepository;
import com.dcc.osheaapp.repository.IProductCategoryRepository;
import com.dcc.osheaapp.repository.IUserCredRepository;
import com.dcc.osheaapp.repository.IUserTypeRepository;
import com.dcc.osheaapp.repository.IVersionRepository;
import com.dcc.osheaapp.vo.BeatName;
import com.dcc.osheaapp.vo.CompanyZoneVo;
import com.dcc.osheaapp.vo.DistributorOutputVo;
import com.dcc.osheaapp.vo.DistributorVo;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import com.dcc.osheaapp.vo.FormType;
import com.dcc.osheaapp.vo.OutletChannelVo;
import com.dcc.osheaapp.vo.ProductCategoryVo;
import com.dcc.osheaapp.vo.UserCredVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import com.dcc.osheaapp.vo.UserTypeVo;
import com.dcc.osheaapp.vo.Version;
import com.dcc.osheaapp.vo.dto.DistributorInputVo;
import com.dcc.osheaapp.vo.views.ProductCategoryView;

import io.jsonwebtoken.ExpiredJwtException;

@Service
public class MasterDataService {

	private static final Logger LOGGER = LogManager.getLogger(MasterDataService.class);

	private IProductCategoryRepository iProductCategoryRepository;
	private IUserTypeRepository iUserTypeRepository;
	private ICompanyZoneRepository iCompanyZoneRepository;
	private IBeatNameRepository iBeatNameRepository;
	private IOutletChannelRepository iOutletChannelRepository;
	private IDistributorRepository iDistributorRepository;
	private IDistributorSerchOutputRepo iDistributorSerchOutputRepo;
	private IDropdownMastereRepository iDropdownMastereRepository;
	private final IVersionRepository versionRepository;
	private UserService userService;
	private final IUserCredRepository userCredRepository;
	private final IUserDistributorMappingRepository iUserDistributorMappingRepository;
	private final ISoBeatNTypeMappingRepo beatNTypeMappingRepo;
	private final IBeatTypeMstRepo iBeatTypeMstRepo;

	@Autowired
	public MasterDataService(IProductCategoryRepository iProductCategoryRepository,
			IUserTypeRepository iUserTypeRepository, ICompanyZoneRepository iCompanyZoneRepository,
			IBeatNameRepository iBeatNameRepository, IOutletChannelRepository iOutletChannelRepository,
			IDropdownMastereRepository iDropdownMastereRepository, IDistributorRepository iDistributorRepository,
			IDistributorSerchOutputRepo iDistributorSerchOutputRepo, IVersionRepository versionRepository,
			UserService userService, IUserDistributorMappingRepository iUserDistributorMappingRepository,
			IUserCredRepository userCredRepository,ISoBeatNTypeMappingRepo beatNTypeMappingRepo,IBeatTypeMstRepo iBeatTypeMstRepo) {
		this.iProductCategoryRepository = iProductCategoryRepository;
		this.iUserTypeRepository = iUserTypeRepository;
		this.iCompanyZoneRepository = iCompanyZoneRepository;
		this.iBeatNameRepository = iBeatNameRepository;
		this.iOutletChannelRepository = iOutletChannelRepository;
		this.iDistributorRepository = iDistributorRepository;
		this.iDropdownMastereRepository = iDropdownMastereRepository;
		this.versionRepository = versionRepository;
		this.userService = userService;
		this.iDistributorSerchOutputRepo = iDistributorSerchOutputRepo;
		this.userCredRepository = userCredRepository;
		this.iUserDistributorMappingRepository = iUserDistributorMappingRepository;
		this.beatNTypeMappingRepo = beatNTypeMappingRepo;
		this.iBeatTypeMstRepo = iBeatTypeMstRepo;
    }

	public ResponseEntity<ApiResponse> fetchAllProductCategory(HttpServletRequest request) {
		LOGGER.info("MasterDataService :: fetchAllProductCategory() called...");

		final String requestTokenHeader = request.getHeader("Authorization");

		String jwtToken = null;

		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
		}

		try {
			UserCredVo userVo = userService.getUserDetails(request, jwtToken);
			if (null == userVo) {
				LOGGER.info("MasterDataService :: fetchLocations :: Invalid User...");
				return new ResponseEntity<>(new ApiResponse(401, "ERROR", "Invalid User", null),
						HttpStatus.UNAUTHORIZED);
			}

			List<ProductCategoryVo> locations = (List<ProductCategoryVo>) iProductCategoryRepository.findAll();
			if (locations != null) {
				return new ResponseEntity<>(
						new ApiResponse(200, "SUCCESS", "ProductCategory fetched successfully.", locations),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ApiResponse(400, "NOT_FOUND", "ProductCategory fetched Failed.", null),
						HttpStatus.NOT_FOUND);
			}
		} catch (ExpiredJwtException exp) {
			LOGGER.info("MasterDataService :: ExpiredJwtException :: Token expired..." + exp.getMessage());
			exp.printStackTrace();
			return new ResponseEntity<>(new ApiResponse(401, "ERROR", "Token expired", null), HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ApiResponse(500, "Internal Server Err", "ProductCategory fetched Failed.", null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public List<UserTypeVo> fetchHigherPriorityUserType(Long userPriority) {
		LOGGER.info("MasterDataService :: fetchHigherPriorityUserType() called...");
		List<UserTypeVo> userTypes = (List<UserTypeVo>) iUserTypeRepository.findUserTypeByPriority(true, userPriority);
		if (userTypes != null) {
			return userTypes;
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "UserTypes" });
		}
	}

	public List<ProductCategoryVo> fetchAllProductCategoryAlt() {
		LOGGER.info("MasterDataService :: fetchAllProductCategory() called...");
		List<ProductCategoryVo> locations = (List<ProductCategoryVo>) iProductCategoryRepository.findAll();
		if (locations != null) {
			return locations;
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Categories" });
		}
	}

	public List<UserTypeVo> fetchAllUserType(FormType formType) {
		LOGGER.info("MasterDataService :: fetchAllUserType() called...");
		List<UserTypeVo> userTypes = (List<UserTypeVo>) iUserTypeRepository.findUserByType(formType.getFormTypeName(),
				true);
		if (userTypes != null) {
			return userTypes;
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "UserTypes" });
		}
	}

	public List<CompanyZoneVo> fetchAllCompanyZone() {
		LOGGER.info("MasterDataService :: fetchAllCompanyZone() called...");
		List<CompanyZoneVo> companyZones = (List<CompanyZoneVo>) iCompanyZoneRepository.findAll();
		if (companyZones != null) {
			return companyZones;
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "CompanyZones" });
		}
	}

	public List<BeatName> fetchAllBeatName() {
		LOGGER.info("MasterDataService :: fetchAllBeatName() called...");
		List<BeatName> companyZones = (List<BeatName>) iBeatNameRepository.findAll();
		if (companyZones != null) {
			return companyZones;
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "BeatNames" });
		}
	}

	public ResponseEntity<ApiResponse> updateStatus(BeatName input) {
		LOGGER.info("MasterDataService :: updateStatus() called...");
		int saveData = iBeatNameRepository.updateStatus(input.getId(), input.getActive());
		if (saveData != 0) {
			return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Data Found", null), HttpStatus.OK);
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "BeataView" });
		}
	}

	public List<OutletChannelVo> fetchAllOutletChannel() {
		LOGGER.info("MasterDataService :: fetchAllOutletChannel() called...");
		List<OutletChannelVo> outletChannels = (List<OutletChannelVo>) iOutletChannelRepository.findAll();
		if (outletChannels != null) {
			return outletChannels;
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "OutletChannels" });
		}
	}

	public List<DistributorVo> fetchAllDistributors() {
		LOGGER.info("MasterDataService :: fetchAllDistributors() called...");
		// List<DistributorVo> distributors = (List<DistributorVo>)
		// iDistributorRepository.findAll();
		List<DistributorVo> distributors = iDistributorRepository.findByIsActive(true);

		if (distributors != null) {
			return distributors;
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Distributors" });
		}
	}

	public List<DropdownMasterVo> fetchAllActiveDropdownValueByFieldType(String fieldType) {
		LOGGER.info("MasterDataService :: fetchAllActiveDropdownValueByFieldType() called...");
		List<DropdownMasterVo> dropdownValues = iDropdownMastereRepository
				.fetchAllActiveDropdownValueByFieldType(fieldType);
		if (dropdownValues != null) {
			return dropdownValues;
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "DropdownValues" });
		}
	}

	public Optional<CompanyZoneVo> findZoneById(Long id) {
		return iCompanyZoneRepository.findById(id);
	}

	public List<ProductCategoryView> fetchProductCategoryList(Long parentId) {
		LOGGER.info("MasterDataService :: fetchProductCategoryList() called...");
		List<ProductCategoryView> locations = iProductCategoryRepository.findByParentIdAndIsActive(parentId, true);
		if (locations != null) {
			return locations;
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Categories" });
		}
	}

	public Version getActiveCategory(String appType) {
		appType = (null != appType && !appType.trim().isEmpty()) ? appType : "OJB_BA";
		return versionRepository.findByStatusAndAppType(true, appType)
				.orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Active Version" }));
	}

	public ResponseEntity<ApiResponse> createBeatName(BeatName input) {
		LOGGER.info("MasterDataService :: createBeatName() called...");
		ResponseEntity<ApiResponse> apiResponse = null;

		try {
			input.setBeatName(input.getBeatName().toUpperCase());
			input.setActive(true);
			if (input.getId() == null) {
				BeatName checkBeatName = iBeatNameRepository.findByBeatName(input.getBeatName());

				if (checkBeatName == null) {
					BeatName saved = iBeatNameRepository.save(input);

					apiResponse = new ResponseEntity<>(
							new ApiResponse(200, "SUCCESS", "Beat Name saved successfully.", saved), HttpStatus.OK);
				} else {
					apiResponse = new ResponseEntity<>(
							new ApiResponse(409, "DUPLICATE", "Beat Name already exists.", checkBeatName),
							HttpStatus.OK);
				}
			} else {
				BeatName saved = iBeatNameRepository.save(input);

				apiResponse = new ResponseEntity<>(
						new ApiResponse(200, "SUCCESS", "Beat Name edited successfully.", saved), HttpStatus.OK);
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("MasterDataService :: createBeatName() :: ERROR :: " + e.getMessage());
			apiResponse = new ResponseEntity<>(new ApiResponse(400, "ERROR", "Error while processing request.", null),
					HttpStatus.BAD_REQUEST);
		}

		LOGGER.info("MasterDataService :: createBeatName() exit...");
		return apiResponse;
	}

	public ResponseEntity<ApiResponse> searchBeatNameByInput(BeatName inputVo) {
		LOGGER.info("MasterDataService::searchBeatNameByInput::Entering...");
		ApiResponse apiResponse = null;
		List<BeatName> getDataList = null;
		String whereClause = "  ";
		String limitStr = "";
		String orderByStr = " order by beat_name ASC";
		if (null == inputVo.getPage() && null == inputVo.getSize()) {
			// do nothing
		} else {
			Integer size = (null != inputVo.getSize()) ? inputVo.getSize() : Constants.DEFAULT_DATA_SIZE_PAGINATION;
			Integer page = (null != inputVo.getPage() && inputVo.getPage() > 1) ? (inputVo.getPage() - 1) * size : 0;
			limitStr = " limit " + size + " offset " + page;
			LOGGER.info("limitStr..." + limitStr);
		}

		if (null != inputVo.getBeatName() && !inputVo.getBeatName().trim().isEmpty()) {
			whereClause += " and beat_name like '%" + inputVo.getBeatName().trim() + "%' ";
		}

		LOGGER.info("Whereclause..." + whereClause);
		getDataList = iBeatNameRepository.searchBeatNameByInput(whereClause, limitStr);

		int totalNo = iBeatNameRepository.countBeatNameByInput(whereClause).intValue();
		LOGGER.info("MasterDataService::searchBeatNameByInput::Exiting...");

		if (getDataList != null) {
			apiResponse = new ApiResponse(200, "success", "success", getDataList, totalNo);
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "outlet" });
		}
	}

	public BeatName fetchById(Long id) {
		BeatName data = iBeatNameRepository.findById(id).orElse(null);
		if (null != data) {
			return data;
		} else {
			new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Outlet" });
		}
		return data;
	}

	public ResponseEntity<ApiResponse> searchDistributorByTnput(DistributorInputVo inputVo) {
		LOGGER.info("MasterService :: searchDistributorByTnput :: Entering....");
		ApiResponse apiResponse = null;
		List<DistributorOutputVo> getDetails = null;
		String whereClause = "  ";
		String limitStr = "";
		String orderByStr = " order by distributorName ASC ";

		if (null == inputVo.getPage() && null == inputVo.getSize()) {

		} else {
			Integer size = (null != inputVo.getSize()) ? inputVo.getSize() : Constants.DEFAULT_DATA_SIZE_PAGINATION;
			Integer page = (null != inputVo.getPage() && inputVo.getPage() > 1) ? (inputVo.getPage() - 1) * size : 0;
			limitStr = " limit " + size + " offset " + page;
			LOGGER.info("limitStr..." + limitStr);
		}

		if (null != inputVo.getActive()) {
			if (inputVo.getActive().isActive())
				whereClause += " and isActive = true";
			if (inputVo.getActive().isInactive())
				whereClause += " and isActive = false";
		}

		if (null != inputVo.getUserType()) {
			whereClause += " and userType = " + inputVo.getUserType();
		}

		if (null != inputVo.getDistributorName()) {
			String distributorName = "'" + inputVo.getDistributorName() + "'";
			whereClause += " and distributorName = " + distributorName;
		}

		if (null != inputVo.getRegion()) {
			whereClause += " and region = " + inputVo.getRegion();
		}
		if (null != inputVo.getStockist()) {
			whereClause += " and stockistType = " + inputVo.getStockist();
		}
		if (null != inputVo.getAddress()) {
			whereClause += " and address = " + inputVo.getAddress();
		}
		if (null != inputVo.getContactNumber()) {
			whereClause += " and contactNumber = " + inputVo.getContactNumber();
		}
		if (null != inputVo.getCreditDu()) {
			whereClause += " and creditDuration = " + inputVo.getCreditDu();
		}
		if (null != inputVo.getGstin()) {
			whereClause += " and gstin = " + inputVo.getGstin();
		}
		if (null != inputVo.getPan()) {
			whereClause += " and panNo = " + inputVo.getPan();
		}
		if (null != inputVo.getCompanyZone()) {
			whereClause += " and companyZone = " + inputVo.getCompanyZone();
		}
		if (null != inputVo.getUserId()) {
			whereClause += " and userId  = " + inputVo.getUserId();
		}

		LOGGER.info("Whereclause..." + whereClause);

		LOGGER.info(whereClause + orderByStr);
		getDetails = iDistributorSerchOutputRepo.search(whereClause + orderByStr, limitStr);

		int totalNo = iDistributorSerchOutputRepo.countDistributorList(whereClause).intValue();
		apiResponse = new ApiResponse(200, "success", "success", getDetails, totalNo);
		LOGGER.info("UserService::searchByInput::Exiting...");
		// return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
		if (getDetails != null) {
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
		} else {
			throw new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Users" });
		}
	}

	public ResponseEntity<ApiResponse> addDistributor(DistributorVo input) {
		LOGGER.info("MasterDataService:: addDistributor() Called....");
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails) principal).getUsername();
		Long loginUserId = userCredRepository.loginUserId(username);
		
		input.setIsActive(true);
		if(null == input.getId()) {
			input.setCreatedBy(loginUserId);
			input.setCreatedOn(new Date());
		}
		input.setUpdatedBy(loginUserId);
		input.setUpdatedOn(new Date());
		DistributorVo saveData = iDistributorRepository.save(input);
		if (saveData != null) {
			//save data in so_distributor_mapping table
			//inActivate existing mapping
			int updateStat = iUserDistributorMappingRepository.updateStatus(false, new Date(), loginUserId, saveData.getId());
			LOGGER.info("Old so_distributor_mapping data updated : "+updateStat);
			List<UserDistributorMappingVo> mappingData = new ArrayList<UserDistributorMappingVo>();
			for (Long userId : input.getUserIds()) {
				UserDistributorMappingVo each = new UserDistributorMappingVo(
						new UserDetailsVo(userId), saveData, true, new Date(), loginUserId);
				mappingData.add(each);
			}
			iUserDistributorMappingRepository.saveAll(mappingData);
			return new ResponseEntity<>(new ApiResponse(200, "SUCCESS", "Save Successfully", saveData), HttpStatus.OK);
		} else {
			throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { "DistributorView" });
		}
	}

	@Transactional
	public ResponseEntity<ApiResponse> createNewBeat(CreateBeatInputDto input){
		LOGGER.info("MasterDataService() :: createNewBeat() call ....");
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails) principal).getUsername();
		Long loginUserId = userCredRepository.loginUserId(username);
		LOGGER.info(" loginUserId from TOKEN =========== > " + loginUserId);

		try{
			BeatName beatName = new BeatName();
			beatName.setCreatedBy(loginUserId);
			beatName.setCreatedOn(new Date());
			beatName.setBeatName(input.getBeatName());
			beatName.setSoId(input.getReportingTo());
			beatName.setBeatType(input.getBeatType());
			beatName.setCompanyZone(input.getCompanyZone());
			beatName.setActive(true);
			beatName.setProductDivision(input.getProductDivision());
			beatName.setAssociateDesignation(input.getAssociateDesignation());

			iBeatNameRepository.save(beatName);
			createBeatAndBeatTypeMap(beatName,loginUserId);
			return ResponseEntity.ok(new ApiResponse(200, "SUCCESS", "New Beat Created successfully", beatName));
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new ApiResponse(400, "ERROR", "Error while Creating new Beat.", null));
		}
	}

	private void createBeatAndBeatTypeMap(BeatName beat, Long loginUserId) {
		LOGGER.info("MasterDataService() :: saveToSOBeat() call ....");
		SoBeatNTypeMappingVo soBeatNTypeMappingVo = new SoBeatNTypeMappingVo();
		Long beatTypeId = beat.getBeatType().getId();
		String beatTypeName = iBeatTypeMstRepo.findBeatNameById(beatTypeId);
		soBeatNTypeMappingVo.setBeatAndTypeName(beat.getBeatName()+ " - "+beatTypeName);
		soBeatNTypeMappingVo.setCreatedBy(loginUserId);
		soBeatNTypeMappingVo.setCreatedOn(new Date());
		soBeatNTypeMappingVo.setUpdatedBy(loginUserId);
		soBeatNTypeMappingVo.setUpdatedOn(new Date());
		soBeatNTypeMappingVo.setBeatId(beat);
		soBeatNTypeMappingVo.setBeatTypeId(beat.getBeatType());
		soBeatNTypeMappingVo.setSoId(beat.getSoId());
		soBeatNTypeMappingVo.setActiveFlag(true);

		beatNTypeMappingRepo.save(soBeatNTypeMappingVo);
		LOGGER.info("MasterDataService() :: save details to so_Beat_type_mapping table ....");
	}

	@Transactional
	public ResponseEntity<ApiResponse> updateBeat(CreateBeatInputDto input){
		LOGGER.info("MasterDataService() :: updateBeat() call ....");
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails) principal).getUsername();
		Long loginUserId = userCredRepository.loginUserId(username);

		try{
			BeatName beatName = iBeatNameRepository.findById(input.getId()).orElse(null);
			BeatName updatedBeat = null;

			if (beatName != null) {
				if(input.getBeatName() != null){
					beatName.setBeatName(input.getBeatName());
				}
				if(input.getBeatType() != null){
					beatName.setBeatType(input.getBeatType());
				}
				if(input.getBeatType() != null){
					beatName.setCompanyZone(input.getCompanyZone());
				}
				if(input.getProductDivision() != null){
					beatName.setProductDivision(input.getProductDivision());
				}
				if(input.getAssociateDesignation() != null){
					beatName.setAssociateDesignation(input.getAssociateDesignation());
				}
				if(input.getReportingTo() != null){
					beatName.setSoId(input.getReportingTo());
				}

				updatedBeat = iBeatNameRepository.save(beatName);
				updateBeatAndBeatTypeMap(updatedBeat,loginUserId);
			}
			return ResponseEntity.ok(new ApiResponse(200, "SUCCESS", "Beat Updated successfully", updatedBeat));
		}catch (Exception e){
			e.printStackTrace();
			return ResponseEntity.ok(new ApiResponse(400, "ERROR", "Error while updating Beat.", null));
		}
	}

	private void updateBeatAndBeatTypeMap(BeatName beat, Long loginUserId) {
		LOGGER.info("MasterDataService() :: updateBeatAndBeatTypeMap() call ....");

		if(beat!= null){
			Long id = beatNTypeMappingRepo.findIdByBeatIdAndActiveFlag(beat.getId());

			if(id == null){ //if the beat is not exist in so_Beat_type_mapping table
				//a new row will be created in so_Beat_type_mapping
				createBeatAndBeatTypeMap(beat,loginUserId);
			}else{
				// it will update exited row, mapping to the beat id
				SoBeatNTypeMappingVo soBeatNTypeMappingVo = beatNTypeMappingRepo.findById(id).orElse(null);
				Long beatTypeId = beat.getBeatType().getId();
				String beatTypeName = iBeatTypeMstRepo.findBeatNameById(beatTypeId);
				soBeatNTypeMappingVo.setBeatAndTypeName(beat.getBeatName()+ " - "+beatTypeName);
				soBeatNTypeMappingVo.setUpdatedBy(loginUserId);
				soBeatNTypeMappingVo.setUpdatedOn(new Date());
				soBeatNTypeMappingVo.setBeatId(beat);
				soBeatNTypeMappingVo.setBeatTypeId(beat.getBeatType());
				soBeatNTypeMappingVo.setSoId(beat.getSoId());

				beatNTypeMappingRepo.save(soBeatNTypeMappingVo);
			}

		}
		LOGGER.info("MasterDataService() :: update details to so_Beat_type_mapping table ....");
	}


	}
