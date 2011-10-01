package cz.poptavka.sample.client.user.admin.tab;

import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.demand.DemandOriginDetail;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Presenter(view = AdminDemandOriginView.class)
public class AdminDemandOriginPresenter
        extends BasePresenter<AdminDemandOriginPresenter.AdminDemandTypeInterface, UserEventBus>
        implements HasValueChangeHandlers<String> {

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public interface AdminDemandTypeInterface {

        Widget getWidgetView();

        ListDataProvider<DemandOriginDetail> getDataProvider();

        DataGrid<DemandOriginDetail> getDataGrid();

        Column<DemandOriginDetail, String> getNameColumn();

        Column<DemandOriginDetail, String> getDescriptionColumn();

        Column<DemandOriginDetail, String> getUrlColumn();

        SingleSelectionModel<DemandOriginDetail> getSelectionModel();

        SimplePanel getAdminSupplierDetail();

        Button getAddRowBtn();

        Button getDeleteRowBtn();

        Button getCommitBtn();

        Button getRollbackBtn();

        Button getRefreshBtn();

        Label getChangesLabel();
    }

    public void onCreateAdminDemandTypeDataProvider(List<DemandOriginDetail> list) {
        view.getDataProvider().setList(list);
    }

    public void onInvokeAdminDemandOrigin() {
        dataToUpdate.clear();
        dataToInsert.clear();
        dataToDelete.clear();
        originalData.clear();
        eventBus.getAdmiDemandTypes();
        eventBus.displayAdminContent(view.getWidgetView());
    }

    @Override
    public void bind() {
        view.getNameColumn().setFieldUpdater(new FieldUpdater<DemandOriginDetail, String>() {
            @Override
            public void update(int index, DemandOriginDetail object, String value) {
                if (!object.getName().equals(value)) {
                    if (!originalData.contains(object)) {
                        originalData.add(new DemandOriginDetail(object));
                    }
                    object.setName(value);
                    eventBus.addDemandTypeToCommit(object);
                }
            }
        });
        view.getDescriptionColumn().setFieldUpdater(new FieldUpdater<DemandOriginDetail, String>() {
            @Override
            public void update(int index, DemandOriginDetail object, String value) {
                if (!object.getDescription().equals(value)) {
                    if (!originalData.contains(object)) {
                        originalData.add(new DemandOriginDetail(object));
                    }
                    object.setDescription(value);
                    eventBus.addDemandTypeToCommit(object);
                }
            }
        });
        view.getUrlColumn().setFieldUpdater(new FieldUpdater<DemandOriginDetail, String>() {
            @Override
            public void update(int index, DemandOriginDetail object, String value) {
                if (!object.getUrl().equals(value)) {
                    if (!originalData.contains(object)) {
                        originalData.add(new DemandOriginDetail(object));
                    }
                    object.setDescription(value);
                    eventBus.addDemandTypeToCommit(object);
                }
            }
        });
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                if (view.getSelectionModel().getSelectedObject() == null) {
                    view.getDeleteRowBtn().setEnabled(false);
                } else {
                    view.getDeleteRowBtn().setEnabled(true);
                }
            }
        });
        view.getCommitBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (Window.confirm("Realy commit changes?")) {
                    view.getDataGrid().setFocus(true);
                    eventBus.loadingShow("Commiting");
                    for (DemandOriginDetail detail : dataToUpdate) {
                        eventBus.updateDemandType(detail);
                    }
                    for (DemandOriginDetail detail : dataToInsert) {
                        eventBus.insertDemandType(detail);
                    }
                    for (DemandOriginDetail detail : dataToDelete.values()) {
                        eventBus.deleteDemandType(detail.getId());
                    }
                    eventBus.loadingHide();
                    dataToUpdate.clear();
                    dataToInsert.clear();
                    dataToDelete.clear();
                    originalData.clear();
                    Window.alert("Changes commited");
                }
            }
        });
        view.getRollbackBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dataToUpdate.clear();
                view.getDataGrid().setFocus(true);
                int idx = 0;
                for (DemandOriginDetail data : originalData) {
                    idx = view.getDataProvider().getList().indexOf(data);
                    if (idx != -1) {
                        view.getDataProvider().getList().get(idx).updateWholeDemandOrigin(data);
                    }
                }
                for (DemandOriginDetail detail : dataToInsert) {
                    view.getDataProvider().getList().remove(detail);
                }
                for (Integer index : dataToDelete.keySet()) {
                    view.getDataProvider().getList().add(index, dataToDelete.get(index));
                }
                view.getDataGrid().flush();
                view.getDataGrid().redraw();
                Window.alert(view.getChangesLabel().getText() + " changes rolledback.");
                view.getChangesLabel().setText("0");
                originalData.clear();
                dataToInsert.clear();
                dataToDelete.clear();
            }
        });
        view.getRefreshBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (dataToUpdate.isEmpty()) {
                    eventBus.getAdmiDemandTypes();
                } else {
                    Window.alert("You have some uncommited data. Do commit or rollback first");
                }
            }
        });
        view.getAddRowBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                DemandOriginDetail detail = new DemandOriginDetail();
                long maxId = 0;
                for (DemandOriginDetail listDetail : view.getDataProvider().getList()) {
                    if (maxId < listDetail.getId()) {
                        maxId = listDetail.getId().longValue();
                    }
                }
                detail.setId(++maxId);
                detail.setName("-- zmen --");
                detail.setDescription("-- zmen --");
                detail.setUrl("-- zmen --");
                view.getDataProvider().getList().add(detail);
                dataToInsert.add(detail);
                updateChangesLabel(detail);
            }
        });
        view.getDeleteRowBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int idx = 0;
                idx = view.getDataProvider().getList().indexOf(view.getSelectionModel().getSelectedObject());
                if (idx != -1) {
                    view.getDataProvider().getList().remove(idx);
                    if (!dataToInsert.contains(view.getSelectionModel().getSelectedObject())) {
                        //iba ak nebol insert toho objektu
                        dataToDelete.put(idx, view.getSelectionModel().getSelectedObject());
                    } else {
                        dataToInsert.remove(view.getSelectionModel().getSelectedObject()); //ak bol insert, rovno vymaz
                    }
                    updateChangesLabel(view.getSelectionModel().getSelectedObject());
                }
            }
        });
    }
    private List<DemandOriginDetail> dataToUpdate = new ArrayList<DemandOriginDetail>();
    private List<DemandOriginDetail> dataToInsert = new ArrayList<DemandOriginDetail>();
    private Map<Integer, DemandOriginDetail> dataToDelete = new HashMap<Integer, DemandOriginDetail>(); //index, detail
    private List<DemandOriginDetail> originalData = new ArrayList<DemandOriginDetail>();

    public void onAddDemandTypeToCommit(DemandOriginDetail data) {
        dataToUpdate.remove(data);
        if (dataToInsert.contains(data)) {
            //ak nahodov uprava vlozeneho zaznamu, tak nedavaj aj do updatovanych, ale aktualizuj vlozneny
//            dataToInsert.remove(dataToInsert.indexOf(data));
//            dataToInsert.add(data);
        } else {
            dataToUpdate.add(data);
        }
        this.updateChangesLabel(data);
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
    }

    private void updateChangesLabel(DemandOriginDetail detail) {
//        if (!dataToUpdate.contains(detail)
//                && !dataToInsert.contains(detail)
//                && !dataToDelete.values().contains(detail)) {
//            int count = Integer.parseInt(view.getChangesLabel().getText());
        view.getChangesLabel().setText(
                Integer.toString(dataToInsert.size()
                + dataToUpdate.size()
                + dataToDelete.values().size()));
//        }
    }
}
