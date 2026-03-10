package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.DropdownMasterVo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IDropdownMastereRepository extends JpaRepository<DropdownMasterVo, Long> {

  @Query(
      nativeQuery = true,
      value = "select * from dropdown_mst where field_type = ?1 and is_active = true")
  List<DropdownMasterVo> fetchAllActiveDropdownValueByFieldType(String fieldType);
  
  @Query(nativeQuery = true, value = "SELECT ID FROM dropdown_mst where LOWER(field_name) = ?1 and field_type = ?2")
  Long getIDByFieldName(String fieldName, String fieldType);

  @Query(nativeQuery = true, value = "select ID from dropdown_mst where field_name = ?1")
  Long getIDByFieldName(String fieldName);

  @Query(nativeQuery = true, value = "select field_name from dropdown_mst where id = ?1")
  String getName(String id);

  @Query(nativeQuery = true, value = "select * from dropdown_mst where field_name = ?1")
  DropdownMasterVo fetchDropdownValueByByFieldName(String fieldName);

  Optional<DropdownMasterVo> findByIdAndFieldType(Long id, String fieldType);
  Optional<DropdownMasterVo> findByFieldTypeAndFieldName(String fieldType, String fieldName);

    List<DropdownMasterVo> findByFieldType(String zone);
}
