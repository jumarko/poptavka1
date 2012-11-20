package com.eprovement.poptavka.client.common.locality;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.service.demand.LocalityRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link TreeViewModel} used to organize contacts into a hierarchy.
 */
public class LocalityTreeViewModel implements TreeViewModel {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    private Cell<LocalityDetail> localityCell = null;
    private final SelectionModel<LocalityDetail> selectionModel;
    private final DefaultSelectionEventManager<LocalityDetail> selectionManager =
            DefaultSelectionEventManager.createCheckboxManager();
    private LocalityRPCServiceAsync localityService;
    //Holds constant values of this class.
    private int checkboxesUsage = -1;
    //Holds constanst values from CategoryCell class.
    private int displayCountOfWhat = -1;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public LocalityTreeViewModel(final SelectionModel<LocalityDetail> selectionModel,
            LocalityRPCServiceAsync localityService,
            final int checkboxesUsage,
            final int displayCountOfWhat) {
        this.selectionModel = selectionModel;
        this.localityService = localityService;
        this.checkboxesUsage = checkboxesUsage;
        this.displayCountOfWhat = displayCountOfWhat;

        List<HasCell<LocalityDetail, ?>> hasCells = new ArrayList<HasCell<LocalityDetail, ?>>();
        hasCells.add(new HasCell<LocalityDetail, Boolean>() {
            private CheckboxCell cell = new CheckboxCell(true, false);

            @Override
            public Cell<Boolean> getCell() {
                return cell;
            }

            @Override
            public FieldUpdater<LocalityDetail, Boolean> getFieldUpdater() {
                return null;
            }

            @Override
            public Boolean getValue(LocalityDetail object) {
                return selectionModel.isSelected(object);
            }
        });
        hasCells.add(new HasCell<LocalityDetail, LocalityDetail>() {
            private LocalityCell cell = new LocalityCell(displayCountOfWhat);

            @Override
            public Cell<LocalityDetail> getCell() {
                return cell;
            }

            @Override
            public FieldUpdater<LocalityDetail, LocalityDetail> getFieldUpdater() {
                return null;
            }

            @Override
            public LocalityDetail getValue(LocalityDetail object) {
                return object;
            }
        });
        localityCell = new CompositeCell<LocalityDetail>(hasCells) {
            @Override
            public void render(Context context, LocalityDetail value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<table><tbody><tr>");
                super.render(context, value, sb);
                sb.appendHtmlConstant("</tr></tbody></table>");
            }

            @Override
            protected Element getContainerElement(Element parent) {
                // Return the first TR element in the table.
                return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
            }

            @Override
            protected <X> void render(Context context, LocalityDetail value,
                    SafeHtmlBuilder sb, HasCell<LocalityDetail, X> hasCell) {
                Cell<X> cell = hasCell.getCell();
                sb.appendHtmlConstant("<td>");
                switch (checkboxesUsage) {
                    case Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS:
                        if (cell instanceof CheckboxCell) {
                            //Ak je checkbox, renderuj, len ak je to list
                            if (value.isLeaf()) {
                                cell.render(context, hasCell.getValue(value), sb);
                            }
                        } else {
                            cell.render(context, hasCell.getValue(value), sb);
                        }
                        break;
                    default:
                        cell.render(context, hasCell.getValue(value), sb);
                        break;

                }
                sb.appendHtmlConstant("</td>");
            }
        };
    }

    /**************************************************************************/
    /* HODE, LEAF definitions                                                 */
    /**************************************************************************/
    /**
     * Then inside getNodeInfo(T value) of your TreeViewModel just return a new
     * DeafultNodeInfo with a new MyDataProvider. It defines type what cells to use
     * in each level. In this way NodeInfo is returned synchronously, but the data provider
     * updates itself asynchronously.
     * @param <T>
     * @param value
     * @return
     */
    @Override
    public <T> NodeInfo<?> getNodeInfo(T value) {
        LocalityDetail detail = (LocalityDetail) value;
        switch (checkboxesUsage) {
            case Constants.WITH_CHECK_BOXES:
                LocalityDataProvider dataProvider = new LocalityDataProvider(detail, localityService);
                return new DefaultNodeInfo(dataProvider, localityCell, selectionModel, selectionManager, null);
            case Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS:
                LocalityDataProvider dataProvider1 = new LocalityDataProvider(detail, localityService);
                return new DefaultNodeInfo(dataProvider1, localityCell, selectionModel, selectionManager, null);
            default:
                LocalityDataProvider dataProvider2 = new LocalityDataProvider(detail, localityService);
                return new DefaultNodeInfo(dataProvider2, new LocalityCell(displayCountOfWhat), selectionModel, null);
        }
    }

    @Override
    public boolean isLeaf(Object value) {
        if (value == null) {
            return false;
        } else {
            return ((LocalityDetail) value).isLeaf();
        }
    }
}