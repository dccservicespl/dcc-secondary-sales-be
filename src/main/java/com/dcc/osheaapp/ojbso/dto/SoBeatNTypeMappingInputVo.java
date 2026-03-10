package com.dcc.osheaapp.ojbso.dto;

import java.util.ArrayList;
import java.util.List;

import com.dcc.osheaapp.ojbso.vo.SoBeatNTypeMappingVo;

public class SoBeatNTypeMappingInputVo {
	List<SoBeatNTypeMappingVo> beatTypeMappingList = new ArrayList<>();

	public List<SoBeatNTypeMappingVo> getBeatTypeMappingList() {
		return beatTypeMappingList;
	}

	public void setBeatTypeMappingList(List<SoBeatNTypeMappingVo> beatTypeMappingList) {
		this.beatTypeMappingList = beatTypeMappingList;
	}
}
