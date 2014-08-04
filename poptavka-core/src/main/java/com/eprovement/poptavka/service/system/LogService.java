/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.service.system;

import com.eprovement.poptavka.domain.enums.LogType;
import com.eprovement.poptavka.domain.system.Log;
import com.eprovement.poptavka.service.GeneralService;
import java.util.Date;
import org.springframework.transaction.annotation.Transactional;

/**
 * For storing and updating log informations.
 * @author Martin Slavkovsky
 */
public final class LogService {

    private GeneralService generalService;

    public LogService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Transactional
    public Log createLog(LogType logType, Integer totalItems, String description) {
        Log log = new Log();
        log.setLogType(logType);
        log.setTotalItems(totalItems);
        log.setDescription(description);
        generalService.save(log);;
        return log;
    }

    @Transactional
    public void updateLog(Log log) {
        generalService.save(log);;
    }

    @Transactional
    public void logError(Log log, Exception ex) {
        log.setEndDate(new Date());
        log.setDescription(ex.getMessage());
        generalService.save(log);;
    }

    @Transactional
    public void finnishLog(Log log) {
        log.setEndDate(new Date());
        log.setProcessedItems(log.getTotalItems());
        generalService.save(log);;
    }
}
