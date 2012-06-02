/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.common.serializer;

import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.rest.common.dto.BusinessUserDto;
import org.apache.commons.lang.Validate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

/**
 * Encapsulates deserialization logic common for all type of {@link BusinessUser}-s, e.g.
 * {@link com.eprovement.poptavka.domain.user.Client} and {@link com.eprovement.poptavka.domain.user.Supplier}.
 *
 */
@Service
public class BusinessUserDeserializer implements Converter<BusinessUserDto, BusinessUser> {


    @Override
    public BusinessUser convert(BusinessUserDto businessUserDto) {
        Validate.notNull(businessUserDto);
        Validate.notNull(businessUserDto);

        final BusinessUser businessUser = new BusinessUser();
        businessUser.setEmail(businessUserDto.getEmail());
        final BusinessUserData businessUserData = new BusinessUserData();
        businessUserData.setCompanyName(businessUserDto.getCompanyName());
        businessUserData.setPersonFirstName(businessUserDto.getFirstName());
        businessUserData.setPersonLastName(businessUserDto.getLastName());
        businessUser.setBusinessUserData(businessUserData);

        return businessUser;
    }
}
