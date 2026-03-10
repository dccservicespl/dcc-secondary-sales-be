package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.UserTypeVo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserTypeRepository extends JpaRepository<UserTypeVo, Long> {

  List<UserTypeVo> findAll();

  @Query(
      nativeQuery = true,
      value =
          "select * from user_type_mst where form_type = :formType and is_active = :isActive order by user_priority desc")
  List<UserTypeVo> findUserByType(String formType, Boolean isActive);

  @Query(
      nativeQuery = true,
      value =
          "select * from user_type_mst "
              + "where is_active = :isActive and user_priority < :userPriority "
              + "order by user_priority desc")
  List<UserTypeVo> findUserTypeByPriority(Boolean isActive, Long userPriority);

  @Query(
      nativeQuery = true,
      value = "select * from user_type_mst where user_type = :userType and is_active = :isActive")
  UserTypeVo findUserByUserType(String userType, Boolean isActive);

  @Query(nativeQuery = true, value = "select user_type from user_type_mst where id = :id")
  String userType(String id);
}
