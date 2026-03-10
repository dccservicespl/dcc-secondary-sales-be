package com.dcc.osheaapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dcc.osheaapp.vo.DistributorOutputVo;

@Repository
public interface IDistributorSerchOutputRepo extends JpaRepository <DistributorOutputVo, Long>{

    

    
@Query(nativeQuery = true, value = "CALL searchDistributorList(:whereClause, :limitStr)")
List<DistributorOutputVo> search(@Param("whereClause") String whereClause, @Param("limitStr") String limitStr);

  @Query(nativeQuery = true, value = "CALL countDistributorByInput(:whereClause)")
  Long countDistributorList(String whereClause);

}
