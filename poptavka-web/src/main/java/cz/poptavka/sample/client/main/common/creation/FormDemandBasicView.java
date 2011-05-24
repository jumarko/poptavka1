package cz.poptavka.sample.client.main.common.creation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import cz.poptavka.sample.client.resources.StyleResource;

public class FormDemandBasicView extends Composite
    implements FormDemandBasicPresenter.FormDemandBasicInterface, ProvidesValidate  {

    private static final Logger LOGGER = Logger.getLogger(FormDemandBasicView.class
            .getName());

    private static FormDemandBasicUiBinder uiBinder = GWT.create(FormDemandBasicUiBinder.class);
    interface FormDemandBasicUiBinder extends UiBinder<Widget, FormDemandBasicView> {
    }

    ArrayList<HasValue> widgets = new ArrayList<HasValue>();
    HashMap<String, Object> map = new HashMap<String, Object>();

    @UiField TextBox titleBox;
    @UiField LongBox priceBox;
    @UiField TextArea descriptionBox;
    @UiField DateBox finishDateBox;
    @UiField DateBox expireDateBox;
    //place for uploadFiles button
    //place for addNextAttachment button

    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
        finishDateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
        expireDateBox.setFormat(new DateBox.DefaultFormat(dateFormat));

        widgets.add(titleBox);
        widgets.add(descriptionBox);
        widgets.add(finishDateBox);
        widgets.add(expireDateBox);

        descriptionBox.setSize("500px", "150px");

    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public boolean isValid() {
        int errorCount = 0;
        for (HasValue item : widgets) {
            ((Widget) item).removeStyleName(StyleResource.INSTANCE.common().errorField());
            if (item.getValue() == null) {
                errorCount++;
                ((Widget) item).setStyleName(StyleResource.INSTANCE.common().errorField());
            } else {
                if (item.getValue().toString().equals("")) {
                    errorCount++;
                    ((Widget) item).setStyleName(StyleResource.INSTANCE.common().errorField());
                }
            }
        }
        return errorCount == 0;
    }

    @Override
    public HashMap<String, Object> getValues() {
        LOGGER.info("Filling map with basic values");
        map.put("title", titleBox.getText());
        map.put("description", descriptionBox.getValue());
        long price = 0;
        try {
            priceBox.getValueOrThrow();
        } catch (ParseException e) {
            price = -1;
        }
        map.put("price", price);
        map.put("endDate", finishDateBox.getValue());
        map.put("expireDate", expireDateBox.getValue());

        LOGGER.info("Filling map with basic values ... DONE");
        return map;
    }

}
