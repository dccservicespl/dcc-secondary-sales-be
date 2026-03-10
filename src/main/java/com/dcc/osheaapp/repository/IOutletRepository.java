package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.OutletVo;
import java.util.List;
import javax.transaction.Transactional;

import com.dcc.osheaapp.vo.UserCredVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IOutletRepository extends JpaRepository<OutletVo, Long> {

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update outlet set is_active = :isActive where id = :id ")
	public int updateStatus(Long id, Boolean isActive);

	@Query(nativeQuery = true, value = "CALL searchByinputLimit(:whereClause, :tableName, :limitStr);")
	List<OutletVo> searchByInput(@Param("whereClause") String whereClause, @Param("tableName") String tableName,
			@Param("limitStr") String limitStr);

	@Query(nativeQuery = true, value = "CALL countTotalByinput(:whereClause, :tableName);")
	Long getTotalCountByInput(@Param("whereClause") String whereClause, @Param("tableName") String tableName);

	@Query(nativeQuery = true, value = "select a.* from outlet a, outlet_user_mapping b "
			+ "where a.id = b.outlet_id and assotiated_user_id = :userId and a.is_active = :isActive and b.is_active = :isActive ")
	List<OutletVo> findAssotiatedOutlet(Long userId, Boolean isActive);

	@Query(nativeQuery = true, value = "select * from outlet where outlet_code = :outletCode")
	OutletVo getIDByOutletCode(String outletCode);

	@Query(nativeQuery = true, value = "select a.* from outlet a, outlet_user_mapping b "
			+ "where a.id = b.outlet_id and assotiated_user_id = :userId")
	List<OutletVo> findAllAssotiatedOutlet(Long userId);

	@Query(nativeQuery = true, value = "select * from outlet where is_active = 1")
	List<OutletVo> findActiveOutlets();

	@Query(nativeQuery = true, value = "select * from outlet where company_zone = :zoneId  and is_active= :isActive")

	List<OutletVo> findActiveOutletsZone(Long zoneId, Boolean isActive);
	@Query(nativeQuery = true, value = "select * from outlet where company_zone = :zoneId ")

	List<OutletVo> findActiveOutletsZone1(Long zoneId); // for sale report
	
	@Query(nativeQuery = true, value = "select ID from outlet where outlet_code = :outletCode and is_active = 1")
	Long findIfExists(String outletCode);


    List<OutletVo> findByBeatAndCompanyZone(Long beat, Long companyZone);

//    OutletVo fetchOutletDetails(Long outletId);
//@Query(nativeQuery = true, value = "select *\n" +
//			" from outlet o\n" +
//			" inner join beat_name_mst bnm on bnm.id = o.beat\n" +
//			" inner join user_details ud on ud.id = bnm.so_id\n" +
//			" where so_id=:soId ;")
//	List<OutletVo> 	findAllOutletBySoId(Long soId);

	@Query(nativeQuery = true,value=
			"select * from outlet o " +
			" inner join so_order so on so.outlet_id=o.id " +
			" where so.so_id = :soId  and DATE_FORMAT(so.created_on,'%Y-%m-%d') = :currDate ")
	List<OutletVo> 	findAllOutletBySoId(Long soId,String currDate);
}
