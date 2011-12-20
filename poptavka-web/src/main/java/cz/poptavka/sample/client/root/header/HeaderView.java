package cz.poptavka.sample.client.root.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.root.ReverseCompositeView;
import cz.poptavka.sample.client.root.interfaces.IHeaderView;
import cz.poptavka.sample.client.root.interfaces.IHeaderView.IHeaderPresenter;

public class HeaderView extends ReverseCompositeView<IHeaderPresenter>
        implements IHeaderView {

    private static HeaderViewUiBinder uiBinder = GWT
            .create(HeaderViewUiBinder.class);

    interface HeaderViewUiBinder extends UiBinder<Widget, HeaderView> {
    }

    public HeaderView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /** login area **/
    @UiField
    Anchor loginButton;
    @UiField
    HTMLPanel headerHolder;

    public HeaderView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Anchor getLoginLink() {
        return loginButton;
    }

    @Override
    public void toggleMainLayout(boolean switchToUserLayout) {
        if (switchToUserLayout) {
            headerHolder.setStyleName(StyleResource.INSTANCE.layout()
                    .layoutUser());
        } else {
            headerHolder.setStyleName(StyleResource.INSTANCE.layout()
                    .layoutPublic());
        }
    }

}
