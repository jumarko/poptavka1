package cz.poptavka.sample.client.user.listOfDemands;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.user.UserEventBus;

@Presenter(view = ListOfDemandsView.class)
public class ListOfDemandsPresenter
    extends BasePresenter<ListOfDemandsPresenter.ListOfDemandsViewInterface, UserEventBus> {

    private static final Logger LOGGER = Logger.getLogger("ListOfDemandsPressenter");

    public interface ListOfDemandsViewInterface {
        void setBody(Widget body);

        Widget getListOfDemandsView();
    }

    public void onSetListOfDemandsWidget() {
        //init
        LOGGER.info("init list of demands widget ...");

    }
}