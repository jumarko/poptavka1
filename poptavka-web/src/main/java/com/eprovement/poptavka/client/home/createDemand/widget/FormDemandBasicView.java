package com.eprovement.poptavka.client.home.createDemand.widget;

import com.eprovement.poptavka.client.common.BigDecimalBox;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

public class FormDemandBasicView extends Composite
        implements FormDemandBasicPresenter.FormDemandBasicInterface, ProvidesValidate, Editor<FullDemandDetail> {

    private static FormDemandBasicUiBinder uiBinder = GWT.create(FormDemandBasicUiBinder.class);

    interface FormDemandBasicUiBinder extends UiBinder<Widget, FormDemandBasicView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField TextBox title;
    @UiField BigDecimalBox price;
    @UiField TextArea description;
    @UiField DateBox endDate;
    @UiField @Ignore Label errorLabelTitle, errorLabelPrice, errorLabelEndDate, errorLabelDesc;

    /** Class attributes. **/
    ArrayList<HasValue> widgets = new ArrayList<HasValue>();
    HashMap<DemandField, Object> map = new HashMap<DemandField, Object>();
    //Validation
    interface Driver extends SimpleBeanEditorDriver<FullDemandDetail, FormDemandBasicView> {
    }
    private FormDemandBasicView.Driver driver = GWT.create(FormDemandBasicView.Driver.class);
    private Validator validator = null;
    private FullDemandDetail fullDemandDetail = new FullDemandDetail();

    /** Constants. **/
    private final static String NORMAL_STYLE = StyleResource.INSTANCE.common().emptyStyle();
    private final static String ERROR_STYLE = StyleResource.INSTANCE.common().errorField();
    private final static int TITLE = 0;
    private final static int PRICE = 1;
    private final static int END_DATE = 2;
    private final static int DESCRIPTION = 3;
    //place for uploadFiles button
    //place for addNextAttachment button
    private Set<Integer> valid = new HashSet<Integer>();

    /**************************************************************************/
    /* Initializatiob                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        initWidget(uiBinder.createAndBindUi(this));
        this.driver.initialize(this);
        this.driver.edit(fullDemandDetail);

        widgets.add(title);
        widgets.add(description);
        widgets.add(endDate);

        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
        endDate.setFormat(new DateBox.DefaultFormat(dateFormat));
        endDate.getDatePicker().getParent().addHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent event) {
                validateEndDate();
            }
        }, CloseEvent.getType());
    }

    @UiHandler("title")
    public void validateTitle(BlurEvent e) {
        FullDemandDetail demandDetail = driver.flush();
        Set<ConstraintViolation<FullDemandDetail>> violations = validator.validateValue(
                FullDemandDetail.class, "title", demandDetail.getTitle(), Default.class);
        this.displayErrors(TITLE, violations);
    }

    @UiHandler("price")
    public void validatePrice(BlurEvent e) {
        FullDemandDetail demandDetail = driver.flush();
        Set<ConstraintViolation<FullDemandDetail>> violations = validator.validateValue(
                FullDemandDetail.class, "price", demandDetail.getPrice(), Default.class);
        this.displayErrors(PRICE, violations);
    }

    public void validateEndDate() {
        FullDemandDetail demandDetail = driver.flush();
        Set<ConstraintViolation<FullDemandDetail>> violations = validator.validateValue(
                FullDemandDetail.class, "endDate", demandDetail.getEndDate(), Default.class);
        this.displayErrors(END_DATE, violations);
    }

    @UiHandler("description")
    public void validateDescription(BlurEvent e) {
        FullDemandDetail demandDetail = driver.flush();
        Set<ConstraintViolation<FullDemandDetail>> violations = validator.validateValue(
                FullDemandDetail.class, "description", demandDetail.getDescription(), Default.class);
        this.displayErrors(DESCRIPTION, violations);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public boolean isValid() {
        validateTitle(null);
        validatePrice(null);
        validateEndDate();
        validateDescription(null);
        return valid.isEmpty();
    }

    @Override
    public HashMap<DemandField, Object> getValues() {
        GWT.log("Filling map with basic values");
        map.put(DemandField.TITLE, title.getText());
        map.put(DemandField.DESCRIPTION, description.getValue());
        try {
            map.put(DemandField.PRICE, price.getValue());
        } catch (Exception ex) {
            Window.alert("Exception: " + ex.getMessage());
        }
        map.put(DemandField.END_DATE, endDate.getValue());

        GWT.log("Filling map with basic values ... DONE");
        return map;
    }

    private void displayErrors(int item, Set<ConstraintViolation<FullDemandDetail>> violations) {
        for (ConstraintViolation<FullDemandDetail> violation : violations) {
            setError(item, ERROR_STYLE, violation.getMessage());
            valid.add(item);
            return;
        }
        setError(item, NORMAL_STYLE, "");
        valid.remove(item);
    }

    /**
     * Set style and error message to given item.
     *
     * @param item - use class constant CITY, STATE, STREET, ZIP
     * @param style - user class constant NORMAL_STYLE, ERROR_STYLE
     * @param errorMessage - message of item's ErrorLabel
     */
    private void setError(int item, String style, String errorMessage) {
        switch (item) {
            case TITLE:
                this.title.setStyleName(style);
                this.errorLabelTitle.setText(errorMessage);
                break;
            case PRICE:
                this.price.setStyleName(style);
                this.errorLabelPrice.setText(errorMessage);
                break;
            case END_DATE:
                this.endDate.setStyleName(style);
                this.errorLabelEndDate.setText(errorMessage);
                break;
            case DESCRIPTION:
                this.description.setStyleName(style);
                this.errorLabelDesc.setText(errorMessage);
                break;
            default:
                break;
        }
    }
}