package cz.poptavka.sample.client.common;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.annotation.InjectService;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.common.category.CategorySelectorPresenter.CategoryType;
import cz.poptavka.sample.client.service.demand.CategoryRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

/**
 * Handler for common used RPC calls for localities and categories and other common components.
 *
 * @author Beho
 *
 */
@EventHandler
public class CommonHandler extends BaseEventHandler<CommonEventBus> {

    private LocalityRPCServiceAsync localityService = null;
    private CategoryRPCServiceAsync categoryService = null;

    private static final Logger LOGGER = Logger.getLogger("CommonHandler");


    @InjectService
    public void setLocalityService(LocalityRPCServiceAsync service) {
        localityService = service;
    }

    @InjectService
    public void setCategoryService(CategoryRPCServiceAsync service) {
        categoryService = service;
    }

    public void onGetLocalities() {
        localityService.getLocalities(LocalityType.DISTRICT, new AsyncCallback<ArrayList<LocalityDetail>>() {
            @Override
            public void onSuccess(ArrayList<LocalityDetail> list) {
                eventBus.setLocalityData(LocalityType.DISTRICT, list);
            }

            @Override
            public void onFailure(Throwable arg0) {
                //TODO empty
            }
        });
    }

    public void onGetChildLocalities(final LocalityType type, String locCode) {
        localityService.getLocalities(locCode, new AsyncCallback<ArrayList<LocalityDetail>>() {
            @Override
            public void onFailure(Throwable arg0) {
                //TODO empty
            }

            @Override
            public void onSuccess(ArrayList<LocalityDetail> list) {
                eventBus.setLocalityData(type, list);
            }
        });
    }

    public void onGetRootCategories() {
        categoryService.getCategories(new AsyncCallback<ArrayList<CategoryDetail>>() {

            @Override
            public void onFailure(Throwable arg0) {
                //empty
            }

            @Override
            public void onSuccess(ArrayList<CategoryDetail> list) {
                eventBus.setCategoryData(CategoryType.ROOT, list);
            }
        });
    }

    public void onGetChildCategories(final CategoryType type, String categoryId) {
        LOGGER.info("starting category service call");
        categoryService.getCategoryChildren(categoryId, new AsyncCallback<ArrayList<CategoryDetail>>() {

            @Override
            public void onSuccess(ArrayList<CategoryDetail> list) {
                eventBus.setCategoryData(type, list);
            }

            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub

            }
        });
        LOGGER.info("ending category service call");
    }
}
