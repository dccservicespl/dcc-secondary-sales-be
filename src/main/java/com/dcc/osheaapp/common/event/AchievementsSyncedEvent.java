package com.dcc.osheaapp.common.event;

import java.time.LocalDateTime;
import java.util.List;

import com.dcc.osheaapp.vo.DropdownMasterVo;
import org.springframework.context.ApplicationEvent;

public class AchievementsSyncedEvent extends ApplicationEvent {
  private final LocalDateTime completionTime;

  private final List<DropdownMasterVo> zone;

  public AchievementsSyncedEvent(Object source, LocalDateTime completionTime,List< DropdownMasterVo> zone) {
    super(source);
    this.completionTime = completionTime;
    this.zone = zone;
  }

  public LocalDateTime getCompletionTime() {
    return completionTime;
  }

  public List<DropdownMasterVo> getZone() {
    return zone;
  }
}
