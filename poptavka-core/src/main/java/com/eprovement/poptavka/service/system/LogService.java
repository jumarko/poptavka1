/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.service.system;

import com.eprovement.poptavka.domain.enums.LogType;
import com.eprovement.poptavka.domain.system.Log;

/**
 * For storing and updating log informations.
 * @author Martin Slavkovsky
 * @since 4.8.2014
 */
public interface LogService {

    /**
     * Create and return log object according to given attributes.
     * The operation is transactional.
     * @param logType
     * @param totalItems
     * @param description
     * @return created enity
     */
    Log createLog(LogType logType, Integer totalItems, String description);

    /**
     * Updates given log object.
     * The operation is transactional.
     * @param log to be updated
     */
    void updateLog(Log log);

    /**
     * Logs error. Sets end date to current date and description according to given exeption message.
     * The operation is transactional.
     * @param log
     * @param ex
     */
    void logError(Log log, Exception ex);

    /**
     * Logs job end. Sets end date to current date and sets process items value to total items value.
     * The operation is transactional.
     * @param log
     * @param ex
     */
    void finnishLog(Log log);
}
