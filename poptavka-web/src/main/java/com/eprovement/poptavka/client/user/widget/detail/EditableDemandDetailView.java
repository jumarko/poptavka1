package com.eprovement.poptavka.client.user.widget.detail;

import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.locality.LocalityCell;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class EditableDemandDetailView extends Composite {

    private static DemandDetailViewUiBinder uiBinder = GWT.create(DemandDetailViewUiBinder.class);

    interface DemandDetailViewUiBinder extends UiBinder<Widget, EditableDemandDetailView> {
    }
    @UiField(provided = true) CellList categories, localities;
    @UiField TextBox demandName, price, endDate, validTo, type, maxNumberOfSuppliers,
    minSupplierRating, excludedSuppliers;
    @UiField TextArea description;
    @UiField HTMLPanel detail, choiceButtonsPanel, editButtonsPanel;
    @UiField Button editDemandButton, deleteDemandButton;
    //i18n
    private final StyleResource styleResource = GWT.create(StyleResource.class);
    private LocalizableMessages bundle = (LocalizableMessages) GWT.create(LocalizableMessages.class);
    private NumberFormat currencyFormat = NumberFormat.getFormat(bundle.currencyFormat());
    private DateTimeFormat dateFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT);

    /**************************************************************************/
    /* INITIALIZATON                                                          */
    /**************************************************************************/
    //Constructors
    public EditableDemandDetailView() {
        categories = new CellList<CategoryDetail>(new CategoryCell(CategoryCell.DISPLAY_COUNT_DISABLED));
        localities = new CellList<LocalityDetail>(new LocalityCell(LocalityCell.DISPLAY_COUNT_DISABLED));
        initWidget(uiBinder.createAndBindUi(this));
        detail.setVisible(true);
        editButtonsPanel.setVisible(false);
        setStyle(styleResource.common().textBoxAsLabel());
        setEnables(false);
        StyleResource.INSTANCE.detailViews().ensureInjected();
    }

    /**************************************************************************/
    /* UI BINDER HANDLERS                                                     */
    /**************************************************************************/
    //This handler only hanlde graphic changes. They don't handle the logic.
    @UiHandler("editDemandButton")
    public void editDemandButtonClickHandler(ClickEvent e) {
        choiceButtonsPanel.setVisible(false);
        editButtonsPanel.setVisible(true);
        setStyle(styleResource.common().emptyStyle());
        setEnables(true);
        //TODO enable editing
    }

    @UiHandler("submitButton")
    public void submitButtonClickHandler(ClickEvent e) {
        choiceButtonsPanel.setVisible(true);
        editButtonsPanel.setVisible(false);
        setStyle(styleResource.common().textBoxAsLabel());
        setEnables(false);
    }

    @UiHandler("cancelButton")
    public void cancelButtonClickHandler(ClickEvent e) {
        choiceButtonsPanel.setVisible(true);
        editButtonsPanel.setVisible(false);
        setStyle(styleResource.common().textBoxAsLabel());
        setEnables(false);
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    public void setDemanDetail(FullDemandDetail demandDetail) {
        GWT.log("detail detail" + demandDetail.toString());
        demandName.setText(demandDetail.getTitle());
        price.setText(currencyFormat.format(demandDetail.getPrice()));
        endDate.setText(dateFormat.format(demandDetail.getEndDate()));
        validTo.setText(dateFormat.format(demandDetail.getValidToDate()));
        type.setText(demandDetail.getDetailType().getValue());
        categories.setRowData(demandDetail.getCategories());
        localities.setRowData(demandDetail.getLocalities());
        maxNumberOfSuppliers.setText(Integer.toString(demandDetail.getMaxOffers()));
        minSupplierRating.setText(Integer.toString(demandDetail.getMinRating()) + "%");
        StringBuilder excludedSuppliersBuildes = new StringBuilder();
        for (FullSupplierDetail supplierDetail : demandDetail.getExcludedSuppliers()) {
            excludedSuppliersBuildes.append(supplierDetail.getCompanyName());
            excludedSuppliersBuildes.append(", ");
        }
        excludedSuppliers.setText(excludedSuppliersBuildes.toString());
        description.setText(demandDetail.getDescription());
//        detail.getElement().getFirstChildElement().getStyle().setDisplay(Display.BLOCK);
    }

    private void setStyle(String cssStyle) {
        demandName.setStyleName(cssStyle);
        price.setStyleName(cssStyle);
        endDate.setStyleName(cssStyle);
        validTo.setStyleName(cssStyle);
        type.setStyleName(cssStyle);
        maxNumberOfSuppliers.setStyleName(cssStyle);
        minSupplierRating.setStyleName(cssStyle);
        excludedSuppliers.setStyleName(cssStyle);
        description.setStyleName(cssStyle);
    }

    private void setEnables(boolean enable) {
        demandName.setEnabled(enable);
        price.setEnabled(enable);
        endDate.setEnabled(enable);
        validTo.setEnabled(enable);
        type.setEnabled(enable);
        maxNumberOfSuppliers.setEnabled(enable);
        minSupplierRating.setEnabled(enable);
        excludedSuppliers.setEnabled(enable);
//        description.setEnabled(enable);
    }

    public void toggleVisible() {
        if (detail.isVisible()) {
            detail.getElement().getStyle().setDisplay(Display.NONE);
        } else {
            detail.getElement().getStyle().setDisplay(Display.BLOCK);
        }
    }

    /**************************************************************************/
    /* GETTER                                                                 */
    /**************************************************************************/
    //Buttons
    public Button getDeleteDemandButton() {
        return deleteDemandButton;
    }

    public Button getEditDemandButton() {
        return editDemandButton;
    }

    //
    public HTMLPanel getDetail() {
        return detail;
    }

    public HTMLPanel getChoiceButtonsPanel() {
        return choiceButtonsPanel;
    }

    public HTMLPanel getEditButtonsPanel() {
        return editButtonsPanel;
    }
}
