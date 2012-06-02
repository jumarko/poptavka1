package com.eprovement.poptavka.client.root.searchBar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.client.root.interfaces.ISearchBarView;
import com.eprovement.poptavka.client.root.interfaces.ISearchBarView.ISearchBarPresenter;

public class SearchBarView extends ReverseCompositeView<ISearchBarPresenter>
        implements ISearchBarView {

    private static SearchBarViewUiBinder uiBinder = GWT
            .create(SearchBarViewUiBinder.class);

    interface SearchBarViewUiBinder extends UiBinder<Widget, SearchBarView> {
    }

    public SearchBarView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField
    Button button;

    public SearchBarView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
        button.setText(firstName);
    }

    @UiHandler("button")
    void onClick(ClickEvent e) {
        Window.alert("Hello!");
    }

    public void setText(String text) {
        button.setText(text);
    }

    public String getText() {
        return button.getText();
    }

}
