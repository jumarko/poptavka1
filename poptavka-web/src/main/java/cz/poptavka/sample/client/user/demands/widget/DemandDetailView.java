package cz.poptavka.sample.client.user.demands.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;

public class DemandDetailView extends Composite {

    private static DemandDetailViewUiBinder uiBinder = GWT.create(DemandDetailViewUiBinder.class);
    interface DemandDetailViewUiBinder extends UiBinder<Widget, DemandDetailView> {
    }

    @UiField HeadingElement titleHeader;
    @UiField ParagraphElement descArea;
    @UiField SpanElement typeSpan;

    public DemandDetailView(FullDemandDetail demand) {
        initWidget(uiBinder.createAndBindUi(this));
        titleHeader.setInnerText(demand.getTitle());
        descArea.setInnerHTML(demand.getDescription());
        typeSpan.setInnerHTML("Full Demand Detail");
    }

    public DemandDetailView(BaseDemandDetail demand) {
        initWidget(uiBinder.createAndBindUi(this));
        titleHeader.setInnerText(demand.getTitle());
        descArea.setInnerHTML(demand.getDescription());
        typeSpan.setInnerHTML("Base Demand Detail");
    }

}
