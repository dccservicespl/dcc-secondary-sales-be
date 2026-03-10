package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.views.BaListOfABdeOutputVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBaUnderABdeRepository extends JpaRepository<BaListOfABdeOutputVo, Long> {
    @Query(nativeQuery = true, value = "CALL searchBaListUnderBde(:whereClause, :limitStr)")
    List<BaListOfABdeOutputVo> searchBaListUnderBde (@Param("whereClause") String whereClause, @Param("limitStr") String limitStr);

    @Query(nativeQuery = true, value = "CALL countBaListUnderBde(:whereClause) ")
    Long countBaListUnderBde(String whereClause);

    @Query(nativeQuery = true, value = "CALL searchBaIdListUnderBde(:whereClause)")
    List<Long> fetchBaIdListUnderBde (@Param("whereClause") String whereClause);


    @Query(nativeQuery = true, value = "select res.* from ( " +
            "        select floor(rand()*401)+100 as id, u.id as actual_id, u.full_name as fullName,substring_index(uc.username,\"@\",1) as baCode, u.user_type as userType, uad.assotiated_user_id as assotiatedUserId,uad.is_active as isActive, " +
            "        oum.outlet_id as outletId, o.outlet_code as outletCode, o.outlet_name as outletName, u.date_of_joining as dateOfJoining, u.release_date as  releaseDate,oum.left_on as leftOn " +
            "        FROM user_assotiation_details uad " +
            "        INNER JOIN  user_details u on uad.user_id = u.id " +
            "        INNER JOIN outlet_user_mapping oum on oum.assotiated_user_id = uad. user_id " +
            "        INNER JOIN outlet o on oum.outlet_id = o.id " +
            "        INNER JOIN user_credential uc on uc.id = u.user_cred " +
            "        ) AS res " +
            "        where   assotiatedUserId = ?1 and  " +
            " ((isActive = true and dateOfJoining <= ?2 ) or (isActive = false and (?2 >= dateOfJoining and ?2 < releaseDate))) and leftOn IS NOT NULL order by trim(outletName);")
    List<BaListOfABdeOutputVo> searchBaListForVacant (Long id,String date);

}
