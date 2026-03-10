package com.dcc.osheaapp.ojbso.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dcc.osheaapp.ojbso.dto.SOActivitySummaryDtlDto;

@Repository
public interface ISOActivitySummaryDtlRepo extends CrudRepository<SOActivitySummaryDtlDto, Long>{

	@Query(nativeQuery = true, value = "SELECT ROW_NUMBER() OVER() + 10000 AS id , category_name desciption, 0 as productMRP, "
			+ "sum(no_of_product) orderNetQty, sum(amount_of_product) orderNetValue  "
            + "FROM so_order_dtl where order_id in :orderId group by category_name ")
    List<SOActivitySummaryDtlDto> findCatDtl(List<Long> orderId);
	
	@Query(nativeQuery = true, value = "SELECT ROW_NUMBER() OVER() + 1000 AS id , sub_category_name desciption, 0 as productMRP, "
			+ "sum(no_of_product) orderNetQty, sum(amount_of_product) orderNetValue  "
            + "FROM so_order_dtl where order_id in :orderId group by sub_category_name ")
    List<SOActivitySummaryDtlDto> findSubCatDtl(List<Long> orderId);
	
	@Query(nativeQuery = true, value = "SELECT pr.id id , pr.product_name desciption, sod.product_mrp productMRP, "
			+ "sum(no_of_product) orderNetQty, sum(amount_of_product) orderNetValue "
			+ "FROM so_order_dtl sod, product pr where pr.id = sod.product_id and order_id in :orderId group by sod.product_id ")
    List<SOActivitySummaryDtlDto> findSKUDtl(List<Long> orderId);
}
