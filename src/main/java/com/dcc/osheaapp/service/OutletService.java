package com.dcc.osheaapp.service;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.common.model.Constants;
import com.dcc.osheaapp.repository.*;
import com.dcc.osheaapp.vo.OutletInputVo;
import com.dcc.osheaapp.vo.OutletUserMappingVo;
import com.dcc.osheaapp.vo.OutletVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import com.dcc.osheaapp.vo.dto.UpdateOutletDto;
import com.dcc.osheaapp.vo.views.Outlet;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.text.RandomStringGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OutletService {

  private static final Logger LOGGER = LogManager.getLogger(OutletService.class);
  private final IOutletRepository iOutletRepository;
  private final IDropdownMastereRepository iDropdownMastereRepository;
  private final IOutletChannelRepository iOutletChannelRepository;
  private final IDistributorRepository iDistributorRepository;
  private final IBeatNameRepository iBeatNameRepository;
  private final IProductDivisionRepository iProductDivisionRepository;
  private final ICompanyZoneRepository iCompanyZoneRepository;
  private final IOutletUserMappingRepository outletUserMappingRepository;
  private final IUserDetailsRepository userDetailsRepository;
  private final IOutletViewRepository outletViewRepository;

  @Autowired
  public OutletService(
      IOutletRepository iOutletRepository,
      IDropdownMastereRepository iDropdownMastereRepository,
      IOutletChannelRepository iOutletChannelRepository,
      IDistributorRepository iDistributorRepository,
      IBeatNameRepository iBeatNameRepository,
      ICompanyZoneRepository iCompanyZoneRepository,
      IProductDivisionRepository iProductDivisionRepository,
      IOutletUserMappingRepository outletUserMappingRepository,
      IUserDetailsRepository userDetailsRepository,
      IOutletViewRepository outletViewRepository) {
    this.iOutletRepository = iOutletRepository;
    this.iDropdownMastereRepository = iDropdownMastereRepository;
    this.iOutletChannelRepository = iOutletChannelRepository;
    this.iDistributorRepository = iDistributorRepository;
    this.iBeatNameRepository = iBeatNameRepository;
    this.iProductDivisionRepository = iProductDivisionRepository;
    this.iCompanyZoneRepository = iCompanyZoneRepository;
    this.outletUserMappingRepository = outletUserMappingRepository;
    this.userDetailsRepository = userDetailsRepository;
    this.outletViewRepository = outletViewRepository;
  }

  public Optional<OutletVo> findOutlet(Long id) {
    return iOutletRepository.findById(id);
  }

  public ResponseEntity<ApiResponse> createOutlet(OutletVo input) {
    LOGGER.info("OutletService :: createOutlet() called...");
    ResponseEntity<ApiResponse> apiResponse = null;

    try {
      input.setCreatedOn(new Date());
      input.setUpdatedOn(new Date());

      if(input.getOutletCode() == null || input.getOutletCode().isEmpty()) {
        Random random = new Random();
        int randomNumber = random.nextInt(10000);
        input.setOutletCode("D" + randomNumber);
      }
      OutletVo saved = iOutletRepository.save(input);

      apiResponse =
          new ResponseEntity<>(
              new ApiResponse(200, "SUCCESS", "Outlet created successfully.", saved),
              HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("OutletService :: createOutlet() :: ERROR :: " + e.getMessage());
      apiResponse =
          new ResponseEntity<>(
              new ApiResponse(400, "ERROR", "Error while processing request.", null),
              HttpStatus.BAD_REQUEST);
    }

    LOGGER.info("OutletService :: createOutlet() exit...");
    return apiResponse;
  }

  public ResponseEntity<ApiResponse> searchByInput(OutletInputVo inputVo) {
    LOGGER.info("OutletService::searchByInput::Entering...");
    ApiResponse apiResponse = null;
    List<OutletVo> getDataList = null;
    String whereClause = " ";
    String limitStr = "";
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

    if (null != inputVo.getOutletCode() && !inputVo.getOutletCode().trim().isEmpty()) {
      whereClause += " and outlet_code like '%" + inputVo.getOutletCode().trim() + "%' ";
    }
    if (null != inputVo.getOutletName() && !inputVo.getOutletName().trim().isEmpty()) {
      LOGGER.info("vo.getOutletName() ------------ " + inputVo.getOutletName());
      whereClause += " and outlet_name like '%" + inputVo.getOutletName().trim() + "%' ";
    }
    if (null != inputVo.getOutletCode() && !inputVo.getOutletCode().trim().isEmpty()) {
      whereClause += " and outlet_code like '%" + inputVo.getOutletCode().trim() + "%' ";
    }
    if (null != inputVo.isActive()) {
      whereClause += " and is_active = " + inputVo.isActive();
    } else {
      whereClause += " and is_active = true "; // by default only active outlets
    }
    if (null != inputVo.getOutletName() && !inputVo.getOutletName().trim().isEmpty()) {
      LOGGER.info("vo.getOutletName() ------------ " + inputVo.getOutletName());
      whereClause += " and outlet_name like '%" + inputVo.getOutletName().trim() + "%' ";
    }

    if (null != inputVo.getSearchInput() && !inputVo.getSearchInput().trim().isEmpty()) {
      whereClause +=
          " and (outlet_name like '%"
              + inputVo.getSearchInput()
              + "%' "
              + " or outlet_code like '%"
              + inputVo.getSearchInput()
              + "%' )";
    }
    LOGGER.info("Whereclause..." + whereClause);
    getDataList = iOutletRepository.searchByInput(whereClause, "outlet", limitStr);

    // To fetch total no of data which satisfy this where clause
    int totalNo = iOutletRepository.getTotalCountByInput(whereClause, "outlet").intValue();
    LOGGER.info("UserService::searchByInput::Exiting...");
    if (getDataList != null) {
      apiResponse = new ApiResponse(200, "success", "success", getDataList, totalNo);
      return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    } else {
      throw new OjbException(ErrorCode.NOT_FOUND, new Object[] {"outlet"});
    }
  }

  public ResponseEntity<ApiResponse> searchViewByInput(OutletInputVo inputVo) {
    LOGGER.info("OutletService::searchByInput::Entering...");
    ApiResponse apiResponse = null;
    List<Outlet> getDataList = null;
    String whereClause = "  ";
    String limitStr = "";
    String orderByStr = " order by outletName ASC";
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

    if (null != inputVo.getOutletCode() && !inputVo.getOutletCode().trim().isEmpty()) {
      whereClause += " and outletCode like '%" + inputVo.getOutletCode().trim() + "%' ";
    }
    if (null != inputVo.isActive()) {
      whereClause += " and isActive = " + inputVo.isActive();
    }

    if (null != inputVo.getBa()) {
      whereClause += " and userId = " + inputVo.getBa();
    }
    if (null != inputVo.getSo()) {
      whereClause += " and userId = " + inputVo.getSo();
    }
    if (null != inputVo.getOutletChannel()) {
      whereClause += " and outletChannel = " + inputVo.getOutletChannel();
    }

    if (null != inputVo.getOutletName() && !inputVo.getOutletName().trim().isEmpty()) {
      LOGGER.info("vo.getOutletName() ------------ " + inputVo.getOutletName());
      whereClause += " and outletName like '%" + inputVo.getOutletName().trim() + "%' ";
    }

    if (null != inputVo.getSearchInput() && !inputVo.getSearchInput().trim().isEmpty()) {
      whereClause +=
          " and outletName like '%"
              + inputVo.getSearchInput()
              + "%' or outletCode like '%"
              + inputVo.getSearchInput()
              + "%'";
    }
    LOGGER.info("Whereclause..." + whereClause);
    getDataList = outletViewRepository.search(whereClause + orderByStr, limitStr);

    // To fetch total no of data which satisfy this where clause
    int totalNo = outletViewRepository.countOutletBySearchInput(whereClause).intValue();
    LOGGER.info("UserService::searchByInput::Exiting...");
    if (getDataList != null) {
      apiResponse = new ApiResponse(200, "success", "success", getDataList, totalNo);
      return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    } else {
      throw new OjbException(ErrorCode.NOT_FOUND, new Object[] {"outlet"});
    }
  }

  public void updateOutletState(OutletVo outlet) {
    if (outlet.getIsActive()) {
      iOutletRepository.updateStatus(outlet.getId(), true);
      return;
    }

    List<OutletUserMappingVo> outletUserMapping =
        outletUserMappingRepository.findByOutletIdAndIsActive(outlet.getId(), true);
    if (outletUserMapping.size() > 0)
      throw new OjbException(ErrorCode.OUTLET_STATE_TOGGLE_ERROR, new Object[] {});
    iOutletRepository.updateStatus(outlet.getId(), false);
  }

  public List<UserDetailsVo> getActiveAssociatedUsers(Long outletId) {
    return outletUserMappingRepository.findByOutletIdAndIsActive(outletId, true).stream()
        .map(OutletUserMappingVo::getAssotiatedUser)
        .map(
            e ->
                userDetailsRepository
                    .findById(e)
                    .orElseThrow(
                        () -> new OjbException(ErrorCode.NOT_FOUND, new Object[] {"User"})))
        .collect(Collectors.toList());
  }

  public List<UserDetailsVo> getActiveAssociatedBA(Long outletId) {
    return outletUserMappingRepository.findByOutletIdAndIsActive(outletId, true).stream()
        .map(OutletUserMappingVo::getAssotiatedUser)
        .map(
            e ->
                userDetailsRepository
                    .findById(e)
                    .orElseThrow(
                        () -> new OjbException(ErrorCode.NOT_FOUND, new Object[] {"User"})))
        .filter(e -> e.getUserType().getUserType().equals("BA"))
        .collect(Collectors.toList());
  }

  public List<UserDetailsVo> getAssociatedUsers(Long outletId) {
    return outletUserMappingRepository.findById(outletId).stream()
        .map(OutletUserMappingVo::getAssotiatedUser)
        .map(
            e ->
                userDetailsRepository
                    .findById(e)
                    .orElseThrow(
                        () -> new OjbException(ErrorCode.NOT_FOUND, new Object[] {"User"})))
        .collect(Collectors.toList());
  }

  public OutletVo fetchById(Long id) {
    OutletVo data = iOutletRepository.findById(id).orElse(null);
    LOGGER.info("outlet details--->"+data);
    if (null != data) {
      data.setOutletTypeObj(iDropdownMastereRepository.findById(data.getOutletType()).orElse(null));
      data.setOutletChanelObj(iDropdownMastereRepository.findById(data.getOutletChannel()).orElse(null));
      data.setProductDiv(iDropdownMastereRepository.findById(data.getProductDivision()).orElse(null));
      data.setCompanyZoneObj(iDropdownMastereRepository.findById(data.getCompanyZone()).orElse(null));
      LOGGER.info(data);
      return data;
    } else {
      new OjbException(ErrorCode.NOT_FOUND, new Object[] {"Outlet"});
    }
    return data;
  }

  public OutletVo updateOutlet(UpdateOutletDto input) {
    OutletVo outlet = iOutletRepository.findById(input.getId()).orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Outlet" }));
    if (null != input.getBeat()) {
      outlet.setBeat(input.getBeat());
    }
    if (null != input.getMarket() && !input.getMarket().isEmpty()) {
      outlet.setMarket(input.getMarket());
    }
    BeanUtils.copyProperties(input, outlet);
//    outlet.setLatitude((input.getLatitude() != null && !input.getLatitude().isEmpty()) ? input.getLatitude() : null);
//    outlet.setLongitude((input.getLongitude() != null && !input.getLongitude().isEmpty()) ? input.getLongitude() : null);
    outlet.setUpdatedOn(new Date());
    LOGGER.info("OutletService :: input details for outlet update:: " + input);
    OutletVo saved = iOutletRepository.save(outlet);
    LOGGER.info("OutletService :: updateOutlet() exit...");
    return fetchById(saved.getId());
  }

  public Optional<UserDetailsVo> checkBaAssociation(Long outletId, Long userId) {
    List<OutletUserMappingVo> mappings =
        outletUserMappingRepository.findByOutletIdAndAssotiatedUserAndIsActive(
            outletId, userId, true);
    return outletUserMappingRepository
        .findByOutletIdAndAssotiatedUserAndIsActive(outletId, userId, true)
        .stream()
        .map(
            e ->
                userDetailsRepository
                    .findById(e.getAssotiatedUser())
                    .orElseThrow(
                        () -> new OjbException(ErrorCode.NOT_FOUND, new Object[] {"User"})))
        .filter(e -> e.getUserType().getUserType().equals("BA"))
        .findAny();
  }
}