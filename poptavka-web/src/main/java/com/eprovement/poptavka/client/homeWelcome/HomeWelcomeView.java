package com.eprovement.poptavka.client.homeWelcome;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.homeWelcome.interfaces.IHomeWelcomeView;
import com.eprovement.poptavka.client.homeWelcome.interfaces.IHomeWelcomeView.IHomeWelcomePresenter;
import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.client.service.demand.SimpleRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import java.util.ArrayList;


/**************************************************************************/
/* Class: Root Category Cell                                                     */
public class HomeWelcomeView extends ReverseCompositeView<IHomeWelcomePresenter> implements IHomeWelcomeView {

    private static HomeWelcomeViewUiBinder uiBinder = GWT.create(HomeWelcomeViewUiBinder.class);
    private SimpleRPCServiceAsync simpleService;

    interface HomeWelcomeViewUiBinder extends UiBinder<Widget, HomeWelcomeView> {
    }

    public HomeWelcomeView() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField HorizontalPanel categorySection;
    @UiField Button button;
    @UiField Button demandCreateBtn;
    @UiField Button securedButton;
    @UiField Button sendUsEmailButton;


    //
    private final SingleSelectionModel<CategoryDetail> selectionRootModel =
            new SingleSelectionModel<CategoryDetail>();

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public HomeWelcomeView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
        button.setText(firstName);
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    @UiHandler("button")
    void onClick(ClickEvent e) {
        Window.alert("Hello!");
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    public void setText(String text) {
        button.setText(text);
    }

    @Override
    public void displayCategories(int columns, ArrayList<CategoryDetail> rootCategories) {
        if (rootCategories.isEmpty()) {
            categorySection.clear();
            return;
        }
        categorySection.clear();
        int size = rootCategories.size();
        int subSize = 0;
        int startIdx = 0;
        if (size < columns) {
            columns = size;
        }
        while (columns != 0) {
            if (size % columns == 0) {
                subSize = size / columns;
            } else {
                subSize = size / columns + 1;
            }
            CellList cellList = null;
            cellList = new CellList<CategoryDetail>(new RootCategoryCell());
            //TOTO Martin - loading indikator nepomoze, pretoze tieto cellListy sa vytvaraju
            //tu v case, ked su data uz k dispozicii
            cellList.setLoadingIndicator(new Label(Storage.MSGS.loadingRootCategories()));
            cellList.setRowCount(subSize, true);
            cellList.setSelectionModel(selectionRootModel);
            cellList.setRowData(rootCategories.subList(startIdx, startIdx + subSize));
            categorySection.add(cellList);
            startIdx += subSize;
            size -= subSize;
            columns--;
        }
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public HorizontalPanel getCategorySection() {
        return categorySection;
    }

    @Override
    public SingleSelectionModel<CategoryDetail> getCategorySelectionModel() {
        return selectionRootModel;
    }

    public String getText() {
        return button.getText();
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Inject
    void setSimpleService(SimpleRPCServiceAsync service) {
        simpleService = service;
    }


    @Override
    public HasClickHandlers getCreateDemandButton() {
        return demandCreateBtn;
    }

    @Override
    public HasClickHandlers getSecuredButton() {
        return securedButton;
    }

    @Override
    public HasClickHandlers getSendUsEmailButton() {
        return sendUsEmailButton;
    }
}
/**************************************************************************/
class RootCategoryCell extends AbstractCell<CategoryDetail> {

    @Override
    public void render(Cell.Context context, CategoryDetail value, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to
         * cells if the underlying data contains a null, or if the data arrives
         * out of order.
         */
        if (value == null) {
            return;
        }

        StringBuilder text = new StringBuilder();

        text.append(value.getName().replaceAll("-a-", " a ").replaceAll("-", ", "));
        text.append(" (");
        text.append(value.getDemandsCount());
        text.append(")");

        sb.appendEscaped(text.toString());
    }
}
