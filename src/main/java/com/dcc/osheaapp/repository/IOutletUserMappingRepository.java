package com.dcc.osheaapp.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.dcc.osheaapp.report.attendance.model.OutletMappingView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dcc.osheaapp.vo.OutletUserMappingVo;
import com.dcc.osheaapp.vo.views.OutletUserMappingView;
import com.dcc.osheaapp.vo.views.OutletUserView;

@Repository
public interface IOutletUserMappingRepository extends JpaRepository<OutletUserMappingVo, Long> {

	List<OutletUserMappingVo> findByAssotiatedUserAndIsActive(Long assotiatedUser, Boolean isActive);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update outlet_user_mapping " + "set is_active = :isActive, left_on = :leftOn "
			+ "where assotiated_user_id = :assotiatedUser and outlet_id in :outletIds ")
	public int updateInactiveStatus(Boolean isActive, Date leftOn, Long assotiatedUser, List<Long> outletIds);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update outlet_user_mapping " + "set is_active = false, left_on = :releaseDate "
			+ "where assotiated_user_id = :assotiatedUser  ORDER BY id DESC LIMIT 1")
	public int updateReleaseStatus(Long assotiatedUser, Date releaseDate);

	List<OutletUserMappingVo> findByOutletIdAndIsActive(Long outletId, boolean b);

	@Query(nativeQuery = true, value = "select * from outlet_user_mapping where outlet_id = ?1 and assotiated_user_id = ?2 and is_active = ?3")
	List<OutletUserMappingVo> findByOutletIdAndAssotiatedUserAndIsActive(Long outletId, Long userId, boolean b);

	@Query(nativeQuery = true, value = "select * from outlet_user_mapping where outlet_id = :outletId ")
	List<OutletUserMappingVo> findExistingAssociatedOutlet(Long outletId);

	@Query(nativeQuery = true, value = "select * from outlet_user_mapping where outlet_id = :outletId and is_active = true")
	List<OutletUserMappingVo> findActiveExistingAssociatedOutlet(Long outletId);
	@Query(nativeQuery = true, value = "select * from outlet_user_mapping where assotiated_user_id = :userId and is_active = true ")
	List<OutletUserMappingVo> findAssociatedOutletByUserId(Long userId);

	@Query(nativeQuery = true, value = "select\n" + "\tud.full_name as baName,\n" + "\tud.ID as baId,\n"
			+ "\tuc.username as baCode,\n" + "\to.ID as outletId,\n" + "\to.outlet_name as outletName,\n"
			+ "\to.outlet_code as outletCode\n" + "from\n" + "\toutlet_user_mapping oum\n"
			+ "inner join user_details ud\n" + "on\n" + "\tud.ID = oum.assotiated_user_id \n" + "inner join outlet o \n"
			+ "on \n" + "\to.ID  = oum.outlet_id \t\n" + "inner join user_credential uc \n" + "on\n"
			+ "\tud.user_cred = uc.ID \n" + "where ud.user_type = 4 and oum.is_active = 1 and ud.is_active = 1\n"
			+ "group by o.ID")
	List<OutletUserMappingView> findActiveOutletMappings();


	// This query for only active BA excel export
//	@Query(nativeQuery = true, value = "select\n" + "\tud.full_name as baName,\n" + "\tud.ID as baId,\n"
//			+ "\tuc.username as baCode,\n" + "\to.ID as outletId,\n" + "\to.outlet_name as outletName,\n"
//			+ "\to.outlet_code as outletCode\n" + "from\n" + "\toutlet_user_mapping oum\n"
//			+ "inner join user_details ud\n" + "on\n" + "\tud.ID = oum.assotiated_user_id \n" + "inner join outlet o \n"
//			+ "on \n" + "\to.ID  = oum.outlet_id \t\n" + "inner join user_credential uc \n" + "on\n"
//			+ "\tud.user_cred = uc.ID \n"
//			+ "where ud.user_type = 4 and oum.is_active = 1 and ud.is_active = 1 and ud.product_division = ?1 and ud.company_zone = ?2\n"
//			+ "group by o.ID")
//	List<OutletUserMappingView> findActiveOutletMappingsByDivision(Long divisionId, Long zone);


	 //Use for Active outlet on a specific zone and last associated user of that outlet
	@Query(nativeQuery = true, value = "select ud.full_name as baName,ud.ID as baId,\n" +
			" uc.username as baCode,o.ID as outletId,o.outlet_name as outletName,\n" +
			" o.outlet_code as outletCode \n" +
			" from outlet o\n" +
			" inner join outlet_user_mapping oum  on oum.outlet_id  = o.ID \n" +
			" AND oum.id = (SELECT MAX(id) FROM outlet_user_mapping WHERE outlet_id = o.ID)\n" +
			" inner join user_details ud on ud.Id = oum.assotiated_user_id \n" +
			" inner join user_credential uc on ud.user_cred = uc.ID \n" +
			" where o.is_active =1  and o.company_zone =?2 and ud.product_division = ?1")
	List<OutletUserMappingView> findActiveOutletMappingsByDivision(Long divisionId, Long zone);

	@Query(nativeQuery = true, value = "SELECT a.full_name as baName, SUBSTRING_INDEX(c.username, '@', 1) AS baCode, a.id as baId "
			+ "FROM user_details a " + "JOIN outlet_user_mapping b ON b.assotiated_user_id = a.id "
			+ "JOIN user_credential c ON c.id = a.user_cred " + "WHERE b.outlet_id = ?1 ")
	List<OutletUserMappingView> findByoutletId(Long outletId);

	@Query(nativeQuery = true, value = "select oum.id associationId, oum.assotiated_user_id associatedUserId, ud.full_name associatedUserFullName, uc.username associatedUserCode, oum.outlet_id outletId from outlet_user_mapping oum inner join user_details ud on ud.ID  = oum.assotiated_user_id inner join user_credential uc on uc.ID = ud.user_cred  where oum.outlet_id = ?1 and oum.is_active = true")
	List<OutletUserView> findAssociatedOutletListViewByUserId(Long outletId);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update outlet_user_mapping set is_active = 0 where id = ?1")
	int removeAssociationById(Long asscId);

	@Query(nativeQuery = true, value = "select  outlet_id  from outlet_user_mapping where assotiated_user_id = :userId and is_active = true ")
	Long findAssociatedOutletIdByUserId(Long userId);

	@Query(nativeQuery = true, value = "select  concat(ou.assotiated_user_id,',', o.ID,',', o.outlet_name)  from outlet_user_mapping ou  inner join outlet o on o.ID = ou.outlet_id "
			+ " where ou.assotiated_user_id in (:userId) and ou.is_active = true")
	List<String> findAssociatedOutletsIdByUserId(List<Long> userId);
	
	@Query(nativeQuery = true, value = "select ou.id as associationId,\n" +
			"       ou.assotiated_user_id as userId,\n" +
			"       o.ID as                  outletId,\n" +
			"       o.outlet_name as         outletName,\n" +
			"       o.outlet_code as      outletCode,\n" +
			" 		 dm.field_name as outletType\n" +
			"from outlet_user_mapping ou\n" +
			"        inner join outlet o on o.ID = ou.outlet_id\n" +
			"		inner join dropdown_mst dm on o.outlet_channel = dm.ID\n" +
			"where ou.assotiated_user_id in (?1)")
	List<OutletMappingView> findAssociatedOutletsIdByUserIdNew(List<Long> userId);

	@Query(nativeQuery = true, value = "select * \n" +
			"from outlet_user_mapping \n" +
			"where assotiated_user_id in (select user_id from user_assotiation_details where assotiated_user_id = :bdeId and \n" +
			"is_active = true) \n" +
			"and is_active = true")
	List<OutletUserMappingVo> getOutletMappingResult(Long bdeId);

	@Query(nativeQuery = true, value = "SELECT assotiated_user_id\n" +
			"FROM (\n" +
			"    SELECT assotiated_user_id, outlet_id,\n" +
			"           ROW_NUMBER() OVER (PARTITION BY outlet_id ORDER BY id DESC) AS row_num\n" +
			"    FROM outlet_user_mapping\n" +
			"    WHERE outlet_id IN (:outletId)\n" +
			") t\n" +
			"WHERE row_num = 1\n")
	List<Long> nonAssociatedOutletUserId(List<Long> outletId);

	@Query(nativeQuery = true, value = "SELECT assotiated_user_id\n" +
			"FROM (\n" +
			"    SELECT assotiated_user_id, outlet_id,\n" +
			"           ROW_NUMBER() OVER (PARTITION BY outlet_id ORDER BY id DESC) AS row_num\n" +
			"    FROM outlet_user_mapping\n" +
			"    WHERE outlet_id IN (:outletId)\n" +
			") t\n" +
			"WHERE row_num = 1\n")
	Long setChainOutletUserId(Long outletId);
}
