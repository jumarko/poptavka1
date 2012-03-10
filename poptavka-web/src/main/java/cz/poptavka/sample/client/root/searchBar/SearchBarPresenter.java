package cz.poptavka.sample.client.root.searchBar;

import com.google.gwt.core.client.GWT;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.root.RootEventBus;
import cz.poptavka.sample.client.root.interfaces.ISearchBarView;
import cz.poptavka.sample.client.root.interfaces.ISearchBarView.ISearchBarPresenter;

@Presenter(view = SearchBarView.class)
public class SearchBarPresenter extends
        BasePresenter<ISearchBarView, RootEventBus> implements
        ISearchBarPresenter {

    public void onStart() {
        GWT.log("Search bar module loaded");
        // TODO praso - preco je toto zakomentovane? Je to kvoli tomu, ze search bar view
        // sa nasetuje do widgetu az na zaklade vybraneho pohladu? Podobne ako sa nasetuje
        // menu podla Home / User casti. Alebo je to tym, ze je nastaveny autodisplay pre
        // tento search module?
        // eventBus.setSearchBar(view);
    }
}
