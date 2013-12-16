
package com.eprovement.poptavka.rest.supplier;

import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.rest.ResourceNotFoundException;
import com.eprovement.poptavka.rest.common.ResourceUtils;
import com.eprovement.poptavka.rest.common.resource.AbstractPageableResource;
import com.eprovement.poptavka.service.user.SupplierService;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping(SupplierResource.SUPPLIER_RESOURCE_URI)
public class SupplierResource extends AbstractPageableResource<Supplier, SupplierDto> {

    static final String SUPPLIER_RESOURCE_URI = "/suppliers";

    private final SupplierService supplierService;
    private final Converter<Supplier, SupplierDto> supplierSerializer;
    private final SupplierDeserializer supplierDeserializer;

    @Autowired
    public SupplierResource(SupplierService supplierService, SupplierSerializer supplierSerializer,
            SupplierDeserializer supplierDeserializer) {
        super(Supplier.class, SUPPLIER_RESOURCE_URI);

        Validate.notNull(supplierService);
        Validate.notNull(supplierSerializer);
        Validate.notNull(supplierDeserializer);

        this.supplierService = supplierService;
        this.supplierSerializer = supplierSerializer;
        this.supplierDeserializer = supplierDeserializer;
    }


    @Override
    public Collection<SupplierDto> convertToDtos(Collection<Supplier> domainObjects) {
        final List<SupplierDto> suppliersDtos = new ArrayList<>();
        for (Supplier supplier : domainObjects) {
            final SupplierDto supplierDto = this.supplierSerializer.convert(supplier);
            setLinks(supplierDto, supplier);
            suppliersDtos.add(supplierDto);
        }
        return suppliersDtos;
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    SupplierDto createSupplier(@RequestBody SupplierDto supplierDto, HttpServletResponse response) {
        Validate.isTrue(supplierService.checkFreeEmail(supplierDto.getEmail()),
                "Supplier with email=" + supplierDto.getEmail() + " already exists!");
        final Supplier createdSupplier = this.supplierService.create(supplierDeserializer.convert(supplierDto));
        response.setHeader("Location", "/suppliers/" + createdSupplier.getId());
        final SupplierDto createdSupplierDto = this.supplierSerializer.convert(createdSupplier);
        setLinks(createdSupplierDto, createdSupplier);
        return createdSupplierDto;
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


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public SupplierDto getSupplierByEmail(@RequestParam String email) {
        Validate.notEmpty(email, "email has to be specified");
        final Supplier supplier = getByEmailAndCheck(email);
        final SupplierDto supplierDto = this.supplierSerializer.convert(supplier);
        setLinks(supplierDto, supplier);
        return supplierDto;
    }


    //--------------------------------------------------- PRIVATE STUFF ------------------------------------------------
    private void setLinks(SupplierDto supplierDto, Supplier supplier) {
        supplierDto.setLinks(ResourceUtils.generateSelfLinks(SUPPLIER_RESOURCE_URI, supplier));
    }

    private Supplier getByEmailAndCheck(String email) {
        final Supplier supplier = this.supplierService.getByEmail(email);
        if (supplier == null) {
            throw new ResourceNotFoundException("No supplier with email=" + email + " has been found!");
        }
        return supplier;
    }


}
