/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.domain.system;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.enums.LogType;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * Stores status informations about long run processes.
 * @author Martin Slavkovsky
 */
@Entity
public class Log extends DomainObject {

    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    @NotNull
    @Enumerated(EnumType.STRING)
    private LogType logType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    private Integer totalItems = 0;
    private Integer processedItems = 0;
    private String description;

    /**************************************************************************/
    /*  Getters & Setters                                                     */
    /**************************************************************************/
    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType type) {
        this.logType = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Integer getProcessedItems() {
        return processedItems;
    }

    public void setProcessedItems(Integer processedItems) {
        this.processedItems = processedItems;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**************************************************************************/
    /*  Override                                                              */
    /**************************************************************************/
    @Override
    public String toString() {
        return "Log{" + ", type=" + logType
            + ", startDate=" + startDate
            + ", endDate=" + endDate
            + ", totalItems=" + totalItems
            + ", processedItems=" + processedItems
            + ", description=" + description + '}';
    }
}
