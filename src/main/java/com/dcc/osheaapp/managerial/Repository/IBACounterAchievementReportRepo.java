package com.dcc.osheaapp.managerial.Repository;

import com.dcc.osheaapp.managerial.vo.BACounterAchievementReportVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBACounterAchievementReportRepo extends JpaRepository<BACounterAchievementReportVo, Long> {

    @Query(nativeQuery = true, value ="select bt.id target_id, o.outlet_name, o.outlet_code, ud.full_name ba_name, SUBSTRING_INDEX(uc.username, '@', 1) ba_code,\n" +
            "            bt.color_target_amount colorTargetAmount, bt.skin_target_amount skinTargetAmount, bt.skin_achieved skinAchieved,\n" +
            "            bt.color_achieved colorAchieved, (100 - ((((bt.color_target_amount + bt.skin_target_amount) - (bt.color_achieved + bt.skin_achieved))/(bt.color_target_amount + bt.skin_target_amount)) * 100))\n" +
            "            as cumulativePercentage\n" +
            "            from ba_target bt \n" +
            "            inner join outlet o on o.ID = bt.outlet_id \n" +
            "            inner join user_details ud on ud.id = bt.ba_id\n" +
            "            inner join user_credential uc on uc.id = ud.user_cred\n" +
            "            WHERE (100 - ((((bt.color_target_amount + bt.skin_target_amount) - (bt.color_achieved + bt.skin_achieved)) / (bt.color_target_amount + bt.skin_target_amount)) * 100)) >= :rangeType1 \n" +
            "  AND (100 - ((((bt.color_target_amount + bt.skin_target_amount) - (bt.color_achieved + bt.skin_achieved)) / (bt.color_target_amount + bt.skin_target_amount)) * 100)) < :rangeType2 \n" +
            "  AND bt.month = :currentMonth and ba_id in :baIds ")
    List<BACounterAchievementReportVo> fetchAcheivementCounterWise(String rangeType1, String rangeType2, List<Long> baIds, String currentMonth);
    @Query(nativeQuery = true, value ="select bt.id target_id, o.outlet_name, o.outlet_code, ud.full_name ba_name, SUBSTRING_INDEX(uc.username, '@', 1) ba_code,\n" +
            "            bt.color_target_amount colorTargetAmount, bt.skin_target_amount skinTargetAmount, bt.skin_achieved skinAchieved,\n" +
            "            bt.color_achieved colorAchieved, (100 - ((((bt.color_target_amount + bt.skin_target_amount) - (bt.color_achieved + bt.skin_achieved))/(bt.color_target_amount + bt.skin_target_amount)) * 100))\n" +
            "            as cumulativePercentage\n" +
            "            from sync_achievement bt \n" +
            "            inner join outlet o on o.ID = bt.outlet_id \n" +
            "            inner join user_details ud on ud.id = bt.ba_id\n" +
            "            inner join user_credential uc on uc.id = ud.user_cred\n" +
            "            WHERE (100 - ((((bt.color_target_amount + bt.skin_target_amount) - (bt.color_achieved + bt.skin_achieved)) / (bt.color_target_amount + bt.skin_target_amount)) * 100)) >= :rangeType1 \n" +
            "  AND (100 - ((((bt.color_target_amount + bt.skin_target_amount) - (bt.color_achieved + bt.skin_achieved)) / (bt.color_target_amount + bt.skin_target_amount)) * 100)) < :rangeType2 \n" +
            "  AND bt.month = :currentMonth and bt.outlet_id in :bdeOutlets ")
    List<BACounterAchievementReportVo> fetchAchievementCounterWiseV2(String rangeType1, String rangeType2, List<Long> bdeOutlets, String currentMonth);
}
