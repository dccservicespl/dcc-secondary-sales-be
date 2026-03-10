package com.dcc.osheaapp.service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.dcc.osheaapp.common.event.UserUpdatedEvent;
import com.dcc.osheaapp.report.common.model.UserChainFlat;
import com.dcc.osheaapp.repository.IUserChainFlatRepository;
import com.dcc.osheaapp.repository.IUserChainRepository;
import com.dcc.osheaapp.repository.IUserDetailsRepository;
import com.dcc.osheaapp.vo.UserDetailsVo;
import com.dcc.osheaapp.vo.views.UserChain;

@Service
public class UserChainService {

	private static final Logger LOGGER = LogManager.getLogger(UserChainService.class);
	private final IUserChainRepository userChainRepository;
	private final IUserDetailsRepository userDetailsRepository;
	private final UserService userService;
	private final IUserChainFlatRepository userChainFlatRepository;

	@PersistenceContext
	EntityManager em;

	@Autowired
	public UserChainService(IUserChainRepository userChainRepository, IUserDetailsRepository userDetailsRepository,
			UserService userService, IUserChainFlatRepository userChainFlatRepository) {
		this.userChainRepository = userChainRepository;
		this.userDetailsRepository = userDetailsRepository;
		this.userService = userService;
		this.userChainFlatRepository = userChainFlatRepository;
	}

	public void getAllActiveUserChain() {
		List<Object[]> activeAssociatedBaId = em.createNativeQuery(
				"select uad.user_id UserId, ud.company_zone Zone  from user_assotiation_details uad inner join user_details ud on ud.ID = uad.user_id where ud.user_type = 4 and uad.is_active = 1")
				.getResultList();
		List<UserChainFlat> chains = activeAssociatedBaId.stream().map(e -> {
			em.clear();
			BigInteger userId = (BigInteger) e[0];
			BigInteger zone = (BigInteger) e[1];
			LOGGER.info("[Generation] user: " + userId);
			UserChainFlat chainFlat = generateChainFlattened(userId.longValue());
			chainFlat.setZone(zone.longValue());
			return chainFlat;
		}).collect(Collectors.toList());
		userChainFlatRepository.saveAll(chains);
	}

	@EventListener
	private void syncChains(UserUpdatedEvent event) {
		CompletableFuture.supplyAsync(() -> getUserChainFlat(event))
				.thenAccept((savedChain) -> LOGGER.info("[User Chain ] Updated:: " + savedChain.getId()));
	}

	public UserChainFlat getUserChainFlat(UserUpdatedEvent event) {

		List<UserChainFlat> datalist;
		UserChainFlat flattenedChain = generateChainFlattened(event.getUser().getId());
		String userType = event.getUser().getUserType().getUserType();

		switch (userType) {
			case "BA" :
				UserChainFlat existingChain = userChainFlatRepository.findByBaId(event.getUser().getId())
						.map((e) -> flattenedChain.setId(e.getId())).orElse(flattenedChain);
				return userChainFlatRepository.save(existingChain);
			case "SO" :
				System.out.println("Processing SO user type");
				datalist = userChainFlatRepository.findBySoId(event.getUser().getId());
				updateFields(datalist, flattenedChain, userType);
				userChainFlatRepository.saveAll(datalist);
				return flattenedChain;
			case "BDE" :
				System.out.println("Processing BDE user type");
				datalist = userChainFlatRepository.findByBdeId(event.getUser().getId());
				updateFields(datalist, flattenedChain, userType);
				userChainFlatRepository.saveAll(datalist);
				return flattenedChain;
			case "ASE" :
				System.out.println("Processing ASE user type");
				datalist = userChainFlatRepository.findByAseId(event.getUser().getId());
				updateFields(datalist, flattenedChain, userType);
				userChainFlatRepository.saveAll(datalist);
				return flattenedChain;
			case "ASM" :
				System.out.println("Processing ASM user type");
				datalist = userChainFlatRepository.findByAsmId(event.getUser().getId());
				updateFields(datalist, flattenedChain, userType);
				userChainFlatRepository.saveAll(datalist);
				return flattenedChain;
			case "BDM" :
				System.out.println("Processing BDM user type");
				datalist = userChainFlatRepository.findByBdmId(event.getUser().getId());
				updateFields(datalist, flattenedChain, userType);
				userChainFlatRepository.saveAll(datalist);
				return flattenedChain;
			case "RSM" :
				System.out.println("Processing RSM user type");
				datalist = userChainFlatRepository.findByRsmId(event.getUser().getId());
				updateFields(datalist, flattenedChain, userType);
				userChainFlatRepository.saveAll(datalist);
				return flattenedChain;
			case "ZSM" :
				System.out.println("Processing ZSM user type");
				datalist = userChainFlatRepository.findByZsmId(event.getUser().getId());
				updateFields(datalist, flattenedChain, userType);
				userChainFlatRepository.saveAll(datalist);
				return flattenedChain;
			case "NSM" :
				System.out.println("Processing NSM user type");
				datalist = userChainFlatRepository.findByNsmId(event.getUser().getId());
				updateFields(datalist, flattenedChain, userType);
				userChainFlatRepository.saveAll(datalist);
				return flattenedChain;
			default :
				System.out.println("Processing default case");
				return userChainFlatRepository.save(flattenedChain);
		}
	}

	public List<UserChainFlat> retrieveChains(List<Long> baIds) {
		return this.userChainFlatRepository.findAllByBaId(baIds);
	}
	public UserChainFlat retrieveChain(Long baId) {
		return this.userChainFlatRepository.findByBaId(baId).orElse(new UserChainFlat());
	}
	/**
	 * Generates a userchain flattened and trimmed down to only the names. e.g,
	 * Jeetendra(MD) > Partha(NSM) > ....
	 * 
	 * @param target
	 * @return the flattened chain.
	 */
	public UserChainFlat generateChainFlattened(Long target) {
		UserChainFlat dto = new UserChainFlat();
		Map<Long, UserChain> chain = getAssociatedUserChainMap(target);
		return populateUserChain(chain, target, dto);
	}

	public UserChainFlat generateChainFlattened1(Long target) {
		UserChainFlat dto = new UserChainFlat();
		Map<Long, UserChain> chain = getAssociatedUserChainMap1(target);
		return populateUserChain(chain, target, dto);
	}

	public List<UserChain> getUserChain(Long userId) {
		return userChainRepository.findUserChainById(userId);
	}
	public List<UserChain> getUserChain1(Long userId) {
		return userChainRepository.findUserChainById1(userId);
	}
	public UserChain getNextUserInChain(Map<Long, UserChain> chainMap, Long userId) {
		Map<Long, UserChain> map = getAssociatedUserChainMap(userId);
		return map.get(userId);
	}

	public Map<Long, UserChain> getAssociatedUserChainMap(Long userId) {
		List<UserChain> chainList = getUserChain(userId);
		Map<Long, UserChain> map = new HashMap<>();
		chainList.forEach(e -> map.put(e.getUserId(), e));
		return map;
	}

	public Map<Long, UserChain> getAssociatedUserChainMap1(Long userId) {
		List<UserChain> chainList = getUserChain1(userId);
		Map<Long, UserChain> map = new HashMap<>();
		chainList.forEach(e -> map.put(e.getUserId(), e));
		return map;
	}

	/**
	 * Populates the userchain e.g., MD > NSM >...>BA etc recursively.
	 *
	 * @param chainMap
	 * @param userId
	 * @param dto
	 * @return
	 */
	public UserChainFlat populateUserChain(Map<Long, UserChain> chainMap, Long userId, UserChainFlat dto) {

		UserChain user = chainMap.get(userId);
		if (user == null)
			return dto;

		if (user.getUserType().equals("BA")) {
			UserDetailsVo ba = userService.findUserById(user.getUserId()).orElse(null);
			dto.setBaName(user.getFullName()).setBaCode(ba == null ? null : ba.getUserCred().getCode());
			dto.setBaId(user.getUserId());
		}

		if (user.getUserType().equals("SO")) {
			dto.setSo(user.getFullName());
			dto.setSoId(user.getUserId());
		}

		if (user.getUserType().equals("BDE")) {
			dto.setBde(user.getFullName());
			dto.setBdeId(user.getUserId());
		}

		if (user.getUserType().equals("ASM")) {
			dto.setAsm(user.getFullName());
			dto.setAsmId(user.getUserId());
		}

		if (user.getUserType().equals("NSM")) {
			dto.setNsm(user.getFullName());
			dto.setNsmId(user.getUserId());
		}
		if (user.getUserType().equals("BDM")) {
			dto.setBdm(user.getFullName());
			dto.setBdmId(user.getUserId());
		}
		if (user.getUserType().equals("ASE")) {
			dto.setAse(user.getFullName());
			dto.setAseId(user.getUserId());
		}
		if (user.getUserType().equals("ZSM")) {
			dto.setZsm(user.getFullName());
			dto.setZsmId(user.getUserId());
		}
		if (user.getUserType().equals("RSM")) {
			dto.setRsm(user.getFullName());
			dto.setRsmId(user.getUserId());
		}
		// if anyone reports to MD... it will not be in the map as there is no
		// association of MD
		// further.
		// checking if the user reports to the md.. in that case the md's name would be
		// there is dto
		if (user.getAssociatedUserType().equals("MD")) {
			dto.setMd(user.getAssociatedUserName());
			dto.setMdId(user.getAssociatedUserId());
		} ;
		return populateUserChain(chainMap, user.getAssociatedUserId(), dto);
	}

	private void updateFields(List<UserChainFlat> datalist, UserChainFlat flattenedChain, String userType) {
		for (UserChainFlat d : datalist) {
			if (userType.equals("SO")) {
				d.setBde(flattenedChain.getBde());
				d.setAse(flattenedChain.getAse());
				d.setBdm(flattenedChain.getBdm());
				d.setAsm(flattenedChain.getAsm());
				d.setRsm(flattenedChain.getRsm());
				d.setZsm(flattenedChain.getZsm());
				d.setNsm(flattenedChain.getNsm());
				d.setMd(flattenedChain.getMd());

				d.setBdeId(flattenedChain.getBdeId());
				d.setAseId(flattenedChain.getAseId());
				d.setBdmId(flattenedChain.getBdmId());
				d.setAsmId(flattenedChain.getAsmId());
				d.setRsmId(flattenedChain.getRsmId());
				d.setZsmId(flattenedChain.getZsmId());
				d.setNsmId(flattenedChain.getNsmId());
				d.setMdId(flattenedChain.getMdId());
			} else if (userType.equals("BDE")) {
				d.setAse(flattenedChain.getAse());
				d.setBdm(flattenedChain.getBdm());
				d.setAsm(flattenedChain.getAsm());
				d.setRsm(flattenedChain.getRsm());
				d.setZsm(flattenedChain.getZsm());
				d.setNsm(flattenedChain.getNsm());
				d.setMd(flattenedChain.getMd());

				d.setAseId(flattenedChain.getAseId());
				d.setBdmId(flattenedChain.getBdmId());
				d.setAsmId(flattenedChain.getAsmId());
				d.setRsmId(flattenedChain.getRsmId());
				d.setZsmId(flattenedChain.getZsmId());
				d.setNsmId(flattenedChain.getNsmId());
				d.setMdId(flattenedChain.getMdId());
			} else if (userType.equals("ASE")) {
				d.setBdm(flattenedChain.getBdm());
				d.setAsm(flattenedChain.getAsm());
				d.setRsm(flattenedChain.getRsm());
				d.setZsm(flattenedChain.getZsm());
				d.setNsm(flattenedChain.getNsm());
				d.setMd(flattenedChain.getMd());

				d.setBdmId(flattenedChain.getBdmId());
				d.setAsmId(flattenedChain.getAsmId());
				d.setRsmId(flattenedChain.getRsmId());
				d.setZsmId(flattenedChain.getZsmId());
				d.setNsmId(flattenedChain.getNsmId());
				d.setMdId(flattenedChain.getMdId());
			} else if (userType.equals("BDM")) {
				d.setRsm(flattenedChain.getRsm());
				d.setZsm(flattenedChain.getZsm());
				d.setNsm(flattenedChain.getNsm());
				d.setMd(flattenedChain.getMd());

				d.setRsmId(flattenedChain.getRsmId());
				d.setZsmId(flattenedChain.getZsmId());
				d.setNsmId(flattenedChain.getNsmId());
				d.setMdId(flattenedChain.getMdId());
			} else if (userType.equals("ASM")) {
				d.setRsm(flattenedChain.getRsm());
				d.setZsm(flattenedChain.getZsm());
				d.setNsm(flattenedChain.getNsm());
				d.setMd(flattenedChain.getMd());

				d.setRsmId(flattenedChain.getRsmId());
				d.setZsmId(flattenedChain.getZsmId());
				d.setNsmId(flattenedChain.getNsmId());
				d.setMdId(flattenedChain.getMdId());
			} else if (userType.equals("RSM")) {
				d.setZsm(flattenedChain.getZsm());
				d.setNsm(flattenedChain.getNsm());
				d.setMd(flattenedChain.getMd());

				d.setZsmId(flattenedChain.getZsmId());
				d.setNsmId(flattenedChain.getNsmId());
				d.setMdId(flattenedChain.getMdId());
			} else if (userType.equals("ZSM")) {
				d.setNsm(flattenedChain.getNsm());
				d.setMd(flattenedChain.getMd());

				d.setNsmId(flattenedChain.getNsmId());
				d.setMdId(flattenedChain.getMdId());
			} else if (userType.equals("NSM")) {
				d.setMd(flattenedChain.getMd());
				d.setMdId(flattenedChain.getMdId());
			}
		}
	}
}
