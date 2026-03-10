package com.dcc.osheaapp.managerial.service;

import com.dcc.osheaapp.common.model.Constants;
import com.dcc.osheaapp.repository.IBaUnderABdeRepository;
import com.dcc.osheaapp.repository.IUserActivityRepository;
import com.dcc.osheaapp.vo.BaListOfABdeVo;
import com.dcc.osheaapp.vo.UserActivityRegisterVo;
import com.dcc.osheaapp.vo.views.BaListOfABdeOutputVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    @Autowired
    BaUnderBDEService baUnderBDEService;

    @Autowired
    IUserActivityRepository userActivityRepository;

	@Autowired
	IBaUnderABdeRepository iBaUnderABdeRepository;

    public static Predicate<List<UserActivityRegisterVo>> isPresent = e -> e.stream()
	    .anyMatch(f -> ActivityEnum.PRESENT.getActivities().contains(f.getActivityType()));
    public static Predicate<List<UserActivityRegisterVo>> isLeave = e -> e.stream()
	    .anyMatch(f -> ActivityEnum.LEAVE.getActivities().contains(f.getActivityType()));
    public static Predicate<List<UserActivityRegisterVo>> isAbsent = List::isEmpty;

    public List<BaListOfABdeOutputVo> findBaListByActivity(BaListOfABdeVo input, ActivityEnum activity) {
	if (activity == null)
	    return new ArrayList<>();

	List<BaListOfABdeOutputVo> baWithActivityList = findBaListByActivity(input, activity.getActivities());
		System.out.println("Total BA with activity " + activity + ": " + baWithActivityList.size());

	if (activity.compareTo(ActivityEnum.LEAVE) == 0)
	    return baWithActivityList.parallelStream()
				.sorted(Comparator.comparing(BaListOfABdeOutputVo::getOutletName))
				.filter(f -> isLeave.test(f.getActivities()))
		    .map(e -> e.setActivities(new ArrayList<>())).collect(Collectors.toList());
	if (activity.compareTo(ActivityEnum.PRESENT) == 0)
	    return baWithActivityList.parallelStream()
	    		.sorted(Comparator.comparing(BaListOfABdeOutputVo::getOutletName))
	    		.filter(f -> isPresent.test(f.getActivities()))
		    .map(e -> e.setActivities(new ArrayList<>())).collect(Collectors.toList());
	if (activity.compareTo(ActivityEnum.ABSENT) == 0)
	    return baWithActivityList.parallelStream()
				.sorted(Comparator.comparing(BaListOfABdeOutputVo::getOutletName))
				.filter(f -> isAbsent.test(f.getActivities()))
		    .map(e -> e.setActivities(new ArrayList<>())).collect(Collectors.toList());
	return new ArrayList<>();

    }

    public List<BaListOfABdeOutputVo> findBaListByActivity(BaListOfABdeVo input,
	    List<Constants.BA_Activity_Enum> filters) {

	List<BaListOfABdeOutputVo> totalBa = baUnderBDEService.baListUnderBde(input);
	List<UserActivityRegisterVo> activities = new ArrayList<>();
		List<UserActivityRegisterVo> activities1 = new ArrayList<>();
	List<BaListOfABdeOutputVo> results = new ArrayList<>();

	if (filters.isEmpty()) {
	    activities = userActivityRepository.findByActivityTypeAndDateAndCreatedBy(
		    ActivityEnum.PRESENT.getActivities().stream().map(Enum::name).collect(Collectors.toList()),
		    input.getDate(), totalBa.stream().map(BaListOfABdeOutputVo::getId).collect(Collectors.toList()));
		activities1 =  userActivityRepository.findByActivityTypeAndDateAndCreatedBy(
				ActivityEnum.LEAVE.getActivities().stream().map(Enum::name).collect(Collectors.toList()),
				input.getDate(), totalBa.stream().map(BaListOfABdeOutputVo::getId).collect(Collectors.toList()));
//		List<Long> distinctUsers = activities.stream().map(UserActivityRegisterVo::getCreatedBy).distinct().collect(Collectors.toList());
//	    totalBa.forEach(e -> {
//			if(!distinctUsers.contains(e.getId()))
//				results.add(e);
//	    });
		Set<Long> distinctUsers = new HashSet<>();
		distinctUsers.addAll(activities.stream().map(UserActivityRegisterVo::getCreatedBy).collect(Collectors.toList()));
		distinctUsers.addAll(activities1.stream().map(UserActivityRegisterVo::getCreatedBy).collect(Collectors.toList()));

		// Filter totalBa to include only those not in distinctUsers
		totalBa.forEach(e -> {
			if (!distinctUsers.contains(e.getId())) {
				results.add(e);
			}
		});
	}
//	else {
//		if(filters.size() == 3){
//			activities = userActivityRepository.findByActivityTypeAndDateAndCreatedBy(
//					ActivityEnum.PRESENT.getActivities().stream().map(Enum::name).collect(Collectors.toList()),
//					input.getDate(), totalBa.stream().map(BaListOfABdeOutputVo::getId).collect(Collectors.toList()));
//
//			List<Long> distinctUsers = activities.stream().map(UserActivityRegisterVo::getCreatedBy).distinct().collect(Collectors.toList());
//			activities.forEach(e -> {
//				BaListOfABdeOutputVo user = totalBa.parallelStream().filter(f -> f.getId().equals(e.getCreatedBy()))
//						.findAny().orElseThrow(() -> new IllegalArgumentException("No Ba found."));
//				user.getActivities().add(e);
//				results.add(user);
//			});
//
//		}
//		activities = userActivityRepository.findByActivityTypeAndDateAndCreatedBy(
//				ActivityEnum.LEAVE.getActivities().stream().map(Enum::name).collect(Collectors.toList()),
//				input.getDate(), totalBa.stream().map(BaListOfABdeOutputVo::getId).collect(Collectors.toList()));
//
//		List<Long> distinctUsers = activities.stream().map(UserActivityRegisterVo::getCreatedBy).distinct().collect(Collectors.toList());
//		activities.forEach(e -> {
//			BaListOfABdeOutputVo user = totalBa.parallelStream().filter(f -> f.getId().equals(e.getCreatedBy()))
//					.findAny().orElseThrow(() -> new IllegalArgumentException("No Ba found."));
//			user.getActivities().add(e);
//			results.add(user);
//		});
//
//	}

	else if(filters.size() == 4) {
	    activities = userActivityRepository.findByActivityTypeAndDateAndCreatedBy(
		    ActivityEnum.LEAVE.getActivities().stream().map(Enum::name).collect(Collectors.toList()),
		    input.getDate(), totalBa.stream().map(BaListOfABdeOutputVo::getId).collect(Collectors.toList()));

		List<Long> distinctUsers = activities.stream().map(UserActivityRegisterVo::getCreatedBy).distinct().collect(Collectors.toList());
	    activities.forEach(e -> {
		BaListOfABdeOutputVo user = totalBa.parallelStream().filter(f -> f.getId().equals(e.getCreatedBy()))
			.findAny().orElseThrow(() -> new IllegalArgumentException("No Ba found."));
		user.getActivities().add(e);
		results.add(user);
	    });

	}
	else{
		activities = userActivityRepository.findByActivityTypeAndDateAndCreatedBy(
				ActivityEnum.PRESENT.getActivities().stream().map(Enum::name).collect(Collectors.toList()),
				input.getDate(), totalBa.stream().map(BaListOfABdeOutputVo::getId).collect(Collectors.toList()));

		List<Long> distinctUsers = activities.stream().map(UserActivityRegisterVo::getCreatedBy).distinct().collect(Collectors.toList());
		activities.forEach(e -> {
			BaListOfABdeOutputVo user = totalBa.parallelStream().filter(f -> f.getId().equals(e.getCreatedBy()))
					.findAny().orElseThrow(() -> new IllegalArgumentException("No Ba found."));
			user.getActivities().add(e);
			results.add(user);
		});

	}
	return results;
    }

	public List<BaListOfABdeOutputVo> findBaListForVacant(BaListOfABdeVo input, ActivityEnum activity){
		List<BaListOfABdeOutputVo> baWithActivityList = iBaUnderABdeRepository.searchBaListForVacant(input.getId(),input.getDate());
		return  baWithActivityList;

	}

}
