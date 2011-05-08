package cz.poptavka.sample.client.home.demands;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.home.demands.flexPager.FlexPagerView;
import cz.poptavka.sample.client.resources.StyleResource;

/**
 *
 * @author Martin Slavkovsky
 *
 */
public class DemandsView extends Composite implements DemandsPresenter.DemandsViewInterface {

    private static DemandsUiBinder uiBinder = GWT.create(DemandsUiBinder.class);

    interface DemandsUiBinder extends UiBinder<Widget, DemandsView> {
    }

    @UiField
    VerticalPanel verticalContent;
    @UiField
    ListBox category;
    @UiField
    ListBox locality;

    private FlexPagerView myPager;

    public DemandsView() {
        initWidget(uiBinder.createAndBindUi(this));
        myPager = new FlexPagerView();
        verticalContent.add(myPager);
        // styling layout - styled in UiBinder
        StyleResource.INSTANCE.cssBase().ensureInjected();
    }

    @Override
    public ListBox getCategoryList() {
        return category;
    }

    @Override
    public ListBox getLocalityList() {
        return locality;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
