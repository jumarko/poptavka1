package cz.poptavka.sample.client.main.common.locality;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.main.MainEventBus;
import cz.poptavka.sample.shared.domain.LocalityDetail;

@Presenter(view = LocalitySelectorView.class)
public class LocalitySelectorPresenter
    extends BasePresenter<LocalitySelectorPresenter.LocalitySelectorInterface, MainEventBus> {

    /** View interface methods. **/
    public interface LocalitySelectorInterface  {
        ListBox getDistrictList();

        ListBox getCityList();

        ListBox getTownshipList();

        ListBox getSelectedList();

        String getSelectedItem(int localityType);

        void toggleLoader();

        boolean isValid();

        void addToSelectedList();

        void removeFromSelectedList();

        Widget getWidgetView();

        ArrayList<String> getSelectedLocalityCodes();
    }

    private static final Logger LOGGER = Logger.getLogger("LocalitySelectorPresenter");

    public void bind() {
        view.getDistrictList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent arg0) {
                view.toggleLoader();
                view.getTownshipList().setVisible(false);
                view.getCityList().setVisible(false);
                eventBus.getChildLocalities(LocalityDetail.DISTRICT, view.getSelectedItem(LocalityDetail.REGION));
            }
        });
        view.getTownshipList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                view.toggleLoader();
                view.getCityList().setVisible(false);
                eventBus.getChildLocalities(LocalityDetail.CITY, view.getSelectedItem(LocalityDetail.DISTRICT));
            }
        });
        view.getCityList().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                view.addToSelectedList();
            }
        });
        view.getSelectedList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent arg0) {
                view.removeFromSelectedList();
            }
        });
    }

    public void onInitLocalityWidget(SimplePanel embedWidget) {
        LOGGER.info("Initializing widget view, RPC call... ");
        eventBus.getRootLocalities();
        embedWidget.setWidget(view.getWidgetView());
    }

    public void onSetLocalityData(int localityType, ArrayList<LocalityDetail> list) {
        switch (localityType) {
            case LocalityDetail.REGION:
                setData(view.getDistrictList(), list);
                break;
            case LocalityDetail.DISTRICT:
                view.toggleLoader();
                setData(view.getTownshipList(), list);
                break;
            case LocalityDetail.CITY:
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
                LOGGER.info("Filling Locality list...");
                if (list.size() == 0) {
                    LOGGER.info("NO CONTAINING LOCALITIES");
                }
                for (int i = 0; i < list.size(); i++) {
                    box.addItem(list.get(i).getName(), list.get(i).getCode());
                }
            }

        });
    }

}
