package com.eprovement.poptavka.client.root.searchBar;

import com.google.gwt.core.client.GWT;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.root.interfaces.ISearchBarView;
import com.eprovement.poptavka.client.root.interfaces.ISearchBarView.ISearchBarPresenter;

@Presenter(view = SearchBarView.class)
public class SearchBarPresenter extends
        BasePresenter<ISearchBarView, RootEventBus> implements
        ISearchBarPresenter {

    public void onStart() {
        GWT.log("Search bar presenter loaded");
        // TODO praso - preco je toto zakomentovane? Je to kvoli tomu, ze search bar view
        // sa nasetuje do widgetu az na zaklade vybraneho pohladu? Podobne ako sa nasetuje
        // menu podla Home / User casti. Alebo je to tym, ze je nastaveny autodisplay pre
        // tento search module? Ano je to autodisplayom. TO teda znamena, ze tento presenter
        // je tu zbytocny, nie? mal by sa pouzit presenter, ktory je v module SearchModule a
        // jeho pohlad sa automaticky nastavi vdaka autodisplay v RootEventBus.
        // TODO praso - tento search bar sa musi pouzivat rovnako ako sa pouzivaju ostatne
        // layouty v RootView. Momentalne sa pouziva zle alebo ho kompletne prepisuje iny
        // presenter
        // eventBus.setSearchBar(view);
    }
}
