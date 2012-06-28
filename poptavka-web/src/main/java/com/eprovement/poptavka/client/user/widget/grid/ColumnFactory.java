package com.eprovement.poptavka.client.user.widget.grid;

import java.util.Comparator;
import java.util.Date;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.view.client.SelectionModel;

import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.user.widget.grid.cell.ClickableDateCell;
import com.eprovement.poptavka.client.user.widget.grid.cell.DemandStatusImageCell;
import com.eprovement.poptavka.client.user.widget.grid.cell.StarCell;
import com.eprovement.poptavka.client.user.widget.grid.cell.UrgentImageCell;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.domain.demand.BaseDemandDetail;
import com.eprovement.poptavka.shared.domain.message.TableDisplay;

/**
 * Factory class for creating DataGrid columns including optional sorting ability.
 * Contains constants used for column customization.
 *
 * - Contains CheckBox column
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
    public static final int DATE_CREATED = 0;
    public static final int DATE_FINISHED = 1;
    public static final int DATE_EXPIRE = 2;
    public static final int DATE_VALIDTO = 3;
    public static final int DATE_RECEIVED = 4;
    public static final int DATE_ACCEPTED = 5;
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
     * Image header for rating column.
     *
     * @return new header
     */
    public Header<ImageResource> createRatingImgHeader() {

//        return new Header<ImageResource>(new AbstractCell<ImageResource>()) {
//
//            @Override
//            public ImageResource getValue() {
//                return Storage.RSCS.images().urgent();
//            }
//        };
        return null;
    }

    /**
     * Create checkbox column providing selecting whole row/rows.
     *
     * @param selectionModel
     * @return checkColumn
     */
    public Column<T, Boolean> createCheckboxColumn(final SelectionModel<T> selectionModel) {
        Column<T, Boolean> checkColumn = new Column<T, Boolean>(new CheckboxCell(true, false)) {

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
        col.setCellStyleNames(Storage.RSCS.grid().cellTableHandCursor());
        return col;
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
    public Column<T, DemandStatus> createStatusColumn() {
        Column<T, DemandStatus> col = new Column<T, DemandStatus>(new DemandStatusImageCell()) {

            @Override
            public DemandStatus getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                return obj.getDemandStatus();
            }
        };
        //set column style
        col.setCellStyleNames(Storage.RSCS.grid().cellTableHandCursor());
        return col;
    }

    /**
     * Create title column.
     *
     * @param sortHandler sortHandler for sorting. If sortHandler is null, no sorting will be associated.
     * @param displayMessageCount
     *
     * @return titleColumn
     */
    public Column<T, String> createTitleColumn(ListHandler<T> sortHandler, final boolean displayMessageCount) {
        Column<T, String> titleColumn = (new Column<T, String>(tableTextCell) {

            private boolean displayMessages = displayMessageCount;

            @Override
            public String getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                if (displayMessages) {
                    return BaseDemandDetail.displayHtml(obj.getDemandTitle()
                            + " " + obj.getFormattedMessageCount(), obj.isRead());
                } else {
                    return BaseDemandDetail.displayHtml(obj.getDemandTitle(), obj.isRead());
                }
            }
        });
        if (sortHandler != null) {
            titleColumn.setSortable(true);
            sortHandler.setComparator(titleColumn, new Comparator<T>() {

                @Override
                public int compare(T o1, T o2) {

                    return ((TableDisplay) o1).getDemandTitle().compareTo(((TableDisplay) o2).getDemandTitle());
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
                return BaseDemandDetail.displayHtml(obj.getClientName(), obj.isRead());
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
        Column<T, Date> urgencyColumn = new Column<T, Date>(new UrgentImageCell()) {

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
    public Column<T, String> createRatingColumn(ListHandler<T> sortHandler) {
        Column<T, String> ratingColumn = new Column<T, String>(tableTextCell) {

            @Override
            public String getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                return obj.getClientRating() + "%";
            }
        };
        if (sortHandler != null) {
            ratingColumn.setSortable(true);
            sortHandler.setComparator(ratingColumn, new Comparator<T>() {

                @Override
                public int compare(T o1, T o2) {
                    Integer count1 = ((TableDisplay) o1).getClientRating();
                    Integer count2 = ((TableDisplay) o2).getClientRating();
                    return count1.compareTo(count2);
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
     * @param displayMessageCount
     * @return clientName column
     */
    public Column<T, String> createClientColumn(ListHandler<T> sortHandler, final boolean displayMessageCount) {
        Column<T, String> nameColumn = new Column<T, String>(tableTextCell) {

            private boolean displayMessages = displayMessageCount;

            @Override
            public String getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                if (displayMessages) {
                    return obj.getClientName() + " " + obj.getFormattedMessageCount();
                } else {
                    return obj.getClientName();
                }
            }
        };
        if (sortHandler != null) {
            nameColumn.setSortable(true);
            sortHandler.setComparator(nameColumn, new Comparator<T>() {

                @Override
                public int compare(T o1, T o2) {
                    Integer count1 = ((TableDisplay) o1).getMessageCount();
                    Integer count2 = ((TableDisplay) o2).getMessageCount();
                    return count1.compareTo(count2);
                }
            });
        }
        return nameColumn;
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
    public Column<T, Date> createDateColumn(ListHandler<T> sortHandler, final int dateType) {
        Column<T, Date> ratingColumn = new Column<T, Date>(new ClickableDateCell()) {

            private int type = dateType;

            @Override
            public Date getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                Date date = null;
                switch (type) {
                    case DATE_CREATED:
                        date = obj.getCreated();
                        break;
                    case DATE_EXPIRE:
                        date = obj.getExpireDate();
                        break;
                    case DATE_FINISHED:
                        date = obj.getEndDate();
                        break;
                    default:
                        break;
                }
                return date;
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

    public Column<T, DemandStatus> createStatusColumn(ListHandler<T> sortHandler) {
        Column<T, DemandStatus> statusColumn = new Column<T, DemandStatus>(new DemandStatusImageCell()) {

            @Override
            public DemandStatus getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                return obj.getDemandStatus();
            }
        };
        if (sortHandler != null) {
            statusColumn.setSortable(true);
            sortHandler.setComparator(statusColumn, new Comparator<T>() {

                @Override
                public int compare(T o1, T o2) {
                    return ((TableDisplay) o1).getDemandStatus().compareTo(((TableDisplay) o2).getDemandStatus());
                }
            });
        }
        return statusColumn;
    }
}
