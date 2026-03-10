package com.dcc.osheaapp.common.event;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

public class StockTransferEvent  extends ApplicationEvent {
    private final Events event;
    private final LocalDateTime completionTime;

    public StockTransferEvent(Object source, LocalDateTime completionTime) {
        super(source);
        this.completionTime = completionTime;
        this.event = Events.STOCKS_TRANSFERRED;
    }

    public Events getEvent() {
        return event;
    }

    public LocalDateTime getCompletionTime() {
        return completionTime;
    }
}
