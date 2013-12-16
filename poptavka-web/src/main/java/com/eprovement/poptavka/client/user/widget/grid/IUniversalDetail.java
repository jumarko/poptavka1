/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.grid;

import com.eprovement.poptavka.shared.domain.TableDisplayDetailModule;

/**
 * This interface represent universal detail used in UniversalTableWidget.
 * It contains all needed method definitions for all possible cases.
 * Each detail object what is going to be used with UniversalTableWidget must implement it.
 *
 * @author Martin Slavkovsky
 */
public interface IUniversalDetail extends TableDisplayDetailModule {
//public interface IUniversalDetail extends TableDisplayRating, TableDisplayDisplayName,
//        TableDisplayUserMessage, TableDisplayDemandTitle, TableDisplayPrice, TableDisplayDemandStatus,
//        TableDisplayOfferReceivedDate, TableDisplayFinishDate, TableDisplayValidTo, TableDisplayEndDate {

    // Client part & Supplier part
    //--------------------------------------------------------------------------
//    long getClientId();

    // Message part
    //--------------------------------------------------------------------------
//    long getMessageId();


    // Offer part
    //--------------------------------------------------------------------------
    long getOfferId();
}
