package com.dcc.osheaapp.managerial.Repository;

import com.dcc.osheaapp.managerial.vo.LeftBaOfBdeAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILeftBaAchivementOfBde extends JpaRepository<LeftBaOfBdeAchievement, Long> {

    @Query(nativeQuery = true, value ="select sum(amount) as totalSum, a.created_by as createdBy, ud.full_name as fullName,ud.release_date as releaseDate,  \n" +
            "(select outlet_name from outlet o, outlet_user_mapping uom \n" +
            "where o.id = uom.outlet_id and uom.assotiated_user_id = ud.id \n" +
            "and uom.is_active = false and uom.left_on like :monYr order by uom.id desc limit 1) outletName,\n" +
            "(select CAST(skin_target_amount AS SIGNED) + CAST(color_target_amount AS SIGNED) from ba_target where ba_id = ud.id and created_on like :monYr\n" +
            ") target\n" +
            "from stock_entry a, stock_entry_dtl b , user_details ud \n" +
            "where a.id = b.stock_entry_id and a.created_by = ud.id \n" +
            "and activity_type = 'sale' and ud.is_active = false \n" +
            "and transaction_date like :monYr and ud.release_date like :monYr \n" +
            "and a.created_by in (select user_id from user_assotiation_details uad where assotiated_user_id = :bdeId and is_active = false) \n" +
            "group by a.created_by ")
    List<LeftBaOfBdeAchievement> getLeftBaAchievement(Long bdeId, String monYr);
}
