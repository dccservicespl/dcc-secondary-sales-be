package com.dcc.osheaapp.report.baRank.controller;

public class BaRankReportInputDto {

        String yearMonth;
        Long zoneId;

        public Long getZoneId(){
            return  zoneId;
        }

        public  String getYearMonth(){
            return yearMonth;
        }
}
