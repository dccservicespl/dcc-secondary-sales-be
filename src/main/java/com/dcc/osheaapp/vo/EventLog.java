package com.dcc.osheaapp.vo;

import com.dcc.osheaapp.common.event.Events;
import org.yaml.snakeyaml.events.Event;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.Month;

@Entity
public class EventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_name")
    @Enumerated(EnumType.STRING)
    private Events eventName;

    @Column(name = "event_time")
    private LocalDateTime eventTime;

    public EventLog() {
    }

    public EventLog(Events eventName, LocalDateTime eventTime) {
        this.eventName = eventName;
        this.eventTime = eventTime;
    }

    public Long getId() {
        return id;
    }

    public EventLog setId(Long id) {
        this.id = id;
        return this;
    }

    public Events getEventName() {
        return eventName;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public Month month() {
        return this.eventTime == null ? null : this.eventTime.getMonth();
    }
}
