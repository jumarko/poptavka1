package com.eprovement.poptavka.client.user.widget.detail;

import com.eprovement.poptavka.client.common.BigDecimalBox;
import com.eprovement.poptavka.client.common.IntegerBox;
import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.locality.LocalityCell;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.client.user.widget.grid.cell.SupplierCell;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
//import org.springframework.util.ReflectionUtils;

public class EditableDemandDetailView extends Composite implements ProvidesValidate, Editor<FullDemandDetail> {

    @Override
    public boolean isValid() {
        return valid.isEmpty();
    }

    interface Driver extends SimpleBeanEditorDriver<FullDemandDetail, EditableDemandDetailView> {
    }
    private EditableDemandDetailView.Driver driver = GWT.create(EditableDemandDetailView.Driver.class);
    private Validator validator = null;
    private FullDemandDetail fullDemandDetail = new FullDemandDetail();
    private Set<String> valid = new HashSet<String>();
    //Constants
    private final static String NORMAL_STYLE = StyleResource.INSTANCE.common().emptyStyle();
    private final static String ERROR_STYLE = StyleResource.INSTANCE.common().errorField();

    interface DemandDetailViewUiBinder extends UiBinder<Widget, EditableDemandDetailView> {
    }
    private static DemandDetailViewUiBinder uiBinder = GWT.create(DemandDetailViewUiBinder.class);
    private static final String EMPTY = "";
    private static final String CATEGORY = "categories";
    private static final String LOCALITY = "localities";
    private static final String EXCLUDED_SUPPLIERS = "excludedSuppliers";
    private static final String TITLE = "title";
    private static final String PRICE = "price";
    private static final String END_DATE = "endDate";
    private static final String VALID_TO_DATE = "validToDate";
    private static final String MAX_OFFERS = "maxOffers";
    private static final String MIN_RATING = "minRating";
    private static final String DESCRIPTION = "description";
    //
    @UiField(provided = true)
    CellList categories, localities, excludedSuppliers;
    @UiField
    TextBox title;
    @UiField
    BigDecimalBox price;
    @UiField
    DateBox endDate, validToDate;
    @UiField
    IntegerBox maxOffers, minRating;
    @UiField
    TextArea description;
    @UiField
    HTMLPanel detail, choiceButtonsPanel, editButtonsPanel;
    @UiField
    Button editDemandButton, deleteDemandButton;
    //
    @Ignore
    @UiField
    Label errorLabelTitle, errorLabelPrice, errorLabelEndDate, errorLabelValidToDate,
    errorLabelCategories, errorLabelLocalities, errorLabelMaxOffers, errorLabelMinRating,
    errorLabelExclidedSuppliers, errorLabelDescription;
    //i18n
    private LocalizableMessages bundle = (LocalizableMessages) GWT.create(LocalizableMessages.class);
    private NumberFormat currencyFormat = NumberFormat.getFormat(bundle.currencyFormat());
    private DateTimeFormat dateFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT);

    /**************************************************************************/
    /* INITIALIZATON                                                          */
    /**************************************************************************/
    //Constructors
    public EditableDemandDetailView() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        categories = new CellList<CategoryDetail>(new CategoryCell(CategoryCell.DISPLAY_COUNT_DISABLED));
        localities = new CellList<LocalityDetail>(new LocalityCell(LocalityCell.DISPLAY_COUNT_DISABLED));
        excludedSuppliers = new CellList<FullSupplierDetail>(new SupplierCell());
        initWidget(uiBinder.createAndBindUi(this));

        driver.initialize(this);
        driver.edit(fullDemandDetail);

        detail.setVisible(true);
        editButtonsPanel.setVisible(false);
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
        setEnables(true);
    }

    @UiHandler("submitButton")
    public void submitButtonClickHandler(ClickEvent e) {
        validate();
        if (isValid()) {
            //edit
            choiceButtonsPanel.setVisible(true);
            editButtonsPanel.setVisible(false);
            setEnables(false);
        }
    }

    @UiHandler("cancelButton")
    public void cancelButtonClickHandler(ClickEvent e) {
        choiceButtonsPanel.setVisible(true);
        editButtonsPanel.setVisible(false);
        setEnables(false);
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    public void setDemanDetail(FullDemandDetail demandDetail) {
        GWT.log("detail detail" + demandDetail.toString());
        title.setText(demandDetail.getTitle());
        price.setValue(demandDetail.getPrice());
        endDate.setValue(demandDetail.getEndDate());
        validToDate.setValue(demandDetail.getValidToDate());
        categories.setRowData(demandDetail.getCategories());
        localities.setRowData(demandDetail.getLocalities());
        maxOffers.setValue(demandDetail.getMaxOffers());
        minRating.setValue(demandDetail.getMinRating());
        excludedSuppliers.setRowData(demandDetail.getExcludedSuppliers());
        description.setText(demandDetail.getDescription());
    }

    public void clear() {
        title.setText(EMPTY);
        price.setText(EMPTY);
        endDate.getTextBox().setText(EMPTY);
        validToDate.getTextBox().setText(EMPTY);
        categories.setRowCount(0);
        localities.setRowCount(0);
        maxOffers.setText(EMPTY);
        minRating.setText(EMPTY);
        excludedSuppliers.setRowCount(0);
        description.setText(EMPTY);
    }

    private void setEnables(boolean enable) {
        title.setEnabled(enable);
        price.setEnabled(enable);
        endDate.setEnabled(enable);
        validToDate.setEnabled(enable);
        maxOffers.setEnabled(enable);
        minRating.setEnabled(enable);
        description.setEnabled(enable);
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

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void validate() {
        //reset
        for (String item : valid) {
            setError(item, NORMAL_STYLE, EMPTY);
        }
        valid.clear();
        //validate
        FullDemandDetail demandDetail = driver.flush();
        Set<ConstraintViolation<FullDemandDetail>> violations = validator.validate(demandDetail, Default.class);
        displayErrors(violations);
    }

    private void displayErrors(Set<ConstraintViolation<FullDemandDetail>> violations) {
        for (ConstraintViolation<FullDemandDetail> violation : violations) {
            setError(violation.getPropertyPath().toString(), ERROR_STYLE, violation.getMessage());
            valid.add(violation.getPropertyPath().toString());
            return;
        }
    }

    /**
     * Set style and error message to given item.
     *
     * @param item - use class constant CITY, STATE, STREET, ZIP
     * @param style - user class constant NORMAL_STYLE, ERROR_STYLE
     * @param errorMessage - message of item's ErrorLabel
     */
    private void setError(String item, String style, String errorMessage) {
        if (item.equals(TITLE)) {
            this.title.setStyleName(style);
            this.errorLabelTitle.setText(errorMessage);
        } else if (item.equals(PRICE)) {
            this.price.setStyleName(style);
            this.errorLabelPrice.setText(errorMessage);
        } else if (item.equals(END_DATE)) {
            this.endDate.setStyleName(style);
            this.errorLabelEndDate.setText(errorMessage);
        } else if (item.equals(DESCRIPTION)) {
            this.description.setStyleName(style);
            this.errorLabelDescription.setText(errorMessage);
        } else if (item.equals(CATEGORY)) {
            this.categories.setStyleName(style);
            this.errorLabelCategories.setText(errorMessage);
        } else if (item.equals(LOCALITY)) {
            this.localities.setStyleName(style);
            this.errorLabelLocalities.setText(errorMessage);
        } else if (item.equals(EXCLUDED_SUPPLIERS)) {
            this.excludedSuppliers.setStyleName(style);
            this.errorLabelExclidedSuppliers.setText(errorMessage);
        } else if (item.equals(VALID_TO_DATE)) {
            this.validToDate.setStyleName(style);
            this.errorLabelValidToDate.setText(errorMessage);
        } else if (item.equals(MAX_OFFERS)) {
            this.maxOffers.setStyleName(style);
            this.errorLabelMaxOffers.setText(errorMessage);
        } else if (item.equals(MIN_RATING)) {
            this.minRating.setStyleName(style);
            this.errorLabelMinRating.setText(errorMessage);
        }
    }
}
