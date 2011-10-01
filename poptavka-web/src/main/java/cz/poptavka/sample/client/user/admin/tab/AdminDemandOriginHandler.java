package cz.poptavka.sample.client.user.admin.tab;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.DemandRPCServiceAsync;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.demand.DemandOriginDetail;

@EventHandler
public class AdminDemandOriginHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private DemandRPCServiceAsync demandService = null;

    /**********************************************************************************************
     ***********************  DEMAND TYPE SECTION. *****************************************************
     **********************************************************************************************/
    public void onGetAdmiDemandTypes() {
        demandService.getDemandOrigins(new AsyncCallback<List<DemandOriginDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(List<DemandOriginDetail> result) {
                eventBus.createAdminDemandTypeDataProvider(result);
            }
        });

    }

    public void onInsertDemandType(DemandOriginDetail detail) {
        demandService.insertDemandOrigin(detail, new AsyncCallback<List<DemandOriginDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(List<DemandOriginDetail> result) {
            }
        });
    }

    public void onUpdateDemandType(DemandOriginDetail detail) {
        demandService.updateDemandOrigin(detail, new AsyncCallback<List<DemandOriginDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(List<DemandOriginDetail> result) {
            }
        });
    }

    public void onDeleteDemandType(Long detailId) {
        demandService.deleteDemandOrigin(detailId, new AsyncCallback<List<DemandOriginDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(List<DemandOriginDetail> result) {
            }
        });
    }
}
