package com.eprovement.poptavka.client.user.widget.grid.unused;

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
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.message.PotentialDemandMessage;

import java.math.BigDecimal;
import java.util.Comparator;

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

        final Column<PotentialDemandMessage, String> titleColumn = createDemandTitleColumn(tableTextCell);
        Column<PotentialDemandMessage, String> priceColumn = createDemandPriceColumn(msgs, tableTextCell);
        final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
        Column<PotentialDemandMessage, String> endDateColumn = createDemandFinishColumn(tableTextCell, dateFormat);
        Column<PotentialDemandMessage, String> validToDateColumn =
                createDemandSentDateColumn(tableTextCell, dateFormat);

        // sort methods ****************************
        setupSortingForTitle(sortHandler, titleColumn);
        setupSortingForPrice(sortHandler, priceColumn);
        endDateColumn.setSortable(true);
        validToDateColumn.setSortable(true);
        setupSortingForEndDate(sortHandler, endDateColumn);
        setupSortingForValidToDate(sortHandler, validToDateColumn);
        // add columns into table
        // this.addColumn(checkBoxColumn);
        this.addColumn(titleColumn, msgs.title());
        this.addColumn(priceColumn, msgs.price());
        this.addColumn(endDateColumn, msgs.endDate());
        this.addColumn(validToDateColumn, msgs.expireDate());
    }

    private void setupSortingForValidToDate(ListHandler<PotentialDemandMessage> sortHandler,
                                            Column<PotentialDemandMessage, String> validToDateColumn) {
        Comparator<PotentialDemandMessage> validComparator = new Comparator<PotentialDemandMessage>() {
            @Override
            public int compare(PotentialDemandMessage o1, PotentialDemandMessage o2) {
                return o1.getValidToDate().compareTo(o2.getValidToDate());
            }
        };
        sortHandler.setComparator(validToDateColumn, validComparator);
    }

    private void setupSortingForEndDate(ListHandler<PotentialDemandMessage> sortHandler,
                                        Column<PotentialDemandMessage, String> endDateColumn) {
        Comparator<PotentialDemandMessage> endComparator = new Comparator<PotentialDemandMessage>() {
            @Override
            public int compare(PotentialDemandMessage o1, PotentialDemandMessage o2) {
                return o1.getEndDate().compareTo(o2.getEndDate());
            }
        };
        sortHandler.setComparator(endDateColumn, endComparator);
    }

    private void setupSortingForPrice(ListHandler<PotentialDemandMessage> sortHandler,
                                      Column<PotentialDemandMessage, String> priceColumn) {
        priceColumn.setSortable(true);
        sortHandler.setComparator(priceColumn,
                new Comparator<PotentialDemandMessage>() {
                    @Override
                    public int compare(PotentialDemandMessage o1,
                            PotentialDemandMessage o2) {
                        return o1.getDemandPrice().compareTo(o2.getDemandPrice());
                    }
                });
    }

    private void setupSortingForTitle(ListHandler<PotentialDemandMessage> sortHandler,
                                      Column<PotentialDemandMessage, String> titleColumn) {
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
    }

    private Column<PotentialDemandMessage, String> createDemandSentDateColumn(final TextCell tableTextCell,
                                                                              final DateTimeFormat dateFormat) {
        return (new Column<PotentialDemandMessage, String>(tableTextCell) {
            @Override
            public String getValue(PotentialDemandMessage object) {
                return dateFormat.format(object.getValidToDate());
            }
        });
    }

    private Column<PotentialDemandMessage, String> createDemandFinishColumn(final TextCell tableTextCell,
                                                                            final DateTimeFormat dateFormat) {
        return (new Column<PotentialDemandMessage, String>(tableTextCell) {
            @Override
            public String getValue(PotentialDemandMessage object) {
                return dateFormat.format(object.getEndDate());
            }
        });
    }

    private Column<PotentialDemandMessage, String> createDemandPriceColumn(final LocalizableMessages msgs,
                                                                           final TextCell tableTextCell) {
        return (new Column<PotentialDemandMessage, String>(tableTextCell) {
            @Override
            public String getValue(PotentialDemandMessage object) {
                if (object.getDemandPrice().equals(BigDecimal.ZERO)) {
                    return msgs.notEntered();
                }
                return object.getDemandPrice().toString();
            }
        });
    }

    private Column<PotentialDemandMessage, String> createDemandTitleColumn(final TextCell tableTextCell) {
        return (new Column<PotentialDemandMessage, String>(tableTextCell) {
            @Override
            public String getValue(PotentialDemandMessage object) {
                return object.getSubject();
            }
        });
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
