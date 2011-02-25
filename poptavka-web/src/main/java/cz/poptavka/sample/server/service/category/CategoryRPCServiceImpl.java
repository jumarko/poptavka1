package cz.poptavka.sample.server.service.category;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import cz.poptavka.sample.client.service.demand.CategoryRPCService;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.demand.CategoryService;

public class CategoryRPCServiceImpl extends AutoinjectingRemoteService
        implements CategoryRPCService {

    private CategoryService categoryService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryRPCServiceImpl.class);

    @Override
    public List<Category> getCategories() {
        // TODO Auto-generated method stub
        return categoryService.getAll();
    }

    @Autowired
    @Required
    public void setLocalityService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

}
