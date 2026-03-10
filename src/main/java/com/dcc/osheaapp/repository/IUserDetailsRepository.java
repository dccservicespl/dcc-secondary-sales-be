package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.leaderboard.domain.model.Leaderboard;
import com.dcc.osheaapp.vo.UserDetailsVo;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDetailsRepository extends JpaRepository<UserDetailsVo, Long> {


	List<UserDetailsVo> findByIsActive(Boolean isActive);

	@Query(nativeQuery = true, value = "select * from user_details where company_zone = :zoneId  and is_active= :isActive and user_Type = :userType")
	List<UserDetailsVo> findByIsActiveZone(Long zoneId, Boolean isActive,int userType);


	@Query(nativeQuery = true, value = "select * from user_details where company_zone = :zoneId and user_Type = :userType")
	List<UserDetailsVo> findByZone(Long zoneId,int userType);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update user_details set is_active = :isActive, release_date = null where id = :id  ")
	public int updateStatus(Long id, Boolean isActive);

	@Query(nativeQuery = true, value = "select * from user_details where user_type = :userType and is_active = :isActive ")
	List<UserDetailsVo> findUserByType(Long userType, Boolean isActive);

	@Query(nativeQuery = true, value = "CALL searchByinputLimit(:whereClause, :tableName, :limitStr);")
	List<UserDetailsVo> searchByInput(@Param("whereClause") String whereClause, @Param("tableName") String tableName,
			@Param("limitStr") String limitStr);

	@Query(nativeQuery = true, value = "CALL countTotalByinput(:whereClause, :tableName);")
	Long getTotalCountByInput(@Param("whereClause") String whereClause, @Param("tableName") String tableName);

	@Query(nativeQuery = true, value = "select * from user_details where user_cred = :id and is_active = :isActive ")
	UserDetailsVo findUserDetails(Long id, Boolean isActive);

	@Query(nativeQuery = true, value = "select * from user_details where user_cred = :id and is_active = :isActive ")
	Optional<UserDetailsVo> findUserDetailsAlt(Long id, Boolean isActive);

	@Query(nativeQuery = true, value = "select a.* from user_details a, user_assotiation_details b "
			+ "where a.id = b.assotiated_user_id and user_id = :userId and a.is_active = :isActive and b.is_active = :isActive ")
	List<UserDetailsVo> findAssotiatedUser(Long userId, Boolean isActive);



	@Query(nativeQuery = true, value = "SELECT (ifnull(count(id), 0) + 1) as num FROM user_details "
			+ "where user_type = :userType and product_division = :productDivision")
	int countUser(Long userType, Long productDivision);

	@Query(nativeQuery = true, value = "SELECT (ifnull(count(id), 0) + 1) as num FROM user_details "
			+ "where product_division = :productDivision")
	int countSalesTeamUser(Long productDivision);

//	@Query(nativeQuery = true, value = "select a.* FROM "
//			+ "user_details a, user_assotiation_details b, user_assotiation_details c "
//			+ "where a.id = c.assotiated_user_id and c.user_id = b.assotiated_user_id and b.user_id = :userId "
//			+ "and a.is_active = true and b.is_active = true and b.is_active = true " + "UNION "
//			+ "select * from user_details where user_type = 1 and is_active = true ")
//	List<UserDetailsVo> findUserForEditApproval(Long userId);

	@Query(nativeQuery = true, value = "select a.* FROM "
			+ "user_details a, user_assotiation_details b "
			+ "where a.id = b.assotiated_user_id and b.user_id = :userId "
			+ "and a.is_active = true and b.is_active = true and a.user_type=12 " + "UNION "
			+ "select * from user_details where user_type = 1 and is_active = true ")
	List<UserDetailsVo> findUserForEditApproval(Long userId);


	@Query(nativeQuery = true, value = "select * from user_details where user_cred = ?1")
	Optional<UserDetailsVo> findByUserCred(Long id);

	Optional<UserDetailsVo> findByIdAndIsActive(Long id, boolean isActive);

	Optional<UserDetailsVo> findByIdAndIsActiveAndUserType(Long id, boolean isActive, String userType);

	@Query(nativeQuery = true, value = "select * from user_details where full_name =:fullName and user_type = :userType and is_active = :isActive ")
	UserDetailsVo findUserDetailsByNameAndType(String fullName, Long userType, Boolean isActive);

	@Query(nativeQuery = true, value = "select * from user_details where contact_number =:contactNumber ")
	UserDetailsVo findExistingContactNoOfUser(String contactNumber);

	@Query(nativeQuery = true, value = "select contact_number from user_details where contact_number =:contactNumber ")
	Optional<String> findExistingContactNumber(String contactNumber);

	@Query(nativeQuery = true, value = "select * from user_details where contact_number =:contactNumber ")
	List<UserDetailsVo> findByContactNumber(String contactNumber);

	@Query(nativeQuery = true, value = "select * from user_details where is_active = ?1 and user_type = ?2")
	List<UserDetailsVo> findByIsActiveAndUserType(boolean isActive, Long userType);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update user_details set is_active = false, "
			+ "release_date = :releaseDate, updated_by = :loginUser, updated_on = :updatedOn " + "where id = :id ")
	public int updateReleaseStatus(Long id, Date releaseDate, Long loginUser, Date updatedOn);
	
	@Query(nativeQuery = true, value = "select ud.id from user_details ud, user_credential uc "
			+ "where ud.user_cred = uc.id and username like '%:username%' and is_active = :isActive ")
	Long ifUserExists(String username, Boolean isActive);

	@Query(nativeQuery = true, value = "select count(*) from user_details where user_type = 4 and is_active = 1 ")
	Long baCount();



	@Query(nativeQuery = true, value = "select count(*) from user_details where is_active = 1 and user_type = 4 and company_zone = 8 ")
	Long baEastCount();
	@Query(nativeQuery = true, value = "select count(*) from user_details where is_active = 1 and user_type = 4 and company_zone = 9 ")
	Long baWestCount();
	@Query(nativeQuery = true, value = "select count(*) from user_details where is_active = 1 and user_type = 4 and company_zone = 10 ")
	Long baNorthCount();
	@Query(nativeQuery = true, value = "select count(*) from user_details where is_active = 1 and user_type = 4 and company_zone = 11 ")
	Long baSouthCount();

	@Query(nativeQuery = true, value = "select full_name from user_details where Id = :id")
	String getFullName ( String id);


	@Query(nativeQuery = true, value = "select * from user_details where user_type = ?1")
    List<UserDetailsVo> findByUserType(long type);


	@Query(nativeQuery = true, value = "select * from user_details where user_cred = ?1")
	UserDetailsVo findByCreadId(Long id);
	
	@Query(nativeQuery = true, value = "select * from user_details where id = " +
			"(select assotiated_user_id from user_assotiation_details where user_id = :soId and is_active=1)")
	List<UserDetailsVo> findSrUsers(Long soId);

	@Query(nativeQuery = true, value = "select a.* FROM user_details a, user_assotiation_details b " +
			"where a.id = b.assotiated_user_id and b.user_id = :loginUserId " +
			"and a.is_active = true and b.is_active = true " +
			"UNION select * from user_details where user_type = 1 and is_active = true;")
    List<UserDetailsVo> findUserForTourPlanApproval(Long loginUserId);

	@Query(nativeQuery = true,value="select * from user_details where id = ?1")
	UserDetailsVo bdeDetails(Long id);
}
