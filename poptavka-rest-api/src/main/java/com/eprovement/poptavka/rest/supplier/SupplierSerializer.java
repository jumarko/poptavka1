/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.supplier;

import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.rest.common.dto.BusinessUserDto;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class SupplierSerializer implements Converter<Supplier, SupplierDto> {

    private final Converter<BusinessUser, BusinessUserDto> businessUserSerializer;


    @Autowired
    public SupplierSerializer(Converter<BusinessUser, BusinessUserDto> businessUserSerializer) {
        Validate.notNull(businessUserSerializer);
        this.businessUserSerializer = businessUserSerializer;
    }



    @Override
    public SupplierDto convert(Supplier supplier) {
        Validate.notNull(supplier);
        final SupplierDto supplierDto = new SupplierDto();
        final BusinessUserDto businessUserDto = businessUserSerializer.convert(supplier.getBusinessUser());
        supplierDto.setOveralRating(supplier.getOveralRating());

        return supplierDto;
    }


}
