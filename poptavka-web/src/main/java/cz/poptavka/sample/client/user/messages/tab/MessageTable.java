package cz.poptavka.sample.client.user.messages.tab;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.message.UserMessageDetail;

public class MessageTable extends DataGrid<UserMessageDetail> {

    private ListDataProvider<UserMessageDetail> dataProvider = new ListDataProvider<UserMessageDetail>();

    public MessageTable(LocalizableMessages msgs, StyleResource rscs) {
        super(KEY_PROVIDER);

        ListHandler<UserMessageDetail> sorHandler = new ListHandler<UserMessageDetail>(dataProvider.getList());
        this.addColumnSortHandler(sorHandler);

        final SelectionModel<UserMessageDetail> selectionModel = new MultiSelectionModel<UserMessageDetail>(
                KEY_PROVIDER);
        this.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<UserMessageDetail>createDefaultManager());

        initTableColumns(selectionModel, sorHandler, msgs);

        // dataProvider
        dataProvider.addDataDisplay(this);
    }

    private void initTableColumns(final SelectionModel<UserMessageDetail> tableSelectionModel,
            ListHandler<UserMessageDetail> sortHandler, final LocalizableMessages msgs) {
        // for EVERY text display
        TextCell tableTextCell = new TextCell(new SafeHtmlRenderer<String>() {

            @Override
            public SafeHtml render(String object) {
                return SafeHtmlUtils.fromTrustedString(object);
            }

            @Override
            public void render(String object, SafeHtmlBuilder builder) {
                builder.appendHtmlConstant(object);
            }
        });
//         MultipleSelection Checkbox
        Column<UserMessageDetail, Boolean> checkBoxColumn = new Column<UserMessageDetail, Boolean>(
                new CheckboxCell(true, false)) {

            @Override
            public Boolean getValue(UserMessageDetail object) {
                return tableSelectionModel.isSelected(object);
            }
        };

        // Message Subject Column
        Column<UserMessageDetail, String> subjectColumn = (new Column<UserMessageDetail, String>(tableTextCell) {

            @Override
            public String getValue(UserMessageDetail object) {
                return UserMessageDetail.displayHtml(object.getMessageDetail().getSubject(), object.isRead());
            }
        });

//        // Demand Price Column
//        Column<UserMessageDetail, String> priceColumn
//            = (new Column<UserMessageDetail, String>(tableTextCell) {
//                @Override
//                public String getValue(UserMessageDetail object) {
//                    if (object.getDemandPrice().equals(BigDecimal.ZERO)) {
//                        return BaseDemandDetail.displayHtml(msgs.notEntered(), object.isRead());
//                    }
//                    return BaseDemandDetail.displayHtml(object.getDemandPrice().toString(), object.isRead());
//                }
//            });

        final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);

        // Message Created Column
        Column<UserMessageDetail, String> createdDateColumn = (new Column<UserMessageDetail, String>(tableTextCell) {

            @Override
            public String getValue(UserMessageDetail object) {
                if (object.getMessageDetail().getCreated() == null) {
                    return "";
                } else {
                    return UserMessageDetail.displayHtml(dateFormat.format(
                            object.getMessageDetail().getCreated()), object.isRead());
                }
            }
        });

        // Message sent Date column
        Column<UserMessageDetail, String> sentDateColumn = (new Column<UserMessageDetail, String>(tableTextCell) {

            @Override
            public String getValue(UserMessageDetail object) {
                if (object.getMessageDetail().getSent() == null) {
                    return "";
                } else {
                    return UserMessageDetail.displayHtml(dateFormat.format(
                            object.getMessageDetail().getSent()), object.isRead());
                }
            }
        });

//        // sort methods ****************************
//        titleColumn.setSortable(true);
//        sortHandler.setComparator(titleColumn,
//                new Comparator<UserMessageDetail>() {
//                    @Override
//                    public int compare(UserMessageDetail o1,
//                            UserMessageDetail o2) {
//                        if (o1 == o2) {
//                            return 0;
//                        }
//
//                        // Compare the name columns.
//                        if (o1 != null) {
//                            return (o2 != null) ? o1.getMessageDetail().getSubject()
//                                    .compareTo(o2.getMessageDetail().getSubject()) : 1;
//                        }
//                        return -1;
//                    }
//                });
//        priceColumn.setSortable(true);
//        sortHandler.setComparator(priceColumn,
//                new Comparator<UserMessageDetail>() {
//                    @Override
//                    public int compare(UserMessageDetail o1,
//                            UserMessageDetail o2) {
//                        return o1.getDemandPrice().compareTo(o2.getDemandPrice());
//                    }
//                });
//        endDateColumn.setSortable(true);
//        validToDateColumn.setSortable(true);
//        Comparator<UserMessageDetail> endComparator = new Comparator<UserMessageDetail>() {
//            @Override
//            public int compare(UserMessageDetail o1, UserMessageDetail o2) {
//                return o1.getEndDate().compareTo(o2.getEndDate());
//            }
//        };
//        Comparator<UserMessageDetail> validComparator = new Comparator<UserMessageDetail>() {
//            @Override
//            public int compare(UserMessageDetail o1, UserMessageDetail o2) {
//                return o1.getValidToDate().compareTo(o2.getValidToDate());
//            }
//        };
//        sortHandler.setComparator(endDateColumn, endComparator);
//        sortHandler.setComparator(validToDateColumn, validComparator);
//        // add columns into table
        this.addColumn(checkBoxColumn);
        this.addColumn(subjectColumn, msgs.title());
//        this.addColumn(priceColumn, msgs.price());
        this.addColumn(createdDateColumn, msgs.createdDate());
        this.addColumn(sentDateColumn, msgs.sentDate());
    }
    private static final ProvidesKey<UserMessageDetail> KEY_PROVIDER = new ProvidesKey<UserMessageDetail>() {

        @Override
        public Object getKey(UserMessageDetail item) {
            return item == null ? null : item.getMessageDetail().getDemandId();
        }
    };

    public ListDataProvider<UserMessageDetail> getDataProvider() {
        return dataProvider;
    }
}
