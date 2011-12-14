package cz.poptavka.sample.client.main.common.search.views;

import com.google.gwt.user.client.ui.PopupPanel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import cz.poptavka.sample.client.main.common.search.SearchModuleEventBus;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;

@Presenter(view = MessagesTabViewView.class)
public class MessagesTabViewPresenter
        extends BasePresenter<SearchModulePresenter.SearchModulesViewInterface, SearchModuleEventBus> {

    @Override
    public void bind() {
    }

    public void onInitMessagesTabView(PopupPanel popupPanel) {
        popupPanel.setWidget(view.getWidgetView());
    }
}