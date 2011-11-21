package cz.poptavka.sample.client.homesettings;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;


@Presenter(view = HomeSettingsView.class)
public class HomeSettingsPresenter extends BasePresenter<
        HomeSettingsPresenter.HomeSettingsViewInterface, HomeSettingsEventBus> {
    public interface HomeSettingsViewInterface {

    }
}
