package cz.poptavka.sample.client.main.common.search;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import cz.poptavka.sample.client.home.HomeEventBus;

@Presenter(view = AdvancedSearchView.class)
public class AdvancedSearchPresenter
        extends BasePresenter<AdvancedSearchPresenter.AdvancedSerachViewInterface, HomeEventBus> {

    private final static Logger LOGGER = Logger.getLogger("    AdvancedSearchPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static final int COLUMNS = 4;

    public interface AdvancedSerachViewInterface { //extends LazyView {

        Button getSearchBtn();

        SearchDataHolder getFilter();

        Widget getWidgetView();
    }

    @Override
    public void bind() {
        view.getSearchBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (view.getFilter().getWhere() == 0) {
                    eventBus.goToHomeDemands(view.getFilter());
                } else {
                    eventBus.goToHomeSuppliers(view.getFilter());
                }
            }
        });
    }

    public void onStart() {
        // TODO praso
    }

    public void onForward() {
        // TODO praso - switch css to selected menu button.
        //eventBus.selectCompanyMenu();
    }

    public void onGoToHomeSuppliers() {
//        super.
    }
}