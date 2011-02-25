package cz.poptavka.sample.client.main;


import java.util.List;

import com.google.gwt.user.client.Window;
import com.mvp4g.client.annotation.InjectService;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.service.demand.CategoryRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.LocalityRPCServiceAsync;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;

@Presenter(view = MainView.class)
public class MainPresenter extends BasePresenter<MainPresenter.MainViewInterface, MainEventBus> {

    //testing
    private LocalityRPCServiceAsync service = null;
    private CategoryRPCServiceAsync categoryService = null;

    public interface MainViewInterface {
        void setData(List<Locality> data);
        void setCategories(List<Category> categories);
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

    public void onSetCategories(List<Category> data) {
        if (data == null) {
            Window.alert("null data");
        } else {
            getView().setCategories(data);
        }
        //view.setData(data);
    }

    @InjectService
    public void setService(LocalityRPCServiceAsync service) {
        this.service = service;
    }
}
