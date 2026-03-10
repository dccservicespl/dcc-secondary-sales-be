package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.common.event.Events;
import com.dcc.osheaapp.vo.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface IEventLogRepository extends JpaRepository<EventLog, Long> {
    @Query(nativeQuery = true, value = "SELECT * from EventLog el WHERE DATE_FORMAT(el.event_time, '%Y-%m') = ?1 and event_name = ?2")
    Optional<EventLog> findByEventTimeAndEventName(String yearMonth, String event);
}
