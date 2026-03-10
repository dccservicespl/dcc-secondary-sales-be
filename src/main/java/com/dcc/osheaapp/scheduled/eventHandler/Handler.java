package com.dcc.osheaapp.scheduled.eventHandler;

import com.dcc.osheaapp.common.event.StockTransferEvent;
import com.dcc.osheaapp.repository.IEventLogRepository;
import com.dcc.osheaapp.vo.EventLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
final class Handler {
    @Autowired
    IEventLogRepository eventLogRepository;
    @EventListener
   private void handleStockTransferEvent(StockTransferEvent event) {
    eventLogRepository.save(new EventLog(event.getEvent(), event.getCompletionTime()));
   }
}
