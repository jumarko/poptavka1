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
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.main.MainEventBus;
import cz.poptavka.sample.shared.domain.LocalityDetail;

@Presenter(view = LocalitySelectorView.class, multiple = true)
public class LocalitySelectorPresenter
    extends LazyPresenter<LocalitySelectorPresenter.LocalitySelectorInterface, MainEventBus> {

    /** View interface methods. **/
    public interface LocalitySelectorInterface extends LazyView {

        ListBox getRegionList();

        ListBox getDistrictList();

        ListBox getCityList();

        ListBox getSelectedList();

        String getSelectedItem(int localityType);

        void toggleLoader();

        boolean isValid();

        void addToSelectedList(int type);

        void removeFromSelectedList();

        Widget getWidgetView();

        ArrayList<String> getSelectedLocalityCodes();
    }

    private static final Logger LOGGER = Logger.getLogger("LocalitySelectorPresenter");

    public void bindView() {
        view.getRegionList().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (event.isControlKeyDown()) {
                    view.addToSelectedList(LocalityDetail.REGION);
                } else {
                    view.toggleLoader();
                    view.getDistrictList().setVisible(false);
                    view.getCityList().setVisible(false);
                    eventBus.getChildLocalities(LocalityDetail.DISTRICT, view.getSelectedItem(LocalityDetail.REGION));
                }
            }
        });
        view.getDistrictList().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (event.isControlKeyDown()) {
                    view.addToSelectedList(LocalityDetail.DISTRICT);
                } else {
                    view.toggleLoader();
                    view.getCityList().setVisible(false);
                    eventBus.getChildLocalities(LocalityDetail.CITY, view.getSelectedItem(LocalityDetail.DISTRICT));
                }
            }
        });
        view.getCityList().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                view.addToSelectedList(LocalityDetail.CITY);
            }
        });
        view.getSelectedList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent arg0) {
                view.removeFromSelectedList();
            }
        });
    }

    public void initLocalityWidget(SimplePanel embedWidget) {
        LOGGER.info("Initializing widget view, RPC call... ");
        eventBus.getRootLocalities();
        embedWidget.setWidget(view.getWidgetView());
    }

    public void onSetLocalityData(int localityType, ArrayList<LocalityDetail> list) {
        switch (localityType) {
            case LocalityDetail.DISTRICT:
                view.toggleLoader();
                setData(view.getDistrictList(), list);
                break;
            case LocalityDetail.REGION:
                setData(view.getRegionList(), list);
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
