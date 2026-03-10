package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.UserBeatsAssociation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserBeatsAssociationRepo extends JpaRepository<UserBeatsAssociation, Long> {
    List<UserBeatsAssociation> findAllByUser(Long id);

    UserBeatsAssociation findByUserAndBeatId(Long user, Long beatId);

//    @Query(
//            nativeQuery = true,
//            value = "select * from user_assotiation_details where assotiated_user_id = ?1")
}
