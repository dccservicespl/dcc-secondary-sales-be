package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.UserCredVo;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserCredRepository extends JpaRepository<UserCredVo, Long> {
  UserCredVo findByUsername(String username);

  @Modifying
  @Transactional
  @Query(
      nativeQuery = true,
      value =
          "update user_credential set is_active = :isActive "
              + "where id = (select user_cred from user_details where id = :userKey) ")
  public int updateCredStatus(long userKey, Boolean isActive);

  @Modifying
  @Transactional
  @Query(
      nativeQuery = true,
      value =
          "update user_credential set password = :password where userKey = :userKey and isactive = true ")
  public int updatePassword(Long userKey, String password);

  @Query(nativeQuery = true, value = "select * from user_credential where username=?1")
  Optional<UserCredVo> findByUsernameAlt(String username);

  @Query(
      nativeQuery = true,
      value =
          "SELECT a.id FROM user_details a, user_credential b where a.user_cred = b.id and b.username like ?1% ")
  Long loginUserId(String username);

  @Query(
      nativeQuery = true,
      value =
          "select a.* from osheadb.user_credential a, osheadb.user_details b, osheadb.outlet_user_mapping c where a.id = b.user_cred and c.assotiated_user_id = b.id and b.is_active = 1 and outlet_id = :outletId ")
  List<UserCredVo> findExistingUserforAssociatedOutlet(Long outletId);

  @Query(
      nativeQuery = true,
      value =
          "select a.* from osheadb.user_credential a, osheadb.user_details b where b.user_cred = a.id and user_cred = :userCred ")
  UserCredVo findExistingUserCredforUpdate(Long userCred);


}
