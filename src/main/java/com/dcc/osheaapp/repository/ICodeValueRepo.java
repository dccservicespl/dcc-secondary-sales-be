package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.CodeValueVo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICodeValueRepo extends CrudRepository<CodeValueVo, Long> {

    @Query(nativeQuery = true, value ="SELECT 1 as id, sum(total_amount_of_item) as value, sum(total_no_of_item) as code FROM\n" +
            "so_order where DATE_FORMAT(created_on,'%Y-%m') = :monYr AND outlet_id = :outletId")
    CodeValueVo fetchMtdValue(String monYr, Long outletId);
}
