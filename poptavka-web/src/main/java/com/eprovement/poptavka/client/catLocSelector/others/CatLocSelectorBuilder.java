/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.catLocSelector.others;

/**
 *
 * @author Mato
 */
public final class CatLocSelectorBuilder {

    /**************************************************************************/
    /* Constants                                                              */
    /**************************************************************************/
    public static final int SELECTOR_TYPE_CATEGORIES = 1;
    public static final int SELECTOR_TYPE_LOCALITIES = 2;
    public static final int WIDGET_TYPE_MANAGER = 0;
    public static final int WIDGET_TYPE_TREE = 1;
    public static final int WIDGET_TYPE_BROWSER = 2;
    public static final int CHECKBOXES_DISABLED = 0;
    public static final int CHECKBOXES = 1;
    public static final int CHECKBOXES_ON_LEAF_ONLY = 2;
    public static final int COUNTS_DISABLED = 0;
    public static final int COUNTS_DEMANDS = 1;
    public static final int COUNTS_SUPPLIERS = 2;

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private int selectorType;
    private int widgetType;
    private int checkboxes;
    private int displayCountsOfWhat;
    private int selectionRestriction;

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    public void setSelectorType(int selectorType) {
        this.selectorType = selectorType;
    }

    public void setCheckboxes(int checkboxes) {
        this.checkboxes = checkboxes;
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public int getSelectorType() {
        return selectorType;
    }

    public int getWidgetType() {
        return widgetType;
    }

    public int getCheckboxes() {
        return checkboxes;
    }

    public int getDisplayCountsOfWhat() {
        return displayCountsOfWhat;
    }

    public int getSelectionRestriction() {
        return selectionRestriction;
    }

    /**************************************************************************/
    /* Builder                                                                */
    /**************************************************************************/
    public static class Builder {

        /**
         * Categories
         * Localities
         */
        private int selectorType = SELECTOR_TYPE_CATEGORIES;
        /**
         * Manager
         * Tree
         * Browser
         */
        private int widgetType = WIDGET_TYPE_MANAGER;
        /**
         * without checkboxes
         * with checkboxes
         * with checkboxes only on leafs
         */
        private int checkboxes = CHECKBOXES_DISABLED;
        /**
         * no counts displayed
         * demand's counts displayed
         * supplier's counts displayed
         */
        private int displayCountsOfWhat = COUNTS_DISABLED;
        /**
         * 0 - no restriction
         * x - allow x selected categories
         */
        private int selectionRestriction = 0;

        public Builder() {
        }

        public Builder initCategorySelector() {
            this.selectorType = SELECTOR_TYPE_CATEGORIES;
            return this;
        }

        public Builder initLocalitySelector() {
            this.selectorType = SELECTOR_TYPE_LOCALITIES;
            return this;
        }

        public Builder initSelectorManager() {
            this.widgetType = WIDGET_TYPE_MANAGER;
            return this;
        }

        public Builder initSelectorTreeBrowser() {
            this.widgetType = WIDGET_TYPE_TREE;
            return this;
        }

        public Builder initSelectorCellBrowser() {
            this.widgetType = WIDGET_TYPE_BROWSER;
            return this;
        }

        public Builder displayCountOfDemands() {
            this.displayCountsOfWhat = COUNTS_DEMANDS;
            return this;
        }

        public Builder displayCountOfSuppliers() {
            this.displayCountsOfWhat = COUNTS_SUPPLIERS;
            return this;
        }

        public Builder withCheckboxes() {
            this.checkboxes = CHECKBOXES;
            return this;
        }

        public Builder withCheckboxesOnLeafsOnly() {
            this.checkboxes = CHECKBOXES_ON_LEAF_ONLY;
            return this;
        }

        public Builder setSelectionRestriction(int selectionRestriction) {
            this.selectionRestriction = selectionRestriction;
            return this;
        }

        public CatLocSelectorBuilder build() {
            return new CatLocSelectorBuilder(this);
        }
    }

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    private CatLocSelectorBuilder(Builder builder) {

        this.selectorType = builder.selectorType;
        this.widgetType = builder.widgetType;
        this.checkboxes = builder.checkboxes;
        this.displayCountsOfWhat = builder.displayCountsOfWhat;
        this.selectionRestriction = builder.selectionRestriction;
    }
}
