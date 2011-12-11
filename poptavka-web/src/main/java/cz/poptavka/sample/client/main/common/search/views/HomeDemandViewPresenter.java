package cz.poptavka.sample.client.main.common.search.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import cz.poptavka.sample.client.main.common.search.SearchModuleEventBus;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import java.util.ArrayList;

@Presenter(view = HomeDemandViewView.class)
public class HomeDemandViewPresenter
        //        extends LazyPresenter<SearchModulePresenter.SearchModulesViewInterface, SearchModuleEventBus> {
        extends BasePresenter<SearchModulePresenter.SearchModulesViewInterface, SearchModuleEventBus> {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static final int COLUMNS = 4;

    @Override
//    public void bindView() {
    public void bind() {
    }

    public void onInitHomeDemandView(PopupPanel popupPanel) {
        eventBus.requestCategories();
        eventBus.requestLocalities();
        popupPanel.setWidget(view.getWidgetView());
    }

    public void onStart() {
        // TODO praso
    }

    public void onForward() {
        // TODO praso - switch css to selected menu button.
        //eventBus.selectCompanyMenu();
    }

    public void onResponseLocalities(final ArrayList<LocalityDetail> list) {
        final ListBox box = view.getLocalityList();
        box.clear();
        box.setVisible(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                box.addItem("All localities...");
                for (int i = 0; i < list.size(); i++) {
                    box.addItem(list.get(i).getName(), list.get(i).getCode());
                }
                box.setSelectedIndex(0);
                GWT.log("Locality List filled");
            }
        });
    }

    public void onResponseCategories(final ArrayList<CategoryDetail> list) {
        final ListBox box = view.getCategoryList();
        box.clear();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                box.addItem(MSGS.allCategories());
                for (int i = 0; i < list.size(); i++) {
                    box.addItem(list.get(i).getName(), String.valueOf(list.get(i).getId()));
                }
                box.setSelectedIndex(0);
                GWT.log("Category Lists filled");
            }
        });
    }
}