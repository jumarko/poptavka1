package cz.poptavka.sample.client.user.demands.widget.table;

import java.math.BigDecimal;
import java.util.Comparator;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;

public class PotentialDemandTable extends CellTable<PotentialDemandMessage> {

    private ListDataProvider<PotentialDemandMessage> dataProvider = new ListDataProvider<PotentialDemandMessage>();

    public PotentialDemandTable(LocalizableMessages msgs, StyleResource rscs) {
        super(KEY_PROVIDER);

        ListHandler<PotentialDemandMessage> sorHandler
            = new ListHandler<PotentialDemandMessage>(dataProvider.getList());
        this.addColumnSortHandler(sorHandler);

        final SelectionModel<PotentialDemandMessage> selectionModel = new MultiSelectionModel<PotentialDemandMessage>(
                KEY_PROVIDER);
        this.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<PotentialDemandMessage>createDefaultManager());

        initTableColumns(selectionModel, sorHandler, msgs);

        // dataProvider
        dataProvider.addDataDisplay(this);
    }

    private void initTableColumns(final SelectionModel<PotentialDemandMessage> tableSelectionModel,
            ListHandler<PotentialDemandMessage> sortHandler, final LocalizableMessages msgs) {
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
        Column<PotentialDemandMessage, Boolean> checkBoxColumn = new Column<PotentialDemandMessage, Boolean>(
                new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(PotentialDemandMessage object) {
                return tableSelectionModel.isSelected(object);
            }
        };

        // Demand Title Column
        Column<PotentialDemandMessage, String> titleColumn
            = (new Column<PotentialDemandMessage, String>(tableTextCell) {
                @Override
                public String getValue(PotentialDemandMessage object) {
                    return BaseDemandDetail.displayHtml(object.getSubject(), object.isRead());
                }
            });

        // Demand Price Column
        Column<PotentialDemandMessage, String> priceColumn
            = (new Column<PotentialDemandMessage, String>(tableTextCell) {
                @Override
                public String getValue(PotentialDemandMessage object) {
                    if (object.getPrice().equals(BigDecimal.ZERO)) {
                        return BaseDemandDetail.displayHtml(msgs.notEntered(), object.isRead());
                    }
                    return BaseDemandDetail.displayHtml(object.getPrice().toString(), object.isRead());
                }
            });

        final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);

        // Demand Finish Column
        Column<PotentialDemandMessage, String> endDateColumn
            = (new Column<PotentialDemandMessage, String>(tableTextCell) {
                @Override
                public String getValue(PotentialDemandMessage object) {
                    return BaseDemandDetail.displayHtml(dateFormat.format(object.getEndDate()), object.isRead());
                }
            });

        // Demand sent Date column
        Column<PotentialDemandMessage, String> validToDateColumn
            = (new Column<PotentialDemandMessage, String>(tableTextCell) {
                @Override
                public String getValue(PotentialDemandMessage object) {
                    return BaseDemandDetail.displayHtml(dateFormat.format(object.getValidToDate()), object.isRead());
                }
            });

        // sort methods ****************************
        titleColumn.setSortable(true);
        sortHandler.setComparator(titleColumn,
                new Comparator<PotentialDemandMessage>() {
                    @Override
                    public int compare(PotentialDemandMessage o1,
                            PotentialDemandMessage o2) {
                        if (o1 == o2) {
                            return 0;
                        }

                        // Compare the name columns.
                        if (o1 != null) {
                            return (o2 != null) ? o1.getSubject()
                                    .compareTo(o2.getSubject()) : 1;
                        }
                        return -1;
                    }
                });
        priceColumn.setSortable(true);
        sortHandler.setComparator(priceColumn,
                new Comparator<PotentialDemandMessage>() {
                    @Override
                    public int compare(PotentialDemandMessage o1,
                            PotentialDemandMessage o2) {
                        return o1.getPrice().compareTo(o2.getPrice());
                    }
                });
        endDateColumn.setSortable(true);
        validToDateColumn.setSortable(true);
        Comparator<PotentialDemandMessage> endComparator = new Comparator<PotentialDemandMessage>() {
            @Override
            public int compare(PotentialDemandMessage o1, PotentialDemandMessage o2) {
                return o1.getEndDate().compareTo(o2.getEndDate());
            }
        };
        Comparator<PotentialDemandMessage> validComparator = new Comparator<PotentialDemandMessage>() {
            @Override
            public int compare(PotentialDemandMessage o1, PotentialDemandMessage o2) {
                return o1.getValidToDate().compareTo(o2.getValidToDate());
            }
        };
        sortHandler.setComparator(endDateColumn, endComparator);
        sortHandler.setComparator(validToDateColumn, validComparator);
        // add columns into table
        // this.addColumn(checkBoxColumn);
        this.addColumn(titleColumn, msgs.title());
        this.addColumn(priceColumn, msgs.price());
        this.addColumn(endDateColumn, msgs.endDate());
        this.addColumn(validToDateColumn, msgs.expireDate());
    }

    private static final ProvidesKey<PotentialDemandMessage> KEY_PROVIDER = new ProvidesKey<PotentialDemandMessage>() {
        @Override
        public Object getKey(PotentialDemandMessage item) {
            return item == null ? null : item.getDemandId();
        }
    };

    public ListDataProvider<PotentialDemandMessage> getDataProvider() {
        return dataProvider;
    }

}
