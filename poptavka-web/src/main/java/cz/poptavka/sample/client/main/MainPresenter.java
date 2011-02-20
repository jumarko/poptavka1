package cz.poptavka.sample.client.main;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;
import com.mvp4g.client.annotation.InjectService;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.domain.address.Locality;

@Presenter(view = MainView.class)
public class MainPresenter extends BasePresenter<MainPresenter.MainViewInterface, MainEventBus> {

    //testing
    private LocalityRPCServiceAsync service = null;

    public interface MainViewInterface {
        void setData(ArrayList<Locality> data);
    }

    public void onStart() {
    }

    public void onSetData(ArrayList<Locality> data) {
        if (data == null) {
            Window.alert("null data");
        } else {
            Window.alert("Locality count:" + String.valueOf(data.size()));
        }
        //view.setData(data);
    }

    @InjectService
    public void setService(LocalityRPCServiceAsync service) {
        this.service = service;
    }
}
