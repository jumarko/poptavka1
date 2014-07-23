/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.search;

/**
 * Each enum describing search attributes must implement.
 * Used to determine class of enum in DomainObjectMapping, where appropriate prefixes are set.
 *
 * @author Martin Slavkovsky
 */
public interface ISortField {

    String getValue();
}
