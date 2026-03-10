package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.FormMediaMappingVo;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IFormMediaMappingRepository extends JpaRepository<FormMediaMappingVo, Long> {
  List<FormMediaMappingVo> findByFormTypeAndTabNameAndTransactionId(
      String formType, String tabName, Long transactionId);

  List<FormMediaMappingVo> findByFormTypeAndTransactionId(String formType, Long transactionId);

  @Modifying
  @Transactional
  @Query(
      nativeQuery = true,
      value =
          "delete from form_media_mapping where"
              + " form_type = :formType and tab_name = :tabName and transaction_id = :transactionId ")
  public int deleteMedia(String formType, String tabName, Long transactionId);

  @Transactional
  @Query(
          nativeQuery = true,
          value =
                  "select file_path from form_media_mapping where"
                          + " form_type = :formType and tab_name = :tabName and transaction_id = :transactionId ")
  public String fetchOutletImage(String formType, String tabName, Long transactionId);
}
