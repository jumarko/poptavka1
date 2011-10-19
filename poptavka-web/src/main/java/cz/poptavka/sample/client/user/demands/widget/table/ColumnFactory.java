package cz.poptavka.sample.client.user.demands.widget.table;

import java.util.Comparator;
import java.util.Date;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.view.client.SelectionModel;

import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;
import cz.poptavka.sample.shared.domain.message.TableDisplay;

/**
 * Factory class for creating DataGrid columns.
 * Contains constants used for column customization.
 *
 * @author beho
 *
 */
public class ColumnFactory<T> {

    //col pos
    public static final int COL_ZERO = 0;
    public static final int COL_ONE = 1;
    public static final int COL_TWO = 2;
    public static final int COL_THREE = 3;
    public static final int COL_FOUR = 4;
    public static final int COL_FIVE = 5;
    public static final int COL_SIX = 6;
    public static final int COL_SEVEN = 7;

    //widths
    public static final int WIDTH_40 = 40;

    // for EVERY text display
    // providing HTML safe display
    private ClickableTextCell tableTextCell = new ClickableTextCell(new SafeHtmlRenderer<String>() {
        @Override
        public SafeHtml render(String object) {
            return SafeHtmlUtils.fromTrustedString(object);
        }
        @Override
        public void render(String object, SafeHtmlBuilder builder) {
            builder.appendHtmlConstant(object);
        }
    });

    /**
     * ClickBox header with default UNCHECKED option. It's getValue method ALWAYS return false.
     *
     * @return new header
     */
    public Header<Boolean> createCheckBoxHeader() {
        return new Header<Boolean>(new CheckboxCell()) {

            @Override
            public Boolean getValue() {
                return false;
            }
        };
    }

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
     * Creates star-column depending on messages' isRead value. By clicking this cell, STAR attribute is immediately
     * updated in database.
     *
     * NOTE:
     * Sorting is not implemented now.
     * //TODO
     * Implement sorting according to star status
     *
     * @return star column
     */
    public Column<T, Boolean> createStarColumn() {
        Column<T, Boolean> col = new Column<T, Boolean>(new StarCell()) {

            @Override
            public Boolean getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                return obj.isStarred();
            }

        };
        //set column style
        return col;
    }


    /**
     * Create title column.
     *
     * @param sortHandler sortHandler for sorting. If sortHandler is null, no sorting will be associated.
     * @return titleColumn
     */
    public Column<T, String> createTitleColumn(ListHandler<T> sortHandler) {
        Column<T, String> titleColumn = (new Column<T, String>(tableTextCell) {
            @Override
            public String getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                return BaseDemandDetail.displayHtml(obj.getTitle(), obj.isRead());
            }
        });
        if (sortHandler != null) {
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

    /**
     * Create price column.
     *
     * @param sortHandler sortHandler for sorting. If sortHandler is null, no sorting will be associated.
     * @return priceColumn
     */
    public Column<T, String> createPriceColumn(ListHandler<T> sortHandler) {
        Column<T, String> titleColumn = (new Column<T, String>(tableTextCell) {
            @Override
            public String getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                return BaseDemandDetail.displayHtml(obj.getDemandPrice(), obj.isRead());
            }
        });
        if (sortHandler != null) {
            titleColumn.setSortable(true);
            sortHandler.setComparator(titleColumn, new Comparator<T>() {
                @Override
                public int compare(T o1, T o2) {
                    return ((TableDisplay) o1).getDemandPrice().compareTo(((TableDisplay) o2).getDemandPrice());
                }
            });
        }
        return titleColumn;
    }

    /**
     * Creates urgencyDisplay column.
     *
     * @param sortHandler
     * @return urgencyColumn
     */
    public Column<T, Date> createUrgentColumn(ListHandler<T> sortHandler) {
        Column<T, Date> urgencyColumn = new Column<T, Date>(new ClickableImageCell()) {

            @Override
            public Date getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                return obj.getEndDate();
            }
        };
        if (sortHandler != null) {
            urgencyColumn.setSortable(true);
            sortHandler.setComparator(urgencyColumn, new Comparator<T>() {

                @Override
                public int compare(T o1, T o2) {
                    return ((TableDisplay) o1).getEndDate().compareTo(((TableDisplay) o2).getEndDate());
                }
            });
        }
        return urgencyColumn;
    }

    /**
     * Creates clientRating column.
     *
     * TODO
     * in devel progress
     *
     * @param sortHandler
     * @return ratingColumn
     */
    public Column<T, String> createClientRatingColumn(ListHandler<T> sortHandler) {
        Column<T, String> ratingColumn = new Column<T, String>(tableTextCell) {

            @Override
            public String getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                //TODO
//                return obj.getRating();
                return "#TODO";
            }

        };
        if (sortHandler != null) {
            ratingColumn.setSortable(true);
            sortHandler.setComparator(ratingColumn, new Comparator<T>() {

                @Override
                public int compare(T o1, T o2) {
                    //TODO
//                    return ((TableDisplay) o1).get().compareTo(((TableDisplay) o2).getEndDate());
                    return 0;
                }
            });
        }
        return ratingColumn;
    }

    /**
     * Creates client column with number of message in conversation.
     *
     * TODO
     * in devel progress - no mandatory attributes are implementend in messageObjects - clinektName, messageCount
     * update sorting accordingly
     *
     * @param sortHandler
     * @return ratingColumn
     */
    public Column<T, String> createClientColumn(ListHandler<T> sortHandler) {
        Column<T, String> ratingColumn = new Column<T, String>(tableTextCell) {

            @Override
            public String getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                //TODO
//                return obj.getRating();
                return "#TODO - CilentName (messageCount)";
            }

        };
        if (sortHandler != null) {
            ratingColumn.setSortable(true);
            sortHandler.setComparator(ratingColumn, new Comparator<T>() {

                @Override
                public int compare(T o1, T o2) {
                    //TODO
//                    return ((TableDisplay) o1).get().compareTo(((TableDisplay) o2).getEndDate());
                    return 0;
                }
            });
        }
        return ratingColumn;
    }

    /**
     * Creates demandCreation column.
     *
     * TODO
     * in devel progress
     *
     * @param sortHandler
     * @return ratingColumn
     */
    public Column<T, Date> createCreationDateColumn(ListHandler<T> sortHandler) {
        Column<T, Date> ratingColumn = new Column<T, Date>(new ClickableDateCell()) {

            @Override
            public Date getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                return obj.getCreated();
            }

        };
        if (sortHandler != null) {
            ratingColumn.setSortable(true);
            sortHandler.setComparator(ratingColumn, new Comparator<T>() {

                @Override
                public int compare(T o1, T o2) {
                    return ((TableDisplay) o1).getCreated().compareTo(((TableDisplay) o2).getCreated());
                }
            });
        }
        return ratingColumn;
    }


}
