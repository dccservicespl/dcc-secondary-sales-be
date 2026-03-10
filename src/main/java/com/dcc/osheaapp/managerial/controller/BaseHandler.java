package com.dcc.osheaapp.managerial.controller;

import com.dcc.osheaapp.managerial.model.ActivityCountDto;
import com.dcc.osheaapp.managerial.service.ActivityEnum;
import com.dcc.osheaapp.managerial.service.ActivityService;
import com.dcc.osheaapp.managerial.service.BaUnderBDEService;
import com.dcc.osheaapp.vo.BaListOfABdeVo;
import com.dcc.osheaapp.vo.views.BaListOfABdeOutputVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseHandler {

    @Autowired
    BaUnderBDEService baUnderBDEService;

    @Autowired
    ActivityService activityService;

    public List<BaListOfABdeOutputVo> getBaList(BaListOfABdeVo input, ActivityEnum activity) {
	if (activity == null) {
	    return baUnderBDEService.baListUnderBde(input);
	} else if (activity == ActivityEnum.VACANT) {
		return activityService.findBaListForVacant(input, activity);

	}
		return activityService.findBaListByActivity(input, activity);
    }

    public List<ActivityCountDto> countBaList(BaListOfABdeVo input, ActivityEnum activity) {
	if (activity == null) {
	    return List.of(new ActivityCountDto(null, baUnderBDEService.countBaUnderBde(input)),
		    new ActivityCountDto(ActivityEnum.LEAVE,
			    (long) activityService.findBaListByActivity(input, ActivityEnum.LEAVE).size()),
		    new ActivityCountDto(ActivityEnum.PRESENT,
			    (long) activityService.findBaListByActivity(input, ActivityEnum.PRESENT).size()),
		    new ActivityCountDto(ActivityEnum.ABSENT,
			    (long) activityService.findBaListByActivity(input, ActivityEnum.ABSENT).size()));
	}
	return List.of(
		new ActivityCountDto(activity, (long) activityService.findBaListByActivity(input, activity).size()));
    }
}
