package com.eprovement.poptavka.client.common.locality;

import com.eprovement.poptavka.client.service.demand.LocalityRPCServiceAsync;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.view.client.SelectionModel;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.TreeViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link TreeViewModel} used to organize contacts into a hierarchy.
 */
public class LocalityTreeViewModel implements TreeViewModel {

    /**
     * The images used for this example.
     */
    interface Images extends ClientBundle {

        ImageResource contact();

        ImageResource contactsGroup();
    }

    /**
     * Tracks the number of contacts in a locality that begin with the same
     * letter.
     */
    private static class LetterCount implements Comparable<LetterCount> {

        private final LocalityDetail locality;
        private final char firstLetter;
        private int count;

        /**
         * Construct a new {@link LetterCount} for one contact.
         *
         * @param locality the locality
         * @param firstLetter the first letter of the contacts name
         */
        public LetterCount(LocalityDetail locality, char firstLetter) {
            this.locality = locality;
            this.firstLetter = firstLetter;
            this.count = 1;
        }

        public int compareTo(LetterCount o) {
            return (o == null) ? -1 : (firstLetter - o.firstLetter);
        }

        @Override
        public boolean equals(Object o) {
            return compareTo((LetterCount) o) == 0;
        }

        @Override
        public int hashCode() {
            return firstLetter;
        }

        /**
         * Increment the count.
         */
        public void increment() {
            count++;
        }
    }

    /**
     * A Cell used to render the LetterCount.
     */
    private static class LocalityCountCell extends AbstractCell<LocalityDetail> {

        @Override
        public void render(Context context, LocalityDetail value, SafeHtmlBuilder sb) {
            if (value != null) {
                sb.appendEscaped(value.getName());
//                sb.append(" (").append(value.count).append(")");//ziskaj pocet
            }
        }
    }
    /**
     * The static images used in this model.
     */
    private static Images images;
    private Cell<LocalityDetail> localityCell = null;
    private final SelectionModel<LocalityDetail> selectionModel;
    private final DefaultSelectionEventManager<LocalityDetail> selectionManager =
            DefaultSelectionEventManager.createCheckboxManager();
    private LocalityRPCServiceAsync localityService;

    public LocalityTreeViewModel(final SelectionModel<LocalityDetail> selectionModel,
            LocalityRPCServiceAsync localityService) {
        this.selectionModel = selectionModel;
        this.localityService = localityService;
        List<HasCell<LocalityDetail, ?>> hasCells = new ArrayList<HasCell<LocalityDetail, ?>>();
        hasCells.add(new HasCell<LocalityDetail, Boolean>() {

            private CheckboxCell cell = new CheckboxCell(true, false);

            public Cell<Boolean> getCell() {
                return cell;
            }

            public FieldUpdater<LocalityDetail, Boolean> getFieldUpdater() {
                return null;
            }

            public Boolean getValue(LocalityDetail object) {
                return selectionModel.isSelected(object);
            }
        });
        hasCells.add(new HasCell<LocalityDetail, LocalityDetail>() {

            private LocalityCell cell = new LocalityCell();

            public Cell<LocalityDetail> getCell() {
                return cell;
            }

            public FieldUpdater<LocalityDetail, LocalityDetail> getFieldUpdater() {
                return null;
            }

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
                cell.render(context, hasCell.getValue(value), sb);
                sb.appendHtmlConstant("</td>");
            }
        };
    }

    /**
     * ...
     * Then inside getNodeInfo(T value) of your TreeViewModel just return a new
     * DeafultNodeInfo with a new MyDataProvider. In this way your NodeInfo is
     * returned syncronously, but the data provider updates itself asyncronously.
     * ...
     *
     * @param <T>
     * @param value
     * @return
     */
    @Override
    public <T> NodeInfo<?> getNodeInfo(T value) {
        LocalityDetail detail = (LocalityDetail) value;
        if (detail == null || (detail.getLocalityType() != LocalityType.DISTRICT)) {
            LocalityDataProvider dataProvider1 = new LocalityDataProvider(detail, localityService);
            return new DefaultNodeInfo(dataProvider1, new LocalityCell());
        } else if (detail.getLocalityType() == LocalityType.DISTRICT) {
            LocalityDataProvider dataProvider2 = new LocalityDataProvider(detail, localityService);
            return new DefaultNodeInfo(dataProvider2, localityCell, selectionModel, selectionManager, null);
        }
        // Unhandled type.
        String type = value.getClass().getName();
        throw new IllegalArgumentException("Unsupported object type: " + type);
    }

    @Override
    public boolean isLeaf(Object value) {
        return ((LocalityDetail) value).getLocalityType() == LocalityType.CITY;
    }
}