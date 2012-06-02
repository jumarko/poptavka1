/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.supplier;

import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.rest.common.dto.BusinessUserDto;
import org.apache.commons.lang.Validate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class SupplierDeserializer implements Converter<SupplierDto, Supplier> {

    private final Converter<BusinessUserDto, BusinessUser> businessUserDeserializer;


    public SupplierDeserializer(Converter<BusinessUserDto, BusinessUser> businessUserDeserializer) {
        Validate.notNull(businessUserDeserializer);
        this.businessUserDeserializer = businessUserDeserializer;
    }


    @Override
    public Supplier convert(SupplierDto supplierDto) {
        Validate.notNull(supplierDto);
        final Supplier supplier = new Supplier();
        final BusinessUser businessUser = businessUserDeserializer.convert(supplierDto);
        supplierDto.setOveralRating(supplier.getOveralRating());

        return supplier;
    }

}
