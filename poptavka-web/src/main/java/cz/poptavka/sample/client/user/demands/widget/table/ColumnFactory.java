package cz.poptavka.sample.client.user.demands.widget.table;

import java.util.Comparator;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.view.client.SelectionModel;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;
import cz.poptavka.sample.shared.domain.message.TableDisplay;

/**
 * Factory class for creating DataGrid columns.
 *
 * @author beho
 *
 */
public class ColumnFactory<T> {

    // for EVERY text display
    // providing HTML safe display
    private TextCell tableTextCell = new TextCell(new SafeHtmlRenderer<String>() {
        @Override
        public SafeHtml render(String object) {
            return SafeHtmlUtils.fromTrustedString(object);
        }
        @Override
        public void render(String object, SafeHtmlBuilder builder) {
            builder.appendHtmlConstant(object);
        }
    });

    //TODO maybe this selection model will have to be called from Table itself. Got to test later.
    /**
     * Create checkbox column providing selecting whole row/rows.
     *
     * @param selectionModel
     * @return checkColumn
     */
    public Column<T, Boolean> createCheckboxColumn(final SelectionModel<T> selectionModel) {
        Column<T, Boolean> checkColumn =  new Column<T, Boolean>(new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(T object) {
              // Get the value from the selection model.
                return selectionModel.isSelected(object);
            }
        };
        return checkColumn;
    }

    /**
     * Creates star-column deending on messages' isRead value.
     *
     * TODO implements clickability
     *
     * @return
     */
    public Column<T, ImageResource> createStarColumn() {
        Column<T, ImageResource> col = new Column<T, ImageResource>(new ImageResourceCell()) {

            @Override
            public ImageResource getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                if (obj.isStarred()) {
                    return Storage.RSCS.images().starGold();
                } else {
                    return Storage.RSCS.images().starSilver();
                }
            }
        };
        return col;
    }


    /**
     * Create title column.
     *
     * @param sortHandler sortHandler for sorting. If sortHandler is null, no sorting will be associated.
     * @param sortable sortable or not
     * @return titleColumn
     */
    public Column<T, String> createTitleColumn(ListHandler<T> sortHandler, boolean sortable) {
        Column<T, String> titleColumn = (new Column<T, String>(tableTextCell) {
            @Override
            public String getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                return BaseDemandDetail.displayHtml(obj.getTitle(), obj.isRead());
            }
        });
        if (sortable && (sortHandler != null)) {
            titleColumn.setSortable(true);
            sortHandler.setComparator(titleColumn, new Comparator<T>() {
                @Override
                public int compare(T o1, T o2) {

                    return ((TableDisplay) o1).getTitle().compareTo(((TableDisplay) o2).getTitle());
                }
            });
        }
        return titleColumn;
    }

    private class ClickableImageCell extends AbstractCell<ImageResource> {

        @Override
        public void render(Cell.Context context,
                ImageResource value, SafeHtmlBuilder sb) {
            sb.appendEscaped("<div>");
            sb.appendEscaped(value.getSafeUri() + "</div>");
        }


    }

}
