package com.eprovement.poptavka.server.service.homewelcome;

import com.eprovement.poptavka.client.service.demand.HomeWelcomeRPCService;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

@Configurable
public class HomeWelcomeRPCServiceImpl extends AutoinjectingRemoteService
        implements HomeWelcomeRPCService {

    /**************************************************************************/
    /* RPC Services                                                           */
    /**************************************************************************/
    private CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**************************************************************************/
    /* Converters                                                             */
    /**************************************************************************/
    private Converter<Category, CategoryDetail> categoryConverter;

    @Autowired
    public void setCategoryConverter(
            @Qualifier("categoryConverter") Converter<Category, CategoryDetail> categoryConverter) {
        this.categoryConverter = categoryConverter;
    }

    /**************************************************************************/
    /* Methods                                                                */
    /**************************************************************************/
    @Override
    public ArrayList<CategoryDetail> getRootCategories() throws RPCException {
        return categoryConverter.convertToTargetList(categoryService.getRootCategories());
    }
}
