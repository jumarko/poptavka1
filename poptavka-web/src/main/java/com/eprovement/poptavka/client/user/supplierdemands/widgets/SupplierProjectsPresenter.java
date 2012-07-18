/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.supplierdemands.SupplierDemandsEventBus;
import com.eprovement.poptavka.client.user.widget.DevelDetailWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableWidget;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Arrays;
import java.util.List;

@Presenter(view = SupplierProjectsView.class)
public class SupplierProjectsPresenter
        extends LazyPresenter<SupplierProjectsPresenter.SupplierProjectsLayoutInterface,
        SupplierDemandsEventBus> {

    public interface SupplierProjectsLayoutInterface extends LazyView, IsWidget {

        //Table
        UniversalTableWidget getTableWidget();

        //Buttons
        Button getOfferBtn();

        Button getReplyBtn();

        //ListBox
        ListBox getActions();

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
    private DevelDetailWrapperPresenter detailSection = null;
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
        addColumnFieldUpdaters();
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
        view.getTableWidget().getGrid().getDataCount(eventBus, searchDataHolder);

        view.getWidgetView().asWidget().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        //TODO myslisiet, aby sa DevelDetailWeapperPresenter mohol pouzivat vo viacerych moduloch
        //Ide to to, ze on ma nadefinovany jeden eventBus a ked sa pouziva vo viacerych, tak to pada
        //teda eventBus.addHandler(presenter) musi byt ten eventBus, ktory ma daty prezenter ako definovany
        eventBus.requestDetailWrapperPresenter();
    }

    /**************************************************************************/
    /* Business events handled by presenter */
    /**************************************************************************/
    public void onResponseDetailWrapperPresenter(DevelDetailWrapperPresenter detailSection) {
        this.detailSection = detailSection;
        this.detailSection.initDetailWrapper(view.getWrapperPanel(), type);
    }

    /**
     * DEVEL METHOD
     *
     * Used for JRebel correct refresh. It is called from DemandModulePresenter, when removing instance of
     * SupplierListPresenter. it has to remove it's detailWrapper first.
     */
    public void develRemoveDetailWrapper() {
        detailSection.develRemoveReplyWidget();
        eventBus.removeHandler(detailSection);
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplaySupplierPotentialProjects(List<SupplierPotentialProjectDetail> data) {
        GWT.log("++ onResponseClientsOfferedProjects");

        view.getTableWidget().getGrid().updateRowData(data);
    }

    /**
     * New data are fetched from db.
     *
     * @param demandId ID for demand detail
     * @param messageId ID for demand related contest
     * @param userMessageId ID for demand related contest
     */
    public void displayDetailContent(SupplierPotentialProjectDetail detail) {
//        detailSection.requestDemandDetail(detail.getDemandId(), type);
        detailSection.requestDemandDetail(123L, type);

//        detailSection.requestSupplierDetail(detail.getSupplierId(), type);
        detailSection.requestSupplierDetail(142811L, type);

//        detailSection.requestContest(detail.getMessageId(),
//                detail.getUserMessageId(), Storage.getUser().getUserId());
        detailSection.requestConversation(124L, 289L, 149L);
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
    // TableWidget handlers
    public void addCheckHeaderUpdater() {
        view.getTableWidget().getCheckHeader().setUpdater(new ValueUpdater<Boolean>() {

            @Override
            public void update(Boolean value) {
                List<SupplierPotentialProjectDetail> rows = view.getTableWidget().getGrid().getVisibleItems();
                for (SupplierPotentialProjectDetail row : rows) {
                    ((MultiSelectionModel)
                            view.getTableWidget().getGrid().getSelectionModel()).setSelected(row, value);
                }
            }
        });
    }

    public void addStarColumnFieldUpdater() {
        view.getTableWidget().getStarColumn().setFieldUpdater(
                new FieldUpdater<SupplierPotentialProjectDetail, Boolean>() {

                    @Override
                    public void update(int index, SupplierPotentialProjectDetail object, Boolean value) {
                        object.setStarred(!value);
                        view.getTableWidget().getGrid().redraw();
                        Long[] item = new Long[]{object.getUserMessageId()};
                        eventBus.requestStarStatusUpdate(Arrays.asList(item), !value);
                    }
                });
    }

    public void addColumnFieldUpdaters() {
        FieldUpdater textFieldUpdater = new FieldUpdater<SupplierPotentialProjectDetail, Object>() {

            @Override
            public void update(int index, SupplierPotentialProjectDetail object, Object value) {
                if (lastOpenedProjectContest != object.getUserMessageId()) {
                    lastOpenedProjectContest = object.getUserMessageId();
                    object.setRead(true);
                    view.getTableWidget().getGrid().redraw();
                    displayDetailContent(object);
                }
            }
        };
        view.getTableWidget().getClientNameColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getDemandTitleColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getPriceColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getUrgencyColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getRatingColumn().setFieldUpdater(textFieldUpdater);
        view.getTableWidget().getReceivedColumn().setFieldUpdater(textFieldUpdater);
    }

    // Widget action handlers
    private void addActionChangeHandler() {
        view.getActions().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                switch (view.getActions().getSelectedIndex()) {
                    case 1:
                        eventBus.requestReadStatusUpdate(view.getTableWidget().getSelectedIdList(), true);
                        break;
                    case 2:
                        eventBus.requestReadStatusUpdate(view.getTableWidget().getSelectedIdList(), false);
                        break;
                    case 3:
                        eventBus.requestStarStatusUpdate(view.getTableWidget().getSelectedIdList(), true);
                        break;
                    case 4:
                        eventBus.requestStarStatusUpdate(view.getTableWidget().getSelectedIdList(), false);
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