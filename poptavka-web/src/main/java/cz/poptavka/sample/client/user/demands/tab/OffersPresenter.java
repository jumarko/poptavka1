package cz.poptavka.sample.client.user.demands.tab;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.demands.widgets.OffersFlexTable;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;

@Presenter(view = OffersView.class)
public class OffersPresenter extends
        LazyPresenter<OffersPresenter.OffersInterface, UserEventBus> {

    private ArrayList<ArrayList<OfferDetail>> offers = new ArrayList<ArrayList<OfferDetail>>();

    private static final Logger LOGGER = Logger.getLogger(OffersPresenter.class
            .getName());

    public interface OffersInterface extends LazyView {
        OffersFlexTable getTable();

        Widget getWidgetView();
    }

    public void bindView() {
        view.getTable().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int[] out = view.getTable().getClickedRow(event);
                if (out[2] == 0) {
                    setDemandOffers(out[0], out[1]);
                } else {
                    Window.alert("Message ID is : " + out[1]);
                }
            }
        });
    }

    /** Init method. **/
    public void onInvokeOffers() {
        eventBus.displayContent(view.getWidgetView());
        eventBus.requestDemands();
    }

    public void onResponseDemands(ArrayList<DemandDetail> demands) {
        if (demands != null) {
            view.getTable().setData(demands);
            eventBus.requestOffers(getDemandIds(demands));
        }
    }

    public void onResponseOffers(ArrayList<ArrayList<OfferDetail>> offersList) {
        offers = offersList;
    }

    /** Help method for requestOffers. **/
    private ArrayList<Long> getDemandIds(ArrayList<DemandDetail> demands) {
        ArrayList<Long> idList = new ArrayList<Long>();
        for (DemandDetail demand : demands) {
            idList.add(demand.getId());
        }
        return idList;
    }

    /**
     * Call to display demand offers
     *
     * @param clickedRow
     *            clicked row, so append offers after it
     */
    private void setDemandOffers(int clickedRow, int demandId) {
        LOGGER.fine("init offers load");
        for (ArrayList<OfferDetail> offerItem : offers) {
            if (offerItem.size() != 0) {
                if (offerItem.get(0).getDemandId() == demandId) {
                    LOGGER.fine("EQUAL displaying for row #" + clickedRow);
                    view.getTable().displayOffers(clickedRow, offerItem);
                } else {
                    LOGGER.fine("NOT EQUAL");
                }
            }
        }
    }
}
