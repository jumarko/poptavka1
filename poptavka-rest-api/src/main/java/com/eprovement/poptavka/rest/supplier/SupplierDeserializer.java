package com.eprovement.poptavka.rest.supplier;

import static org.apache.commons.lang.Validate.notNull;

import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.rest.common.dto.BusinessUserDto;
import com.eprovement.poptavka.rest.common.serializer.CategoryDeserializer;
import com.eprovement.poptavka.rest.common.serializer.LocalityDeserializer;
import com.eprovement.poptavka.service.user.SupplierService;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class SupplierDeserializer implements Converter<SupplierDto, Supplier> {

    private final Converter<BusinessUserDto, BusinessUser> businessUserDeserializer;
    private final CategoryDeserializer categoryDeserializer;
    private final LocalityDeserializer localityDeserializer;
    private SupplierService supplierService;


    @Autowired
    public SupplierDeserializer(Converter<BusinessUserDto, BusinessUser> businessUserDeserializer,
                                SupplierService supplierService, CategoryDeserializer categoryDeserializer,
                                LocalityDeserializer localityDeserializer) {
        notNull(businessUserDeserializer);
        notNull(supplierService);
        notNull(categoryDeserializer);
        this.supplierService = supplierService;
        this.businessUserDeserializer = businessUserDeserializer;
        this.categoryDeserializer = categoryDeserializer;
        this.localityDeserializer = localityDeserializer;
    }


    @Override
    public Supplier convert(SupplierDto supplierDto) {
        Validate.notNull(supplierDto, "supplierDto cannot be null");

        if (supplierDto.getId() != null) {
            // existing client, load him from DB
            final Supplier supplierById = supplierService.getById(supplierDto.getId());
            notNull(supplierById, "Invalid supplier id - no such supplier");
            return supplierById;
        }

        final Supplier supplier = new Supplier();
        final BusinessUser businessUser = businessUserDeserializer.convert(supplierDto);
        supplier.setBusinessUser(businessUser);
        supplier.setOveralRating(supplierDto.getOveralRating());
        supplier.setCertified(supplierDto.isCertified());
        supplier.setCategories(categoryDeserializer.convertCategories(supplierDto.getCategories()));
        supplier.setLocalities(localityDeserializer.convertLocalities(supplierDto.getLocalities()));

        return supplier;
    }

}
