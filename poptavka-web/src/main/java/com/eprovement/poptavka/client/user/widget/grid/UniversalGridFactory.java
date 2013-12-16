/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.user.widget.grid.cell.StarImageCell;
import com.eprovement.poptavka.client.user.widget.grid.cell.UrgentImageCell;
import com.eprovement.poptavka.client.user.widget.grid.columns.CreatedDateColumn;
import com.eprovement.poptavka.client.user.widget.grid.columns.DemandStatusColumn;
import com.eprovement.poptavka.client.user.widget.grid.columns.DemandTitleColumn;
import com.eprovement.poptavka.client.user.widget.grid.columns.DisplayNameColumn;
import com.eprovement.poptavka.client.user.widget.grid.columns.EndDateColumn;
import com.eprovement.poptavka.client.user.widget.grid.columns.FinishDateColumn;
import com.eprovement.poptavka.client.user.widget.grid.columns.LocalityColumn;
import com.eprovement.poptavka.client.user.widget.grid.columns.MessageSentDateColumn;
import com.eprovement.poptavka.client.user.widget.grid.columns.MessageTextColumn;
import com.eprovement.poptavka.client.user.widget.grid.columns.OfferReceivedDateColumn;
import com.eprovement.poptavka.client.user.widget.grid.columns.PriceColumn;
import com.eprovement.poptavka.client.user.widget.grid.columns.RatingColumn;
import com.eprovement.poptavka.client.user.widget.grid.columns.SenderColumn;
import com.eprovement.poptavka.client.user.widget.grid.columns.StarColumn;
import com.eprovement.poptavka.client.user.widget.grid.columns.SubjectColumn;
import com.eprovement.poptavka.client.user.widget.grid.columns.UrgencyColumn;
import com.eprovement.poptavka.resources.datagrid.DataGridResources;
import com.eprovement.poptavka.shared.domain.FullClientDetail.ClientField;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail.OfferField;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.eprovement.poptavka.shared.domain.message.MessageDetail.MessageField;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail.UserMessageField;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail.SupplierField;
import com.eprovement.poptavka.shared.search.SortDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Provides easy way to create UniversalAsyncGrid by choosing needed elements.
 * To create table with Demand title column just call:
 * <pre>
 * {@code
 * new UniversalGridFactory.Builder<T>().addColumnDemandTitle(null).build().
 * }
 * </pre>
 * Features:
 * <ul>
 *   <li>addColumnAXZ(columnFieldUpdater),</li>
 *   <li>addDefaultSort(tableSortDefinitions),</li>
 *   <li>addSelectionModel(tableSelectionModel),</li>
 *   <li>addRowStyles(tableRowStyles).</li>
 * </ul>
 *
 * As for this widget has no presenter, action handlers for column field updater
 * must be defined in widget's presenter that is using this one.
 *
 * @author Martin Slavkovsky
 */
public class UniversalGridFactory {

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Constuctor.
     * @param loadedView - define Constant in Constants class which define widget
     * for which table schema is generated.
     */
    public UniversalGridFactory() {
    }

    /**************************************************************************/
    /* Builder                                                                */
    /**************************************************************************/
    public static class Builder<T> {

        /**********************************************************************/
        /* Attributes                                                         */
        /**********************************************************************/
        private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
        private static final DataGridResources GRSCS = GWT.create(DataGridResources.class);
        private List<SortPair> sortColumns = new ArrayList<SortPair>();
        private List<SortPair> defaultSortColumns = new ArrayList<SortPair>();
        private UniversalAsyncGrid table;

        /**********************************************************************/
        /* Constructor                                                        */
        /**********************************************************************/
        /**
         * Initialize Builder for constructing UniversalAsyncGrid wih page size = Constants.PAGER_SIZE_DEFAULT.
         */
        public Builder() {
            CssInjector.INSTANCE.ensureGridStylesInjected(GRSCS);
            table = new UniversalAsyncGrid(Constants.PAGER_SIZE_DEFAULT, GRSCS);
        }

        /**
         * Initialize Builder for constructing UniversalAsyncGrid with given page size.
         */
        public Builder(int pageSize) {
            CssInjector.INSTANCE.ensureGridStylesInjected(GRSCS);
            table = new UniversalAsyncGrid(pageSize, GRSCS);
        }

        /**********************************************************************/
        /* Selection Model initialization                                     */
        /**********************************************************************/
        public Builder addSelectionModel(SelectionModel selectionModel, ProvidesKey<T> keyProvider) {
            // Selection Model - must define different from default which is used in UniversalAsyncGrid
            // Add a selection model so we can select cells.
            if (selectionModel instanceof MultiSelectionModel) {
                table.setSelectionModel(new MultiSelectionModel<T>(keyProvider),
                    DefaultSelectionEventManager.<T>createCheckboxManager());
            } else {
                table.setSelectionModel(new SingleSelectionModel<T>(keyProvider));
            }
            return this;
        }

        /**********************************************************************/
        /* Sort initialization                                                */
        /**********************************************************************/
        public Builder addDefaultSort(List<SortPair> defaultSortPairs) {
            defaultSortColumns = defaultSortPairs;
            return this;
        }

        /**********************************************************************/
        /* Row styles initialization                                          */
        /**********************************************************************/
        /**
         * Set custom row style according to given row styles condition.
         */
        public Builder addRowStyles(RowStyles rowStyles) {
            table.setRowStyles(rowStyles);
            return this;
        }

        /**********************************************************************/
        /* Columns initialization                                             */
        /**********************************************************************/
        //* Text columns. **/
        public Builder addColumnDisplayName(FieldUpdater fieldUpdater) {
            sortColumns.add(null);
            table.addColumn(MSGS.columnSupplierName(), GRSCS.dataGridStyle().colWidthDisplayName(),
                new DisplayNameColumn(fieldUpdater));
            return this;
        }

        public Builder addColumnDemandTitle(FieldUpdater fieldUpdater) {
            sortColumns.add(new SortPair(DemandField.TITLE));
            table.addColumn(MSGS.columnTitle(), GRSCS.dataGridStyle().colWidthTitle(),
                new DemandTitleColumn(fieldUpdater));
            return this;
        }

        public Builder addColumnMessageText(FieldUpdater fieldUpdater) {
            sortColumns.add(new SortPair(MessageField.BODY));
            table.addColumn(MSGS.columnText(), GRSCS.dataGridStyle().colWidthMessageText(),
                new MessageTextColumn(fieldUpdater));
            return this;
        }

        public Builder addColumnSender(FieldUpdater fieldUpdater) {
            sortColumns.add(new SortPair(MessageField.SENDER));
            table.addColumn(MSGS.columnFrom(), GRSCS.dataGridStyle().colWidthSender(),
                new SenderColumn(fieldUpdater));
            return this;
        }

        public Builder addColumnSubject(FieldUpdater fieldUpdater) {
            sortColumns.add(new SortPair(MessageField.SUBJECT));
            table.addColumn(MSGS.columnSubject(), GRSCS.dataGridStyle().colWidthTitle(),
                new SubjectColumn(fieldUpdater));
            return this;
        }

        public Builder addColumnLocality(FieldUpdater fieldUpdater) {
            sortColumns.add(null);
            table.addColumn(MSGS.columnLocality(), GRSCS.dataGridStyle().colWidthLocality(),
                new LocalityColumn(fieldUpdater));
            return this;
        }

        /** Number columns. **/
        public Builder addColumnClientRating(FieldUpdater fieldUpdater) {
            sortColumns.add(new SortPair(ClientField.OVERALL_RATING));
            table.addColumn(MSGS.columnRating(), GRSCS.dataGridStyle().colWidthRatting(),
                new RatingColumn(fieldUpdater));
            return this;
        }

        public Builder addColumnSupplierRating(FieldUpdater fieldUpdater) {
            sortColumns.add(new SortPair(SupplierField.OVERALL_RATING));
            table.addColumn(MSGS.columnRating(), GRSCS.dataGridStyle().colWidthRatting(),
                new RatingColumn(fieldUpdater));
            return this;
        }

        public Builder addColumnPrice(FieldUpdater fieldUpdater) {
            sortColumns.add(new SortPair(DemandField.PRICE));
            table.addColumn(MSGS.columnPrice(), GRSCS.dataGridStyle().colWidthPrice(),
                new PriceColumn(fieldUpdater));
            return this;
        }

        /** Date columns. **/
        public Builder addColumnOfferReceivedDate(FieldUpdater fieldUpdater) {
            sortColumns.add(new SortPair(OfferField.CREATED));
            table.addColumn(MSGS.columnReceived(), GRSCS.dataGridStyle().colWidthReceivedDate(),
                new OfferReceivedDateColumn(fieldUpdater));
            return this;
        }

        public Builder addColumnFinishDate(FieldUpdater fieldUpdater) {
            sortColumns.add(new SortPair(OfferField.FINISH_DATE));
            table.addColumn(MSGS.columnDeliveryDate(), GRSCS.dataGridStyle().colWidthDate(),
                new FinishDateColumn(fieldUpdater));
            return this;
        }

        public Builder addColumnEndDate(FieldUpdater fieldUpdater) {
            sortColumns.add(new SortPair(DemandField.END_DATE));
            table.addColumn(MSGS.columnEndDate(), GRSCS.dataGridStyle().colWidthDate(),
                new EndDateColumn(fieldUpdater));
            return this;
        }

        public Builder addColumnMessageSent(FieldUpdater fieldUpdater) {
            sortColumns.add(new SortPair(MessageField.SENT));
            table.addColumn(MSGS.columnDate(), GRSCS.dataGridStyle().colWidthDate(),
                new MessageSentDateColumn(fieldUpdater));
            return this;
        }

        public Builder addColumnMessageCreated(FieldUpdater fieldUpdater) {
            sortColumns.add(new SortPair(MessageField.CREATED));
            table.addColumn(MSGS.columnReceived(), GRSCS.dataGridStyle().colWidthDate(),
                new CreatedDateColumn(fieldUpdater));
            return this;
        }

        public Builder addColumnDemandCreated(FieldUpdater fieldUpdater) {
            sortColumns.add(new SortPair(DemandField.CREATED));
            table.addColumn(MSGS.columnCreatedDate(), GRSCS.dataGridStyle().colWidthCreatedDate(),
                new CreatedDateColumn(fieldUpdater));
            return this;
        }

        /** Custom columns. **/
        public Builder addColumn(String header, String width, Column customColumn) {
            table.addColumn(header, width, customColumn);
            return this;
        }
        /* Create checkbox column providing selecting whole row/rows.
         *
         * @param selectionModel
         * @return checkColumn
         */
        public Builder addColumnCheckbox(ValueUpdater valueHeaderUpdater) {
            sortColumns.add(null);
            Header<Boolean> checkHeader = new Header<Boolean>(new CheckboxCell()) {
                @Override
                public Boolean getValue() {
                    return false;
                }
            };
            checkHeader.setUpdater(valueHeaderUpdater);
            Column<T, Boolean> checkColumn = new Column<T, Boolean>(new CheckboxCell(true, false)) {
                @Override
                public Boolean getValue(T object) {
                    // Get the value from the selection model.
                    return table.getSelectionModel().isSelected(object);
                }
            };
            table.addColumn(checkHeader, GRSCS.dataGridStyle().colWidthIcon(), checkColumn);
            return this;
        }

        /**
         * Creates star-column depending on messages' isStared value.
         * By clicking this cell, STAR attribute is immediately updated in database.
         *
         * NOTE:
         * Sorting is not implemented now.
         * Implement sorting according to star status
         *
         * @return star column
         */
        public Builder addColumnStar(FieldUpdater fieldUpdater) {
            sortColumns.add(new SortPair(UserMessageField.STARRED));
            //create star header represented by star image
            Header starHeader = new Header<Boolean>(new StarImageCell()) {
                @Override
                public Boolean getValue() {
                    /* Returning null value tells StarCell to use header image.
                     * Using it this way we can use same class StarCell for
                     * providing star images as for header as for star column items.
                     * Otherwise we must create new class image cell providing only header's image. */
                    return null;
                }
            };
            starHeader.setHeaderStyleNames(GRSCS.dataGridStyle().cellStyleStar());
            //put it all together
            table.addColumn(starHeader, GRSCS.dataGridStyle().colWidthStar(), new StarColumn(fieldUpdater));
            return this;
        }

        /**
         * Creates demand status image column.
         *
         * @return created demands status image column
         */
        public Builder addColumnDemandStatus() {
            sortColumns.add(new SortPair(DemandField.DEMAND_STATUS));
            table.addColumn(MSGS.columnStatus(), GRSCS.dataGridStyle().colWidthIcon(),
                new DemandStatusColumn());
            return this;
        }

        /** Custom columns. **/
        /**
         * Creates urgency's column with urgency's header represented by urgency's image.
         *
         * @return urgencyColumn
         */
        public Builder addColumnUrgency() {
            sortColumns.add(new SortPair(DemandField.VALID_TO));
            //create urgency's header represented by urgency's image
            Header urgencyHeader = new Header<Date>(new UrgentImageCell()) {
                @Override
                public Date getValue() {
                    /* Returning null value tells UrgetUmageCell to use header image.
                     * Using it this way we can use same class:UrgentImageCell for
                     * providing urgency images as for hear as for urgency column items.
                     * Otherwise we must create new class image cell providing only header's image. */
                    return null;
                }
            };
            urgencyHeader.setHeaderStyleNames(GRSCS.dataGridStyle().cellStyleUrgency());
            //put it together
            table.addColumn(urgencyHeader, GRSCS.dataGridStyle().colWidthUrgency(), new UrgencyColumn());
            return this;
        }

        public UniversalAsyncGrid build() {
            table.setWidth("100%");
            //In order to have no scroll bar inside table, its height must be always set
            //exactly according to its row count. Therefore height is set in updateRowData in UniversalAsyncGrid
            table.setGridColumns(new SortDataHolder(defaultSortColumns, sortColumns));
            return table;
        }
    }
}