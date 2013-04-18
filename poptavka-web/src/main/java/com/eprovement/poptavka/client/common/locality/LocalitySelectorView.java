package com.eprovement.poptavka.client.common.locality;

import com.eprovement.poptavka.client.common.locality.LocalitySelectorPresenter.LocalitySelectorInterface;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.resources.cellbrowser.CustomCellBrowser;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class LocalitySelectorView extends ReverseCompositeView<LocalitySelectorPresenter>
        implements ProvidesValidate, LocalitySelectorInterface {

    private static LocalitySelectorUiBinder uiBinder = GWT.create(LocalitySelectorUiBinder.class);

    interface LocalitySelectorUiBinder extends UiBinder<Widget, LocalitySelectorView> {
    }

    /**************************************************************************/
    /* ATTRIBUTES                                                              */
    /**************************************************************************/
    /** UiBinder attirbutes. **/
    @UiField HTML loader;
    @UiField Label selectedCountLabel;
    @UiField SimplePanel cellBrowserHolder;
    @UiField(provided = true) CellList<LocalityDetail> cellList;
    /** Class attributes. **/
    private MultiSelectionModel cellBrowserSelectionModel = new MultiSelectionModel();
    private SingleSelectionModel cellListSelectionModel = new SingleSelectionModel();
    private ListDataProvider<LocalityDetail> cellListDataProvider = new ListDataProvider<LocalityDetail>();

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        cellList = new CellList<LocalityDetail>(new ItemCell());
        cellList.setSelectionModel(cellListSelectionModel);
        cellListDataProvider.addDataDisplay(cellList);

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void createCellBrowser(int checkboxes, int displayCountsOfWhat) {
        CellBrowser.Resources resource = GWT.create(CustomCellBrowser.class);
        CellBrowser cellBrowser = new CellBrowser(new LocalityTreeViewModel(
                cellBrowserSelectionModel,
                presenter.getLocalityService(),
                presenter.getEventBus(),
                checkboxes, displayCountsOfWhat), null, resource);
        cellBrowser.setAnimationEnabled(true);
        cellBrowserHolder.setWidget(cellBrowser);
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setSelectedCountLabel(int count) {
        selectedCountLabel.setText(
                Storage.MSGS.commonSelected().concat(" (").concat(
                Integer.toString(count)).concat("/").concat(
                Integer.toString(Constants.REGISTER_MAX_LOCALITIES)).concat("):"));
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

    @Override
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
}

/**
 * The Cell used to render a selected Locality detail {@link LocalityDetail} from CellBrowser.
 */
class ItemCell extends AbstractCell<LocalityDetail> {

    @Override
    public void render(Context context, LocalityDetail value, SafeHtmlBuilder sb) {
        // Value can be null, so do a null check..
        if (value == null) {
            return;
        }

        // Add category name.
        sb.appendHtmlConstant("<div class=\"selected-Item\">");
        sb.appendEscaped(value.getName());
        // Add image for remove.
        sb.appendHtmlConstant(AbstractImagePrototype.create(
                StyleResource.INSTANCE.images().errorIcon()).getHTML());
        sb.appendHtmlConstant("</div>");
    }
}
