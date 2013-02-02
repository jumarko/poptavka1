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
import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.widget.SupplierSettingsPresenter.SupplierSettingsViewInterface;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.Arrays;
import java.util.List;

/**
 * TODO = check for number of categories according to payed service
 *      = authentication code when updating email
 *      = what else need to do when updating services? send invoice??.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = SupplierSettingsView.class, multiple = true)
public class SupplierSettingsPresenter extends LazyPresenter<SupplierSettingsViewInterface, SettingsEventBus> {

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface SupplierSettingsViewInterface extends LazyView {

        /** SETTERS. **/
        void setSupplierSettings(SettingDetail detail);

        void setCategoriesHeader(String headerText);

        void setLocalitiesHeader(String headerText);

        void setServicesHeader(String headerText);

        void setCategoriesList(List<CategoryDetail> categoriesList);

        void setLocalitiesList(List<LocalityDetail> localitiesList);

        void setServicesList(List<ServiceDetail> servicesList);

        SettingDetail updateSupplierSettings(SettingDetail detail);

        /** GETTERS. **/
        //Panels
        DisclosurePanel getCategoriesPanel();

        DisclosurePanel getLocalitiesPanel();

        DisclosurePanel getServicesPanel();

        PopupPanel getCategorySelectorPopup();

        PopupPanel getLocalitySelectorPopup();

        List<CategoryDetail> getCategories();

        List<LocalityDetail> getLocalities();

        List<ServiceDetail> getServices();

        void initChangeCheking(String originalString);

        void evaluateChanges(String key, String newString);

        //TextBoxes
        TextBox getSupplierRating();

        TextBox getStatus();

        //Others
        boolean isSettingChange();

        Widget getWidgetView();
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    private static final String CATEGORIES = "categories";
    private static final String LOCALITIES = "localities";
    private static final String SERVICES = "services";

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        addCategoriesHandlers();
        addLocalitiesHandlers();
        addServicesHandlers();
        addStatusHandler();
    }

    /**************************************************************************/
    /* BIND - Helper methods                                                  */
    /**************************************************************************/
    private void addCategoriesHandlers() {
        view.getCategoriesPanel().addOpenHandler(new OpenHandler<DisclosurePanel>() {
            @Override
            public void onOpen(OpenEvent<DisclosurePanel> event) {
                eventBus.initCategoryWidget(
                        view.getCategorySelectorPopup(),
                        Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS,
                        CategoryCell.DISPLAY_COUNT_DISABLED,
                        view.getCategories());
                view.getCategorySelectorPopup().center();
                view.initChangeCheking(view.getCategories().toString());
            }
        });
        view.getCategorySelectorPopup().addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                view.getCategoriesPanel().setOpen(false);
                CategorySelectorView categoryWidget =
                        (CategorySelectorView) view.getCategorySelectorPopup().getWidget();
                view.setCategoriesList(categoryWidget.getCellListDataProvider().getList());
                view.setCategoriesHeader(categoryWidget.getCellBrowserSelectionModel().getSelectedSet().toString());
                view.evaluateChanges(CATEGORIES,
                        categoryWidget.getCellBrowserSelectionModel().getSelectedSet().toString());
            }
        });
    }

    private void addLocalitiesHandlers() {
        view.getLocalitiesPanel().addOpenHandler(new OpenHandler<DisclosurePanel>() {
            @Override
            public void onOpen(OpenEvent<DisclosurePanel> event) {
                event.getSource();
                eventBus.initLocalityWidget(
                        view.getLocalitySelectorPopup(),
                        Constants.WITH_CHECK_BOXES,
                        CategoryCell.DISPLAY_COUNT_DISABLED,
                        view.getLocalities());
                view.getLocalitySelectorPopup().center();
                view.initChangeCheking(view.getLocalities().toString());
            }
        });
        view.getLocalitySelectorPopup().addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                view.getLocalitiesPanel().setOpen(false);
                LocalitySelectorView localityWidget =
                        (LocalitySelectorView) view.getLocalitySelectorPopup().getWidget();
                view.setLocalitiesList(localityWidget.getCellListDataProvider().getList());
                view.setLocalitiesHeader(localityWidget.getCellBrowserSelectionModel().getSelectedSet().toString());
                view.evaluateChanges(LOCALITIES,
                        localityWidget.getCellBrowserSelectionModel().getSelectedSet().toString());
            }
        });
    }

    private void addServicesHandlers() {
        final SimplePanel servicesPanel = (SimplePanel) view.getServicesPanel().getContent();
        view.getServicesPanel().addOpenHandler(new OpenHandler<DisclosurePanel>() {
            @Override
            public void onOpen(OpenEvent<DisclosurePanel> event) {
                //todo set widget
                eventBus.initServicesWidget(servicesPanel);
                view.initChangeCheking(view.getServices().toString());
            }
        });
        view.getServicesPanel().addCloseHandler(new CloseHandler<DisclosurePanel>() {
            @Override
            public void onClose(CloseEvent<DisclosurePanel> event) {
                ServicesSelectorView servicesWidget = (ServicesSelectorView) servicesPanel.getWidget();
                //TODO RELEASE
                view.setServicesList(Arrays.asList(servicesWidget.getSelectedService()));
                view.setServicesHeader(Arrays.asList(servicesWidget.getSelectedService()).toString());
                view.evaluateChanges(SERVICES, view.getServices().toString());
            }
        });
    }

    public void addStatusHandler() {
        view.getStatus().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                eventBus.updateSupplierStatus(view.isSettingChange());
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
    public void onSetSupplierSettings(SettingDetail detail) {
        view.setSupplierSettings(detail);
    }
}
