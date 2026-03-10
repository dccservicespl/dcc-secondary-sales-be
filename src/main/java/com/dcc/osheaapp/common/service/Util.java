package com.dcc.osheaapp.common.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.repository.IUserCredRepository;
import com.dcc.osheaapp.repository.IUserDetailsRepository;
import com.dcc.osheaapp.vo.UserCredVo;
import com.dcc.osheaapp.vo.UserDetailsVo;

@Service
public class Util {

	public static final Logger LOGGER = LogManager.getLogger(Util.class);
	private final IUserCredRepository userCredRepository;
	private final IUserDetailsRepository userDetailsRepository;

	@Autowired
	public Util(IUserCredRepository userCredRepository, IUserDetailsRepository userDetailsRepository) {
		this.userCredRepository = userCredRepository;
		this.userDetailsRepository = userDetailsRepository;
	}

	public static Date getDate(int monthDiff) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -monthDiff);
		Date result = cal.getTime();
		LOGGER.info("Current Date === > " + new Date());
		LOGGER.info("Previous Date === > " + result);
		return result;
	}

	public static String getMonthYearFormatted(YearMonth yearMonth, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return formatter.format(yearMonth);
	}

	/**
	 * @return current month and year in the format 'yyyy-MM'
	 */
	public static String getCurrentMonthYear() {
		YearMonth yearMonth = YearMonth.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
		return formatter.format(yearMonth);
	}

	/**
	 * @return previous month and year in the format 'yyyy-MM'
	 */
	public static String getPrevMonthYr(YearMonth currentYrMonth) {
		YearMonth yearMonth = currentYrMonth.minusMonths(1L);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
		return formatter.format(yearMonth);
	}

	public static int binarySearch(List<Long> list, long target) {
		int left = 0;
		int right = list.size() - 1;

		while (left <= right) {
			int mid = left + (right - left) / 2;
			long midValue = list.get(mid);

			if (midValue == target) {
				return mid; // Element found, return its index
			} else if (midValue < target) {
				left = mid + 1; // Target is in the right half
			} else {
				right = mid - 1; // Target is in the left half
			}
		}

		return -1; // Element not found
	}

	public static int daysInMonth(YearMonth yearMonth) {
		return yearMonth.lengthOfMonth();
	}

	public static int daysUntilToday(YearMonth yearMonth) {
		LocalDate today = LocalDate.now();
		LocalDate endOfMonth = yearMonth.atEndOfMonth();
		if (today.getYear() == yearMonth.getYear() && today.getMonth() == yearMonth.getMonth()) {
			return ((int) ChronoUnit.DAYS.between(yearMonth.atDay(1), today)) + 1;
		} else {
			return endOfMonth.getDayOfMonth();
		}
	}

	public static int daysUntilDate(YearMonth yearMonth, LocalDate date) {
		var firstDate = yearMonth.atDay(1);
		return (int) ChronoUnit.DAYS.between(firstDate, date) + 1;
	}

	public static LocalDate localDateFromDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()) // Use system's default time zone
				.toLocalDate();
	}

	public UserDetailsVo loggedInUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails principalUserDetails = (UserDetails) principal;
		if (null == principalUserDetails)
			throw new OjbException(ErrorCode.NOT_LOGGED_IN, new Object[]{});
		UserCredVo userCreds = userCredRepository.findByUsernameAlt(principalUserDetails.getUsername())
				.orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[]{"Logged In User"}));
		return userDetailsRepository.findByUserCred(userCreds.getId())
				.orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[]{"User Details"}));
	}
}
