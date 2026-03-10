package com.dcc.osheaapp.ojbso.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.dcc.osheaapp.ojbso.dto.SoBeatNTypeMappingMini;
import com.dcc.osheaapp.ojbso.vo.SoBeatNTypeMappingVo;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ISoBeatNTypeMappingRepo extends CrudRepository<SoBeatNTypeMappingVo,Long> {

	@Query(nativeQuery = true, value = "select * from so_Beat_type_mapping where so_id = :soId and active_flag = true")
    List<SoBeatNTypeMappingVo> findBeatDetailsBySoId(Long soId);
    
    List<SoBeatNTypeMappingMini> findBysoIdIdAndActiveFlag(Long soId, boolean activeFlag);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update so_Beat_type_mapping  set active_flag = 0 WHERE so_id = :loginUserId and beat_id in :beatId")
    public int deactivateExistingMappings(Long loginUserId, List<Long> beatId);

    @Query(nativeQuery = true, value = "Select * from so_Beat_type_mapping where so_id = :soId  and active_flag=1")
    List<SoBeatNTypeMappingVo> fetchActiveBeatMapping(Long soId);

    @Query(
            nativeQuery = true,
            value = "SELECT id FROM so_Beat_type_mapping WHERE beat_id = :beatId and active_flag =1"
    )
    Long findIdByBeatIdAndActiveFlag(@Param("beatId") Long beatId);


    @Query(nativeQuery = true, value = "Select * from so_Beat_type_mapping where beat_id = :id  and active_flag=1")
     SoBeatNTypeMappingVo findActiveBeatMapping(Long id);


}
