/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.detail;

import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.smallPopups.SimpleConfirmPopup;
import com.eprovement.poptavka.client.detail.DetailModuleBuilder;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsModuleEventBus;
import com.eprovement.poptavka.client.user.widget.detail.EditableDemandDetailPresenter.IEditableDemandDetailView;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.List;

/**
 *
 * @author mato
 */
@Presenter(view = EditableDemandDetailView.class, multiple = true)
public class EditableDemandDetailPresenter extends
        LazyPresenter<IEditableDemandDetailView, ClientDemandsModuleEventBus> {

    public interface IEditableDemandDetailView extends LazyView, IsWidget {

        void setDemanDetail(FullDemandDetail demandDetail);

        FluidRow getEditButtonsPanel();

        long getDemandId();

        SimpleConfirmPopup getSelectorPopup();

        FullDemandDetail updateDemandDetail(FullDemandDetail demandToUpdate);

        Button getEditCatBtn();

        Button getEditLocBtn();

        Button getSubmitButton();

        Button getCancelButton();

        Tooltip getSubmitBtnTooltip();

        List<ICatLocDetail> getCategories();

        List<ICatLocDetail> getLocalities();

        boolean isValid();

        void resetFields();
    }
    //history of changes
    private boolean editingCategories;

    @Override
    public void bindView() {
        view.getEditCatBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                editingCategories = true;
                eventBus.initCatLocSelector(
                        view.getSelectorPopup().getSelectorPanel(),
                        new CatLocSelectorBuilder.Builder()
                            .initCategorySelector()
                            .initSelectorManager()
                            .withCheckboxesOnLeafsOnly()
                            .setSelectionRestriction(Constants.REGISTER_MAX_CATEGORIES)
                            .build(),
                        Constants.USER_CLIENT_MODULE);
                eventBus.setCatLocs(view.getCategories());
                view.getSelectorPopup().show();
            }
        });
        view.getEditLocBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                editingCategories = false;
                eventBus.initCatLocSelector(
                        view.getSelectorPopup().getSelectorPanel(),
                        new CatLocSelectorBuilder.Builder()
                            .initLocalitySelector()
                            .initSelectorManager()
                            .withCheckboxesOnLeafsOnly()
                            .setSelectionRestriction(Constants.REGISTER_MAX_LOCALITIES)
                            .build(),
                        -Constants.USER_CLIENT_MODULE);
                eventBus.setCatLocs(view.getLocalities());
                view.getSelectorPopup().show();
            }
        });
        view.getSelectorPopup().getSubmitBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (editingCategories) {
                    eventBus.fillCatLocs(view.getCategories(), Constants.USER_CLIENT_MODULE);
                } else {
                    eventBus.fillCatLocs(view.getLocalities(), -Constants.USER_CLIENT_MODULE);
                }
                view.getSelectorPopup().show();
            }
        });
        view.getSubmitButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (view.isValid()) {
                    eventBus.requestUpdateDemand(view.getDemandId(), updateDemandDetail(new FullDemandDetail()));
                } else {
                    view.getSubmitBtnTooltip().show();
                }
            }
        });
        view.getCancelButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.setCustomWidget(DetailModuleBuilder.DEMAND_DETAIL_TAB, null);
            }
        });
    }

    public FullDemandDetail updateDemandDetail(FullDemandDetail detail) {
        return view.updateDemandDetail(detail);
    }
}
