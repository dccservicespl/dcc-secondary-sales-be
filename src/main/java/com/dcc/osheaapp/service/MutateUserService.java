package com.dcc.osheaapp.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dcc.osheaapp.report.common.model.UserChainFlat;
import com.dcc.osheaapp.repository.*;
import com.dcc.osheaapp.vo.dto.BdeOutletAssociation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcc.osheaapp.common.event.UserUpdatedEvent;
import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.Password;
import com.dcc.osheaapp.vo.OutletUserMappingVo;
import com.dcc.osheaapp.vo.OutletVo;
import com.dcc.osheaapp.vo.UserAssotiationDtlVo;
import com.dcc.osheaapp.vo.UserBeatsAssociation;
import com.dcc.osheaapp.vo.UserCredVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import com.dcc.osheaapp.vo.dto.UpdateUserDto;

@Service
public class MutateUserService {

	private static final Logger LOGGER = LogManager.getLogger(MutateUserService.class);
	private final IUserDetailsRepository userDetailsRepository;

	private final IUserChainFlatRepository iUserChainFlatRepository;
	private final IOutletUserMappingRepository outletUserMappingRepository;
	private final IOutletRepository outletRepository;
	private final IUserAssotoationRepository userAssociationRepository;
	private final IStockEntryRepository iStockEntryRepository;
	private final ApplicationEventPublisher eventPublisher;
	private final IOutletUserMappingRepository iOutletUserMappingRepository;
	private final IUserBeatsAssociationRepo iUserBeatsAssociationRepo;

	private final IBdeOutletMappingRepository iBdeOutletMappingRepository;

	@Autowired
	public  UserChainService userChainService;
	@Value("${ba.userCount}")
	private int baUserCount;

	public MutateUserService(IUserDetailsRepository userDetailsRepository,
			IOutletUserMappingRepository outletUserMappingRepository, IOutletRepository outletRepository,
			IUserAssotoationRepository userAssociationRepository, ApplicationEventPublisher eventPublisher,
			IUserBeatsAssociationRepo iUserBeatsAssociationRepo, IStockEntryRepository iStockEntryRepository,
			IOutletUserMappingRepository iOutletUserMappingRepository,IUserChainFlatRepository iUserChainFlatRepository,
							 IBdeOutletMappingRepository iBdeOutletMappingRepository) {
		this.userDetailsRepository = userDetailsRepository;
		this.outletUserMappingRepository = outletUserMappingRepository;
		this.outletRepository = outletRepository;
		this.eventPublisher = eventPublisher;
		this.userAssociationRepository = userAssociationRepository;
		this.iUserBeatsAssociationRepo = iUserBeatsAssociationRepo;
		this.iStockEntryRepository = iStockEntryRepository;
		this.iOutletUserMappingRepository = iOutletUserMappingRepository;
		this.iUserChainFlatRepository = iUserChainFlatRepository;
		this.iBdeOutletMappingRepository = iBdeOutletMappingRepository;
	}

	@Transactional
	public UserDetailsVo  register(UserDetailsVo input) {
		LOGGER.info("MutateUserService :: register() called...");

		checkForDuplicateContactNumber(input);
		String username = generateUserName(input);
		UserCredVo userCredentials = generateUserCredentials(input.getContactNumber(), username);
		UserDetailsVo newUser = new UserDetailsVo().setFullName(input.getFullName())
				.setContactNumber(input.getContactNumber()).setUserType(input.getUserType())
				.setUserCode(input.getUserCode()).setCreatedBy(input.getCreatedBy()).setAadhar(input.getAadhar())
				.setPan(input.getPan()).setBankName(input.getBankName()).setBankBranchName(input.getBankBranchName())
				.setBankAccountNo(input.getBankAccountNo()).setIfscCode(input.getIfscCode())
				.setEmployeeType(input.getEmployeeType()).setSalary(input.getSalary())
				.setReportingTo(input.getReportingTo()).setState(input.getState())
				.setProductDivision(input.getProductDivision()).setCompanyZone(input.getCompanyZone())
				.setUserCred(userCredentials).setIsActive(true).setCreatedOn(new Date()).setUpdatedOn(new Date())
				.setDateOfJoining(input.getDateOfJoining());

//    newUser.setBeat(input.getBeat());

		UserDetailsVo savedUser = userDetailsRepository.save(newUser);
		associateUserToOutlet(savedUser, input.getAssotiateOutlet());
		associateUser(savedUser, input.getReportingTo());
		if(input.getUserType().getId() == 4)
			associateBdeToOutlet(savedUser,input.getAssotiateOutlet());
		if (null != input.getBeat()) 	associatedBeats(savedUser, input.getBeat());
		updateStockAssociation(savedUser, input.getAssotiateOutlet(), 1L);
		UserUpdatedEvent event = new UserUpdatedEvent(this, savedUser);
		userChainService.getUserChainFlat(event);
//		eventPublisher.publishEvent(event);
		return savedUser;
	}

	private void associatedBeats(UserDetailsVo savedUser, List<UserBeatsAssociation> beat) {
		List<UserBeatsAssociation> beatUser = new ArrayList<>();
		for (UserBeatsAssociation uba : beat) {
			UserBeatsAssociation ubaObj = new UserBeatsAssociation();
			ubaObj.setBeatId(uba.getBeatId());
			ubaObj.setBeatName(uba.getBeatName());
			ubaObj.setUser(savedUser.getId());

			beatUser.add(ubaObj);
		}
		iUserBeatsAssociationRepo.saveAll(beatUser);

	}

	private void checkForDuplicateContactNumber(UserDetailsVo input) {
//		userDetailsRepository.findExistingContactNumber(input.getContactNumber()).ifPresent(e -> {
//			throw new OjbException(ErrorCode.ALREADY_EXIST, new Object[] { "Phone number" });
//		});
		// Rejoin user with same number

		List<UserDetailsVo> users = userDetailsRepository.findByContactNumber(input.getContactNumber());
		boolean hasActiveUser = users.stream().anyMatch(user -> Boolean.TRUE.equals(user.getIsActive()));
		if (hasActiveUser) {
			throw new OjbException(ErrorCode.ALREADY_EXIST, new Object[] { "Active User with this Phone Number" });
		}

	}

	private UserCredVo generateUserCredentials(String contactNo, String username) {
		String encryptedPassword = "";
		try {
			encryptedPassword = Password.getSaltedHash(contactNo);
		} catch (Exception e) {
			throw new OjbException(ErrorCode.GENERAL, new Object[] {});
		}
		return new UserCredVo(encryptedPassword, username, true);
	}

	private void updatePasswordForNonBA(UserCredVo userCred, String contactNo) {
		try {
			String encryptedPassword = Password.getSaltedHash(contactNo);
			userCred.setPassword(encryptedPassword);
		} catch (Exception e) {
			throw new OjbException(ErrorCode.GENERAL, new Object[] {});
		}
	}

	private String generateUserName(UserDetailsVo input) {
		if (null != input.getUserCode() && !input.getUserCode().isEmpty()) {
			return input.ifBA() ? input.getUserCode() + "@" + input.getContactNumber() : input.getUserCode();
		}

		if (input.ifBA()) {
			int n = userDetailsRepository.countUser(input.getUserType().getId(), input.getProductDivision().getId());
			LOGGER.info("Next baUserCount ============ >> " + baUserCount);
			String num = String.format("%05d", n + baUserCount);
			String code = input.getUserType().getUserType() + num;
			return code + "@" + input.getContactNumber();
		}

		int n = userDetailsRepository.countSalesTeamUser(input.getProductDivision().getId());
		String num = String.format("%04d", n);
		String div = (input.getProductDivision().getFieldName().equalsIgnoreCase("oshea")) ? "OSH"
				: (input.getProductDivision().getFieldName().equalsIgnoreCase("louis")) ? "LOU" : "";
		return div + num;
	}

	private List<OutletUserMappingVo> associateUserToOutlet(UserDetailsVo input,
			List<OutletVo> outletsToBeAssociatedWith) {
		List<OutletUserMappingVo> existingActiveAssociations = outletUserMappingRepository
				.findByAssotiatedUserAndIsActive(input.getId(), true);

		if (outletsToBeAssociatedWith == null || outletsToBeAssociatedWith.isEmpty()) {
			if (!existingActiveAssociations.isEmpty()) {
				return outletUserMappingRepository.saveAll(existingActiveAssociations.stream()
						.map(OutletUserMappingVo::dissociate).collect(Collectors.toList()));
			}
			return new ArrayList<>();
		}
		List<OutletVo> outlets = outletRepository
				.findAllById(outletsToBeAssociatedWith.stream().map(OutletVo::getId).collect(Collectors.toList()));
		if (outlets.stream().anyMatch(e -> !e.getIsActive()))
			throw new OjbException(ErrorCode.INACTIVE_OUTLET_ASSOCIATION, new Object[] {});

		if (input.ifBA()) {
			if (outletsToBeAssociatedWith.size() > 1)
				throw new OjbException(ErrorCode.INVALID_MULTIPLE_OUTLET_ASSOCIATION, new Object[] {});
		}

		// disassociate existing mappings

		List<OutletUserMappingVo> dissociatedOutlets = getDissociatedOutlets(outletsToBeAssociatedWith,
				existingActiveAssociations);
		List<OutletUserMappingVo> newAssociations = getNewAssociations(input, outletsToBeAssociatedWith,
				existingActiveAssociations);

		return outletUserMappingRepository
				.saveAll(Stream.concat(newAssociations.parallelStream(), dissociatedOutlets.parallelStream())
						.collect(Collectors.toList()));
	}

	private static List<OutletUserMappingVo> getNewAssociations(UserDetailsVo input,
			List<OutletVo> outletsToBeAssociatedWith, List<OutletUserMappingVo> existingActiveAssociations) {
		return outletsToBeAssociatedWith.stream().filter(
				o -> existingActiveAssociations.stream().noneMatch(e -> e.getOutlet().getId().equals(o.getId())))
				.map(e -> new OutletUserMappingVo(outletsToBeAssociatedWith.get(0), input.getId(),
						input.getUpdatedOn(), null, true))
				.collect(Collectors.toList());
	}

	private static List<OutletUserMappingVo> getDissociatedOutlets(List<OutletVo> outletsToBeAssociatedWith,
			List<OutletUserMappingVo> existingActiveAssociations) {
		List<OutletUserMappingVo> uncommonAssociations = existingActiveAssociations.stream()
				.filter(o -> outletsToBeAssociatedWith.stream().noneMatch(e -> e.getId().equals(o.getOutlet().getId())))
				.collect(Collectors.toList());
		return uncommonAssociations.stream().map(OutletUserMappingVo::dissociate).collect(Collectors.toList());
	}

	private UserAssotiationDtlVo associateUser(UserDetailsVo user, UserDetailsVo userToBeAssociatedWith) {
		if (userToBeAssociatedWith == null)
			return new UserAssotiationDtlVo();
		List<UserAssotiationDtlVo> associations = new ArrayList<>();
		UserAssotiationDtlVo userAssociation = userAssociationRepository.findByUser(user.getId(), true).orElse(null);
//		UserChainFlat userChainFlat = iUserChainFlatRepository.findBaId(user.getId());
		if (userAssociation != null) {
			userAssociation.setIsActive(false);
			associations.addAll(List.of(userAssociation, new UserAssotiationDtlVo(user, userToBeAssociatedWith, true)));
//			userChainFlat.setBde(userAssociation.getAssotiatedUser().getFullName());
		} else {
			associations.add(new UserAssotiationDtlVo(user, userToBeAssociatedWith, true));
		}

		userAssociationRepository.saveAll(associations);
		return userAssociationRepository.findByUser(user.getId(), true).orElse(new UserAssotiationDtlVo());
	}

	public UserDetailsVo update(UpdateUserDto input) {
		LOGGER.info("UserService :: update() called...");
		UserDetailsVo user = userDetailsRepository.findById(input.getId())
				.orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] { "User" }));

		String oldUsername = user.getUserCred().getUsername();
		String newUsername = user.ifBA() ? input.getUserCode() + "@" + input.getContactNumber().trim()
				: user.getUserCred().getUsername();

		if (!oldUsername.equalsIgnoreCase(newUsername)) {
			user.setUserCred(generateUserCredentials(input.getContactNumber(), newUsername));
		}else{
			updatePasswordForNonBA(user.getUserCred(), input.getContactNumber());
		}

		BeanUtils.copyProperties(input, user);

		user.setUpdatedOn(new Date());
		UserDetailsVo savedUser = userDetailsRepository.save(user);

		associateUserToOutlet(savedUser, user.getAssotiateOutlet());
		associateUser(savedUser, user.getReportingTo());
		if(input.getUserType().getId() == 4)
			associateBdeToOutlet(savedUser,input.getAssotiateOutlet());
		eventPublisher.publishEvent(new UserUpdatedEvent(this, savedUser));
		return userDetailsRepository.findById(savedUser.getId()).orElse(new UserDetailsVo());
	}

	public void removeOutletAssociation(Long asscId) {
		OutletUserMappingVo mapping = outletUserMappingRepository.findById(asscId).map(OutletUserMappingVo::dissociate)
				.orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] { "Association" }));
		outletUserMappingRepository.save(mapping);
	}

	private String updateStockAssociation(UserDetailsVo input,
			List<OutletVo> outletsToBeAssociatedWith, Long loginUserId) {
		//check if existing stock in outlet
		for (OutletVo each : outletsToBeAssociatedWith) {
			String existingStock = iStockEntryRepository.getActiveStockOfMonthYear(each.getId());
			if(null != existingStock) {
				String stockId = existingStock.split(",")[0];
				String existingUserId = existingStock.split(",")[1];
				//check if associated user association is active, it should be one data
					List<OutletUserMappingVo> activeAssociation = iOutletUserMappingRepository.findByOutletIdAndAssotiatedUserAndIsActive(
							each.getId(), Long.parseLong(existingUserId), true);
					if(null != activeAssociation && activeAssociation.size() == 0) {		//!activeAssociation.get(0).getIsActive()
						int updateStockUser = iStockEntryRepository.updateStockUser(Long.parseLong(stockId), input.getId(), new Date(), loginUserId);
						LOGGER.info("User Assotiation updated in outlet : "+each.getOutletCode() +" == status : "+updateStockUser);
				}
			}			
		}
		return "Updated Stock Association.";
	}

	private List<BdeOutletAssociation>  associateBdeToOutlet ( UserDetailsVo input,
															   List<OutletVo> outletsToBeAssociatedWith){
		LOGGER.info("Enter in associatedBde ----> " + input.getReportingTo().getId());
		//  outlet ids that need to be associated
		List<Long> outletIds = outletsToBeAssociatedWith.stream()
				.map(OutletVo::getId)
				.collect(Collectors.toList());
		// Deactivate only associations where BDE is different
		iBdeOutletMappingRepository.deactivateOutdatedAssociations(outletIds, input.getReportingTo().getId());
		// insert new association
		List<BdeOutletAssociation> newAssociations = outletsToBeAssociatedWith.stream()
				.map(outlet -> new BdeOutletAssociation(
						null, new Date(), input.getCreatedBy(), new Date(), input.getCreatedBy(),
						outlet, input.getReportingTo(), input.getUserType(), true
				))
				.collect(Collectors.toList());
		// Save new active associations
		return iBdeOutletMappingRepository.saveAll(newAssociations);

	}
}
