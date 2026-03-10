package com.dcc.osheaapp.report.attendance.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.dcc.osheaapp.common.model.Constants;
import com.dcc.osheaapp.common.service.Util;
import com.dcc.osheaapp.report.attendance.model.ActivityModel;
import com.dcc.osheaapp.report.attendance.model.UserActivityModel;

public class SummaryCalculator {
	private final Long presentCount;
	private final Long leaveCount;
	private final Long compOffCount;
	private final Long holidayCount;
	private final Long weekOffCount;
	private final Long ofcWorkCount;
	private final int totalDays;
	private final Long absentCount;
	private Predicate<ActivityModel> isPresent = p -> p.getActivityType()
			.equals(Constants.BA_Activity_Enum.store_login);
	private Predicate<ActivityModel> isAbsent = Objects::isNull;
	private Predicate<ActivityModel> isOfcWork = e -> e.getActivityType()
			.equals(Constants.BA_Activity_Enum.office_work);
	private Predicate<ActivityModel> isOnLeave = e -> e.getActivityType().equals(Constants.BA_Activity_Enum.leave);
	private Predicate<ActivityModel> isOnCompOff = e -> e.getActivityType().equals(Constants.BA_Activity_Enum.comp_off);
	private Predicate<ActivityModel> isOnHoliday = e -> e.getActivityType().equals(Constants.BA_Activity_Enum.holiday);
	private Predicate<ActivityModel> isOnWeekOff = e -> e.getActivityType().equals(Constants.BA_Activity_Enum.week_off);
	private Predicate<List<ActivityModel>> isPresentList = (e) -> {
		// if user release date -->
		if (e == null)
			return false;
		if (e.isEmpty())
			return false;
		List<ActivityModel> sortedList = e.stream().filter(activity -> isPresent.test(activity) || isOfcWork.test(activity) || isOnWeekOff.test(activity))
				.sorted(Comparator.comparing(ActivityModel::getActivityTime))
				.collect(Collectors.toList());
		if (!sortedList.isEmpty()) {
			ActivityModel firstActivity = sortedList.get(0);

			// If the first sorted activity is office work or a week off, return false
			if (isOfcWork.test(firstActivity) || isOnWeekOff.test(firstActivity)) {
				return false;
			}
		}

		// Return true if any remaining activity is of type isPresent
		return sortedList.stream().anyMatch(isPresent);
	};
	private Predicate<List<ActivityModel>> isAbsentList = e -> {
		if (e == null)
			return true;
		return e.isEmpty();
	};
	private Predicate<List<ActivityModel>> isOfcWorkList = e -> {
		if (e == null)
			return false;
		if (e.isEmpty())
			return false;

		List<ActivityModel> sortedList = e.stream().filter(activity -> isPresent.test(activity) || isOfcWork.test(activity) || isOnWeekOff.test(activity))
				.sorted(Comparator.comparing(ActivityModel::getActivityTime))
				.collect(Collectors.toList());
		if (!sortedList.isEmpty()) {
			ActivityModel firstActivity = sortedList.get(0);

			// If the first sorted activity is office work or a week off, return false
			if (isOnWeekOff.test(firstActivity) || isPresent.test(firstActivity)) {
				return false;
			}
		}

		// Return true if any remaining activity is of type isPresent
		return sortedList.stream().anyMatch(isOfcWork);
	};
	private Predicate<List<ActivityModel>> isOnLeaveList = e -> {
		if (e == null)
			return false;
		if (e.isEmpty())
			return false;
		return e.stream().anyMatch(isOnLeave);
	};
	private Predicate<List<ActivityModel>> isOnCompOffList = e -> {
		if (e == null)
			return false;
		if (e.isEmpty())
			return false;
		return e.stream().anyMatch(isOnCompOff);
	};
	private Predicate<List<ActivityModel>> isOnHolidayList = e -> {
		if (e == null)
			return false;
		if (e.isEmpty())
			return false;
		return e.stream().anyMatch(isOnHoliday);
	};
	private Predicate<List<ActivityModel>> isOnWeekOffList = e -> {
		if (e == null)
			return false;
		if (e.isEmpty())
			return false;
		List<ActivityModel> sortedList = e.stream().filter(activity -> isPresent.test(activity) || isOfcWork.test(activity) || isOnWeekOff.test(activity))
				.sorted(Comparator.comparing(ActivityModel::getActivityTime))
				.collect(Collectors.toList());
		if (!sortedList.isEmpty()) {
			ActivityModel firstActivity = sortedList.get(0);

			// If the first sorted activity is office work or a week off, return false
			if (isOfcWork.test(firstActivity) || isPresent.test(firstActivity)) {
				return false;
			}
		}

		// Return true if any remaining activity is of type isPresent
		return sortedList.stream().anyMatch(isOnWeekOff);
	};
	private final BiFunction<UserActivityModel, LocalDate, Boolean> isNotApplicable = (activity, date) -> {
		var joining = activity.getJoiningDate();
		if (joining == null)
			return false;
		if (date.isBefore(joining))
			return true;

		var release = activity.getReleaseDate();
		if (release == null)
			return false;
		return date.isAfter(release);
	};
	private UserActivityModel userActivity;
	private YearMonth yearMonth;
	public SummaryCalculator(UserActivityModel userActivity, YearMonth yearMonth) {
		this.userActivity = userActivity;
		this.yearMonth = yearMonth;
		this.totalDays = calculateTotalDays();
		this.presentCount = this.presentCount();
		this.leaveCount = this.leaveCount();
		this.compOffCount = this.compOffCount();
		this.holidayCount = this.holidayCount();
		this.ofcWorkCount = this.ofcWorkCount();
		this.weekOffCount = this.weekOffCount();
		this.absentCount = this.getTotalAbsent();
	}

	/**
	 * Calculate total days of a working BA.
	 * @return total days.
	 */
	private int calculateTotalDays() {
		int td = Util.daysUntilToday(yearMonth);
		if (userActivity.getReleaseDate() != null) {
			var today = LocalDate.now();
			LocalDate endOfMonth = null;
			if (today.getYear() == yearMonth.getYear() && today.getMonth() == yearMonth.getMonth()) {
				endOfMonth = today;
			} else {
				endOfMonth = yearMonth.atEndOfMonth();
			}
			td = td - (int) ChronoUnit.DAYS.between(userActivity.getReleaseDate(), endOfMonth);
		}
		if (userActivity.getJoiningDate().getYear() == yearMonth.getYear()
				&& userActivity.getJoiningDate().getMonth().equals(yearMonth.getMonth()))
			td = td - (int) ChronoUnit.DAYS.between(yearMonth.atDay(1), userActivity.getJoiningDate());
		return td;
	}
	private Long getTotalAbsent() {
		return totalDays - (presentCount + weekOffCount + compOffCount + holidayCount + leaveCount + ofcWorkCount);
	}

	private Long getAggregatedValue(UserActivityModel userActivity, Predicate<List<ActivityModel>> predicate) {
		var actMap = userActivity.getActivityMap();
//		Predicate<List<ActivityModel>> aggregatorPredicate = e -> e.stream().anyMatch(predicate);
		BiFunction<LocalDate, LocalDate, Boolean> pastReleaseDate = (activityDate, releaseDate) -> {
			if (releaseDate == null)
				return false;
			return activityDate.isAfter(releaseDate);
		};
		var applicableActivities = actMap.entrySet().stream()
				.filter(e -> !pastReleaseDate.apply(parseDate(e.getKey()), userActivity.getReleaseDate()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		return applicableActivities.entrySet().stream().filter(e -> predicate.test(e.getValue())).count();
	}

	private List<ActivityModel> getActivityPerDay(String day) {
		// creating hashMap key(<dd/mm/yyyy>) from column name (dd(DAY))
		String mapKey = day.split("\\(")[0] + "/" + yearMonth.toString().split("-")[1] + "/"
				+ yearMonth.toString().split("-")[0];
		return userActivity.getActivityMap().get(mapKey);
	}

	public Boolean isPresent(String date) {
		var activities = getActivityPerDay(date);
		return isPresentList.test(activities);
	}
	public Boolean isOnLeave(String date) {
		var activities = getActivityPerDay(date);
		return isOnLeaveList.test(activities);
	}
	public Boolean isOnCompOff(String date) {
		var activities = getActivityPerDay(date);
		return isOnCompOffList.test(activities);
	}
	public Boolean isOnHoliday(String date) {
		var activities = getActivityPerDay(date);
		return isOnHolidayList.test(activities);
	}
	public Boolean isOnWeekOff(String date) {
		var activities = getActivityPerDay(date);
		return isOnWeekOffList.test(activities);
	}
	public Boolean isOnOfcWork(String date) {
		var activities = getActivityPerDay(date);
		return isOfcWorkList.test(activities);
	}
	public Boolean isAbsent(String date) {
		var activities = getActivityPerDay(date);
		return isAbsentList.test(activities);
	}
	public Boolean isNotApplicable(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String mapKey = date.split("\\(")[0] + "/" + yearMonth.toString().split("-")[1] + "/"
				+ yearMonth.toString().split("-")[0];
		return isNotApplicable.apply(userActivity, LocalDate.parse(mapKey.replace("/", "-"), formatter));
	}
	LocalDate parseDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return LocalDate.parse(date, formatter);
	}

	Long presentCount() {
		return getAggregatedValue(userActivity, isPresentList);
	}

	Long ofcWorkCount() {
		return getAggregatedValue(userActivity, isOfcWorkList);
	}

	Long compOffCount() {
		return getAggregatedValue(userActivity, isOnCompOffList);
	}

	Long weekOffCount() {
		return getAggregatedValue(userActivity, isOnWeekOffList);
	}

	Long holidayCount() {
		return getAggregatedValue(userActivity, isOnHolidayList);
	}

	Long leaveCount() {
		return getAggregatedValue(userActivity, isOnLeaveList);
	}

	public Long getPresentCount() {
		return presentCount;
	}

	public Long getLeaveCount() {
		return leaveCount;
	}

	public Long getCompOffCount() {
		return compOffCount;
	}

	public Long getHolidayCount() {
		return holidayCount;
	}

	public Long getWeekOffCount() {
		return weekOffCount;
	}

	public Long getOfcWorkCount() {
		return ofcWorkCount;
	}

	public int getTotalDays() {
		return totalDays;
	}

	public Long getAbsentCount() {
		return absentCount;
	}
}
