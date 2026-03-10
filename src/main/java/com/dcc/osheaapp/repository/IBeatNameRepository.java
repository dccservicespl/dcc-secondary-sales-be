package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.BeatName;
import java.util.List;
import java.util.Optional;

import com.dcc.osheaapp.vo.UserBeatsAssociation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface IBeatNameRepository extends JpaRepository<BeatName, Long> {

  List<BeatName> findAll();

  @Query(nativeQuery = true, value = "Select * from beat_name_mst where so_id = :id")
  List<BeatName> findAllBySoId(Long id);

  
  @Query(nativeQuery = true, value = "select * from beat_name_mst where beat_name = ?1")
  BeatName fetchByBeatName(String beatName);

  @Query(nativeQuery = true, value = "select Id from beat_name_mst where beat_name = ?1")
  Long getIDByBeatName(String beatName);

  @Query(nativeQuery = true, value = "select Id from beat_name_mst where lower(beat_name) = ?1")
  Long getIDByBeatNameSo(String beatName);

  BeatName findByBeatName(String beatName);
  
  @Query(nativeQuery = true, value = "select beat_name from beat_name_mst where id = ?1 ")
  String getName(String id);
  
  @Query(nativeQuery = true, value = "CALL searchBeatNameByInputLimit(:whereClause, :limitStr)")
  List<BeatName> searchBeatNameByInput(@Param("whereClause") String whereClause, @Param("limitStr") String limitStr);
  
  @Query(nativeQuery = true, value = "CALL countBeatNameByInput(:whereClause)")
  Long countBeatNameByInput(String whereClause);

@Transactional
@Modifying
  @Query(nativeQuery = true, value = "update beat_name_mst set is_active =  :isActive where id = :id")
  public int updateStatus(Long id, Boolean isActive);

@Query(nativeQuery = true, value = "select * from beat_name_mst where id = :id" )
public String findByBeatId (Long id);

}
