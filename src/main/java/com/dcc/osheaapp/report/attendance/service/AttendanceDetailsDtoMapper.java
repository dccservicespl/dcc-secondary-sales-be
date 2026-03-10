package com.dcc.osheaapp.report.attendance.service;

import static com.dcc.osheaapp.common.service.Util.localDateFromDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dcc.osheaapp.common.model.Constants;
import com.dcc.osheaapp.report.attendance.model.ActivityModel;
import com.dcc.osheaapp.report.common.model.UserChainFlat;
import com.dcc.osheaapp.repository.IDropdownMastereRepository;
import com.dcc.osheaapp.repository.UserViewRepository;
import com.dcc.osheaapp.service.UserChainService;
import com.dcc.osheaapp.vo.UserActivityRegisterVo;
import com.dcc.osheaapp.vo.views.UserView;

@Service
public class AttendanceDetailsDtoMapper {

	private static final Logger LOGGER = LogManager.getLogger(AttendanceDetailsDtoMapper.class);
	private final UserChainService userChainService;
	private final UserViewRepository userViewRepository;
	private final IDropdownMastereRepository dropdownMastereRepository;

	Function<Date, LocalDateTime> getLocalDateTime = e -> e.toInstant().atZone(ZoneId.of("Asia/Kolkata"))
			.toLocalDateTime();

	Function<Date, LocalDateTime> getLocalDateTimeAlt = e -> e.toInstant().atZone(ZoneId.systemDefault())
			.toLocalDateTime();

	public AttendanceDetailsDtoMapper(UserChainService userChainService, UserViewRepository userViewRepository,
			IDropdownMastereRepository dropdownMastereRepository) {
		this.userChainService = userChainService;
		this.userViewRepository = userViewRepository;
		this.dropdownMastereRepository = dropdownMastereRepository;
	}

	public List<AttendanceDetailsDto> toDtos(List<UserActivityRegisterVo> entities) {
		Map<String, List<UserActivityRegisterVo>> userActivityMap = getUserActivityMap(entities);
		List<Long> distinctUsers = userActivityMap.keySet().stream().map(e -> Long.parseLong(e.split("-")[0]))
				.distinct().collect(Collectors.toList());
		Map<Long, UserChainFlat> chainMap = userChainService.retrieveChains(distinctUsers).stream()
				.collect(Collectors.toMap(UserChainFlat::getBaId, e -> e));
		Map<Long, UserView> userViewMap = userViewRepository.findAllByUserId(distinctUsers).stream()
				.collect(Collectors.toMap(UserView::getUserId, e -> e));

		return userActivityMap.entrySet().parallelStream().map(e -> toDto(e, chainMap, userViewMap))
				.collect(Collectors.toList());
	}

	/**
	 * user activity list is mapped date and userwise
	 *
	 * @param entities
	 * @return hashmap : ['1-12/11/2023' --> [{...activity}, {...activity}]]
	 */
	public Map<String, List<UserActivityRegisterVo>> getUserActivityMap(List<UserActivityRegisterVo> entities) {
		Map<String, List<UserActivityRegisterVo>> userActivityMap = new HashMap<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyy");
		DateFormat dt = new SimpleDateFormat("dd/MM/yyy");
		Function<UserActivityRegisterVo, String> getMapKey = e -> e.getCreatedBy() + "-"
				+ getLocalDateTimeAlt.apply(e.getActivityTime()).format(formatter);

		// Function<UserActivityRegisterVo, String> getMapKey =
		// e -> e.getCreatedBy() + "-" + dt.format(e.getActivityTime());
		entities.forEach(e -> {
			String key = getMapKey.apply(e);
			if (userActivityMap.containsKey(key))
				userActivityMap.get(key).add(e);
			else {
				List<UserActivityRegisterVo> list = new ArrayList<>();
				list.add(e);
				userActivityMap.put(key, list);
			}
		});
		LOGGER.info("[Attendance Report]: Activity Map prepared");

		return userActivityMap;
	}

	/**
	 * user activity list is mapped date and userwise
	 *
	 * @param entities
	 * @return hashmap : ['1-12/11/2023' --> [{...activity}, {...activity}]]
	 */
	public Map<String, List<ActivityModel>> getUserActivityModelMap(List<UserActivityRegisterVo> entities) {
		Map<String, List<ActivityModel>> userActivityMap = new HashMap<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyy");
		DateFormat dt = new SimpleDateFormat("dd/MM/yyy");
		Function<UserActivityRegisterVo, String> getMapKey = e -> e.getCreatedBy() + "-"
				+ getLocalDateTimeAlt.apply(e.getActivityTime()).format(formatter);
		entities.forEach(e -> {
			String key = getMapKey.apply(e);
			ActivityModel activityModel = new ActivityModel().setActivityType(e.getActivityType())
					.setActivityTime(localDateFromDate(e.getActivityTime()));
			if (userActivityMap.containsKey(key))
				userActivityMap.get(key).add(activityModel);
			else {
				List<ActivityModel> list = new ArrayList<>();
				list.add(activityModel);
				userActivityMap.put(key, list);
			}
		});
		LOGGER.info("[Attendance Report]: Activity Map prepared");
		return userActivityMap;
	}

	private AttendanceDetailsDto toDto(Map.Entry<String, List<UserActivityRegisterVo>> listMap,
			Map<Long, UserChainFlat> chainMap, Map<Long, UserView> userViewMap) {
		if (listMap.getValue() == null || listMap.getValue().isEmpty())
			return new AttendanceDetailsDto();

		UserActivityRegisterVo firstObj = listMap.getValue().get(0);
		Long userId = Long.parseLong(listMap.getKey().split("-")[0]);
		UserView user = userViewMap.get(userId);
		Function<List<UserActivityRegisterVo>, Boolean> isRetailing = e -> e.stream()
				.anyMatch(m -> m.getActivityType().equals(Constants.BA_Activity_Enum.store_login));
		Boolean retailing = isRetailing.apply(listMap.getValue());
		LocalTime storeCheckIn = retailing
				? getStoreCheckIn(listMap.getValue().stream()
						.filter(m -> m.getActivityType().equals(Constants.BA_Activity_Enum.store_login))
						.collect(Collectors.toList())).orElse(null)
				: null;
		LocalTime storeCheckOut = retailing
				? getStoreCheckout(listMap.getValue().stream()
						.filter(m -> m.getActivityType().equals(Constants.BA_Activity_Enum.store_logout))
						.collect(Collectors.toList())).orElse(null)
				: null;
		LocalDate dateLocal = getLocalDateTimeAlt.apply(firstObj.getActivityTime()).toLocalDate();
		Date date = firstObj.getActivityTime();
		DateFormat dt = new SimpleDateFormat("dd/MM/yyy");
		dt.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date dateFormat = null;
		try {
			dateFormat = dt.parse(dt.format(date));
			dt.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			dateFormat = dt.parse(dt.format(date));

		} catch (Exception e) {
			e.printStackTrace();
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyy");
		// Date date1 =
		// Date.from(dateLocal.atStartOfDay().atZone(ZoneId.of("Asia/Kolkata")).toInstant());
		if (firstObj.getCreatedBy() == 1064 && "week_off".equals(firstObj.getActivityType().name())) {
			LOGGER.info(" ===== date =================>> " + date + ", == " + firstObj.getActivityType() + "\n" + ", "
					+ dateFormat);
		}
		UserChainFlat userChain = chainMap.get(userId);

		String activityDate = dt.format(date);
		String month = activityDate.split("/")[1];

		return new AttendanceDetailsDto().setUser(user).setZone(user.getCompanyZone())
				.setType(retailing ? "Retailing" : firstObj.getActivityType().toTitleCase())
				// .setDate(Date.from(dateLocal.atStartOfDay().atZone(ZoneId.of("Asia/Kolkata")).toInstant()))
				.setDate(activityDate).setMonth(Month.of(Integer.parseInt(month))) // + 1
				.setReason(retailing ? "" : firstObj.getLeaveReason())
				.setUserChain(userChain == null ? new UserChainFlat() : userChain).setStore_check_in(storeCheckIn)
				.setStore_check_out(storeCheckOut)
				.setTotal_time(retailing ? getDuration(storeCheckIn, storeCheckOut) : "").setRemarks("");
	}

	private String getDuration(LocalTime x, LocalTime y) {
		if (null != x && null != y) {
			Duration duration = Duration.between(x, y);
			long seconds = duration.getSeconds();

			long hours = seconds / 3600;
			long minutes = (seconds % 3600) / 60;
			long remainingSeconds = seconds % 60;

			return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
		}
		return "";
	}

	private Optional<LocalTime> getStoreCheckIn(List<UserActivityRegisterVo> activities) {
		List<UserActivityRegisterVo> sorted = new ArrayList<>(activities);
		sorted.sort(Comparator.comparing(UserActivityRegisterVo::getActivityTime));
		return sorted.isEmpty()
				? Optional.empty()
				: Optional.of(getLocalDateTime.apply(sorted.get(0).getActivityTime()).toLocalTime());
	}

	private Optional<LocalTime> getStoreCheckout(List<UserActivityRegisterVo> activities) {
		List<UserActivityRegisterVo> sorted = new ArrayList<>(activities);
		sorted.sort(Comparator.comparing(UserActivityRegisterVo::getActivityTime));
		return sorted.isEmpty()
				? Optional.empty()
				: Optional.of(getLocalDateTime.apply(sorted.get(sorted.size() - 1).getActivityTime()).toLocalTime());
	}
}
