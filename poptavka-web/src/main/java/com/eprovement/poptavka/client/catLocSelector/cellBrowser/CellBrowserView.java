package com.eprovement.poptavka.client.catLocSelector.cellBrowser;

import com.eprovement.poptavka.client.catLocSelector.CatLocSelectorInstanceManager.PresentersInterface;
import com.eprovement.poptavka.client.catLocSelector.cellBrowser.CellBrowserPresenter.CellBrowserInterface;
import com.eprovement.poptavka.client.catLocSelector.others.CatLocTreeViewModel;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.resources.cellbrowser.CustomCellBrowser;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.CellBrowser.Builder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;


public class CellBrowserView extends ReverseCompositeView<PresentersInterface>
        implements ProvidesValidate, CellBrowserInterface {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static CategoryCellBrowserUiBinder uiBinder = GWT.create(CategoryCellBrowserUiBinder.class);

    interface CategoryCellBrowserUiBinder extends UiBinder<Widget, CellBrowserView> {
    }

    /**************************************************************************/
    /* ATTRIBUTES                                                              */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField Label selectedCountLabel;
    @UiField SimplePanel cellBrowserHolder;
    @UiField(provided = true) CellList<ICatLocDetail> cellList;
    /** Class attributes. **/
    private CellBrowser cellBrowser;
    private MultiSelectionModel cellBrowserSelectionModel;
    private SingleSelectionModel cellListSelectionModel;
    private ListDataProvider<ICatLocDetail> cellListDataProvider;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        createCellList();

        initWidget(uiBinder.createAndBindUi(this));
    }

    private void createCellList() {
        cellListSelectionModel = new SingleSelectionModel<ICatLocDetail>(CatLocDetail.KEY_PROVIDER);

        cellList = new CellList<ICatLocDetail>(new ItemCell());
        cellList.setSelectionModel(cellListSelectionModel);

        cellListDataProvider = new ListDataProvider<ICatLocDetail>();
        cellListDataProvider.addDataDisplay(cellList);
    }

    @Override
    public void createCellBrowser() {
        final CellBrowser.Resources resource = GWT.create(CustomCellBrowser.class);

        cellBrowserSelectionModel = new MultiSelectionModel<ICatLocDetail>(CatLocDetail.KEY_PROVIDER);
        final CatLocTreeViewModel treeViewModel = new CatLocTreeViewModel(cellBrowserSelectionModel, presenter);

        final Builder<ICatLocDetail> builder = new Builder<ICatLocDetail>(treeViewModel, null);
        builder.pageSize(Integer.MAX_VALUE);
        builder.pagerFactory(null);
        builder.resources(resource);

        cellBrowser = builder.build();
        cellBrowser.setAnimationEnabled(true);
        cellBrowserHolder.setWidget(cellBrowser);
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setSelectedCountLabel(int count, int countRestriction) {
        selectedCountLabel.setText(
                Storage.MSGS.commonSelected() + " (" + count + "/" + countRestriction + "):");
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public CellBrowser getCellBrowser() {
        return cellBrowser;
    }

    @Override
    public CellList getCellList() {
        return cellList;
    }

    @Override
    public MultiSelectionModel<ICatLocDetail> getCellBrowserSelectionModel() {
        return cellBrowserSelectionModel;
    }

    @Override
    public SingleSelectionModel<ICatLocDetail> getCellListSelectionModel() {
        return cellListSelectionModel;
    }

    @Override
    public ListDataProvider<ICatLocDetail> getCellListDataProvider() {
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
 * The Cell used to render a selected Category detail {@link CategoryDetail} from CellBrowser.
 */
class ItemCell extends AbstractCell<ICatLocDetail> {

    @Override
    public void render(Context context, ICatLocDetail value, SafeHtmlBuilder sb) {
        // Value can be null, so do a null check..
        if (value == null) {
            return;
        }

        // Add item name.
        sb.appendHtmlConstant("<div class=\"selected-Item\">");
        sb.appendEscaped(value.toString());
        // Add image for remove.
        sb.appendHtmlConstant(AbstractImagePrototype.create(
                StyleResource.INSTANCE.images().errorIcon()).getHTML());
        sb.appendHtmlConstant("</div>");
    }
}
