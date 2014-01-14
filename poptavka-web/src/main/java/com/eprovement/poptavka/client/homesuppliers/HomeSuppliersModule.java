/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.root.interfaces.HandleResizeEvent;
import com.mvp4g.client.Mvp4gModule;
import com.mvp4g.client.annotation.module.HistoryName;

/**
 * Defines home supplier module
 * @author ivan.vlcek
 */
@HistoryName("homeSuppliers")
public interface HomeSuppliersModule extends Mvp4gModule, HandleResizeEvent {
}
