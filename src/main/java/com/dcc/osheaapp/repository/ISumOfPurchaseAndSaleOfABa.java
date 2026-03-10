package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.OutletUserMappingVo;
import com.dcc.osheaapp.vo.SumOfTotalPurchaseAndSaleOutputVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISumOfPurchaseAndSaleOfABa extends JpaRepository<SumOfTotalPurchaseAndSaleOutputVo, Long> {


    @Query(nativeQuery = true, value = "select u.id as Id, u.full_name as fullName , uad.assotiated_user_id as bdeId, oum.outlet_id as outletId, o.outlet_code as outletCode, " +
            "   FORMAT(SUM(csm.purchase_amount), 2) AS totalPurchase, " +
            "    FORMAT(SUM(csm.sale_amount), 2) AS totalSale,  csm.created_on as CreatedOn " +
            "  from user_assotiation_details uad " +
            "  inner JOIN  user_details u on uad.user_id = u.id " +
            "inner JOIN outlet_user_mapping oum on oum.assotiated_user_id = uad.user_id " +
            "inner JOIN outlet o on oum.outlet_id = o.id " +
            "left join counter_stock_manage csm on csm.created_by = uad.user_id " +
            "where uad.assotiated_user_id= ?2 and u.is_active=true and uad.is_active=true and DATE_FORMAT(csm.created_on, '%Y-%m') = ?1 " +
            "group by Id ")
    List<SumOfTotalPurchaseAndSaleOutputVo> getSumOfPurchaseAndSaleOfBA(String monYr, Long bdeId);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS resultCount FROM(select u.id as Id, u.full_name as fullName ,uad.assotiated_user_id as bdeId, oum.outlet_id as outletId, o.outlet_code as outletCode, " +
            "   FORMAT(SUM(csm.purchase_amount), 2) AS totalPurchase, " +
            "    FORMAT(SUM(csm.sale_amount), 2) AS totalSale, csm.created_on as CreatedOn " +
            "  from user_assotiation_details uad " +
            "  inner JOIN  user_details u on uad.user_id = u.id " +
            "inner JOIN outlet_user_mapping oum on oum.assotiated_user_id = uad.user_id " +
            "inner JOIN outlet o on oum.outlet_id = o.id " +
            "left join counter_stock_manage csm on csm.created_by = uad.user_id " +
            "where uad.assotiated_user_id= ?1 and u.is_active=true and uad.is_active=true and DATE_FORMAT(csm.created_on, '%Y-%m') = ?2 " +
            "group by Id ) as result")
    int countSumOfPurchaseAndSaleOfBA(Long beId,String monYr);





//    @Query(nativeQuery = true, value = "SELECT row_number() over() as id ,uad.assotiated_user_id as bdeId, " +
//            "                  FORMAT(SUM(csm.purchase_amount), 2) AS totalPurchase, " +
//            "                 FORMAT(SUM(csm.sale_amount), 2) AS totalSale, " +
//            "                 DATE_FORMAT(?2, '%Y-%m-%d') AS startDate," +
//            " DATE_FORMAT(?3, '%Y-%m-%d') AS endDate " +
//            "            FROM  " +
//            "                user_assotiation_details uad " +
//            "                INNER JOIN user_details u ON uad.user_id = u.id " +
//            " INNER JOIN outlet_user_mapping oum ON oum.assotiated_user_id = uad.user_id " +
//            "                LEFT JOIN counter_stock_manage csm ON csm.outlet_id = oum.outlet_id " +
//            "            WHERE   " +
//            "                uad.assotiated_user_id = ?1 and " +
//            "                uad.is_active = true " +
//            " and date_format(csm.transaction_date, '%Y-%m-%d') between ?2 and ?3 " +
//            " ")
@Query(nativeQuery = true, value = "SELECT row_number() OVER() AS id, :bdeId AS bdeId, " +
        "FORMAT(SUM(purchase_amount), 2) AS totalPurchase, " +
        "FORMAT(SUM(sale_amount), 2) AS totalSale, " +
        "DATE_FORMAT(:startDate, '%Y-%m-%d') AS startDate, " +
        "DATE_FORMAT(:endDate, '%Y-%m-%d') AS endDate " +
            "FROM counter_stock_manage " +
        "WHERE outlet_id IN ( " +
        "    SELECT id FROM outlet " +
        "    WHERE id IN ( " +
        "        SELECT DISTINCT outlet_id " +
        "        FROM outlet_user_mapping " +
        "        WHERE assotiated_user_id IN ( " +
        "            SELECT user_id " +
        "            FROM user_assotiation_details " +
        "            WHERE assotiated_user_id = :bdeId AND is_active = true " +
        "        ) " +
        "        AND is_active = true " +
        "    ) " +
        "    AND is_active = true " +
        ") " +
        "AND DATE_FORMAT(transaction_date, '%Y-%m-%d') BETWEEN :startDate AND :endDate")
SumOfTotalPurchaseAndSaleOutputVo getTotalPurchaseAndSaleOfBa(Long bdeId,String startDate, String endDate);



    //Prev data come from wrong table
//    @Query(nativeQuery = true, value ="  SELECT\n" +
//            "    ROW_NUMBER() OVER() AS id, :bdeId AS bdeId,\n" +
//            "    FORMAT(SUM(CASE WHEN se.activity_type = 'Purchase' THEN sed.amount ELSE 0 END), 2) AS totalPurchase,\n" +
//            "    FORMAT(SUM(CASE WHEN se.activity_type = 'Sale' THEN sed.amount ELSE 0 END), 2) AS totalSale,\n" +
//            "    DATE_FORMAT(:startDate, '%Y-%m-%d') AS startDate,\n" +
//            "    DATE_FORMAT(:endDate, '%Y-%m-%d') AS endDate\n" +
//            "    FROM stock_entry se\n" +
//            "    INNER JOIN stock_entry_dtl sed ON sed.stock_entry_id = se.id\n" +
//            "            WHERE\n" +
//            "    se.outlet_id IN (\n" +
//            "            SELECT id FROM outlet WHERE id IN (\n" +
//            "            SELECT DISTINCT outlet_id FROM outlet_user_mapping WHERE assotiated_user_id IN (\n" +
//            "            SELECT user_id FROM user_assotiation_details WHERE assotiated_user_id = :bdeId AND is_active = true\n" +
//            "    )\n" +
//            "    AND is_active = true \n" +
//            "              )\n" +
//            "    AND is_active = true\n" +
//            "          )\n" +
//            "    AND DATE_FORMAT(se.transaction_date, '%Y-%m-%d') BETWEEN :startDate AND :endDate ")
//            SumOfTotalPurchaseAndSaleOutputVo getTotalPurchaseAndSaleOfBa(Long bdeId,String startDate, String endDate);
}
