package cz.poptavka.sample.client.root;

import com.google.gwt.user.client.Cookies;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.service.demand.CategoryRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.UserRPCServiceAsync;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import cz.poptavka.sample.shared.domain.UserDetail;

@EventHandler
public class RootHandler extends BaseEventHandler<RootEventBus> {

    @Inject
    private LocalityRPCServiceAsync localityService = null;
    @Inject
    private CategoryRPCServiceAsync categoryService = null;
    @Inject
    private UserRPCServiceAsync userService = null;
    private static final Logger LOGGER = Logger.getLogger("MainHandler");

    public void onGetRootLocalities() {
        localityService.getLocalities(LocalityType.REGION,
                new AsyncCallback<ArrayList<LocalityDetail>>() {

                    @Override
                    public void onSuccess(ArrayList<LocalityDetail> list) {
                        eventBus.setLocalityData(LocalityDetail.REGION, list);
                    }

                    @Override
                    public void onFailure(Throwable arg0) {
                        // TODO empty
                    }

                });
    }

    public void onGetChildLocalities(final int localityType, String locCode) {
        localityService.getLocalities(locCode,
                new AsyncCallback<ArrayList<LocalityDetail>>() {

                    @Override
                    public void onFailure(Throwable arg0) {
                        // TODO empty
                    }

                    @Override
                    public void onSuccess(ArrayList<LocalityDetail> list) {
                        eventBus.setLocalityData(localityType, list);
                    }

                });
    }

    public void onGetRootCategories() {
        categoryService.getCategories(new AsyncCallback<ArrayList<CategoryDetail>>() {

            @Override
            public void onFailure(Throwable arg0) {
                // empty
            }

            @Override
            public void onSuccess(ArrayList<CategoryDetail> list) {
                // eventBus.setCategoryDisplayData(CategoryType.ROOT,
                // list);
                Window.alert("fix this method return value"
                        + "onGetRootCategories() - MainHandler.class");
            }

        });
    }

    public void onGetChildListCategories(final int newListPosition,
            String categoryId) {
        LOGGER.info("starting category service call");
        if (categoryId.equals("ALL_CATEGORIES")) {
            LOGGER.info(" --> root categories");
            categoryService.getCategories(new AsyncCallback<ArrayList<CategoryDetail>>() {

                @Override
                public void onFailure(Throwable arg0) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onSuccess(ArrayList<CategoryDetail> list) {
                    eventBus.setCategoryListData(newListPosition, list);
                }

            });
        } else {
            LOGGER.info(" --> child categories");
            categoryService.getCategoryChildren(Long.valueOf(categoryId),
                    new AsyncCallback<ArrayList<CategoryDetail>>() {

                        @Override
                        public void onSuccess(ArrayList<CategoryDetail> list) {
                            eventBus.setCategoryListData(newListPosition, list);
                        }

                        @Override
                        public void onFailure(Throwable arg0) {
                            // TODO Auto-generated method stub
                        }

                    });
        }
        LOGGER.info("ending category service call");
    }

    /**
     * Get User according to stored sessionID from DB after login.
     */
    public void onGetUser() {
        // get sessionId cookie
        String sessionID = Cookies.getCookie("sid");
        if (sessionID == null) {
            Window.alert("sessionID is null and it shouldn't be");
            return;
        }
        userService.getSignedUser(sessionID, new AsyncCallback<UserDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                eventBus.loadingHide();
                Window.alert("Error during getting logged User detail\n"
                        + caught.getMessage());
            }

            @Override
            public void onSuccess(UserDetail result) {
                eventBus.loadingShow(Storage.MSGS.progressCreatingUserInterface());
                eventBus.setUser(result);
            }

        });
    }

    @Inject
    public void setLocalityService(LocalityRPCServiceAsync service) {
        localityService = service;
    }

    @Inject
    public void setCategoryService(CategoryRPCServiceAsync service) {
        categoryService = service;
    }

}
