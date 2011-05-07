package cz.poptavka.sample.client.user.demands.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MyDemandsView extends Composite implements MyDemandsPresenter.MyDemandsInterface {

    private static MyDemandsViewUiBinder uiBinder = GWT.create(MyDemandsViewUiBinder.class);
    interface MyDemandsViewUiBinder extends UiBinder<Widget, MyDemandsView> {
    }

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }


}
