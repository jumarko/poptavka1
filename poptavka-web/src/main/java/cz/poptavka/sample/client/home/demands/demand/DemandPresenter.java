package cz.poptavka.sample.client.home.demands.demand;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.home.demands.DemandsEventBus;

@Presenter(view = DemandView.class)
public class DemandPresenter extends
    BasePresenter<DemandPresenter.DemandViewInterface, DemandsEventBus> {

    public interface DemandViewInterface {

    }
}
