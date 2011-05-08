package cz.poptavka.sample.client.user.demands.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.user.demands.widgets.OffersFlexTable;

@SuppressWarnings("deprecation")
public class OffersView extends Composite implements OffersPresenter.OffersInterface {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    HorizontalSplitPanel master;
    private Button answerBtn;
    private Button refuseBtn;
    private Button acceptBtn;
    private OffersFlexTable table;
    private SimplePanel messageSection;

    @Override
    public void createView() {
        master = new HorizontalSplitPanel();
        answerBtn = new Button(MSGS.answer());
        refuseBtn = new Button(MSGS.refuse());
        acceptBtn = new Button(MSGS.accept());
        table = new OffersFlexTable();
        messageSection = new SimplePanel();


        VerticalPanel tableMaster = new VerticalPanel();
        HorizontalPanel btnPanel = new HorizontalPanel();
        btnPanel.add(answerBtn);
        btnPanel.add(refuseBtn);
        btnPanel.add(acceptBtn);
        tableMaster.add(btnPanel);
        tableMaster.add(table);
        master.setLeftWidget(tableMaster);
        master.setRightWidget(messageSection);
        initWidget(master);
        master.setWidth("" + (Window.getClientWidth() - 100));
        master.setHeight("500");

    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public OffersFlexTable getTable() {
        return table;
    }


}
