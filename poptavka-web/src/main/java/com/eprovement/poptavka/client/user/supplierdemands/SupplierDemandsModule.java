/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.root.interfaces.HandleResizeEvent;
import com.mvp4g.client.Mvp4gModule;
import com.mvp4g.client.annotation.module.HistoryName;

/**
 * Defines SupplierDemands module.
 *
 * @author Martin Slavkovsky
 */
@HistoryName("userSupplierDemands")
public interface SupplierDemandsModule extends Mvp4gModule, HandleResizeEvent {
}
