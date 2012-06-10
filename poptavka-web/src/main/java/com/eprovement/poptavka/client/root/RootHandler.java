package com.eprovement.poptavka.client.root;

import com.google.gwt.user.client.Cookies;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.main.errorDialog.ErrorDialogPopupView;
import com.eprovement.poptavka.client.service.demand.CategoryRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.LocalityRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.UserRPCServiceAsync;
import com.eprovement.poptavka.domain.address.LocalityType;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.exceptions.ExceptionUtils;
import com.eprovement.poptavka.shared.exceptions.RPCException;

@EventHandler
public class RootHandler extends BaseEventHandler<RootEventBus> {

    @Inject
    private LocalityRPCServiceAsync localityService = null;
    @Inject
    private CategoryRPCServiceAsync categoryService = null;
    @Inject
    private UserRPCServiceAsync userService = null;
    private ErrorDialogPopupView errorDialog;
    private static final Logger LOGGER = Logger.getLogger("MainHandler");

    public void onGetRootLocalities() {
        localityService.getLocalities(LocalityType.REGION,
                new AsyncCallback<ArrayList<LocalityDetail>>() {

                    @Override
                    public void onSuccess(ArrayList<LocalityDetail> list) {
                        eventBus.setLocalityData(LocalityDetail.REGION, list);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
                    }

                });
    }

    public void onGetChildLocalities(final int localityType, String locCode) {
        localityService.getLocalities(locCode,
                new AsyncCallback<ArrayList<LocalityDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
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
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
                public void onFailure(Throwable caught) {
                    if (caught instanceof RPCException) {
                        ExceptionUtils.showErrorDialog(errorDialog, caught);
                    }
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
                        public void onFailure(Throwable caught) {
                            if (caught instanceof RPCException) {
                                ExceptionUtils.showErrorDialog(errorDialog, caught);
                            }
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
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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