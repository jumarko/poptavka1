package com.eprovement.poptavka.rest.supplier;

import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.rest.category.CategorySerializer;
import com.eprovement.poptavka.rest.common.dto.BusinessUserDto;
import com.eprovement.poptavka.rest.locality.LocalitySerializer;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class SupplierSerializer implements Converter<Supplier, SupplierDto> {

    private final Converter<BusinessUser, BusinessUserDto> businessUserSerializer;
    private final CategorySerializer categorySerializer;
    private final LocalitySerializer localitySerializer;


    @Autowired
    public SupplierSerializer(Converter<BusinessUser, BusinessUserDto> businessUserSerializer,
                              CategorySerializer categorySerializer, LocalitySerializer localitySerializer) {
        Validate.notNull(businessUserSerializer, "businessUserSerializer should not be null!");
        Validate.notNull(categorySerializer, "categorySerializer should not be null!");
        Validate.notNull(localitySerializer, "localitySerializer should not be null!");
        this.businessUserSerializer = businessUserSerializer;
        this.categorySerializer = categorySerializer;
        this.localitySerializer = localitySerializer;
    }



    @Override
    public SupplierDto convert(Supplier supplier) {
        Validate.notNull(supplier);
        final SupplierDto supplierDto = new SupplierDto();
        supplierDto.setId(supplier.getId());
        supplierDto.setOveralRating(supplier.getOveralRating());
        supplierDto.setCertified(supplier.isCertified());
        supplierDto.setLocalities(localitySerializer.convertLocalities(supplier.getLocalities()));
        supplierDto.setCategories(categorySerializer.convertCategories(supplier.getCategories()));

        final BusinessUserDto businessUserDto = businessUserSerializer.convert(supplier.getBusinessUser());
        supplierDto.setEmail(businessUserDto.getEmail());
        supplierDto.setPersonFirstName(businessUserDto.getPersonFirstName());
        supplierDto.setPersonLastName(businessUserDto.getPersonLastName());
        supplierDto.setCompanyName(businessUserDto.getCompanyName());
        supplierDto.setPhone(businessUserDto.getPhone());
        supplierDto.setWebsite(businessUserDto.getWebsite());
        supplierDto.setAddresses(businessUserDto.getAddresses());

        return supplierDto;
    }


}
