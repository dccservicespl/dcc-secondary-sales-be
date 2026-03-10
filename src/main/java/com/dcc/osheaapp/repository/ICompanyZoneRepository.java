package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.CompanyZoneVo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICompanyZoneRepository extends JpaRepository<CompanyZoneVo, Long> {

  List<CompanyZoneVo> findAll();

  @Query(nativeQuery = true, value = "select Id from company_zone_mst where company_zone = ?1")
  Long getIDByCompanyZoneName(String companyZoneName);
}
