/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.supplier;

import com.google.common.base.Preconditions;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.rest.common.ResourceUtils;
import com.eprovement.poptavka.rest.common.resource.AbstractPageableResource;
import com.eprovement.poptavka.service.user.SupplierService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(SupplierResource.SUPPLIER_RESOURCE_URI)
public class SupplierResource extends AbstractPageableResource<Supplier, SupplierDto> {

    static final String SUPPLIER_RESOURCE_URI = "/suppliers";

    private final SupplierService supplierService;
    private final Converter<Supplier, SupplierDto> supplierSerializer;

    @Autowired
    public SupplierResource(SupplierService supplierService, Converter<Supplier, SupplierDto> supplierSerializer,
            Converter<SupplierDto, Supplier> supplierDeserializer) {
        super(Supplier.class, SUPPLIER_RESOURCE_URI);

        Validate.notNull(supplierService);
        Validate.notNull(supplierSerializer);

        this.supplierService = supplierService;
        this.supplierSerializer = supplierSerializer;
    }


    @Override
    public Collection<SupplierDto> convertToDtos(Collection<Supplier> domainObjects) {
        final List<SupplierDto> suppliersDtos = new ArrayList<SupplierDto>();
        for (Supplier supplier : domainObjects) {
            final SupplierDto supplierDto = this.supplierSerializer.convert(supplier);
            setLinks(supplierDto, supplier);
            suppliersDtos.add(supplierDto);
        }
        return suppliersDtos;
    }


    @RequestMapping(method = RequestMethod.POST, headers = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public
    @ResponseBody
    SupplierDto createSupplier(@Valid SupplierDto supplier, BindingResult result, HttpServletResponse response)
        throws BindException {

        if (result.hasErrors()) {
            throw new BindException(result);
        }

//        final Supplier createdSupplier = this.supplierService.create(supplier);
//        response.setHeader("Location", "/suppliers/" + createdSupplier.getId());
//        final SupplierDto supplierDto = this.supplierSerializer.convert(supplier);
//        setLinks(supplierDto, supplier);
//        return supplierDto;
        return new SupplierDto();
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    SupplierDto getSupplierById(@PathVariable Long id) {
        Preconditions.checkNotNull(id);
        final Supplier supplier = this.supplierService.getById(id);
        final SupplierDto supplierDto = this.supplierSerializer.convert(supplier);
        setLinks(supplierDto, supplier);
        return supplierDto;
    }


//
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public Supplier getSupplierByFilter(@PathVariable SupplierFilter supplierFilter) {
//        Preconditions.checkNotNull(id);
//        return this.supplierService.getById(id);
//    }
//
//


    @RequestMapping(value = "/{id}",
            method = RequestMethod.PUT,
            headers = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateSupplier(@RequestBody Supplier supplier) {
        this.supplierService.update(supplier);
    }


    //--------------------------------------------------- PRIVATE STUFF ------------------------------------------------
    private void setLinks(SupplierDto supplierDto, Supplier supplier) {
        supplierDto.setLinks(ResourceUtils.generateSelfLinks(SUPPLIER_RESOURCE_URI, supplier));
    }

}
