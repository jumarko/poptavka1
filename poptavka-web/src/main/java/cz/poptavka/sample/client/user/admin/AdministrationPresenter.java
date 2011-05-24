/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.DemandDetail;
import java.util.ArrayList;

/**
 *
 * @author ivan.vlcek
 */
@Presenter(view = AdministrationView.class)
public class AdministrationPresenter
        extends LazyPresenter<AdministrationPresenter.AdministrationInterface, UserEventBus> {

    public interface AdministrationInterface extends LazyView {

        Widget getWidgetView();

//        SimplePager getPager();
        CellTable<DemandDetail> getCellTable();
    }

    private ArrayList<ArrayList<DemandDetail>> offers = new ArrayList<ArrayList<DemandDetail>>();


    public void onInvokeAdministration() {
        eventBus.displayContent(view.getWidgetView());
    }
}
