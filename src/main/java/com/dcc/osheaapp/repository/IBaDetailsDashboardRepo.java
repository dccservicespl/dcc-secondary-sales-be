package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.BaDetailsDashboardVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IBaDetailsDashboardRepo extends JpaRepository<BaDetailsDashboardVo,Long>{


    @Query(nativeQuery = true, value = "select csm.created_by as id,o.id as outletId, o.outlet_code as outletCode ,format(sum(purchase_amount),2) as totalPurchase, format(sum(sale_amount),2)totalSale, " +
            "(" +
            " SELECT COUNT(*) " +
            "        FROM stock_entry " +
            "        WHERE outlet_id = csm.outlet_id " +
            "        AND stock_status = 'edit' " +
            "    ) AS editCount " +
            "from counter_stock_manage csm " +
            "inner join outlet o on o.id = csm.outlet_id " +
            " where csm.outlet_id= ?1")
    BaDetailsDashboardVo baAssoDetails(Long id);
}
