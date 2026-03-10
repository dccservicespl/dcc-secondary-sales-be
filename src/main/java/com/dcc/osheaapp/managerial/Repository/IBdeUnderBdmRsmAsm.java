package com.dcc.osheaapp.managerial.Repository;

import com.dcc.osheaapp.managerial.vo.BdeListOutputVo;
import com.dcc.osheaapp.vo.views.BaListOfABdeOutputVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IBdeUnderBdmRsmAsm extends JpaRepository<BdeListOutputVo, Long> {

    @Query(nativeQuery = true, value = "select u.id as id, u.full_name as fullName,substring_index(uc.username,\"@\",1) as userCode, u.user_type as userType, uad.assotiated_user_id as assotiatedUserId,uad.is_active as isActive,\n" +
            "u.date_of_joining as dateOfJoining, u.release_date as  releaseDate\n" +
            "        FROM user_assotiation_details uad\n" +
            "        INNER JOIN  user_details u on uad.user_id = u.id\n" +
            "        INNER JOIN user_credential uc on uc.id = u.user_cred\n" +
            "        where uad.assotiated_user_id=?1 and uad.is_active=true and u.user_type=12;")
    List<BdeListOutputVo> fetchBde (Long id);
}
