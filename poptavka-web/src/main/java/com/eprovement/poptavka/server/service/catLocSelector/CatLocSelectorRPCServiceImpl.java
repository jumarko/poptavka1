/*
 * Copyright (C) 2013, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.service.catLocSelector;

import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.eprovement.poptavka.client.service.demand.CatLocSelectorRPCService;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocSuggestionDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.selectors.SuggestionResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * This RPC handles all requests for CatLocSelector module.
 * @author Martin Slavkovsky
 */
@Configurable
public class CatLocSelectorRPCServiceImpl extends AutoinjectingRemoteService
    implements CatLocSelectorRPCService {

    private CategoryService categoryService;
    private LocalityService localityService;
    private Converter<Category, ICatLocDetail> categoryConverter;
    private Converter<Locality, ICatLocDetail> localityConverter;
    private static final Logger LOGGER = LoggerFactory.getLogger("CategoryRPCServiceImpl");

    /**************************************************************************/
    /* Autowired services                                                     */
    /**************************************************************************/
    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    /**************************************************************************/
    /* Autowired converters                                                   */
    /**************************************************************************/
    @Autowired
    public void setClientDemandMessageConverter(
        @Qualifier("categoryConverter") Converter<Category, ICatLocDetail> categoryConverter) {
        this.categoryConverter = categoryConverter;
    }

    @Autowired
    public void setLocalityConverter(
        @Qualifier("localityConverter") Converter<Locality, ICatLocDetail> localityConverter) {
        this.localityConverter = localityConverter;
    }

    /**************************************************************************/
    /* Business logic                                                         */
    /**************************************************************************/
    /**
     * Get category or locality. Depends on given selectorType value.
     * @param selectorType - {@value #SELECTOR_TYPE_CATEGORIES} for categories,
     *                       {@value #SELECTOR_TYPE_LOCALITIES} for localities.
     * @param selectorType
     * @param id
     * @return category or locality object
     * @throws RPCException
     */
    @Override
    public ICatLocDetail getItem(int selectorType, long id) throws RPCException {
        switch (selectorType) {
            case CatLocSelectorBuilder.SELECTOR_TYPE_CATEGORIES:
                return categoryConverter.convertToTarget(categoryService.getById(id));
            case CatLocSelectorBuilder.SELECTOR_TYPE_LOCALITIES:
                return localityConverter.convertToTarget(localityService.getById(id));
            default:
                return null;
        }
    }

    /**
     * Get root category or locality. Depends on given selectorType value.
     * @param selectorType - {@value #SELECTOR_TYPE_CATEGORIES} for categories,
     *                       {@value #SELECTOR_TYPE_LOCALITIES} for localities.
     * @param selectorType
     * @return list of root categories/localities
     * @throws RPCException
     */
    @Override
    public List<ICatLocDetail> getRootItems(int selectorType) throws RPCException {
        switch (selectorType) {
            case CatLocSelectorBuilder.SELECTOR_TYPE_CATEGORIES:
                return categoryConverter.convertToTargetList(categoryService.getRootCategories());
            case CatLocSelectorBuilder.SELECTOR_TYPE_LOCALITIES:
                return localityConverter.convertToTargetList(localityService.getLocalities(LocalityType.REGION));
            default:
                return new ArrayList<ICatLocDetail>();
        }

    }

    /**
     * Get children categories or localities. Depends on given selectorType value.
     * @param selectorType - {@value #SELECTOR_TYPE_CATEGORIES} for categories,
     *                       {@value #SELECTOR_TYPE_LOCALITIES} for localities.
     * @param id
     * @return list of child categories/localities
     * @throws RPCException
     */
    @Override
    public List<ICatLocDetail> getItemChildren(int selectorType, long id) throws RPCException {
        switch (selectorType) {
            case CatLocSelectorBuilder.SELECTOR_TYPE_CATEGORIES:
                return categoryConverter.convertToTargetList(categoryService.getById(id).getChildren());
            case CatLocSelectorBuilder.SELECTOR_TYPE_LOCALITIES:
                return localityConverter.convertToTargetList(localityService.getSubLocalities(id));
            default:
                return new ArrayList<ICatLocDetail>();
        }
    }

    /**
     * Request parent hierarchy for given item as list of pairs: item - index
     * @param detail represent pair ICatLocDetail - it's index
     * @return
     */
    @Override
    public LinkedList<ICatLocDetail> requestHierarchy(int selectorType, ICatLocDetail detail) throws RPCException {
        switch (selectorType) {
            case CatLocSelectorBuilder.SELECTOR_TYPE_CATEGORIES:
                return requestCategoryHierarchy(detail);
            case CatLocSelectorBuilder.SELECTOR_TYPE_LOCALITIES:
                return requestLocalityHierarchy(detail);
            default:
                return new LinkedList<ICatLocDetail>();
        }
    }

    /**
     * Get category/locality suggestions.
     * @param requestId
     * @param selectorType - {@value #SELECTOR_TYPE_CATEGORIES} for categories,
     *                       {@value #SELECTOR_TYPE_LOCALITIES} for localities.
     * @param cityLike query
     * @param wordLength lenght constraint
     * @return suggestions
     * @throws RPCException
     */
    @Override
    public SuggestionResponse<CatLocSuggestionDetail> getSuggestions(
        int requestId, int selectorType, String cityLike, int wordLength) throws RPCException {
        ArrayList<CatLocSuggestionDetail> suggestions = null;
        switch (selectorType) {
            case CatLocSelectorBuilder.SELECTOR_TYPE_CATEGORIES:
                suggestions = getCategorySuggestions(cityLike, wordLength);
                break;
            case CatLocSelectorBuilder.SELECTOR_TYPE_LOCALITIES:
                suggestions = getLocalitySuggestions(cityLike, wordLength);
                break;
            default:
                suggestions = new ArrayList<CatLocSuggestionDetail>();
                break;
        }
        return new SuggestionResponse<CatLocSuggestionDetail>(requestId, suggestions);
    }

    /**
     * Get short category/locality suggestions.
     * Short word's lenght is less than wordLenght constraint.
     * @param requestId
     * @param selectorType - {@value #SELECTOR_TYPE_CATEGORIES} for categories,
     *                       {@value #SELECTOR_TYPE_LOCALITIES} for localities.
     * @param cityLike
     * @param wordLength
     * @return
     * @throws RPCException
     */
    @Override
    public SuggestionResponse<CatLocSuggestionDetail> getShortSuggestions(
        int requestId, int selectorType, String cityLike, int wordLength) throws RPCException {
        ArrayList<CatLocSuggestionDetail> suggestions = null;
        switch (selectorType) {
            case CatLocSelectorBuilder.SELECTOR_TYPE_CATEGORIES:
                suggestions = getShortCategorySuggestions(cityLike, wordLength);
                break;
            case CatLocSelectorBuilder.SELECTOR_TYPE_LOCALITIES:
                suggestions = getShortLocalitySuggestions(cityLike, wordLength);
                break;
            default:
                suggestions = new ArrayList<CatLocSuggestionDetail>();
                break;
        }
        return new SuggestionResponse<CatLocSuggestionDetail>(requestId, suggestions);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Get category hierarchy of given category.
     * @param catDetail
     * @return category hierarchy as a linked list.
     * @throws RPCException
     */
    private LinkedList<ICatLocDetail> requestCategoryHierarchy(ICatLocDetail catDetail) throws RPCException {
        LOGGER.info("Requesting category hierarchy for " + catDetail.toString());
        final LinkedList<ICatLocDetail> categoryHierarchy = new LinkedList<ICatLocDetail>();

        Category category = categoryConverter.convertToSource(catDetail);
        while (category != null) {
            categoryHierarchy.addFirst(categoryConverter.convertToTarget(category));
            category = category.getParent();
        }

        LOGGER.info("Requested hierarchy: " + categoryHierarchy.toString());
        return categoryHierarchy;
    }

    /**
     * Get locality hierarchy of given category.
     * @param locDetail
     * @return locality hierarchy as a linked list.
     * @throws RPCException
     */
    private LinkedList<ICatLocDetail> requestLocalityHierarchy(
        ICatLocDetail locDetail) throws RPCException {
        LOGGER.info("Requesting locality hierarchy for " + locDetail.toString());
        final LinkedList<ICatLocDetail> localityHierarchy = new LinkedList<ICatLocDetail>();

        Locality locality = localityConverter.convertToSource(locDetail);
        while (locality != null) {
            localityHierarchy.addFirst(localityConverter.convertToTarget(locality));
            locality = locality.getParent();
        }
        //Remove first because COUNTRY is not supported yet.
        localityHierarchy.removeFirst();

        LOGGER.info("Requested hierarchy: " + localityHierarchy.toString());
        return localityHierarchy;
    }

    /**
     * Get cateogry suggestions.
     * @param cityLike
     * @param wordLength
     * @return list of CatLocSUggestionDetails
     * @throws RPCException
     */
    private ArrayList<CatLocSuggestionDetail> getCategorySuggestions(
        String cityLike, int wordLength) throws RPCException {
        final List<Category> categories = categoryService.getCategoriesByMinLength(wordLength, cityLike);
        final ArrayList<CatLocSuggestionDetail> categorySuggestions = new ArrayList<CatLocSuggestionDetail>();
        for (Category cat : categories) {
            categorySuggestions.add(new CatLocSuggestionDetail(
                cat.getParent() == null ? null : categoryConverter.convertToTarget(cat.getParent()),
                categoryConverter.convertToTarget(cat)));
        }
        return categorySuggestions;
    }

    /**
     * Get locality suggestions.
     * @param cityLike
     * @param wordLength
     * @return list of CatLocSUggestionDetails
     * @throws RPCException
     */
    private ArrayList<CatLocSuggestionDetail> getLocalitySuggestions(
        String localityLike, int wordLength) throws RPCException {
        final List<Locality> categories = localityService.getLocalitiesByMinLength(wordLength, localityLike);
        final ArrayList<CatLocSuggestionDetail> localitySuggestions = new ArrayList<CatLocSuggestionDetail>();
        for (Locality loc : categories) {
            if (loc.getType() != LocalityType.COUNTRY) { //skip adding united states to suggestions
                localitySuggestions.add(new CatLocSuggestionDetail(
                    loc.getParent() == null ? null : localityConverter.convertToTarget(loc.getParent()),
                    localityConverter.convertToTarget(loc)));
            }
        }
        return localitySuggestions;
    }

    /**
     * Get short cateogry suggestions.
     * Short word's lenght is less than wordLenght constraint.
     * @param cityLike
     * @param wordLength
     * @return list of CatLocSUggestionDetails
     * @throws RPCException
     */
    private ArrayList<CatLocSuggestionDetail> getShortCategorySuggestions(
        String cityLike, int wordLength) throws RPCException {
        final List<Category> categories = categoryService.getCategoriesByMaxLengthExcl(wordLength, cityLike);
        final ArrayList<CatLocSuggestionDetail> categorySuggestions = new ArrayList<CatLocSuggestionDetail>();
        for (Category cat : categories) {
            categorySuggestions.add(new CatLocSuggestionDetail(
                cat.getParent() == null ? null : categoryConverter.convertToTarget(cat.getParent()),
                categoryConverter.convertToTarget(cat)));
        }
        return categorySuggestions;
    }

    /**
     * Get short locality suggestions.
     * Short word's lenght is less than wordLenght constraint.
     * @param cityLike
     * @param wordLength
     * @return list of CatLocSUggestionDetails
     * @throws RPCException
     */
    private ArrayList<CatLocSuggestionDetail> getShortLocalitySuggestions(
        String localityLike, int wordLength) throws RPCException {
        final List<Locality> localities = localityService.getLocalitiesByMaxLengthExcl(wordLength, localityLike);
        final ArrayList<CatLocSuggestionDetail> localitySuggestions = new ArrayList<CatLocSuggestionDetail>();
        for (Locality loc : localities) {
            localitySuggestions.add(new CatLocSuggestionDetail(
                loc.getParent() == null ? null : localityConverter.convertToTarget(loc.getParent()),
                localityConverter.convertToTarget(loc)));
        }
        return localitySuggestions;
    }
}
