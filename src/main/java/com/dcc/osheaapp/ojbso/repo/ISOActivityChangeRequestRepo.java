package com.dcc.osheaapp.ojbso.repo;


import com.dcc.osheaapp.ojbso.vo.OrderVo;
import com.dcc.osheaapp.ojbso.vo.RequestManagementVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ISOActivityChangeRequestRepo extends JpaRepository<RequestManagementVo, Long> {


    @Query(nativeQuery = true, value = "CALL searchByinputLimit(:whereClause, :tableName, :limitStr);")
    List<RequestManagementVo> searchByInput(@Param("whereClause") String whereClause, @Param("tableName") String tableName,
                                @Param("limitStr") String limitStr);

    @Query(nativeQuery = true, value = "CALL countTotalByinput(:whereClause, :tableName);")
    Long getTotalCountByInput(@Param("whereClause") String whereClause, @Param("tableName") String tableName);


    @Modifying
    @Transactional
    @Query(nativeQuery = true, value ="update so_request_management set approval_status = ?2 where id = ?1" )
    public int updateStatus(Long id, String status);

}
