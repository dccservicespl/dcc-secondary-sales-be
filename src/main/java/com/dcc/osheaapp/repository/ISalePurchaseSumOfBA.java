package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.views.PurchaseSaleOutputVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISalePurchaseSumOfBA extends JpaRepository<PurchaseSaleOutputVo,Long> {

    @Query(nativeQuery = true, value = "select row_number() over() as id, sum(purchase_amount) as totalPurchase , sum(sale_amount) as totalSale , outlet_id as outletId" +
            " from counter_stock_manage where Date(transaction_date) between ?1 and ?2 and outlet_id = ?3")
    PurchaseSaleOutputVo getSalePurchaseSum(String formDate, String toDate, Long outletId);
}
