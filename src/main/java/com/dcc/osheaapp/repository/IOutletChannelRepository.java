package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.OutletChannelVo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IOutletChannelRepository extends JpaRepository<OutletChannelVo, Long> {

  List<OutletChannelVo> findAll();

  @Query(nativeQuery = true, value = "select Id from outlet_channel_mst where channel_name = ?1")
  Long getIDByOutletChannelName(String outletChannelName);

  @Query(nativeQuery = true, value = "select channel_name from outlet_channel_mst where id = ?1 ")
  String getName(String id);
}
