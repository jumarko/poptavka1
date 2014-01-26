/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.admin.AdminEventBus;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.type.ClientDemandType;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ivan.vlcek, edited by Martin Slavkovsky
 */
@Presenter(view = AdminDemandsView.class)
public class AdminDemandsPresenter
        extends LazyPresenter<AdminDemandsPresenter.AdminDemandsInterface, AdminEventBus> {

    //need to remember for asynchDataProvider if asking for more data
    private SearchModuleDataHolder searchDataHolder;
    private boolean editingCategories;

    /**
     * Interface for widget AdminMessagesView.
     */
    public interface AdminDemandsInterface extends LazyView {

        //TABLE
        UniversalAsyncGrid<FullDemandDetail> getDataGrid();

        Column<FullDemandDetail, String> getIdColumn();

        Column<FullDemandDetail, String> getCidColumn();

        Column<FullDemandDetail, String> getDemandTitleColumn();

        Column<FullDemandDetail, String> getDemandTypeColumn();

        Column<FullDemandDetail, String> getDemandStatusColumn();

        Column<FullDemandDetail, Date> getDemandExpirationColumn();

        Column<FullDemandDetail, Date> getDemandEndColumn();

        // PAGER
        SimplePager getPager();

        ListBox getPageSizeCombo();

        int getPageSize();

        // BUTTONS
        Button getCommitBtn();

        Button getRollbackBtn();

        Button getRefreshBtn();

        Label getChangesLabel();

        // DETAIL
        AdminDemandInfoView getAdminDemandDetail();

        Widget getWidgetView();
    }

    //*************************************************************************/
    //                          INITIALIZATOIN                                */
    //*************************************************************************/
    /**
     * Initial methods for handling starting.
     *
     * @param filter
     */
    public void onInitDemands(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_DEMANDS);
        searchDataHolder = filter;
        view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
    }

    //*************************************************************************/
    //                              DISPLAY                                   */
    //*************************************************************************/
    /**
     * Displays retrieved data.
     *
     * @param accessRoles -- list to display
     */
    public void onDisplayAdminTabDemands(List<FullDemandDetail> demands) {
        view.getDataGrid().getDataProvider().updateRowData(
                view.getDataGrid().getStart(), demands);
        eventBus.loadingHide();
    }

    //*************************************************************************/
    //                          ACTION HANDLERS                               */
    //*************************************************************************/
    /**
     * Register handlers for widget actions.
     */
    @Override
    public void bindView() {
        addPageChangeHandler();
        //
        setIdColumnUpdater();
        setCidColumnUpdater();
        setDemandTitleColumnUpdater();
        setDemandTypeColumnUpdater();
        setDemandStatusColumnUpdater();
        setDemandExpirationColumnUpdater();
        setDemandEndColumnUpdater();
        //
        addCommitButtonHandler();
        addRollbackButtonHandler();
        addRefreshButtonHandler();
        view.getAdminDemandDetail().getEditCatBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                editingCategories = true;
                final CatLocSelectorBuilder builder = new CatLocSelectorBuilder.Builder(Constants.ADMIN_DEMANDS)
                            .initCategorySelector()
                            .initSelectorManager()
                            .withCheckboxes()
                            .setSelectionRestriction(Constants.REGISTER_MAX_CATEGORIES)
                            .build();
                eventBus.initCatLocSelector(
                    view.getAdminDemandDetail().getSelectorWidgetPopup().getSelectorPanel(), builder);
                eventBus.setCatLocs(view.getAdminDemandDetail().getCategories(), builder.getInstanceId());
                view.getAdminDemandDetail().getSelectorWidgetPopup().show();
            }
        });
        view.getAdminDemandDetail().getEditLocBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                editingCategories = false;
                final CatLocSelectorBuilder builder = new CatLocSelectorBuilder.Builder(Constants.ADMIN_DEMANDS)
                            .initLocalitySelector()
                            .initSelectorManager()
                            .withCheckboxes()
                            .setSelectionRestriction(Constants.REGISTER_MAX_LOCALITIES)
                            .build();
                eventBus.initCatLocSelector(
                        view.getAdminDemandDetail().getSelectorWidgetPopup().getSelectorPanel(), builder);
                eventBus.setCatLocs(view.getAdminDemandDetail().getLocalities(), builder.getInstanceId());
                view.getAdminDemandDetail().getSelectorWidgetPopup().show();
            }
        });
        view.getAdminDemandDetail().getSelectorWidgetPopup().getSubmitBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (editingCategories) {
                    eventBus.fillCatLocs(
                            view.getAdminDemandDetail().getCategories(),
                            Constants.ADMIN_DEMANDS);
                } else {
                    eventBus.fillCatLocs(
                            view.getAdminDemandDetail().getLocalities(),
                            -Constants.ADMIN_DEMANDS);
                }
            }
        });
    }

    /**
     * TABLE PAGE CHANGER.
     */
    private void addPageChangeHandler() {
        view.getPageSizeCombo().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent arg0) {
                int page = view.getPager().getPageStart() / view.getPageSize();
                view.getPager().setPageStart(page * view.getPageSize());
                view.getPager().setPageSize(view.getPageSize());
            }
        });
    }

    /**
     * COLUMN UPDATER - END COLUMN.
     */
    private void setDemandEndColumnUpdater() {
        view.getDemandEndColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, Date>() {
            @Override
            public void update(int index, FullDemandDetail object, Date value) {
                if (!object.getEndDate().equals(value)) {
                    object.setEndDate(value);
                }
            }
        });
    }

    /**
     * COLUMN UPDATER - EXPIRATION.
     */
    private void setDemandExpirationColumnUpdater() {
        view.getDemandExpirationColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, Date>() {
            @Override
            public void update(int index, FullDemandDetail object, Date value) {
                if (!object.getValidTo().equals(value)) {
                    object.setValidTo(value);
                }
            }
        });
    }

    /**
     * COLUMN UPDATER - STATUS.
     */
    private void setDemandStatusColumnUpdater() {
        view.getDemandStatusColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {
            @Override
            public void update(int index, FullDemandDetail object, String value) {
                for (DemandStatus demandStatusType : DemandStatus.values()) {
                    if (demandStatusType.getValue().equals(value)) {
                        if (!object.getDemandStatus().equals(demandStatusType)) {
                            object.setDemandStatus(DemandStatus.valueOf(demandStatusType.name()));
                        }
                    }
                }
            }
        });
    }

    /**
     * COLUMN UPDATER - TYPE.
     */
    private void setDemandTypeColumnUpdater() {
        view.getDemandTypeColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {
            @Override
            public void update(int index, FullDemandDetail object, String value) {
                for (ClientDemandType clientDemandType : ClientDemandType.values()) {
                    if (clientDemandType.getValue().equals(value)) {
                        if (!object.getDemandType().equals(clientDemandType.name())) {
                            object.setDemandType(clientDemandType.name());
                        }
                    }
                }
            }
        });
    }

    /**
     * COLUMN UPDATER - TITLE.
     */
    private void setDemandTitleColumnUpdater() {
        view.getDemandTitleColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {
            @Override
            public void update(int index, FullDemandDetail object, String value) {
                if (!object.getDemandTitle().equals(value)) {
                    object.setDemandTitle(value);
                }
            }
        });
    }

    /**
     * COLUMN UPDATER - CID.
     */
    private void setCidColumnUpdater() {
        view.getCidColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {
            @Override
            public void update(int index, FullDemandDetail object, String value) {
//                view.getAdminDemandDetail().setDemandDetail(object);
                displayDemandDetail(object);
            }
        });
    }

    /**
     * COLUMN UPDATER - ID.
     */
    private void setIdColumnUpdater() {
        view.getIdColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {
            @Override
            public void update(int index, FullDemandDetail object, String value) {
//                view.getAdminDemandDetail().setDemandDetail(object);
                displayDemandDetail(object);
            }
        });
    }

    private void displayDemandDetail(FullDemandDetail demand) {
        view.getAdminDemandDetail().setDemandDetail(demand);
    }

    /**
     * COMMIT.
     */
    private void addCommitButtonHandler() {
        view.getCommitBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (Window.confirm("Realy commit changes?")) {
                    eventBus.loadingShow("Commiting...");
                    //TODO LATER if needed refactor - use editor instead
                    //eventBus.updateDemands(updatedFields);
                    view.getChangesLabel().setText("0");
                    view.getDataGrid().redraw();
                }
            }
        });
    }

    /**
     * ROLLBACK.
     */
    private void addRollbackButtonHandler() {
        view.getRollbackBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.getChangesLabel().setText("0");
                view.getDataGrid().redraw();
            }
        });
    }

    /**
     * REFRESH.
     */
    private void addRefreshButtonHandler() {
        view.getRefreshBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.getPager().startLoading();
            }
        });
    }

    public void onResponseUpdateDemands(Boolean result) {
        eventBus.loadingHide();
        if (result) {
            Window.alert("Changes commited");
        } else {
            Window.alert("Error while commiting");
        }
    }
}
