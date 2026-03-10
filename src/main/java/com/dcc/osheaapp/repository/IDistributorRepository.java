package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.DistributorVo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IDistributorRepository extends JpaRepository<DistributorVo, Long> {

  List<DistributorVo> findAll();

  List<DistributorVo> findByIsActive(boolean isActive);

  @Query(nativeQuery = true, value = "select Id from distributor_mst where distributor_name = ?1")
  Long getIDByDistributorName(String distributorName);

@Query(nativeQuery = true, value = "CALL searchDistributorList(:whereClause, :limitStr)")
List<DistributorVo> search(@Param("whereClause") String whereClause, @Param("limitStr") String limitStr);

  @Query(nativeQuery = true, value = "CALL countDistributorByInput(:whereClause)")
  Long countDistributorList(String whereClause);

  @Query(nativeQuery = true, value = "select distributor_name from distributor_mst where company_zone = :id")
  String getName( String id);

  @Query(nativeQuery = true, value = "select * from distributor_mst where company_zone = :zoneId and is_active = :isActive")
  List<DistributorVo> findActiveDistributorZone( Long zoneId, Boolean isActive);
}
