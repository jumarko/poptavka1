package cz.poptavka.sample.client.main;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;


public class MainPageView extends ViewImpl implements MainPagePresenter.MyView {

    private static final Logger LOGGER = Logger.getLogger(MainPageView.class.getName());

    private final FlexTable table;

    @Inject
    public MainPageView(FlexTable table) {
        this.table = table;
    }

    @Override
    public Widget asWidget() {
        return table;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {

        LOGGER.fine("setInSlot()");

        if (slot == MainPagePresenter.TYPE_SET_CONTEXT_AREA_CONTENT) {
            // TODO insert into slot
        } else {
            super.setInSlot(slot, content);
        }
    }

    public FlexTable getTable() {
        return table;
    }

}
