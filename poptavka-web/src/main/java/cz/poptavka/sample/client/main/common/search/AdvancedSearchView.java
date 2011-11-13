package cz.poptavka.sample.client.main.common.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import com.google.gwt.user.datepicker.client.DateBox;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.domain.demand.DemandType.Type;

public class AdvancedSearchView extends Composite implements AdvancedSearchPresenter.AdvancedSearchViewInterface {

    private static AdvancedSearchViewUiBinder uiBinder = GWT.create(AdvancedSearchViewUiBinder.class);
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    interface AdvancedSearchViewUiBinder extends UiBinder<Widget, AdvancedSearchView> {
    }
//    @UiField
//    Label searchTextLabel;
    @UiField
    TextBox demandContent, supplierContent, priceFrom, priceTo, ratingFrom, ratingTo, supplierDescription;
    @UiField
    ListBox demandTypes, creationDate;
    @UiField
    DateBox finnishDate;
    @UiField
    HorizontalPanel demandPanel, supplierPanel;
//    @UiField
//    ToggleButton demandBtn, supplierBtn;
    @UiField
    HTMLPanel container;

    public AdvancedSearchView() {
        initWidget(uiBinder.createAndBindUi(this));
        demandContent.setText(MSGS.searchContent());
//        demandBtn.setDown(true);
        demandPanel.setVisible(false);
//        supplierBtn.setDown(false);
        supplierPanel.setVisible(false);


//        category.addItem(MSGS.allCategories());
//        locality.addItem(MSGS.allLocalities());

        ratingFrom.setText("0");
        ratingTo.setText("100");

        for (Type type : DemandType.Type.values()) {
            demandTypes.addItem(type.name());
        }
        creationDate.addItem(MSGS.today());
        creationDate.addItem(MSGS.yesterday());
        creationDate.addItem(MSGS.lastWeek());
        creationDate.addItem(MSGS.lastMonth());
        creationDate.addItem(MSGS.noLimits());
        creationDate.setSelectedIndex(4);
    }

    public void setBaseInfo(String content, int whereIdx) { //, int catIdx, int locIdx) {
        if (whereIdx == 0) {
            this.demandContent.setText(content);
//            demandBtn.setDown(true);
//            searchTextLabel.setText(MSGS.demandText());
//            supplierBtn.setDown(false);
            supplierPanel.setVisible(false);
            demandPanel.setVisible(true);
        } else {
            this.supplierContent.setText(content);
//            demandBtn.setDown(false);
//            searchTextLabel.setText(MSGS.supplierName());
//            supplierBtn.setDown(true);
            demandPanel.setVisible(false);
            supplierPanel.setVisible(true);
        }
//        this.category.setSelectedIndex(catIdx);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    private String getContent() {
        TextBox content = null;
        if (demandPanel.isVisible()) {
            content = demandContent;
        } else {
            content = supplierContent;
        }
        if (content.getText().equals(MSGS.searchContent())) {
            return "";
        } else {
            return content.getText();
        }
    }

    @Override
    public SearchDataHolder getFilter() {
        SearchDataHolder data = new SearchDataHolder();

        data.setText(this.getContent());
        if (demandPanel.isVisible()) {
            data.setWhere(0);
            data.setPriceFrom(this.getPriceFrom());
            data.setPriceTo(this.getPriceTo());
            data.setDemandType(demandTypes.getItemText(demandTypes.getSelectedIndex()));
            data.setEndDate(finnishDate.getValue());
            data.setCreationDate(creationDate.getSelectedIndex());
        } else {
            data.setWhere(1);
            data.setRatingFrom(this.getRatingFrom());
            data.setRatingTo(this.getRatingTo());
            data.setSupplierDescription(supplierDescription.getText());
        }

//        if (category.getSelectedIndex() != 0) {
//            data.setCategory(new CategoryDetail(
//                    Long.valueOf(category.getValue(category.getSelectedIndex())),
//                    category.getItemText(category.getSelectedIndex())));
//        }
//        if (locality.getSelectedIndex() != 0) {
//            data.setLocality(new LocalityDetail(
//                    null,
//                    category.getItemText(category.getSelectedIndex()),
//                    locality.getValue(locality.getSelectedIndex())));
//        }
        data.setAddition(true);

        return data;
    }

    public int getPriceFrom() {
        if (priceFrom.getText().equals("")) {
            return -1;
        } else {
            return Integer.valueOf(priceFrom.getText());
        }
    }

    public int getPriceTo() {
        if (priceTo.getText().equals("")) {
            return -1;
        } else {
            return Integer.valueOf(priceTo.getText());
        }
    }

    public int getRatingFrom() {
        if (ratingFrom.getText().equals("")) {
            return -1;
        } else {
            return Integer.valueOf(ratingFrom.getText());
        }
    }

    public int getRatingTo() {
        if (ratingTo.getText().equals("")) {
            return -1;
        } else {
            return Integer.valueOf(ratingTo.getText());
        }
    }
//
//    @Override
//    public TextBox getSupplierDescription() {
//        return supplierDescription;
//    }
//

//    public ListBox getCategory() {
//        return category;
//    }
//
//    public ListBox getLocality() {
//        return locality;
//    }
    public ListBox getDemandTypes() {
        return demandTypes;
    }

    public ListBox getCreationDate() {
        return creationDate;
    }

    public DateBox getFinnishDate() {
        return finnishDate;
    }
//
//    @Override
//    public Label getTitleLabel() {
//        return title;
//    }
//
//    @Override
//    public HTMLPanel getContainer() {
//        return container;
//    }

//    @UiHandler("demandBtn")
//    void handleDemandClick(ClickEvent event) {
//        if (demandBtn.isDown()) {
//            supplierBtn.setDown(false);
//            supplierPanel.setVisible(false);
//            searchTextLabel.setText(MSGS.demandText());
//            demandPanel.setVisible(true);
//        }
//    }
//
//    @UiHandler("supplierBtn")
//    void handleSupplierClick(ClickEvent event) {
//        if (supplierBtn.isDown()) {
//            demandBtn.setDown(false);
//            demandPanel.setVisible(false);
//            searchTextLabel.setText(MSGS.supplierName());
//            supplierPanel.setVisible(true);
//        }
//    }
    @UiHandler("priceFrom")
    void validatePriceFrom(ChangeEvent event) {
        if (!priceFrom.getText().matches("[0-9]+")) {
            priceFrom.setText("");
        }
    }

    @UiHandler("priceTo")
    void validatePriceTo(ChangeEvent event) {
        if (!priceTo.getText().matches("[0-9]+")) {
            priceTo.setText("");
        }
    }

    @UiHandler("ratingFrom")
    void validateRatingFrom(ChangeEvent event) {
        if (!ratingFrom.getText().matches("[0-9]+")) {
            ratingFrom.setText("0");
        }
    }

    @UiHandler("ratingTo")
    void validateRatingTo(ChangeEvent event) {
        if (!ratingTo.getText().matches("[0-9]+")) {
            ratingTo.setText("100");
        }
    }

    @UiHandler("demandContent")
    void handleDemandContentClick(ClickEvent event) {
        if (demandContent.getText().equals(MSGS.searchContent())) {
            demandContent.setText("");
        }
    }

    @UiHandler("demandContent")
    void handleDemandContentDrag(MouseOutEvent event) {
        if (demandContent.getText().equals("")) {
            demandContent.setText(MSGS.searchContent());
        }
    }

    @UiHandler("supplierContent")
    void handleSupplierContentClick(ClickEvent event) {
        if (supplierContent.getText().equals(MSGS.searchContent())) {
            supplierContent.setText("");
        }
    }

    @UiHandler("supplierContent")
    void handleSupplierContentDrag(MouseOutEvent event) {
        if (supplierContent.getText().equals("")) {
            supplierContent.setText(MSGS.searchContent());
        }
    }
}