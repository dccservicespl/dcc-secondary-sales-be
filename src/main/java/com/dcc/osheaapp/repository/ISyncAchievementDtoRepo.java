package com.dcc.osheaapp.repository;


import com.dcc.osheaapp.vo.SyncAchievementDto;
import com.dcc.osheaapp.vo.views.PocketMISDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISyncAchievementDtoRepo extends JpaRepository<SyncAchievementDto, Long> {

    @Query(
            nativeQuery = true,
            value = "select ROW_NUMBER() over() as id , se.user_id as ba_Id, sed.category_name as categoryName,\n" +
                    "sum(sed.amount) as totalSaleAmount,  se.outlet_id as outlet_Id\n" +
                    "from stock_entry se\n" +
                    "inner join stock_entry_dtl sed on se.id = sed.stock_entry_id\n" +
                    "where DATE_FORMAT(ifnull(se.transaction_date, se.updated_on),'%Y-%m') =?1 and se.activity_type = \"Sale\" and se.outlet_id  in (?2)\n" +
                    " group by categoryName,ba_Id order by outlet_id" )
    List<SyncAchievementDto> findByUsersAndMonth(String monyr, List<Long> user);

    @Query(
            nativeQuery = true,
            value = "select ROW_NUMBER() over() as id , se.user_id as ba_Id, sed.category_name as categoryName,\n" +
                    "sum(sed.amount) as totalSaleAmount,  se.outlet_id as outlet_Id\n" +
                    "from stock_entry se\n" +
                    "inner join stock_entry_dtl sed on se.id = sed.stock_entry_id\n" +
                    "where DATE_FORMAT(ifnull(se.transaction_date, se.updated_on),'%Y-%m') =?1 and se.activity_type = \"sale_return\" and se.outlet_id  in (?2)\n" +
                    " group by categoryName,ba_Id order by outlet_id" )
    List<SyncAchievementDto> findByUsersAndMonthSaleReturn(String monyr, List<Long> user);
}
