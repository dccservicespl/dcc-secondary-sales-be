package com.dcc.osheaapp.vo;

import java.time.LocalDate;

public interface StockTransferProjection {
	
	Long getProductId();
//	Long getOutletId();
	//LocalDate getTransactionDate();
	Long getClosingStock();
	Long getOpenningStock();
	String getOpenningStockAmount();
	String getClosingStockAmount();
	
}
