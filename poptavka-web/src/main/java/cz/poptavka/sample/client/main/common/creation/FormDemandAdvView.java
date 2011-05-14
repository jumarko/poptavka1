package cz.poptavka.sample.client.main.common.creation;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;

public class FormDemandAdvView extends Composite
    implements FormDemandAdvPresenter.FormDemandAdvViewInterface, ProvidesValidate  {

    private static FormDemandAdvViewUiBinder uiBinder = GWT.create(FormDemandAdvViewUiBinder.class);
    interface FormDemandAdvViewUiBinder extends UiBinder<Widget, FormDemandAdvView> {    }

    ArrayList<HasValue> widgets = new ArrayList<HasValue>();
    HashMap<String, Object> map = new HashMap<String, Object>();

    @UiField IntegerBox maxOffersBox;
    @UiField IntegerBox minRatingBox;
    @UiField Button excludeBtn;
    @UiField ListBox excludedList;
    @UiField RadioButton classicRadio;
    @UiField RadioButton attractiveRadio;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        widgets.add(maxOffersBox);
        widgets.add(minRatingBox);
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
                ((Widget) item).setStyleName(StyleResource.INSTANCE.common().errorField());
                errorCount++;
            }
        }
        //devel only
//        return true;
        return errorCount == 0;
    }

    @Override
    public HashMap<String, Object> getValues() {
        map.put("maxOffers", maxOffersBox.getValue());
        map.put("minRating", minRatingBox.getValue());
        //demand types
        String resultValue = "";
        if (classicRadio.getValue()) {
            resultValue = "normal";
        } else {
            if (attractiveRadio.getValue()) {
                resultValue = "attractive";
            }
        }
        map.put("demandType", resultValue);
        // TODO excluded suppliers
        return map;
    }

}
