package cz.poptavka.sample.client.home;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = HomeView.class)
public class HomePresenter extends LazyPresenter<HomePresenter.HomeInterface, HomeEventBus> {

    public enum AnchorEnum {
        FIRST, SECOND, THIRD
    }

    public interface HomeInterface extends LazyView {

        void setContent(AnchorEnum anchor, Widget content);

        Widget getWidgetView();
    }

    public void bindView() {

    }

    public void onInitHome() {
        eventBus.setBodyHolderWidget(view.getWidgetView());
        eventBus.initLocalitySelector(AnchorEnum.FIRST);
    }

    public void onSetAnchorWidget(AnchorEnum anchor, Widget content) {
        view.setContent(anchor, content);
    }

}
