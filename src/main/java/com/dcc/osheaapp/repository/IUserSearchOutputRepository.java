package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.UserSearchOutputVo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserSearchOutputRepository extends JpaRepository<UserSearchOutputVo, Long> {

  @Query(nativeQuery = true, value = "CALL searchUserByinputLimit(:whereClause, :limitStr);")
  List<UserSearchOutputVo> searchByInput(
      @Param("whereClause") String whereClause, @Param("limitStr") String limitStr);

  @Query(nativeQuery = true, value = "CALL countTotalUserByinput(:whereClause);")
  Long getTotalCountByInput(@Param("whereClause") String whereClause);
}
