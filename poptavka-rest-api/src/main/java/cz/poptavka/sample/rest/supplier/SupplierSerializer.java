/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.rest.supplier;

import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.rest.common.dto.BusinessUserDto;
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
