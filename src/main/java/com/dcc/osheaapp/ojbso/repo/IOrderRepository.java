package com.dcc.osheaapp.ojbso.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dcc.osheaapp.ojbso.vo.OrderVo;

@Repository
public interface IOrderRepository extends JpaRepository<OrderVo, Long> {

	@Query(nativeQuery = true, value = "select * from so_order where "
            + "so_id = :soId AND outlet_id = :outletId order by updated_on desc limit 5 ")
    List<OrderVo> findLastOrdersOfUser(Long soId, Long outletId);
	
	@Query(nativeQuery = true, value = "CALL searchByinputLimit(:whereClause, :tableName, :limitStr);")
	List<OrderVo> searchByInput(@Param("whereClause") String whereClause, @Param("tableName") String tableName,
			@Param("limitStr") String limitStr);

	@Query(nativeQuery = true, value = "CALL countTotalByinput(:whereClause, :tableName);")
	Long getTotalCountByInput(@Param("whereClause") String whereClause, @Param("tableName") String tableName);
}
