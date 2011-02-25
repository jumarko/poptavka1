package cz.poptavka.sample.client.main;


import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.annotation.InjectService;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.CategoryRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.domain.demand.Category;

@EventHandler
public class MainHandler extends BaseEventHandler<MainEventBus> {

    private static final Logger LOGGER = Logger.getLogger(MainHandler.class.getName());
    @Inject
    public MainHandler() {
    }

    private LocalityRPCServiceAsync service = null;
    private CategoryRPCServiceAsync categoryService = null;

    public void onStart() {
        service.getLocalities(LocalityType.DISTRICT, new AsyncCallback<List<Locality>>() {

            @Override
            public void onSuccess(List<Locality> result) {
                LOGGER.info("result " + result);
                // TODO Auto-generated method stub
                eventBus.setData(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                eventBus.setData(null);
            }
        });

        categoryService.getCategories(new AsyncCallback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> result) {
                LOGGER.info("result " + result);
                // TODO Auto-generated method stub
                eventBus.setCategories(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                eventBus.setData(null);
            }
        });

    }

    @InjectService
    public void setService(LocalityRPCServiceAsync service) {
        this.service = service;
    }

    @InjectService
    public void setCategoryService(CategoryRPCServiceAsync categoryService) {
        this.categoryService = categoryService;
    }

}
