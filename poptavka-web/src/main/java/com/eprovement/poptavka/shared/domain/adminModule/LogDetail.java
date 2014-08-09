/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain.adminModule;

import com.eprovement.poptavka.domain.enums.LogType;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * @author Martin Slavkovsky
 * @since 5.8.2014
 */
public class LogDetail implements IsSerializable {

    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    private LogType logType;
    private Date startDate;
    private Date endDate;
    private Integer totalItems;
    private Integer processedItems;
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

    /**
     * @return percentage progress value as bigdecimal rounded by 3 scale UP.
     */
    public BigDecimal getPercentageProgress() {
        return BigDecimal.valueOf((double) (processedItems * 100 / totalItems))
            .setScale(2, RoundingMode.UP);
    }
}
