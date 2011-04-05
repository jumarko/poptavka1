package cz.poptavka.sample.client.common.creation.widget;

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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class FormDemandBasicView extends Composite implements FormDemandBasicPresenter.FormDemandBasicInterface {

    private static final Logger LOGGER = Logger.getLogger(FormDemandBasicView.class
            .getName());

    private static FormDemandBasicUiBinder uiBinder = GWT.create(FormDemandBasicUiBinder.class);
    interface FormDemandBasicUiBinder extends UiBinder<Widget, FormDemandBasicView> {
    }

    ArrayList<HasValue> widgets = new ArrayList<HasValue>();
    HashMap<String, Object> map = new HashMap<String, Object>();

    @UiField TextBox titleBox;
    @UiField LongBox priceBox;
    @UiField(provided = true) RichTextToolbarWidget descriptionBox;
    @UiField DateBox finishDateBox;
    @UiField DateBox expireDateBox;
    //place for uploadFiles button
    //place for addNextAttachment button

    public void createView() {
        descriptionBox = new RichTextToolbarWidget();
        initWidget(uiBinder.createAndBindUi(this));
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
        finishDateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
        expireDateBox.setFormat(new DateBox.DefaultFormat(dateFormat));

        widgets.add(titleBox);
        widgets.add(descriptionBox);
        widgets.add(priceBox);
        widgets.add(finishDateBox);
        widgets.add(expireDateBox);

    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public boolean isValid() {
        int errorCount = 0;
        for (HasValue item : widgets) {
            LOGGER.fine("checking... " + item.getClass().getName());
            if (item.getValue() == null) {
                // TODO mark error grafically
                LOGGER.fine("REAL ERROR");
                errorCount++;
            } else {
                if (item.getValue().toString().equals("")) {
                    // TODO mark error grafically
                    LOGGER.fine("REAL TEXT ERROR");
                    errorCount++;
                }
            }
        }
//        return errorCount == 0;
        //devel only
        return true;
    }

    @Override
    public HashMap<String, Object> getValues() {
        map.put("title", titleBox.getText());
        map.put("description", descriptionBox.getValue());
        map.put("price", priceBox.getText());
        map.put("endDate", finishDateBox.getValue());
        map.put("expireDate", expireDateBox.getValue());
        return map;
    }

}
