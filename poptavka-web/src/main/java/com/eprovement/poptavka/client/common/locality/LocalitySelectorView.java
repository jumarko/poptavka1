package com.eprovement.poptavka.client.common.locality;

import com.eprovement.poptavka.client.common.locality.LocalitySelectorPresenter.LocalitySelectorInterface;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.view.ReverseViewInterface;

public class LocalitySelectorView extends Composite
        implements ProvidesValidate, ReverseViewInterface<LocalitySelectorPresenter>, LocalitySelectorInterface {

    private static LocalitySelectorUiBinder uiBinder = GWT.create(LocalitySelectorUiBinder.class);

    interface LocalitySelectorUiBinder extends UiBinder<Widget, LocalitySelectorView> {
    }

    /**************************************************************************/
    /* PRESENTER                                                              */
    /**************************************************************************/
    @Override
    public void setPresenter(LocalitySelectorPresenter presenter) {
        this.localitySelectorPresenter = presenter;
    }

    @Override
    public LocalitySelectorPresenter getPresenter() {
        return localitySelectorPresenter;
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                              */
    /**************************************************************************/
    //Presenter
    private LocalitySelectorPresenter localitySelectorPresenter;
    @UiField
    HTML loader;
    //Cell Brower
    @UiField(provided = true)
    CellBrowser cellBrowser;
    //Cell List
    @UiField(provided = true)
    CellList<LocalityDetail> cellList;
    //Selection Moders
    MultiSelectionModel cellBrowserSelectionModel = new MultiSelectionModel();
    SingleSelectionModel cellListSelectionModel = new SingleSelectionModel();
    //Data Providers
    ListDataProvider<LocalityDetail> cellListDataProvider = new ListDataProvider<LocalityDetail>();

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        cellBrowser = new CellBrowser(new LocalityTreeViewModel(
                cellBrowserSelectionModel,
                localitySelectorPresenter.getLocalityService()), null);
        cellBrowser.setSize("500px", "200px");
        cellBrowser.setAnimationEnabled(true);

        cellList = new CellList<LocalityDetail>(new LocalityCell());
        cellList.setSelectionModel(cellListSelectionModel);
        cellListDataProvider.addDataDisplay(cellList);

        initWidget(uiBinder.createAndBindUi(this));
        StyleResource.INSTANCE.layout().ensureInjected();
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public MultiSelectionModel<LocalityDetail> getCellBrowserSelectionModel() {
        return cellBrowserSelectionModel;
    }

    @Override
    public SingleSelectionModel<LocalityDetail> getCellListSelectionModel() {
        return cellListSelectionModel;
    }

    public ListDataProvider<LocalityDetail> getCellListDataProvider() {
        return cellListDataProvider;
    }

    @Override
    public boolean isValid() {
        return !cellListDataProvider.getList().isEmpty();
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public void toggleLoader() {
        loader.setVisible(!loader.isVisible());
    }
}
