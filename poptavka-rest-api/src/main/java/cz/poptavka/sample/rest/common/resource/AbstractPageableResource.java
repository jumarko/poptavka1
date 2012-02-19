/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.rest.common.resource;

import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.rest.common.dto.PageableCollectionDto;
import cz.poptavka.sample.rest.common.ResourceUtils;
import cz.poptavka.sample.service.GeneralService;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Common abstract parent for all resources that want to support paging.
 * Encapsulates common logic for loading page of domain objects from database and constructing pageable collection
 * using computed values.
 * <p>
 *     The only part which must be done in descendant is conversion from domain objects to dtos.
 * </p>
 *
 * @param <Domain> concrete domain object extending {@link DomainObject}
 * @param <Dto> type of dto objects returned by {@link #listPage(String, int, int)} method.
 */
public abstract class AbstractPageableResource<Domain extends DomainObject, Dto> implements PageableResource {

    protected static final int DEFAULT_PAGE_SIZE = 100;

    /** Needed because of erasure made Domain.class expression illegal at runtime. */
    private final Class<Domain> domainClass;
    private final String collectionUri;

    @Autowired
    private GeneralService generalService;


    protected AbstractPageableResource(Class<Domain> domainClass, String collectionUri) {
        Validate.notNull(domainClass, "Domain object class must be specified to be able to search for concrete"
                + " domain objects in listPage method!");
        Validate.notEmpty(collectionUri, "Uri to pageable collection resource must be specified!");

        this.domainClass = domainClass;
        this.collectionUri = collectionUri;
    }

    /** {@inheritDoc} */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody PageableCollectionDto<Dto> listPage(
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "" + DEFAULT_PAGE_SIZE) int limit) {

        // for sure
        Validate.notNull(this.generalService);

        if (offset < 0) {
            offset = 0;
        }

        final Search domainObjectsPageSearch = new Search(domainClass);
        domainObjectsPageSearch.addSort(Sort.asc(sort));
        domainObjectsPageSearch.setFirstResult(offset);

        // try to find limit + 1 number of demands to be able to determine if there is any other page
        // or this is the last one
        domainObjectsPageSearch.setMaxResults(limit + 1);
        final List<Domain> domainObjectsPage = this.generalService.search(domainObjectsPageSearch);

        // contains only those objects that will be passed to the client, not trailing one
        final boolean isLastPage = isLastPage(domainObjectsPage, limit);
        final List<Domain> domainObjectsPageLimited;
        if (isLastPage) {
            domainObjectsPageLimited = domainObjectsPage;
        } else {
            domainObjectsPageLimited = domainObjectsPage.subList(0, limit);
        }

        final Collection<Dto> dtosPage = convertToDtos(domainObjectsPageLimited);

        final int count = domainObjectsPageLimited.size();
        return new PageableCollectionDto<Dto>(
                dtosPage,
                new PageableCollectionDto.Paging(offset, count,
                        generateNextPageLink(isLastPage, offset, count)),
                ResourceUtils.generateCollectionLinks(this.collectionUri));

    }

    /**
     * Convert page of domain objects to the DTOs. Other logic can be performed in this method to modify default
     * behavior.
     * @param domainObjectsPage
     * @return converted objects in form of DTOs
     */
    public abstract Collection<Dto> convertToDtos(Collection<Domain> domainObjectsPage);


    /**
     * Checks wheter collection of domain objects represents the last page of all domain objects of given type
     * {@code Domain}.
     * Collection is considered to represent the last page IFF its size is less or equal to {@code limit}.
     * @param domainObjectsPage collection of domain object, typically of size {@code limit + 1}
     * @param limit  original limit passed as request parameter from client
     * @return true if {@code domainObjectsPage} is the last page, false otherwise
     */
    protected boolean isLastPage(List<Domain> domainObjectsPage, int limit) {
        Validate.notNull(domainObjectsPage);
        return domainObjectsPage.size() <= limit;
    }

    /**
     * Generates link to the next page in order. This does make sense only if processed page is not the last one.
     *
     * @param isLastPage
     * @param currentPageOffset
     * @param currentPageSize
     * @return link to the next page or null if {@code isLastPage} is true
     */
    protected String generateNextPageLink(boolean isLastPage, int currentPageOffset, int currentPageSize) {
        if (isLastPage) {
            return null;
        }

        final int offset = currentPageOffset + currentPageSize;
        final int limit = currentPageSize;
        return ResourceUtils.generateCollectionLink(this.collectionUri)
                + String.format("?offset=%s&limit=%s", offset, limit);
    }


    /**
     * Mainly for testing.
     */
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }
}
