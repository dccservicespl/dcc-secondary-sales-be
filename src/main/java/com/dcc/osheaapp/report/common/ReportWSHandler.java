package com.dcc.osheaapp.report.common;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.report.purchaseSale.controller.PurchaseSaleReqHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReportWSHandler {

    private static final Logger LOGGER = LogManager.getLogger(ReportWSHandler.class);
    private final SimpMessagingTemplate template;

    public ReportWSHandler(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendReportReadyNotification(ReportWSPayload data) {
        LOGGER.info("Sending Generated Report:: " + data.fileName);
        template.convertAndSend("/topic/report-ready", data);

    }
    public void sendReportErrNotification(ApiResponse data) {
        LOGGER.info("Error Generating Report:: " + data.getMessage());
        template.convertAndSend("/topic/report-err", data);

    }

}
