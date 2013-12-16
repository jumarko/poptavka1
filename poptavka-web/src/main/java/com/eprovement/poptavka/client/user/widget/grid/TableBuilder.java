/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.grid;

import com.eprovement.poptavka.shared.search.SortPair;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for defining columns to be created in UniversalAsynchGrid.
 * Define: Columns, Selection model, Default sort pair
 *
 * @author Mato
 */
public final class TableBuilder {

    /**************************************************************************/
    /* Selection model enum                                                   */
    /**************************************************************************/
    public enum SelectionModelEnum {

        SINGLE,
        MULTI
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private boolean addColumnCheckBox;
    private boolean addColumnStar;
    private boolean addColumnDemandTitle;
    private boolean addColumnDemandStatus;
    private boolean addColumnDisplayName;
    private boolean addColumnClientRating;
    private boolean addColumnSupplierRating;
    private boolean addColumnPrice;
    private boolean addColumnEndDate;
    private boolean addColumnUrgency;
    private boolean addColumnMesasgeBody;
    private boolean addColumnMesasgeSent;
    private boolean addColumnOfferReceived;
    private boolean addColumnFinnishDate;
    private List<SortPair> defaultSort;
    private SelectionModelEnum selectionModel;

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public boolean isColumnCheckBox() {
        return addColumnCheckBox;
    }

    public boolean isColumnStar() {
        return addColumnStar;
    }

    public boolean isColumnDemandTitle() {
        return addColumnDemandTitle;
    }

    public boolean isColumnDemandStatus() {
        return addColumnDemandStatus;
    }

    public boolean isColumnDisplayName() {
        return addColumnDisplayName;
    }

    public boolean isColumnClientRating() {
        return addColumnClientRating;
    }

    public boolean isColumnSupplierRating() {
        return addColumnSupplierRating;
    }

    public boolean isColumnPrice() {
        return addColumnPrice;
    }

    public boolean isColumnEndDate() {
        return addColumnEndDate;
    }

    public boolean isColumnUrgency() {
        return addColumnUrgency;
    }

    public boolean isColumnMesasgeBody() {
        return addColumnMesasgeBody;
    }

    public boolean isColumnMesasgeSent() {
        return addColumnMesasgeSent;
    }

    public boolean isColumnOfferReceived() {
        return addColumnOfferReceived;
    }

    public boolean isColumnFinnishDate() {
        return addColumnFinnishDate;
    }

    public List<SortPair> getDefaultSort() {
        return defaultSort;
    }

    public SelectionModelEnum getSelectionModel() {
        return selectionModel;
    }

    /**************************************************************************/
    /* Builder                                                                */
    /**************************************************************************/
    public static class Builder {

        private boolean addColumnCheckBox;
        private boolean addColumnStar;
        private boolean addColumnDemandTitle;
        private boolean addColumnDemandStatus;
        private boolean addColumnDisplayName;
        private boolean addColumnClientRating;
        private boolean addColumnSupplierRating;
        private boolean addColumnPrice;
        private boolean addColumnEndDate;
        private boolean addColumnUrgency;
        private boolean addColumnMesasgeBody;
        private boolean addColumnMesasgeSent;
        private boolean addColumnOfferReceived;
        private boolean addColumnFinnishDate;
        private List<SortPair> defaultSort = new ArrayList<SortPair>();
        private SelectionModelEnum selectionModel = SelectionModelEnum.SINGLE;

        public Builder() {
        }

        public Builder addColumnCheckBox() {
            this.addColumnCheckBox = true;
            return this;
        }

        public Builder addColumnStar() {
            this.addColumnStar = true;
            return this;
        }

        public Builder addColumnDemandTitle() {
            this.addColumnDemandTitle = true;
            return this;
        }

        public Builder addColumnDemandStatus() {
            this.addColumnDemandStatus = true;
            return this;
        }

        public Builder addColumnDisplayName() {
            this.addColumnDisplayName = true;
            return this;
        }

        public Builder addColumnClientRating() {
            this.addColumnClientRating = true;
            return this;
        }

        public Builder addColumnSupplierRating() {
            this.addColumnSupplierRating = true;
            return this;
        }

        public Builder addColumnPrice() {
            this.addColumnPrice = true;
            return this;
        }

        public Builder addColumnEndDate() {
            this.addColumnEndDate = true;
            return this;
        }

        public Builder addColumnUrgency() {
            this.addColumnUrgency = true;
            return this;
        }

        public Builder addColumnMesasgeBody() {
            this.addColumnMesasgeBody = true;
            return this;
        }

        public Builder addColumnMesasgeSent() {
            this.addColumnMesasgeSent = true;
            return this;
        }

        public Builder addColumnOfferReceived() {
            this.addColumnOfferReceived = true;
            return this;
        }

        public Builder addColumnFinnishDate() {
            this.addColumnFinnishDate = true;
            return this;
        }

        public Builder setDefaultSort(List<SortPair> defaultSort) {
            this.defaultSort = defaultSort;
            return this;
        }

        public Builder setSelectionModel(SelectionModelEnum selectionModel) {
            this.selectionModel = selectionModel;
            return this;
        }

        public TableBuilder build() {
            return new TableBuilder(this);
        }
    }

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    private TableBuilder(Builder builder) {

        this.addColumnCheckBox = builder.addColumnCheckBox;
        this.addColumnStar = builder.addColumnStar;
        this.addColumnDemandTitle = builder.addColumnDemandTitle;
        this.addColumnDemandStatus = builder.addColumnDemandStatus;
        this.addColumnDisplayName = builder.addColumnDisplayName;
        this.addColumnClientRating = builder.addColumnClientRating;
        this.addColumnSupplierRating = builder.addColumnSupplierRating;
        this.addColumnPrice = builder.addColumnPrice;
        this.addColumnEndDate = builder.addColumnEndDate;
        this.addColumnUrgency = builder.addColumnUrgency;
        this.addColumnMesasgeBody = builder.addColumnMesasgeBody;
        this.addColumnMesasgeSent = builder.addColumnMesasgeSent;
        this.addColumnOfferReceived = builder.addColumnOfferReceived;
        this.addColumnFinnishDate = builder.addColumnFinnishDate;
        this.defaultSort = builder.defaultSort;
        this.selectionModel = builder.selectionModel;
    }
}
