package com.eprovement.poptavka.client.common.address;

import com.eprovement.poptavka.client.common.session.Storage;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import java.util.List;

@Presenter(view = AddressSelectorView.class, multiple = true)
public class AddressSelectorPresenter
        extends LazyPresenter<AddressSelectorPresenter.AddressSelectorInterface, RootEventBus> {

    /**
     * View interface methods. *
     */
    public interface AddressSelectorInterface extends LazyView {

        ListBox getCountry();

        ListBox getRegion();

        ListBox getCity();

        ListBox getDistrict();

        boolean isValid();

        AddressDetail createAddress();

        Widget getWidgetView();
    }

    @Override
    public void bindView() {
        view.getCountry().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                if (view.getCountry().getSelectedIndex() != 0) {
                    eventBus.getChildLocalities(LocalityType.REGION,
                            view.getCountry().getValue(view.getCountry().getSelectedIndex()), null);
                }
            }
        });
        view.getRegion().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                if (view.getRegion().getSelectedIndex() != 0) {
                    eventBus.getChildLocalities(LocalityType.CITY,
                            view.getRegion().getValue(view.getRegion().getSelectedIndex()), null);
                }
            }
        });
        view.getCity().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                if (view.getCity().getSelectedIndex() != 0) {
                    eventBus.getChildLocalities(LocalityType.DISTRICT,
                            view.getCity().getValue(view.getCity().getSelectedIndex()), null);
                }
            }
        });
    }

    public void initAddressWidget(SimplePanel embedWidget) {
        eventBus.getLocalities(LocalityType.COUNTRY, null);
        embedWidget.setWidget(view.getWidgetView());
    }

    public void onSetLocalityData(LocalityType localityType, List<LocalityDetail> list) {
        switch (localityType) {
            case COUNTRY:
                setData(view.getCountry(), list);
                break;
            case REGION:
                setData(view.getRegion(), list);
                break;
            case CITY:
                setData(view.getCity(), list);
                break;
            case DISTRICT:
                setData(view.getDistrict(), list);
                break;
            default:
                break;
        }
    }

    private void setData(final ListBox box, final List<LocalityDetail> list) {
        box.clear();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                box.addItem(Storage.MSGS.select());
                for (int i = 0; i < list.size(); i++) {
                    box.addItem(list.get(i).getName(), list.get(i).getCode());
                }
            }
        });
        box.setVisible(true);
    }
}
