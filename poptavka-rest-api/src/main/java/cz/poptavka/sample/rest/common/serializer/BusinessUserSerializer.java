/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.rest.common.serializer;

import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.rest.common.dto.BusinessUserDto;
import org.apache.commons.lang.Validate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class BusinessUserSerializer implements Converter<BusinessUser, BusinessUserDto> {

    @Override
    public BusinessUserDto convert(BusinessUser businessUser) {
        Validate.notNull(businessUser);
        Validate.notNull(businessUser.getBusinessUserData());

        final BusinessUserDto businessUserDto = new BusinessUserDto();
        businessUserDto.setEmail(businessUser.getEmail());
        businessUserDto.setCompanyName(businessUser.getBusinessUserData().getCompanyName());
        businessUserDto.setFirstName(businessUser.getBusinessUserData().getPersonFirstName());
        businessUserDto.setLastName(businessUser.getBusinessUserData().getPersonLastName());

        return businessUserDto;
    }
}
