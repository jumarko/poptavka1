/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.rest.locality;

import com.google.common.base.Preconditions;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.rest.common.ResourceUtils;
import cz.poptavka.sample.rest.common.resource.AbstractPageableResource;
import cz.poptavka.sample.service.address.LocalityService;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(LocalityResource.LOCALITY_RESOURCE_URI)
public class LocalityResource extends AbstractPageableResource<Locality, LocalityDto> {

    static final String LOCALITY_RESOURCE_URI = "/localities";

    private final LocalityService localityServcie;
    private final Converter<Locality, LocalityDto> localitySerializer;

    @Autowired
    public LocalityResource(LocalityService localityService, Converter<Locality, LocalityDto> localitySerializer) {
        super(Locality.class, LOCALITY_RESOURCE_URI);

        Validate.notNull(localityService);
        Validate.notNull(localitySerializer);
        this.localityServcie = localityService;
        this.localitySerializer = localitySerializer;
    }


    @Override
    public Collection<LocalityDto> convertToDtos(Collection<Locality> domainObjectsPage) {
        final ArrayList<LocalityDto> categoryDtosPage = new ArrayList<LocalityDto>();
        for (Locality locality : domainObjectsPage) {
            final LocalityDto localityDto = this.localitySerializer.convert(locality);
            setLinks(localityDto, locality);
            categoryDtosPage.add(localityDto);
        }

        return categoryDtosPage;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    LocalityDto getLocalityById(@PathVariable Long id) {
        Preconditions.checkNotNull(id);
        final Locality locality = this.localityServcie.getById(id);
        final LocalityDto localityDto = this.localitySerializer.convert(locality);
        setLinks(localityDto, locality);
        return localityDto;
    }


    //--------------------------------------------------- PRIVATE STUFF ------------------------------------------------
    private void setLinks(LocalityDto localityDto, Locality locality) {
        localityDto.setLinks(ResourceUtils.generateSelfLinks(LOCALITY_RESOURCE_URI, locality));
    }

}
