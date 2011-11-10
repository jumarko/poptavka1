package cz.poptavka.sample.client.main.common.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.google.gwt.user.datepicker.client.DateBox;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.domain.demand.DemandType.Type;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;

public class AdvancedSearchView extends Composite implements AdvancedSearchPresenter.AdvancedSearchViewInterface {

    private static AdvancedSearchViewUiBinder uiBinder = GWT.create(AdvancedSearchViewUiBinder.class);
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    interface AdvancedSearchViewUiBinder extends UiBinder<Widget, AdvancedSearchView> {
    }
    @UiField
    Label searchTextLabel;
    @UiField
    TextBox content, priceFrom, priceTo, ratingFrom, ratingTo, supplierDescription;
    @UiField
    ListBox category, locality, demandTypes, creationDate;
    @UiField
    DateBox finnishDate;
    @UiField
    VerticalPanel demandPanel, supplierPanel;
    @UiField
    ToggleButton demandBtn, supplierBtn;
    @UiField
    HTMLPanel container;

    public AdvancedSearchView() {
        initWidget(uiBinder.createAndBindUi(this));
        demandBtn.setDown(true);
        demandPanel.setVisible(true);
        supplierBtn.setDown(false);
        supplierPanel.setVisible(false);

        category.addItem(MSGS.allCategories());
        locality.addItem(MSGS.allLocalities());

        priceFrom.setText("0");
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

    public void setBaseInfo(String content, int whereIdx, int catIdx, int locIdx) {
        this.content.setText(content);
        if (whereIdx == 0) {
            demandBtn.setDown(true);
            searchTextLabel.setText(MSGS.demandText());
            supplierBtn.setDown(false);
        } else {
            demandBtn.setDown(false);
            searchTextLabel.setText(MSGS.supplierName());
            supplierBtn.setDown(true);
        }
        this.category.setSelectedIndex(catIdx);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    private String getContent() {
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
        if (demandBtn.isDown()) {
            data.setWhere(0);
            data.setPriceFrom(Integer.valueOf(priceFrom.getText()));
            data.setPriceTo(Integer.valueOf(priceTo.getText()));
            data.setDemandType(demandTypes.getItemText(demandTypes.getSelectedIndex()));
            data.setEndDate(finnishDate.getValue());
            data.setCreationDate(creationDate.getSelectedIndex());
        } else {
            data.setWhere(1);
            data.setRatingFrom(Integer.valueOf(ratingFrom.getText()));
            data.setRatingTo(Integer.valueOf(ratingTo.getText()));
            data.setSupplierDescription(supplierDescription.getText());
        }

        if (category.getSelectedIndex() != 0) {
            data.setCategory(new CategoryDetail(
                    Long.valueOf(category.getValue(category.getSelectedIndex())),
                    category.getItemText(category.getSelectedIndex())));
        }
        if (locality.getSelectedIndex() != 0) {
            data.setLocality(new LocalityDetail(
                    null,
                    category.getItemText(category.getSelectedIndex()),
                    locality.getValue(locality.getSelectedIndex())));
        }
        data.setAddition(true);

        return data;
    }

    public TextBox getPriceFrom() {
        return priceFrom;
    }

    public TextBox getPriceTo() {
        return priceTo;
    }

    public TextBox getRatingFrom() {
        return ratingFrom;
    }

    public TextBox getRatingTo() {
        return ratingTo;
    }
//
//    @Override
//    public TextBox getSupplierDescription() {
//        return supplierDescription;
//    }
//

    public ListBox getCategory() {
        return category;
    }

    public ListBox getLocality() {
        return locality;
    }

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

    @UiHandler("demandBtn")
    void handleDemandClick(ClickEvent event) {
        if (demandBtn.isDown()) {
            supplierBtn.setDown(false);
            supplierPanel.setVisible(false);
            searchTextLabel.setText(MSGS.demandText());
            demandPanel.setVisible(true);
        }
    }

    @UiHandler("supplierBtn")
    void handleSupplierClick(ClickEvent event) {
        if (supplierBtn.isDown()) {
            demandBtn.setDown(false);
            demandPanel.setVisible(false);
            searchTextLabel.setText(MSGS.supplierName());
            supplierPanel.setVisible(true);
        }
    }

    @UiHandler("content")
    void handleContentClick(ClickEvent event) {
        if (content.getText().equals(MSGS.searchContent())) {
            content.setText("");
        }
    }

    @UiHandler("content")
    void handleContentDrag(MouseOutEvent event) {
        if (content.getText().equals("")) {
            content.setText(MSGS.searchContent());
        }
    }
}