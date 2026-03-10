package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.OutletUserMappingVo;
import com.dcc.osheaapp.vo.dto.BdeOutletAssociation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface IBdeOutletMappingRepository extends JpaRepository <BdeOutletAssociation, Long> {
//    @Query(nativeQuery = true, value="SELECT * FROM bde_outlet_association  WHERE associated_user_id = ?1 and  is_active=?2")
//    List<BdeOutletAssociation> findByAssociateUserAndIsActive(Long associatedBdeId, Boolean isActive);

//    @Modifying
//    @Transactional
//    @Query("UPDATE BdeOutletAssociation b SET b.isActive = false WHERE b.outletId.id IN :outletIds AND b.isActive = true")
//    void deactivateExistingAssociations(@Param("outletIds") List<Long> outletIds);

    @Modifying
    @Transactional
    @Query("UPDATE BdeOutletAssociation b SET b.isActive = false " +
            "WHERE b.outletId.id IN :outletIds " +
            "AND b.associateUser.id <> :newBdeId " +
            "AND b.isActive = true")
            void deactivateOutdatedAssociations(@Param("outletIds") List<Long> outletIds, @Param("newBdeId") Long newBdeId);


    @Query( nativeQuery = true , value = "select  distinct(outlet_id) from bde_outlet_association where associated_user_id = ?1")
    List<Long> getOutlet (Long bdeId);
    @Query( nativeQuery = true , value = "select  distinct(outlet_id) from bde_outlet_association where associated_user_id = ?1 and is_active=1")
    List<Long> getOutletIds (Long bdeId);

}
