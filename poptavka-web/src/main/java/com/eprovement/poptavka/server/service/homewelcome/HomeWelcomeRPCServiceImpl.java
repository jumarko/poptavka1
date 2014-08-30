/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.service.homewelcome;

import com.eprovement.poptavka.client.service.demand.HomeWelcomeRPCService;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ILesserCatLocDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * This RPC handles all requests from HomeWelcome module.
 *
 * @author Martin Slavkovsky
 * @author Ivan Vlcek
 * @version 1.1
 */
@Configurable
public class HomeWelcomeRPCServiceImpl extends AutoinjectingRemoteService
        implements HomeWelcomeRPCService {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private CategoryService categoryService;
    private Converter<Category, ILesserCatLocDetail> lesserCategoryConverter;
    private Converter<Category, ICatLocDetail> categoryConverter;

    /**************************************************************************/
    /* Autowire services and converters                                       */
    /**************************************************************************/
    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setLesserCategoryConverter(
            @Qualifier("lesserCategoryConverter") Converter<Category, ILesserCatLocDetail> lesserCategoryConverter) {
        this.lesserCategoryConverter = lesserCategoryConverter;
    }

    @Autowired
    public void setCategoryConverter(
            @Qualifier("categoryConverter") Converter<Category, ICatLocDetail> categoryConverter) {
        this.categoryConverter = categoryConverter;
    }

    /**************************************************************************/
    /* Methods                                                                */
    /**************************************************************************/

    /**
     * Request all root categories in a lesser format.
     * @return list of lesser root cateogires
     * @throws RPCException
     */
    @Override
    public ArrayList<ILesserCatLocDetail> getRootCategories() throws RPCException {
        return lesserCategoryConverter.convertToTargetList(categoryService.getRootCategories());
    }

    /**
     * Request all root categories in a lesser format.
     * @return list of lesser root cateogires
     * @throws RPCException
     */
    @Override
    public ICatLocDetail getICatLocDetail(long categoryId) throws RPCException {
        return categoryConverter.convertToTarget(categoryService.getCategory(categoryId));
    }
}
