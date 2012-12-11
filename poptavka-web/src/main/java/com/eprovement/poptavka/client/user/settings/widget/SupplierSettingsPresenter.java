/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.category.CategorySelectorView;
import com.eprovement.poptavka.client.common.locality.LocalitySelectorView;
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
import com.google.gwt.user.client.ui.SimplePanel;
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

    public interface SupplierSettingsViewInterface extends LazyView {

        DisclosurePanel getCategories();

        DisclosurePanel getLocalities();

        Widget getWidgetView();
    }
    //
    private SettingsDetail settingsDetail;

    @Override
    public void bindView() {
        addCategoriesHandlers();
        addLocalitiesHandlers();
    }

    private void addCategoriesHandlers() {
        view.getCategories().addOpenHandler(new OpenHandler<DisclosurePanel>() {
            @Override
            public void onOpen(OpenEvent<DisclosurePanel> event) {
                eventBus.initCategoryWidget(
                        (SimplePanel) view.getCategories().getContent(),
                        Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS,
                        CategoryCell.DISPLAY_COUNT_DISABLED);
                setCategories(settingsDetail);
            }
        });
        view.getCategories().addCloseHandler(new CloseHandler<DisclosurePanel>() {
            @Override
            public void onClose(CloseEvent<DisclosurePanel> event) {
                SimplePanel categoryHolder = ((SimplePanel) view.getCategories().getContent());
                CategorySelectorView categoryWidget = (CategorySelectorView) categoryHolder.getWidget();
                ((HasText) view.getCategories().getHeader().asWidget()).setText(
                        Storage.MSGS.categories() + ": "
                        + categoryWidget.getCellBrowserSelectionModel().getSelectedSet().toString());
            }
        });
    }

    private void addLocalitiesHandlers() {
        view.getLocalities().addOpenHandler(new OpenHandler<DisclosurePanel>() {
            @Override
            public void onOpen(OpenEvent<DisclosurePanel> event) {
                eventBus.initLocalityWidget(
                        (SimplePanel) view.getLocalities().getContent(),
                        Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS,
                        CategoryCell.DISPLAY_COUNT_DISABLED);
                setLocalities(settingsDetail);
            }
        });
        view.getLocalities().addCloseHandler(new CloseHandler<DisclosurePanel>() {
            @Override
            public void onClose(CloseEvent<DisclosurePanel> event) {
                SimplePanel localityHolder = ((SimplePanel) view.getLocalities().getContent());
                LocalitySelectorView localityWidget = (LocalitySelectorView) localityHolder.getWidget();
                ((HasText) view.getLocalities().getHeader().asWidget()).setText(
                        Storage.MSGS.localities() + ": "
                        + localityWidget.getCellBrowserSelectionModel().getSelectedSet().toString());
            }
        });
    }

    public void initUserSettings(SimplePanel holder) {
        holder.setWidget(view.getWidgetView());
    }

    public void onSetSupplierSettings(SettingsDetail detail) {
        this.settingsDetail = detail;
        //Categories
        ((HasText) view.getCategories().getHeader().asWidget()).setText(
                Storage.MSGS.categories() + ": " + detail.getSupplier().getCategories().toString());
        //Localities
        ((HasText) view.getLocalities().getHeader().asWidget()).setText(
                Storage.MSGS.localities() + ": " + detail.getSupplier().getLocalities().toString());
    }

    private void setCategories(SettingsDetail detail) {
        SimplePanel categoryHolder = (SimplePanel) view.getCategories().getContent();
        CategorySelectorView categoryWidget = (CategorySelectorView) categoryHolder.getWidget();
        for (CategoryDetail catDetail : detail.getSupplier().getCategories()) {
            //Select in cellBrowser
            categoryWidget.getCellBrowserSelectionModel().setSelected(catDetail, true);
            //Display selected categories
            categoryWidget.getCellListDataProvider().getList().add(catDetail);
        }
    }

    private void setLocalities(SettingsDetail detail) {
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
