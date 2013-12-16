/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.resources.datagrid;

import com.google.gwt.resources.client.CssResource.ClassName;
import com.google.gwt.user.cellview.client.DataGrid;

/**
 * Defines DataGrid styles for UniversalAsyncGrid.
 *
 * @author Jaro
 */
public interface DataGridStyles extends DataGrid.Style {

    /*
     * Generally styles
     */
    @ClassName("unread")
    String unread();

    /*
     * Column styles
     */
    @ClassName("col-width-ratting")
    String colWidthRatting();

    @ClassName("col-width-locality")
    String colWidthLocality();

    @ClassName("col-width-title")
    String colWidthTitle();

    @ClassName("col-width-price")
    String colWidthPrice();

    @ClassName("col-width-displayName")
    String colWidthDisplayName();

    @ClassName("col-width-messageText")
    String colWidthMessageText();

    @ClassName("col-width-createdDate")
    String colWidthCreatedDate();

    @ClassName("col-width-receivedDate")
    String colWidthReceivedDate();

    @ClassName("col-width-date")
    String colWidthDate();

    @ClassName("col-width-icon")
    String colWidthIcon();

    @ClassName("col-width-star")
    String colWidthStar();

    @ClassName("col-width-urgency")
    String colWidthUrgency();

    @ClassName("col-width-id")
    String colWidthId();

    @ClassName("col-width-sender")
    String colWidthSender();

    @ClassName("col-width-logo")
    String colWidthLogo();

    /*
     * Cell styles
     */
    @ClassName("cell-style-star")
    String cellStyleStar();

    @ClassName("cell-style-urgency")
    String cellStyleUrgency();
}
