package cz.poptavka.sample.client.user.demands.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.demand.DetailType;

@Presenter(view = DetailWrapperView.class, multiple = true)
public class DetailWrapperPresenter extends
        LazyPresenter<DetailWrapperPresenter.IDetailWrapper, UserEventBus> {

    public interface IDetailWrapper extends LazyView {
        Widget getWidgetView();

        void setDetail(Widget demandDetailWidget);

        void setConversation(Widget conversationWidget);
    }

    private DetailType type;

    public void initDetailWrapper(SimplePanel detailSection) {
        detailSection.setWidget(view.getWidgetView());
    }

    /**
     * Sets style to recognize in which widget it is implemented.
     */
    public void setType(DetailType type) {
        this.type = type;
    }

    public void onSetDemandDetail(DemandDetail detail, DetailType typeOfDetail) {
        if (!typeOfDetail.equals(type)) {
            GWT.log(view.getClass().getName()
                    + " is not suitable for handling this Demand Detail");
            return;
        }
        eventBus.loadingHide();

        // TODO selection if editable or not
        view.setDetail(new DemandDetailView(detail));

    }

    /**
     * Visual sign, that demand detail is loading
     * @param demandId
     * @param typeOfDetail
     */
    public void onGetDemandDetail(Long demandId, DetailType typeOfDetail) {
        if (!typeOfDetail.equals(type)) {
            return;
        }
        Widget anchor = view.getWidgetView();
        eventBus.loadingShowWithAnchor("", anchor);

    }

}
