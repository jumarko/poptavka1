package cz.poptavka.sample.client.main.common.search;


import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import cz.poptavka.sample.client.home.HomeEventBus;

@Presenter(view = AdvancedSearchView.class)
public class AdvancedSearchPresenter
        extends BasePresenter<AdvancedSearchPresenter.AdvancedSearchViewInterface, HomeEventBus> {


    public interface AdvancedSearchViewInterface {

        SearchDataHolder getFilter();

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

    public void onGoToHomeSuppliers() {
    }
}