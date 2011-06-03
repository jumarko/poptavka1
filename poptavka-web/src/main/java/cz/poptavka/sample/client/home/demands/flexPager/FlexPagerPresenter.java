package cz.poptavka.sample.client.home.demands.flexPager;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import cz.poptavka.sample.client.home.demands.DemandsEventBus;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Demand;

import java.util.List;
import java.util.logging.Logger;

@Presenter(view = FlexPagerView.class)
public class FlexPagerPresenter extends BasePresenter<FlexPagerPresenter.FlexPagerViewInterface, DemandsEventBus> {

    private static final Logger LOGGER = Logger.getLogger(FlexPagerPresenter.class.getName());

    public interface FlexPagerViewInterface {

        HasClickHandlers getLessBtn();

        HasClickHandlers getBeginBtn();

        HasClickHandlers getEndBtn();

        HasClickHandlers getMoreBtn();

        int getPageSize();

        ListBox getPageSizeControl();

        Label getPagerLabel();

        Widget getWidgetView();
    }

    public void bind() {
//        view.getLessBtn().addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
//                eventBus.lessClicked();
//            }
//        });
//        view.getMoreBtn().addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
//                eventBus.moreClicked();
//            }
//        });
//        view.getBeginBtn().addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
//                eventBus.beginClicked();
//            }
//        });
//        view.getEndBtn().addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
//                eventBus.endClicked();
//            }
//        });
//        view.getPageSizeControl().addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
//                eventBus.changePagerSize();
//            }
//        });
    }
    private int page = 0;
    private int maxCount;
    private List<Demand> demands;

    public void onBeginClicked() {
        page = 0;
//        eventBus.getDemands(this.getResultsCriteria());
    }

    public void onLessClicked() {
        if (page != 0) {
            page -= 1;
//            eventBus.getDemands(this.getResultsCriteria());
        }
    }

    public void onEndClicked() {
        page = (int) maxCount / view.getPageSize();
        if ((maxCount % view.getPageSize()) == 0) {
            page -= 1;
        }
//        eventBus.getDemands(this.getResultsCriteria());
    }

    public void onMoreClicked() {
        if (this.getUpperValue() < maxCount) {
            page += 1;
//            eventBus.getDemands(this.getResultsCriteria());
        }
    }

    // HELP Methods
    private int getBottomValue() {
        return page * view.getPageSize() + 1;
    }

    private int getUpperValue() {
        return (page + 1) * view.getPageSize();
    }

    public void onSetPagerLabel() {
        int upperValue = this.getUpperValue();
        if (upperValue > maxCount) {
            upperValue = maxCount;
        }
        view.getPagerLabel().setText(this.getBottomValue() + " - " + upperValue + " z " + maxCount);
    }

    public void onChangePagerSize() {
        LOGGER.info("Page Size Changed");
//        eventBus.setRows();
    }

    public ResultCriteria getResultsCriteria() {
        ResultCriteria criteria = ResultCriteria.EMPTY_CRITERIA;

        criteria = new ResultCriteria.Builder()
                .maxResults(view.getPageSize())
                .firstResult(this.getBottomValue())
                .build();

        return criteria;
    }
}