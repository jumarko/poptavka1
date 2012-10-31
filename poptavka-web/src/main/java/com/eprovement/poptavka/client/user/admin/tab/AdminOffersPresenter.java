package com.eprovement.poptavka.client.user.admin.tab;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.i18n.client.NumberFormat;
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
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.admin.AdminEventBus;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Presenter(view = AdminOffersView.class)
public class AdminOffersPresenter
        extends LazyPresenter<AdminOffersPresenter.AdminOffersInterface, AdminEventBus> {

    //history of changes
    private Map<Long, OfferDetail> dataToUpdate = new HashMap<Long, OfferDetail>();
    private Map<Long, OfferDetail> originalData = new HashMap<Long, OfferDetail>();
    //need to remember for asynchDataProvider if asking for more data
    private SearchModuleDataHolder searchDataHolder;
    // i18n
    private LocalizableMessages messages = GWT.create(LocalizableMessages.class);
    private NumberFormat currencyFormat = NumberFormat.getFormat(messages.currencyFormat());

    /**
     * Interface for widget AdminOffersView.
     */
    public interface AdminOffersInterface extends LazyView {

        // TABLE
        UniversalAsyncGrid<OfferDetail> getDataGrid();

        Column<OfferDetail, String> getPriceColumn();

        Column<OfferDetail, String> getOfferStatusColumn();

        Column<OfferDetail, Date> getOfferCreationDateColumn();

        Column<OfferDetail, Date> getOfferFinishDateColumn();

        // PAGER
        SimplePager getPager();

        ListBox getPageSizeCombo();

        int getPageSize();

        // BUTTONS
        Button getCommitBtn();

        Button getRollbackBtn();

        Button getRefreshBtn();

        Label getChangesLabel();

        // WIDGETS
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
    public void onInitOffers(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_OFFERS);
        eventBus.clearSearchContent();
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
    public void onDisplayAdminTabOffers(List<OfferDetail> offers) {
        view.getDataGrid().getDataProvider().updateRowData(
                view.getDataGrid().getStart(), offers);
        Storage.hideLoading();
    }

    //*************************************************************************/
    //                              DATA CHANGE                               */
    //*************************************************************************/
    /**
     * Store changes made in table data.
     */
    public void onAddOfferToCommit(OfferDetail data) {
        dataToUpdate.remove(data.getDemandId());
        dataToUpdate.put(data.getDemandId(), data);
        view.getChangesLabel().setText(Integer.toString(dataToUpdate.size()));
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
    }

    //*************************************************************************/
    //                           ACTION HANDLERS                              */
    //*************************************************************************/
    /**
     * Register handlers for widget actions.
     */
    @Override
    public void bindView() {
        addPageChangeHandler();
        //
        setPriceColumnUpdater();
        setOfferStatusColumnUpdater();
        setOfferFinishDateColumnUpdater();
        //
        addCommitButtonHandler();
        addRollbackButtonHandler();
        addRefreshButtonHandler();
    }

    /*
     * TABLE PAGE CHANGER
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

    /*
     * COLUMN UPDATER - FINNISH DATE
     */
    private void setOfferFinishDateColumnUpdater() {
        view.getOfferFinishDateColumn().setFieldUpdater(new FieldUpdater<OfferDetail, Date>() {

            @Override
            public void update(int index, OfferDetail object, Date value) {
                if (!object.getFinishDate().equals(value)) {
                    if (!originalData.containsKey(object.getDemandId())) {
                        originalData.put(object.getId(), new OfferDetail(object));
                    }
                    object.setFinishDate(value);
                    eventBus.addOfferToCommit(object);
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - STATUS
     */
    private void setOfferStatusColumnUpdater() {
        view.getOfferStatusColumn().setFieldUpdater(new FieldUpdater<OfferDetail, String>() {

            @Override
            public void update(int index, OfferDetail object, String value) {
                for (OfferStateType state : OfferStateType.values()) {
                    if (state.getValue().equals(value)) {
                        if (!object.getState().equals(state.name())) {
                            if (!originalData.containsKey(object.getDemandId())) {
                                originalData.put(object.getId(), new OfferDetail(object));
                            }
                            object.setState(state);
                            eventBus.addOfferToCommit(object);
                        }
                    }
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - PRICE
     */
    private void setPriceColumnUpdater() {
        view.getPriceColumn().setFieldUpdater(new FieldUpdater<OfferDetail, String>() {

            @Override
            public void update(int index, OfferDetail object, String value) {
                if (!object.getPrice().toString().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new OfferDetail(object));
                    }
                    object.setPrice(BigDecimal.valueOf(currencyFormat.parse(value)));
                    eventBus.addOfferToCommit(object);
                }
            }
        });
    }

    /*
     * COMMIT
     */
    private void addCommitButtonHandler() {
        view.getCommitBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (Window.confirm("Realy commit changes?")) {
                    view.getDataGrid().setFocus(true);
                    Storage.showLoading(Storage.MSGS.commit());
                    for (Long idx : dataToUpdate.keySet()) {
                        eventBus.updateOffer(dataToUpdate.get(idx));
                    }
                    Storage.hideLoading();
                    dataToUpdate.clear();
                    originalData.clear();
                    Window.alert("Changes commited");
                }
            }
        });
    }

    /*
     * ROLLBACK
     */
    private void addRollbackButtonHandler() {
        view.getRollbackBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dataToUpdate.clear();
                view.getDataGrid().setFocus(true);
                int idx = 0;
                for (OfferDetail data : originalData.values()) {
                    idx = view.getDataGrid().getVisibleItems().indexOf(data);
                    view.getDataGrid().getVisibleItem(idx).updateWholeOfferDetail(data);
                }
                view.getDataGrid().flush();
                view.getDataGrid().redraw();
                Window.alert(view.getChangesLabel().getText() + " changes rolledback.");
                view.getChangesLabel().setText("0");
                originalData.clear();
            }
        });
    }

    /*
     * REFRESH
     */
    private void addRefreshButtonHandler() {
        view.getRefreshBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (dataToUpdate.isEmpty()) {
                    view.getPager().startLoading();
                    eventBus.getDataCount(view.getDataGrid(), new SearchDefinition(searchDataHolder));
                } else {
                    Window.alert("You have some uncommited data. Do commit or rollback first");
                }
            }
        });
    }
}
