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
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.AccessRoleDetail;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminAccessRolesView.class)
public class AdminAccessRolesPresenter
        extends BasePresenter<AdminAccessRolesPresenter.AdminAccessRolesInterface, UserEventBus>
        implements HasValueChangeHandlers<String> {

    private final static Logger LOGGER = Logger.getLogger("    AdminAccessRolesPresenter");
    private Map<Long, AccessRoleDetail> dataToUpdate = new HashMap<Long, AccessRoleDetail>();
    private Map<Long, AccessRoleDetail> originalData = new HashMap<Long, AccessRoleDetail>();

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public interface AdminAccessRolesInterface {

        Widget getWidgetView();

        DataGrid<AccessRoleDetail> getDataGrid();

        Column<AccessRoleDetail, String> getNameColumn();

        Column<AccessRoleDetail, String> getDescriptionColumn();

        Column<AccessRoleDetail, String> getPermissionsColumn();

        SingleSelectionModel<AccessRoleDetail> getSelectionModel();

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
    private final Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();
    //list of grid columns, used to sort them. First must by blank (checkbox in table)
    private final String[] columnNames = new String[]{
        "id", "code", "name", "description", "permissions"
    };
    private int start = 0;
    private List<String> gridColumns = Arrays.asList(columnNames);

    public void onCreateAdminAccessRoleAsyncDataProvider(final int totalFound) {
        this.start = 0;
        orderColumns.clear();
        orderColumns.put(columnNames[1], OrderType.ASC);
        dataProvider = new AsyncDataProvider<AccessRoleDetail>() {

            @Override
            protected void onRangeChanged(HasData<AccessRoleDetail> display) {
                display.setRowCount(totalFound);
                start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
//                eventBus.getSortedAccessRoles(start, start + length, orderColumns);
                eventBus.getAdminAccessRoles(start, start + length);
                eventBus.loadingHide();
            }
        };
        this.dataProvider.addDataDisplay(view.getDataGrid());
        createAsyncSortHandler();
    }

    public void createAsyncSortHandler() {
        sortHandler = new AsyncHandler(view.getDataGrid()) {

            @Override
            public void onColumnSort(ColumnSortEvent event) {
                orderColumns.clear();
                OrderType orderType = OrderType.DESC;
                if (event.isSortAscending()) {
                    orderType = OrderType.ASC;
                }
                Column<AccessRoleDetail, String> column = (Column<AccessRoleDetail, String>) event.getColumn();
                if (column == null) {
                    return;
                }
                orderColumns.put(gridColumns.get(
                        view.getDataGrid().getColumnIndex(column)), orderType);

                eventBus.getSortedAccessRoles(start, view.getPageSize(), orderColumns);
            }
        };
        view.getDataGrid().addColumnSortHandler(sortHandler);
    }

    public void onInvokeAdminAccessRoles() {
        eventBus.getAdminAccessRolesCount();
        eventBus.displayAdminContent(view.getWidgetView());
    }

    public void onDisplayAdminTabAccessRoles(List<AccessRoleDetail> accessRoles) {
        dataProvider.updateRowData(start, accessRoles);
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
    }

    @Override
    public void bind() {
        view.getNameColumn().setFieldUpdater(new FieldUpdater<AccessRoleDetail, String>() {

            @Override
            public void update(int index, AccessRoleDetail object, String value) {
                if (!object.getName().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new AccessRoleDetail(object));
                    }
                    object.setName(value);
                    eventBus.addAccessRoleToCommit(object);
                }
            }
        });
        view.getDescriptionColumn().setFieldUpdater(new FieldUpdater<AccessRoleDetail, String>() {

            @Override
            public void update(int index, AccessRoleDetail object, String value) {
                if (!object.getDescription().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new AccessRoleDetail(object));
                    }
                    object.setName(value);
                    eventBus.addAccessRoleToCommit(object);
                }
            }
        });
        view.getPermissionsColumn().setFieldUpdater(new FieldUpdater<AccessRoleDetail, String>() {

            @Override
            public void update(int index, AccessRoleDetail object, String value) {

                if (!object.getPermissions().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new AccessRoleDetail(object));
                    }
                }
                eventBus.showDialogBox();
//                final com.gwtext.client.widgets.Window window = new com.gwtext.client.widgets.Window();
//                window.setTitle("Layout Window");
//                window.setClosable(true);
//                window.setWidth(600);
//                window.setHeight(350);
//                window.setPlain(true);
//                window.setLayout(new com.gwtext.client.widgets.layout.BorderLayout());
////                window.add(tabPanel, centerData);
////                window.add(navPanel, westData);
//                window.setCloseAction(com.gwtext.client.widgets.Window.HIDE);

//                            object.setPermissions();
//                            eventBus.addAccessRoleToCommit(object);

            }
        });
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
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
                        eventBus.updateAccessRole(dataToUpdate.get(idx));
                    }
                    eventBus.loadingHide();
                    dataToUpdate.clear();
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
                for (AccessRoleDetail data : originalData.values()) {
                    idx = view.getDataGrid().getVisibleItems().indexOf(data);
                    view.getDataGrid().getVisibleItem(idx).updateWholeAccessRole(data);
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
                    eventBus.getAdminAccessRolesCount();
                } else {
                    Window.alert("You have some uncommited data. Do commit or rollback first");
                }
            }
        });
    }

    public void onAddAccessRoleToCommit(AccessRoleDetail data) {
        dataToUpdate.remove(data.getId());
        dataToUpdate.put(data.getId(), data);
        view.getChangesLabel().setText(Integer.toString(dataToUpdate.size()));
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
    }

    /**
     * Create the dialog box for this example.
     *
     * @return the new dialog box
     */
    public void onShowDialogBox() {
        // Create a dialog box and set the caption text
        final DialogBox dialogBox = new DialogBox();
        dialogBox.ensureDebugId("cwDialogBox");
        dialogBox.setText("Test");

        // Create a table to layout the content
        VerticalPanel dialogContents = new VerticalPanel();
        dialogContents.setWidth("100%");
        dialogContents.setSpacing(4);
        dialogBox.setWidget(dialogContents);
        dialogBox.center();
        dialogBox.show();
    }
}