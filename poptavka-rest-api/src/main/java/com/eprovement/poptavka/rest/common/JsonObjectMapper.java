/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

/**
 * common configuration of Jackson Object Mapper.
 *
 * Typical settings include:
 * <ul>
 *     <li>serialization only non-null fields - end user typically does not want to see these fields at all.</li>
 *     <li>ignoring unknown properties by deserialization - useful eg. for backward compatibility</li>
 *     <li>read unknown enum values as null by deserialization - useful eg. for backward compatibility</li>
 * </ul>
 */
public class JsonObjectMapper extends ObjectMapper {

    public JsonObjectMapper() {
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // sets proper date format to ensure human readable printing - otherwise System epoch time is used
        setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz"));
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
    }
}
