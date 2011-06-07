package cz.poptavka.sample.client.user.demands.widget.table;

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
import cz.poptavka.sample.shared.domain.message.PotentialMessageDetail;

public class PotentialDemandTable extends CellTable<PotentialMessageDetail> {

    private ListDataProvider<PotentialMessageDetail> dataProvider = new ListDataProvider<PotentialMessageDetail>();

    public PotentialDemandTable(LocalizableMessages msgs, StyleResource rscs) {
        super(KEY_PROVIDER);

        ListHandler<PotentialMessageDetail> sorHandler
            = new ListHandler<PotentialMessageDetail>(dataProvider.getList());
        this.addColumnSortHandler(sorHandler);

        final SelectionModel<PotentialMessageDetail> selectionModel = new MultiSelectionModel<PotentialMessageDetail>(
                KEY_PROVIDER);
        this.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<PotentialMessageDetail>createDefaultManager());

        initTableColumns(selectionModel, sorHandler, msgs);

        // dataProvider
        dataProvider.addDataDisplay(this);
    }

    private void initTableColumns(final SelectionModel<PotentialMessageDetail> tableSelectionModel,
            ListHandler<PotentialMessageDetail> sortHandler, LocalizableMessages msgs) {
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
        Column<PotentialMessageDetail, Boolean> checkBoxColumn = new Column<PotentialMessageDetail, Boolean>(
                new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(PotentialMessageDetail object) {
                return tableSelectionModel.isSelected(object);
            }
        };

        // Demand Title Column
        Column<PotentialMessageDetail, String> titleColumn
            = (new Column<PotentialMessageDetail, String>(tableTextCell) {
                @Override
                public String getValue(PotentialMessageDetail object) {
                    return BaseDemandDetail.displayHtml(object.getSubject(), object.isRead());
                }
            });

        // Demand Price Column
        Column<PotentialMessageDetail, String> priceColumn
            = (new Column<PotentialMessageDetail, String>(tableTextCell) {
                @Override
                public String getValue(PotentialMessageDetail object) {
                    String price = (object.getPrice().intValue() < 0
                            ? ("none") : object.getPrice().toString());
                    return BaseDemandDetail.displayHtml(price, object.isRead());
                }
            });

        final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);

        // Demand Finish Column
        Column<PotentialMessageDetail, String> endDateColumn
            = (new Column<PotentialMessageDetail, String>(tableTextCell) {
                @Override
                public String getValue(PotentialMessageDetail object) {
                    return BaseDemandDetail.displayHtml(dateFormat.format(object.getEndDate()), object.isRead());
                }
            });

        // Demand sent Date column
        Column<PotentialMessageDetail, String> validToDateColumn
            = (new Column<PotentialMessageDetail, String>(tableTextCell) {
                @Override
                public String getValue(PotentialMessageDetail object) {
                    return BaseDemandDetail.displayHtml(dateFormat.format(object.getValidToDate()), object.isRead());
                }
            });

        // sort methods ****************************
        titleColumn.setSortable(true);
        sortHandler.setComparator(titleColumn,
                new Comparator<PotentialMessageDetail>() {
                    @Override
                    public int compare(PotentialMessageDetail o1,
                            PotentialMessageDetail o2) {
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
                new Comparator<PotentialMessageDetail>() {
                    @Override
                    public int compare(PotentialMessageDetail o1,
                            PotentialMessageDetail o2) {
                        return o1.getPrice().compareTo(o2.getPrice());
                    }
                });
        endDateColumn.setSortable(true);
        validToDateColumn.setSortable(true);
        Comparator<PotentialMessageDetail> endComparator = new Comparator<PotentialMessageDetail>() {
            @Override
            public int compare(PotentialMessageDetail o1, PotentialMessageDetail o2) {
                return o1.getEndDate().compareTo(o2.getEndDate());
            }
        };
        Comparator<PotentialMessageDetail> validComparator = new Comparator<PotentialMessageDetail>() {
            @Override
            public int compare(PotentialMessageDetail o1, PotentialMessageDetail o2) {
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

    private static final ProvidesKey<PotentialMessageDetail> KEY_PROVIDER = new ProvidesKey<PotentialMessageDetail>() {
        @Override
        public Object getKey(PotentialMessageDetail item) {
            return item == null ? null : item.getDemandId();
        }
    };

    public ListDataProvider<PotentialMessageDetail> getDataProvider() {
        return dataProvider;
    }

}
