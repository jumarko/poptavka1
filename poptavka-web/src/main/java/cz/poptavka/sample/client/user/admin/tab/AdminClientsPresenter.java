/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.ClientDetail;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminClientsView.class)
public class AdminClientsPresenter
        extends BasePresenter<AdminClientsPresenter.AdminClientsInterface, UserEventBus>
        implements HasValueChangeHandlers<String> {

    private final static Logger LOGGER = Logger.getLogger("    AdminDemandsPresenter");
    private Map<Long, ClientDetail> dataToUpdate = new HashMap<Long, ClientDetail>();
    private Map<Long, String> metadataToUpdate = new HashMap<Long, String>();
    private Map<Long, ClientDetail> originalData = new HashMap<Long, ClientDetail>();

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public interface AdminClientsInterface {

        Widget getWidgetView();

        DataGrid<ClientDetail> getDataGrid();

        Column<ClientDetail, String> getCompanyColumn();

        Column<ClientDetail, String> getFirstNameColumn();

        Column<ClientDetail, String> getLastNameColumn();

        Column<ClientDetail, String> getRatingColumn();

        SingleSelectionModel<ClientDetail> getSelectionModel();

        SimplePanel getAdminDemandDetail();

        SimplePager getPager();

        int getPageSize();

        Button getCommitBtn();

        Button getRollbackBtn();

        Button getRefreshBtn();

        Label getChangesLabel();

        ListBox getPageSizeCombo();
    }
    private AsyncDataProvider dataProvider = null;
    private AsyncHandler sortHandler = null;
    //list of grid columns, used to sort them. First must by blank (checkbox in table)
    private final String[] columnNames = new String[]{
        "", "id", "companyName", "firstName", "lastName", "rating"
    };
    private int start = 0;
    private List<String> gridColumns = Arrays.asList(columnNames);

    public void onCreateAdminClientsAsyncDataProvider(final int totalFound) {
        this.start = 0;
        dataProvider = new AsyncDataProvider<ClientDetail>() {

            @Override
            protected void onRangeChanged(HasData<ClientDetail> display) {
                display.setRowCount(totalFound);
                start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                eventBus.getAdminClients(start, start + length);
                eventBus.loadingHide();
            }
        };
        this.dataProvider.addDataDisplay(view.getDataGrid());
        createAsyncSortHandler();
    }

    public void createAsyncSortHandler() {
        //Moze byt hned na zaciatku? Ak ano , tak potom aj asynchdataprovider by mohol nie?
        sortHandler = new AsyncHandler(view.getDataGrid()) {

            @Override
            public void onColumnSort(ColumnSortEvent event) {
                OrderType orderType = OrderType.DESC;
                Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();

                if (event.isSortAscending()) {
                    orderType = OrderType.ASC;
                }
                Column<ClientDetail, String> column = (Column<ClientDetail, String>) event.getColumn();
                if (column == null) {
                    return;
                }
                orderColumns.put(gridColumns.get(
                        view.getDataGrid().getColumnIndex(column)), orderType);

                eventBus.getSortedClients(start, view.getPageSize(), orderColumns);
            }
        };
        view.getDataGrid().addColumnSortHandler(sortHandler);
    }

    public void onInvokeAdminClients() {
        eventBus.getAdminClientsCount();
        eventBus.displayAdminContent(view.getWidgetView());
    }

    public void onDisplayAdminTabClients(List<ClientDetail> clients) {
        dataProvider.updateRowData(start, clients);
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
    }

    public void onResponseAdminDemandDetail(Widget widget) {
        view.getAdminDemandDetail().setWidget(widget);
    }

    @Override
    public void bind() {
        view.getCompanyColumn().setFieldUpdater(new FieldUpdater<ClientDetail, String>() {

            @Override
            public void update(int index, ClientDetail object, String value) {
                if (!object.getUserDetail().getCompanyName().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new ClientDetail(object));
                    }
                    object.getUserDetail().setCompanyName(value);
                    eventBus.addClientToCommit(object, "userData");
                }
            }
        });
        view.getFirstNameColumn().setFieldUpdater(new FieldUpdater<ClientDetail, String>() {

            @Override
            public void update(int index, ClientDetail object, String value) {
                if (!object.getUserDetail().getFirstName().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new ClientDetail(object));
                    }
                    object.getUserDetail().setFirstName(value);
                    eventBus.addClientToCommit(object, "userData");
                }
            }
        });
        view.getLastNameColumn().setFieldUpdater(new FieldUpdater<ClientDetail, String>() {

            @Override
            public void update(int index, ClientDetail object, String value) {
                if (!object.getUserDetail().getLastName().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new ClientDetail(object));
                    }
                    object.getUserDetail().setLastName(value);
                    eventBus.addClientToCommit(object, "userData");
                }
            }
        });
        view.getRatingColumn().setFieldUpdater(new FieldUpdater<ClientDetail, String>() {

            @Override
            public void update(int index, ClientDetail object, String value) {
                if (!Integer.toString(object.getOveralRating()).equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new ClientDetail(object));
                    }
                    object.setOveralRating(Integer.valueOf(value));
                    eventBus.addClientToCommit(object, "userData");
                }
            }
        });
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                if (dataToUpdate.containsKey(view.getSelectionModel().getSelectedObject().getId())) {
                    eventBus.showAdminClientDetail(dataToUpdate.get(
                            view.getSelectionModel().getSelectedObject().getId()));
                } else {
                    eventBus.showAdminClientDetail(view.getSelectionModel().getSelectedObject());
                }
                eventBus.setDetailDisplayedClient(true);
            }
        });
        view.getPageSizeCombo().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                int page = view.getPager().getPageStart() / view.getPageSize();
                view.getPager().setPageStart(page * view.getPageSize());
                view.getPager().setPageSize(view.getPageSize());
            }
        });
        view.getCommitBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (Window.confirm("Realy commit changes?")) {
                    view.getDataGrid().setFocus(true);
                    eventBus.loadingShow("Commiting");
                    for (Long idx : dataToUpdate.keySet()) {
//                        eventBus.updateClient(dataToUpdate.get(idx), metadataToUpdate.get(idx));
                    }
                    eventBus.loadingHide();
                    dataToUpdate.clear();
                    metadataToUpdate.clear();
                    originalData.clear();
                    Window.alert("Changes commited");
                }
            }
        });
        view.getRollbackBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dataToUpdate.clear();
                metadataToUpdate.clear();
                view.getDataGrid().setFocus(true);
                int idx = 0;
                for (ClientDetail data : originalData.values()) {
                    idx = view.getDataGrid().getVisibleItems().indexOf(data);
                    view.getDataGrid().getVisibleItem(idx).updateWholeClient(data);
                }
                view.getDataGrid().flush();
                view.getDataGrid().redraw();
                Window.alert(view.getChangesLabel().getText() + " changes rolledback.");
                view.getChangesLabel().setText("0");
                originalData.clear();
            }
        });
        view.getRefreshBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (dataToUpdate.isEmpty()) {
                    dataProvider.updateRowCount(0, true);
                    dataProvider = null;
                    view.getDataGrid().flush();
                    view.getDataGrid().redraw();
                    eventBus.getAdminClientsCount();
                } else {
                    Window.alert("You have some uncommited data. Do commit or rollback first");
                }
            }
        });
    }
    private Boolean detailDisplayed = false;

    public void onAddClientToCommit(ClientDetail data, String dataType) {
        //TODO Martin - otestovat, alebo celkom zrusit cistocne auktualizovanie
        if (metadataToUpdate.containsKey(data.getId())) {
            dataToUpdate.remove(data.getId());
            metadataToUpdate.remove(data.getId());
            metadataToUpdate.put(data.getId(), "all");
        } else {
            dataToUpdate.put(data.getId(), data);
            metadataToUpdate.put(data.getId(), dataType);
        }
        if (detailDisplayed) {
            eventBus.showAdminClientDetail(data);
        }
        view.getChangesLabel().setText(Integer.toString(dataToUpdate.size()));
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
    }

    public void onSetDetailDisplayedClient(Boolean displayed) {
        detailDisplayed = displayed;
    }
}
