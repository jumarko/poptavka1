package cz.poptavka.sample.client.home.demands.flexPager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
/**
 *
 * @author Martin Slavkovsky
 *
 */
public class FlexPagerView extends Composite implements FlexPagerPresenter.FlexPagerViewInterface {

    private static MyPagerUiBinder uiBinder = GWT.create(MyPagerUiBinder.class);

    interface MyPagerUiBinder extends UiBinder<Widget, FlexPagerView> {
    }
    @UiField
    Button begin;
    @UiField
    Button less;
    @UiField
    Label pagerLabel;
    @UiField
    Button more;
    @UiField
    Button end;
    @UiField
    ListBox pagesize;

    public FlexPagerView() {
        initWidget(uiBinder.createAndBindUi(this));
        pagesize.addItem("10");
        pagesize.addItem("15");
        pagesize.addItem("20");
        pagesize.addItem("25");
        pagesize.addItem("30");
        pagesize.setSelectedIndex(2);
    }

    /**
     * Returns this view instance.
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public HasClickHandlers getLessBtn() {
        return less;
    }

    @Override
    public HasClickHandlers getMoreBtn() {
        return more;
    }

    @Override
    public HasClickHandlers getBeginBtn() {
        return begin;
    }

    @Override
    public HasClickHandlers getEndBtn() {
        return end;
    }

    @Override
    public int getPageSize() {
        return Integer.parseInt(pagesize.getItemText(pagesize.getSelectedIndex()));
    }

    @Override
    public ListBox getPageSizeControl() {
        return pagesize;
    }

    @Override
    public Label getPagerLabel() {
        return pagerLabel;
    }
}
