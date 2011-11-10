package cz.poptavka.sample.client.main.common.search;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import cz.poptavka.sample.client.home.HomeEventBus;

@Presenter(view = SearchView.class)
public class SearchPresenter
        extends BasePresenter<SearchPresenter.SearchViewInterface, HomeEventBus> {

    private final static Logger LOGGER = Logger.getLogger("DisplaySuppliersPresenter");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static final int COLUMNS = 4;

    public interface SearchViewInterface { //extends LazyView {

        Button getSearchBtn();

        Button getSearchAdvBtn();

        SearchDataHolder getFilter();

        String getContent();

        ListBox getWhere();

        ListBox getCategory();

        ListBox getLocality();

        Widget getWidgetView();
    }

    @Override
    public void bind() {
    }

    public void onStart() {
        // TODO praso
    }

    public void onForward() {
        // TODO praso - switch css to selected menu button.
        //eventBus.selectCompanyMenu();
    }
}