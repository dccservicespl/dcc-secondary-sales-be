package com.dcc.osheaapp.service;

import com.dcc.osheaapp.repository.ICounterStockManageRepository;
import com.dcc.osheaapp.vo.CounterStockManageVo;
import com.dcc.osheaapp.vo.StockEntryVo;

import java.util.List;

public abstract class CounterStockEntry {
    final StockEntryVo inputEntry;
    final Long user;
    final ICounterStockManageRepository counterStockManageRepository;

    public CounterStockEntry(StockEntryVo inputEntry, Long user, ICounterStockManageRepository counterStockManageRepository) {
        this.inputEntry = inputEntry;
        this.user = user;
        this.counterStockManageRepository = counterStockManageRepository;
    }

    abstract List<CounterStockManageVo> execute();
}
