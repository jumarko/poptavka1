/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.interfaces;

/**
 * Implement this interface everywhere where handling resize events is recquired.
 * We use it when browser resizes to broatcast event to all presenters implementing this interface.
 * Those presenters are then notified that browser has resized and take appropriate action,
 * like recalculating scroll heights.
 *
 * <b><i>Note</i></b>
 * If I get it right, presenter of module which fires broadcast can implement this handler.
 * But in other modules, module definition class can implement this handler.
 *
 * https://code.google.com/p/mvp4g/wiki/EventBus#Broadcast_Event
 *
 * @author Martin Slavkovsky
 *         Date: 14.01.2014
 */
public interface HandleResizeEvent {
    //nothing by default
}
