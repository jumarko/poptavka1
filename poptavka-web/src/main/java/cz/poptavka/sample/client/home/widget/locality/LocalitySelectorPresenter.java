package cz.poptavka.sample.client.home.widget.locality;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
//import com.mvp4g.client.presenter.LazyPresenter;
//import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.home.HomeEventBus;
import cz.poptavka.sample.client.home.HomePresenter.AnchorEnum;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;

@Presenter(view = LocalitySelectorView.class)
public class LocalitySelectorPresenter
    extends BasePresenter<LocalitySelectorPresenter.LocalitySelectorInterface, HomeEventBus> {

    public interface LocalitySelectorInterface  {
        HasChangeHandlers getDistrictList();

        HasChangeHandlers getRegionList();

        HasChangeHandlers getTownshipList();

        void setLocalities(LocalityType type, List<Locality> list);

        String getSelectedItem();

        void toggleLoader();

        Widget getWidgetView();
    }

    public void bind() {
        view.getRegionList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent arg0) {
                view.toggleLoader();
                eventBus.getLocalities(LocalityType.DISTRICT);
            }
        });
        view.getDistrictList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent arg0) {
                view.toggleLoader();
                eventBus.getLocalities(LocalityType.TOWNSHIP);
            }
        });
        view.getTownshipList().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                Window.alert(view.getSelectedItem());
            }
        });
    }

    public void onInitLocalitySelector(AnchorEnum anchor) {
        eventBus.getLocalities(LocalityType.REGION);
        eventBus.setAnchorWidget(anchor, view.getWidgetView());
    }

    public void onDisplayLocalityList(LocalityType type, List<Locality> list) {
        view.setLocalities(type, list);
    }
}
