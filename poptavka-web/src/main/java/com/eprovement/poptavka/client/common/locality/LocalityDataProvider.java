package com.eprovement.poptavka.client.common.locality;

import com.eprovement.poptavka.client.service.demand.LocalityRPCServiceAsync;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.exceptions.SecurityDialogBoxes;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import java.util.List;
import java.util.logging.Logger;

public class LocalityDataProvider extends AsyncDataProvider<LocalityDetail> {

    private static final Logger LOGGER = Logger.getLogger(LocalityDataProvider.class.getName());
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private LocalityRPCServiceAsync localityService;
    private LocalityDetail localityDetail;

    public LocalityDataProvider(LocalityDetail locCode, LocalityRPCServiceAsync localityService) {
        this.localityDetail = locCode;
        this.localityService = localityService;
    }

    @Override
    protected void onRangeChanged(HasData<LocalityDetail> display) {
        if (localityDetail == null) {
            localityService.getLocalities(LocalityType.REGION, new AsyncCallback<List<LocalityDetail>>() {

                @Override
                public void onSuccess(List<LocalityDetail> result) {
                    updateRowCount(result.size(), true);
                    updateRowData(0, result);
                }

                @Override
                public void onFailure(Throwable caught) {
                    LOGGER.severe("LocalityDataProvider not working, caught=" + caught.getMessage());
                    new SecurityDialogBoxes.AlertBox(MSGS.tryWaiting()).show();
                }
            });
        } else {
            localityService.getLocalities(localityDetail.getCode(), new AsyncCallback<List<LocalityDetail>>() {

                @Override
                public void onSuccess(List<LocalityDetail> result) {
                    updateRowCount(result.size(), true);
                    updateRowData(0, result);
                }

                @Override
                public void onFailure(Throwable caught) {
                    LOGGER.severe("LocalityDataProvider not working, caught=" + caught.getMessage());
                    new SecurityDialogBoxes.AlertBox(MSGS.tryWaiting()).show();
                }
            });
        }
    }
}