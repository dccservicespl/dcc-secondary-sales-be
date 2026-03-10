package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.views.Outlet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IOutletViewRepository extends JpaRepository<Outlet, Long> {
  @Query(nativeQuery = true, value = "CALL searchOutletByInputLimit(:whereClause, :limitStr)")
  List<Outlet> search(@Param("whereClause") String whereClause, @Param("limitStr") String limitStr);

  @Query(nativeQuery = true, value = "CALL countOutletByInput(:whereClause)")
  Long countOutletBySearchInput(String whereClause);
}
