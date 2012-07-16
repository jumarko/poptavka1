package com.eprovement.poptavka.client.user.messages.tab;

import java.util.Comparator;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.view.client.SelectionModel;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.cell.StarCell;
import com.eprovement.poptavka.shared.domain.demand.BaseDemandDetail;

/**
 * Factory class for creating DataGrid columns including optional sorting ability.
 * Contains constants used for column customization.
 *
 * - Contains CheckBox column
 *
 * @author beho
 *
 */
public class MessageColumnFactory<T> {

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
                MessageTableDisplay obj = (MessageTableDisplay) object;
                return obj.isStarred();
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
     * @return titleColumn
     */
    public Column<T, String> createUserColumn(ListHandler<T> sortHandler) {
        Column<T, String> titleColumn = (new Column<T, String>(tableTextCell) {

            @Override
            public String getValue(T object) {
                MessageTableDisplay obj = (MessageTableDisplay) object;
                return BaseDemandDetail.displayHtml(obj.getSenderEmail(), obj.isRead());
            }
        });
        if (sortHandler != null) {
            titleColumn.setSortable(true);
            sortHandler.setComparator(titleColumn, new Comparator<T>() {

                @Override
                public int compare(T o1, T o2) {

                    return ((MessageTableDisplay) o1).getSenderEmail().compareTo(
                            ((MessageTableDisplay) o2).getSenderEmail());
                }
            });
        }
        return titleColumn;
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
    public Column<T, String> createSubjectColumn(ListHandler<T> sortHandler, final boolean displayMessageCount) {
        Column<T, String> clientColumn = new Column<T, String>(tableTextCell) {

            private boolean displayMessages = displayMessageCount;

            @Override
            public String getValue(T object) {
                MessageTableDisplay obj = (MessageTableDisplay) object;
                if (displayMessages) {
                    return BaseDemandDetail.displayHtml(
                            obj.getMessageDetail().getSubject() + " " + obj.getFormattedMessageCount(), obj.isRead());
                } else {
                    return BaseDemandDetail.displayHtml(obj.getMessageDetail().getSubject(), obj.isRead());
                }
            }
        };
        if (sortHandler != null) {
            clientColumn.setSortable(true);
            sortHandler.setComparator(clientColumn, new Comparator<T>() {

                @Override
                public int compare(T o1, T o2) {
                    Integer count1 = ((MessageTableDisplay) o1).getMessageCount();
                    Integer count2 = ((MessageTableDisplay) o2).getMessageCount();
                    return count1.compareTo(count2);
                }
            });
        }
        return clientColumn;
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
    public Column<T, String> createDateColumn(ListHandler<T> sortHandler, final int dateType) {
        Column<T, String> ratingColumn = new Column<T, String>(tableTextCell) {

            private int type = dateType; // ???

            @Override
            public String getValue(T object) {
                MessageTableDisplay obj = (MessageTableDisplay) object;
                return BaseDemandDetail.displayHtml(
                        DateTimeFormat.getFormat("MM/dd/yy").format(obj.getMessageDetail().getCreated()),
                        obj.isRead());
            }
        };
        if (sortHandler != null) {
            ratingColumn.setSortable(true);
            sortHandler.setComparator(ratingColumn, new Comparator<T>() {

                @Override
                public int compare(T o1, T o2) {
                    return ((MessageTableDisplay) o1).getMessageDetail().getCreated().compareTo(
                            ((MessageTableDisplay) o2).getMessageDetail().getCreated());
                }
            });
        }
        return ratingColumn;
    }
//    public Column<T, DemandStatusType> createStatusColumn(ListHandler<T> sortHandler) {
//        Column<T, DemandStatusType> statusColumn = new Column<T, DemandStatusType>(new StatusImageCell()) {
//
//            @Override
//            public DemandStatusType getValue(T object) {
//                MessageTableDisplay obj = (MessageTableDisplay) object;
//                return obj.getDemandStatus();
//            }
//
//        };
//        if (sortHandler != null) {
//            statusColumn.setSortable(true);
//            sortHandler.setComparator(statusColumn, new Comparator<T>() {
//
//                @Override
//                public int compare(T o1, T o2) {
//                    return ((MessageTableDisplay) o1).getDemandStatus()
//.compareTo(((MessageTableDisplay) o2).getDemandStatus());
//                }
//            });
//        }
//        return statusColumn;
//    }
}
