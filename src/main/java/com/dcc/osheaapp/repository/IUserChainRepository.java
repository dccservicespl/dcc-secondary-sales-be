package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.views.UserChain;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;


@Repository
public interface IUserChainRepository extends JpaRepository<UserChain, Long> {
  @Query(nativeQuery = true, value = "CALL findUserChain(:targetId);")
  @QueryHints(value = @QueryHint(name = "javax.persistence.cache.storeMode", value = "REFRESH"))
  List<UserChain> findUserChainById(@Param("targetId") Long targetId);

  @Query(nativeQuery = true, value = "CALL findUserChainsp2(:targetId);")
  @QueryHints(value = @QueryHint(name = "javax.persistence.cache.storeMode", value = "REFRESH"))
  List<UserChain> findUserChainById1(@Param("targetId") Long targetId);


}
