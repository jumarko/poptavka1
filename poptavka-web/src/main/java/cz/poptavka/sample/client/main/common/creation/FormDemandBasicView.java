package cz.poptavka.sample.client.main.common.creation;


import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail.DemandField;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

public class FormDemandBasicView extends Composite
        implements FormDemandBasicPresenter.FormDemandBasicInterface, ProvidesValidate, Editor<FullDemandDetail> {

//    private static final Logger LOGGER = Logger.getLogger(FormDemandBasicView.class
//            .getName());
    private static FormDemandBasicUiBinder uiBinder = GWT.create(FormDemandBasicUiBinder.class);

    interface FormDemandBasicUiBinder extends UiBinder<Widget, FormDemandBasicView> {
    }
    ArrayList<HasValue> widgets = new ArrayList<HasValue>();
    HashMap<DemandField, Object> map = new HashMap<DemandField, Object>();
    @UiField
    TextBox title;
    @UiField
    TextBox priceString;
    @UiField
    TextArea description;
    @UiField
    DateBox endDate;
    @UiField
    DateBox validToDate;
    @UiField
    @Ignore
    Label errorLabelTitle, errorLabelPrice, errorLabelEndDate, errorLabelExpDate, errorLabelDesc;

    interface Driver extends SimpleBeanEditorDriver<FullDemandDetail, FormDemandBasicView> {
    }
    private FormDemandBasicView.Driver driver = GWT.create(FormDemandBasicView.Driver.class);
    private Validator validator = null;
    private FullDemandDetail fullDemandDetail = new FullDemandDetail();
    //Constants
    private final static String NORMAL_STYLE = StyleResource.INSTANCE.common().emptyStyle();
    private final static String ERROR_STYLE = StyleResource.INSTANCE.common().errorField();
    private final static int TITLE = 0;
    private final static int PRICE = 1;
    private final static int END_DATE = 2;
    private final static int EXP_DATE = 3;
    private final static int DESCRIPTION = 4;
    //place for uploadFiles button
    //place for addNextAttachment button
    private int valid = 0;

    public void createView() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        initWidget(uiBinder.createAndBindUi(this));
        this.driver.initialize(this);
        this.driver.edit(fullDemandDetail);

        widgets.add(title);
        widgets.add(description);
        widgets.add(endDate);
        widgets.add(validToDate);

        description.setSize("500px", "150px");

        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
        endDate.setFormat(new DateBox.DefaultFormat(dateFormat));
        endDate.addHandler(new BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                validateEndDate();
            }
        }, BlurEvent.getType());
        validToDate.setFormat(new DateBox.DefaultFormat(dateFormat));
        validToDate.addHandler(new BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                validateValidToDate();
            }
        }, BlurEvent.getType());

    }

    @UiHandler("title")
    public void validateTitle(BlurEvent e) {
        FullDemandDetail demandDetail = driver.flush();
        Set<ConstraintViolation<FullDemandDetail>> violations = validator.validateValue(
                FullDemandDetail.class, "title", demandDetail.getTitle(), Default.class);
        this.displayErrors(TITLE, violations);
    }

    @UiHandler("priceString")
    public void validatePrice(BlurEvent e) {
        FullDemandDetail demandDetail = driver.flush();
        Set<ConstraintViolation<FullDemandDetail>> violations = validator.validateValue(
                FullDemandDetail.class, "priceString", demandDetail.getPriceString(), Default.class);
        this.displayErrors(PRICE, violations);
    }

//    @UiHandler("endDateDateBox")
    public void validateEndDate() {
        FullDemandDetail demandDetail = driver.flush();
        Set<ConstraintViolation<FullDemandDetail>> violations = validator.validateValue(
                FullDemandDetail.class, "endDate", demandDetail.getEndDate(), Default.class);
        this.displayErrors(END_DATE, violations);
    }

//    @UiHandler("expireDateBox")
    public void validateValidToDate() {
        FullDemandDetail demandDetail = driver.flush();
        Set<ConstraintViolation<FullDemandDetail>> violations = validator.validateValue(
                FullDemandDetail.class, "validToDate", demandDetail.getValidToDate(), Default.class);
        this.displayErrors(EXP_DATE, violations);
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
        validateValidToDate();
        validateDescription(null);
        return valid == 0;
    }

    @Override
    public HashMap<DemandField, Object> getValues() {
        GWT.log("Filling map with basic values");
        map.put(DemandField.TITLE, title.getText());
        map.put(DemandField.DESCRIPTION, description.getValue());
        try {
            map.put(DemandField.PRICE, priceString.getText());
        } catch (Exception ex) {
            Window.alert("Exception: " + ex.getMessage());
        }
        map.put(DemandField.FINISH_DATE, endDate.getValue());
        map.put(DemandField.VALID_TO_DATE, validToDate.getValue());

        GWT.log("Filling map with basic values ... DONE");
        return map;
    }

    private void displayErrors(int item, Set<ConstraintViolation<FullDemandDetail>> violations) {
        for (ConstraintViolation<FullDemandDetail> violation : violations) {
            setError(item, ERROR_STYLE, violation.getMessage());
            valid++;
            return;
        }
        setError(item, NORMAL_STYLE, "");
        valid--;//????
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
                this.priceString.setStyleName(style);
                this.errorLabelPrice.setText(errorMessage);
                break;
            case END_DATE:
                this.endDate.setStyleName(style);
                this.errorLabelEndDate.setText(errorMessage);
                break;
            case EXP_DATE:
                this.validToDate.setStyleName(style);
                this.errorLabelExpDate.setText(errorMessage);
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