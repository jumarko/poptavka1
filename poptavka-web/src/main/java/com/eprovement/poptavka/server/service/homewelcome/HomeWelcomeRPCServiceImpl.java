/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.service.homewelcome;

import com.eprovement.poptavka.client.service.demand.HomeWelcomeRPCService;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * This RPC handles all requests from HomeWelcome module.
 * @author Martin Slavkovsky
 */
@Configurable
public class HomeWelcomeRPCServiceImpl extends AutoinjectingRemoteService
        implements HomeWelcomeRPCService {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private CategoryService categoryService;
    private Converter<Category, ICatLocDetail> categoryConverter;

    /**************************************************************************/
    /* Autowire services and converters                                       */
    /**************************************************************************/
    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
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
     * Request all root categories.
     * @return list of root cateogires
     * @throws RPCException
     */
    @Override
    public ArrayList<ICatLocDetail> getRootCategories() throws RPCException {
        return categoryConverter.convertToTargetList(categoryService.getRootCategories());
    }
}
