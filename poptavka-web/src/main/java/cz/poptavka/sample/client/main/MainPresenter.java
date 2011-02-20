package cz.poptavka.sample.client.main;


import com.google.gwt.user.client.Window;
import com.mvp4g.client.annotation.InjectService;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.domain.address.Locality;
import java.util.List;

@Presenter(view = MainView.class)
public class MainPresenter extends BasePresenter<MainPresenter.MainViewInterface, MainEventBus> {

    //testing
    private LocalityRPCServiceAsync service = null;

    public interface MainViewInterface {
        void setData(List<Locality> data);
    }

    public void onStart() {
    }

    public void onSetData(List<Locality> data) {
        if (data == null) {
            Window.alert("null data");
        } else {
            getView().setData(data);
        }
        //view.setData(data);
    }

    @InjectService
    public void setService(LocalityRPCServiceAsync service) {
        this.service = service;
    }
}
