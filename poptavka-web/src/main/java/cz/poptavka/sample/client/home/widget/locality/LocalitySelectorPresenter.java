package cz.poptavka.sample.client.home.widget.locality;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.home.HomeEventBus;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.shared.domain.LocalityDetail;

@Presenter(view = LocalitySelectorView.class)
public class LocalitySelectorPresenter
    extends BasePresenter<LocalitySelectorPresenter.LocalitySelectorInterface, HomeEventBus> {

    public interface LocalitySelectorInterface  {
        ListBox getDistrictList();

        ListBox getCityList();

        ListBox getTownshipList();

        String getSelectedItem(LocalityType type);

        void toggleLoader();
        //debug
        void setSelectedItem();

        Widget getWidgetView();
    }

    private static final Logger LOGGER = Logger.getLogger(LocalitySelectorPresenter.class.getName());

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
        view.getCityList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent arg0) {
                //debug
                view.setSelectedItem();
            }
        });
    }

    public void onInitLocalitySelector(AnchorEnum anchor) {
        eventBus.getLocalities(LocalityType.DISTRICT);
        eventBus.setAnchorWidget(anchor, view.getWidgetView());
    }

    public void onDisplayLocalityList(LocalityType type, List<LocalityDetail> list) {
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

    private void setData(final ListBox box, final List<LocalityDetail> list) {
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
