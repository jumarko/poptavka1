/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.application;

import java.util.List;

/**
 * Interface that lists all methods in Poptavka application which are available via JMX.
 */
public interface PoptavkaManagedOperations {

    List<String> getRecipients();

    void setRecipients(List<String> recipients);

    void addRecipient(String recipient);

    void removeRecipient(String recipient);

}
