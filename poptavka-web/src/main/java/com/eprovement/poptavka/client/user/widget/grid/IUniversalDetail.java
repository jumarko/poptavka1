/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid;

import com.eprovement.poptavka.client.detail.interfaces.TableDisplayDetailModuleSupplier;

/**
 * TODO Martin - refactor, TableDisplays are used now.
 * This interface represent universal detail used in UniversalAsyncGrid.
 * It contains all needed method definitions for all possible cases.
 * Each detail object what is going to be used with UniversalTableWidget must implement it.
 *
 * @author Martin Slavkovsky
 */
public interface IUniversalDetail extends TableDisplayDetailModuleSupplier {

    // Offer part
    //--------------------------------------------------------------------------
    long getOfferId();
}
