package cz.poptavka.sample.client.user.demands.widgets;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;

public class OffersViewWrapper extends Composite {

    private HorizontalSplitPanel panel = new HorizontalSplitPanel();

    public OffersViewWrapper() {
        initWidget(panel);
        panel.setLeftWidget(new HTML("LALALALA"));
        panel.setRightWidget(new HTML("<h1>HEADING</h1>"));
        panel.setHeight("280px");
    }

}
