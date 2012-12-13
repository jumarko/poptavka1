/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.category.CategorySelectorView;
import com.eprovement.poptavka.client.common.locality.LocalitySelectorView;
import com.eprovement.poptavka.client.common.services.ServicesSelectorView;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.widget.SupplierSettingsPresenter.SupplierSettingsViewInterface;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingsDetail;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = SupplierSettingsView.class, multiple = true)
public class SupplierSettingsPresenter extends LazyPresenter<SupplierSettingsViewInterface, SettingsEventBus> {

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface SupplierSettingsViewInterface extends LazyView {

        //Panels
        DisclosurePanel getCategories();

        DisclosurePanel getLocalities();

        DisclosurePanel getServices();

        //TextBoxes
        TextBox getSupplierRating();

        //Others
        Widget getWidgetView();
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    private SettingsDetail settingsDetail;

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        addCategoriesHandlers();
        addLocalitiesHandlers();
        addServicesHandlers();
    }

    /**************************************************************************/
    /* BIND - Helper methods                                                  */
    /**************************************************************************/
    private void addCategoriesHandlers() {
        final PopupPanel categoriesPopup = (PopupPanel) view.getCategories().getContent();
        view.getCategories().addOpenHandler(new OpenHandler<DisclosurePanel>() {
            @Override
            public void onOpen(OpenEvent<DisclosurePanel> event) {
                eventBus.initCategoryWidget(
                        categoriesPopup,
                        Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS,
                        CategoryCell.DISPLAY_COUNT_DISABLED);
                categoriesPopup.center();
//                setCategoriesContent(settingsDetail); //Ako na to???
            }
        });
        categoriesPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                view.getCategories().setOpen(false);
                SimplePanel categoryHolder = ((SimplePanel) view.getCategories().getContent());
                CategorySelectorView categoryWidget = (CategorySelectorView) categoryHolder.getWidget();
                setCategoriesHeader(categoryWidget.getCellBrowserSelectionModel().getSelectedSet().toString());
            }
        });
    }

    private void addLocalitiesHandlers() {
        final PopupPanel localitiesPopup = (PopupPanel) view.getCategories().getContent();
        view.getLocalities().addOpenHandler(new OpenHandler<DisclosurePanel>() {
            @Override
            public void onOpen(OpenEvent<DisclosurePanel> event) {
                event.getSource();
                eventBus.initLocalityWidget(
                        localitiesPopup,
                        Constants.WITH_CHECK_BOXES,
                        CategoryCell.DISPLAY_COUNT_DISABLED);
                localitiesPopup.center();
                setLocalitiesContent(settingsDetail);
            }
        });
        localitiesPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                view.getLocalities().setOpen(false);
                SimplePanel localityHolder = ((SimplePanel) view.getLocalities().getContent());
                LocalitySelectorView localityWidget = (LocalitySelectorView) localityHolder.getWidget();
                ((HasText) view.getLocalities().getHeader().asWidget()).setText(
                        localityWidget.getCellBrowserSelectionModel().getSelectedSet().toString());
            }
        });
    }

    private void addServicesHandlers() {
        final SimplePanel servicesPanel = (SimplePanel) view.getServices().getContent();
        view.getServices().addOpenHandler(new OpenHandler<DisclosurePanel>() {
            @Override
            public void onOpen(OpenEvent<DisclosurePanel> event) {
                eventBus.initServicesWidget(servicesPanel);
            }
        });
        view.getServices().addCloseHandler(new CloseHandler<DisclosurePanel>() {
            @Override
            public void onClose(CloseEvent<DisclosurePanel> event) {
                ServicesSelectorView servicesWidget = (ServicesSelectorView) servicesPanel.getWidget();
                setServicesHeader(Integer.toString(servicesWidget.getSelectedService()));
            }
        });
    }


    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public void initUserSettings(SimplePanel holder) {
        holder.setWidget(view.getWidgetView());
    }

    /**************************************************************************/
    /* METHODS                                                                */
    /**************************************************************************/
    public void onSetSupplierSettings(SettingsDetail detail) {
        this.settingsDetail = detail;
        if (detail.getSupplier().getOverallRating() != null) {
            view.getSupplierRating().setText(Integer.toString(detail.getSupplier().getOverallRating()));
        }
        setCategoriesHeader(detail.getSupplier().getCategories().toString());
        setLocalitiesHeader(detail.getSupplier().getLocalities().toString());
        setServicesHeader(detail.getSupplier().getServices().toString());
    }

    /**************************************************************************/
    /* HELPER METHODS                                                         */
    /**************************************************************************/
    /** HEADER. **/
    private void setCategoriesHeader(String headerText) {
        ((HasText) view.getCategories().getHeader().asWidget()).setText(
                Storage.MSGS.categories() + ": " + headerText);
    }

    private void setLocalitiesHeader(String headerText) {
        ((HasText) view.getLocalities().getHeader().asWidget()).setText(
                Storage.MSGS.localities() + ": " + headerText);
    }

    private void setServicesHeader(String headerText) {
        ((HasText) view.getServices().getHeader().asWidget()).setText(
                Storage.MSGS.services() + ": " + headerText);
    }

    /** CONTENT. **/
    private void setCategoriesContent(SettingsDetail detail) {
        PopupPanel categoryHolder = (PopupPanel) view.getCategories().getContent();
        CategorySelectorView categoryWidget = (CategorySelectorView) categoryHolder.getWidget();
        for (CategoryDetail catDetail : detail.getSupplier().getCategories()) {
            //Select in cellBrowser
            categoryWidget.getCellBrowserSelectionModel().setSelected(catDetail, true);
            //Display selected categories
            categoryWidget.getCellListDataProvider().getList().add(catDetail);
        }
    }

    private void setLocalitiesContent(SettingsDetail detail) {
        SimplePanel localityHolder = (SimplePanel) view.getLocalities().getContent();
        LocalitySelectorView localityWidget = (LocalitySelectorView) localityHolder.getWidget();
        for (LocalityDetail locDetail : detail.getSupplier().getLocalities()) {
            //Select in cellBrowser
            localityWidget.getCellBrowserSelectionModel().setSelected(locDetail, true);
            //Display selected categories
            localityWidget.getCellListDataProvider().getList().add(locDetail);
        }
    }
}
