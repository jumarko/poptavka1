package cz.poptavka.sample.client.home.widget.locality;

import java.util.List;

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

        void setLocalities(LocalityType type, List<LocalityDetail> list);

        String getSelectedItem(LocalityType type);

        void toggleLoader();

        void setSelectedItem();

        Widget getWidgetView();
    }

    public void bind() {
        view.getDistrictList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent arg0) {
                view.toggleLoader();
                view.getCityList().setVisible(false);
                eventBus.getChildLocalities(LocalityType.TOWNSHIP, view.getSelectedItem(LocalityType.DISTRICT));
            }
        });
        view.getTownshipList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                view.toggleLoader();
                eventBus.getChildLocalities(LocalityType.CITY, view.getSelectedItem(LocalityType.TOWNSHIP));
            }
        });
        view.getCityList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent arg0) {
                view.setSelectedItem();
            }
        });
    }

    public void onInitLocalitySelector(AnchorEnum anchor) {
        eventBus.getLocalities(LocalityType.DISTRICT);
        eventBus.setAnchorWidget(anchor, view.getWidgetView());
    }

    public void onDisplayLocalityList(LocalityType type, List<LocalityDetail> list) {
        view.setLocalities(type, list);
    }
}
