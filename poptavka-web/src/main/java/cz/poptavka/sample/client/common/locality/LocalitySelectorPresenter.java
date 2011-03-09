package cz.poptavka.sample.client.common.locality;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.common.CommonEventBus;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.LocalityDetail;

@Presenter(view = LocalitySelectorView.class)
public class LocalitySelectorPresenter
    extends BasePresenter<LocalitySelectorPresenter.LocalitySelectorInterface, CommonEventBus> {

    /** View interface methods. **/
    public interface LocalitySelectorInterface  {
        ListBox getDistrictList();

        ListBox getCityList();

        ListBox getTownshipList();

        String getSelectedItem(LocalityType type);

        void toggleLoader();

        Widget getWidgetView();
    }

    private static final Logger LOGGER = Logger.getLogger("LocalitySelectorPresenter");

    public void bind() {
        view.getDistrictList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent arg0) {
                view.toggleLoader();
                view.getTownshipList().setVisible(false);
                view.getCityList().setVisible(false);
                eventBus.getChildLocalities(LocalityType.TOWNSHIP, view.getSelectedItem(LocalityType.DISTRICT));
            }
        });
        view.getTownshipList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                view.toggleLoader();
                view.getCityList().setVisible(false);
                eventBus.getChildLocalities(LocalityType.CITY, view.getSelectedItem(LocalityType.TOWNSHIP));
            }
        });
    }

    public void onInitLocalityWidget(HasOneWidget embedWidget) {
        LOGGER.info("launching Locality service RPC call ... ");
        eventBus.getLocalities();
        LOGGER.info("Initializing widget view... ");
        embedWidget.setWidget(view.getWidgetView());
    }

    public void onSetLocalityData(LocalityType type, ArrayList<LocalityDetail> list) {
        switch (type) {
            case DISTRICT:
                setData(view.getDistrictList(), list);
                break;
            case TOWNSHIP:
                view.toggleLoader();
                setData(view.getTownshipList(), list);
                break;
            case CITY:
                view.toggleLoader();
                setData(view.getCityList(), list);
                break;
            default:
                break;
        }
    }

    private void setData(final ListBox box, final ArrayList<LocalityDetail> list) {
        box.clear();
        box.setVisible(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                LOGGER.info("Filling list...");
                for (int i = 0; i < list.size(); i++) {
                    box.addItem(list.get(i).getName(), list.get(i).getCode());
                }
                LOGGER.info("List filled");
            }

        });
    }

}
