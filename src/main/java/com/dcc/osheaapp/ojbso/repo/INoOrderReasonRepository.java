package com.dcc.osheaapp.ojbso.repo;

import com.dcc.osheaapp.ojbso.vo.NoOrderReasonMstVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface INoOrderReasonRepository extends JpaRepository<NoOrderReasonMstVo, Long> {
    @Query(nativeQuery = true, value = "select * from so_no_order_reason_mst where parent_id = ?1 ")
    List<NoOrderReasonMstVo> findByParentId(Long parentId);

}
