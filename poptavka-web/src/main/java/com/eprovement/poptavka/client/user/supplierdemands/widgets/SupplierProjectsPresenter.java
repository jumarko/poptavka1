/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.main.Constants;
import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.user.supplierdemands.SupplierDemandsEventBus;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialProjectDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Presenter(view = SupplierProjectsView.class)
public class SupplierProjectsPresenter
        extends LazyPresenter<SupplierProjectsPresenter.SupplierProjectsLayoutInterface, SupplierDemandsEventBus> {

    public interface SupplierProjectsLayoutInterface extends LazyView, IsWidget {

        //Table
        UniversalAsyncGrid<SupplierPotentialProjectDetail> getGrid();

        //Columns
        Column<SupplierPotentialProjectDetail, Boolean> getCheckColumn();

        Column<SupplierPotentialProjectDetail, Boolean> getStarColumn();

        Column<SupplierPotentialProjectDetail, String> getSupplierNameColumn();

        Column<SupplierPotentialProjectDetail, String> getDemandTitleColumn();

        Column<SupplierPotentialProjectDetail, String> getRatingColumn();

        Column<SupplierPotentialProjectDetail, String> getPriceColumn();

        Column<SupplierPotentialProjectDetail, String> getReceivedColumn();

        Column<SupplierPotentialProjectDetail, Date> getUrgencyColumn();

        //Header
        Header getCheckHeader();

        //Buttons
        Button getOfferBtn();

        Button getReplyBtn();

        //ListBox
        ListBox getActions();

        //Other
        int getPageSize();

        List<Long> getSelectedIdList();

        Set<SupplierPotentialProjectDetail> getSelectedMessageList();

        SimplePanel getWrapperPanel();

        IsWidget getWidgetView();

        //Setter
        void setTitleLabel(String text);
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        // nothing
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    //viewType
    private ViewType type = ViewType.EDITABLE;
//    private DevelDetailWrapperPresenter detailSection = null;
    private SearchModuleDataHolder searchDataHolder;
    //attrribute preventing repeated loading of demand detail, when clicked on the same demand
    private long lastOpenedProjectContest = -1;


    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        // Field Updaters
        addCheckHeaderUpdater();
        addStarColumnFieldUpdater();
        addTextColumnFieldUpdaters();
        addDateColumnFieldUpdaters();
        // Listbox actions
        addActionChangeHandler();
        // Buttons Actions
        addOfferBtnClickHandler();
        addReplyBtnClickHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitSupplierProjects(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.SUPPLIER_POTENTIAL_PROJECTS);
        searchDataHolder = filter;
        view.getGrid().getDataCount(eventBus, searchDataHolder);

        view.getWidgetView().asWidget().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        //TODO myslisiet, aby sa DevelDetailWeapperPresenter mohol pouzivat vo viacerych moduloch
        //Ide to to, ze on ma nadefinovany jeden eventBus a ked sa pouziva vo viacerych, tak to pada
        //teda eventBus.addHandler(presenter) musi byt ten eventBus, ktory ma daty prezenter ako definovany
//        if (detailSection == null) {
//            detailSection = eventBus.addHandler(DevelDetailWrapperPresenter.class);
//            detailSection.initDetailWrapper(view.getWrapperPanel(), type);
//        }
    }
    /**************************************************************************/
    /* Business events handled by presenter */
    /**************************************************************************/
    /**
     * DEVEL METHOD
     *
     * Used for JRebel correct refresh. It is called from DemandModulePresenter, when removing instance of
     * SupplierListPresenter. it has to remove it's detailWrapper first.
     */
    public void develRemoveDetailWrapper() {
//        detailSection.develRemoveReplyWidget();
//        eventBus.removeHandler(detailSection);
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplaySupplierPotentialProjects(List<SupplierPotentialProjectDetail> data) {
        GWT.log("++ onResponseClientsOfferedProjects");

        view.getGrid().updateRowData(data);
    }

    /**
     * New data are fetched from db.
     *
     * @param demandId ID for demand detail
     * @param messageId ID for demand related contest
     * @param userMessageId ID for demand related contest
     */
    public void displayDetailContent(SupplierPotentialProjectDetail detail) {
        //TODO
        //copy role check from old implementation
        //
        //

        //can be solved by enum in future or can be accesed from storage class
//        detailSection.showLoading(DevelDetailWrapperPresenter.DEMAND);
//        eventBus.requestDemandDetail(detail.getDemandId(), type);
//        eventBus.requestDemandDetail(123L, type);

        //can be solved by enum in future or can be accesed from storage class
//        detailSection.showLoading(DevelDetailWrapperPresenter.SUPPLIER);
//        eventBus.requestSupplierDetail(detail.getSupplierId(), type);
//        eventBus.requestSupplierDetail(142811L, type);

        //add contest loading events and so on
//        detailSection.showLoading(DevelDetailWrapperPresenter.CHAT);
//        eventBus.requestContest(detail.getMessageId(),
//                detail.getUserMessageId(), Storage.getUser().getUserId());
//        eventBus.requestConversation(124L, 289L, 149L);

        //init default replyWidget
        //it is initalized now, because we do not need to have it visible before first demand selection
//        detailSection.initReplyWidget();
    }

    public void onSendMessageResponse(MessageDetail sentMessage, ViewType handlingType) {
        //neccessary check for method to be executed only in appropriate presenter
        if (type.equals(handlingType)) {
//            detailSection.addConversationMessage(sentMessage);
        }
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    // Field Updaters
    public void addCheckHeaderUpdater() {
        view.getCheckHeader().setUpdater(new ValueUpdater<Boolean>() {

            @Override
            public void update(Boolean value) {
                List<SupplierPotentialProjectDetail> rows = view.getGrid().getVisibleItems();
                for (SupplierPotentialProjectDetail row : rows) {
                    ((MultiSelectionModel) view.getGrid().getSelectionModel()).setSelected(row, value);
                }
            }
        });
    }

    public void addStarColumnFieldUpdater() {
        view.getStarColumn().setFieldUpdater(new FieldUpdater<SupplierPotentialProjectDetail, Boolean>() {

            @Override
            public void update(int index, SupplierPotentialProjectDetail object, Boolean value) {
//              TableDisplay obj = (TableDisplay) object;
                object.setStarred(!value);
                view.getGrid().redraw();
                Long[] item = new Long[]{object.getUserMessageId()};
                eventBus.requestStarStatusUpdate(Arrays.asList(item), !value);
            }
        });
    }

    public void addTextColumnFieldUpdaters() {
        FieldUpdater textFieldUpdater = new FieldUpdater<SupplierPotentialProjectDetail, String>() {

            @Override
            public void update(int index, SupplierPotentialProjectDetail object, String value) {
                if (lastOpenedProjectContest != object.getUserMessageId()) {
                    lastOpenedProjectContest = object.getUserMessageId();
                    object.setRead(true);
                    view.getGrid().redraw();
                    displayDetailContent(object);
                }
            }
        };
        view.getSupplierNameColumn().setFieldUpdater(textFieldUpdater);
        view.getDemandTitleColumn().setFieldUpdater(textFieldUpdater);
        view.getPriceColumn().setFieldUpdater(textFieldUpdater);
        view.getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getReceivedColumn().setFieldUpdater(textFieldUpdater);
    }

    public void addDateColumnFieldUpdaters() {
        view.getUrgencyColumn().setFieldUpdater(new FieldUpdater<SupplierPotentialProjectDetail, Date>() {

            @Override
            public void update(int index, SupplierPotentialProjectDetail object, Date value) {
                if (lastOpenedProjectContest != object.getUserMessageId()) {
                    lastOpenedProjectContest = object.getUserMessageId();
                    object.setRead(true);
                    view.getGrid().redraw();
                    displayDetailContent(object);
                }
            }
        });
    }

    private void addActionChangeHandler() {
        view.getActions().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                switch (view.getActions().getSelectedIndex()) {
                    case 1:
                        eventBus.requestReadStatusUpdate(view.getSelectedIdList(), true);
                        break;
                    case 2:
                        eventBus.requestReadStatusUpdate(view.getSelectedIdList(), false);
                        break;
                    case 3:
                        eventBus.requestStarStatusUpdate(view.getSelectedIdList(), true);
                        break;
                    case 4:
                        eventBus.requestStarStatusUpdate(view.getSelectedIdList(), false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //Buttons
    private void addOfferBtnClickHandler() {
        view.getOfferBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }

    private void addReplyBtnClickHandler() {
        view.getReplyBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }
}