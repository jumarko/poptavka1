/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.catLocSelector.others;

/**
 * CatLocSelectionBuilder for making creation of CellTree, CellBrowser and Manager easier.
 * @author Martin Slavkovsky
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
    private int instanceId;
    private int selectorType;
    private int widgetType;
    private int checkboxes;
    private int displayCountsOfWhat;
    private int selectionRestriction;

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public int getInstanceId() {
        return instanceId;
    }

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

        private int instanceBaseId;
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

        /**
         * Creates CatLocSelectorBuilder. Privides some unique instanceBaseId.
         * The best practise would be to provide widgetId to bind instances to widget which uses them.
         * InstaceBaseId is then used to creates unique instanceId from instanceBaseId(widgetId) and selectorType.
         *
         * @param instanceBaseId unique id which will be used to build instanceId
         */
        public Builder(int instanceBaseId) {
            this.instanceBaseId = instanceBaseId;
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

        public Builder setSelectorType(int selectorType) {
            this.selectorType = selectorType;
            return this;
        }

        public Builder setCheckboxes(int checkboxes) {
            this.checkboxes = checkboxes;
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

        //create unique instanceId from widgetId and selectorType
        this.instanceId = buildInstanceId(builder.instanceBaseId, builder.selectorType);
        this.selectorType = builder.selectorType;
        this.widgetType = builder.widgetType;
        this.checkboxes = builder.checkboxes;
        this.displayCountsOfWhat = builder.displayCountsOfWhat;
        this.selectionRestriction = builder.selectionRestriction;
    }

    /**
     * Creates instanceId from instanceBaseId and selectorType.
     * <br/>
     * Rules:<br/>
     * instance is build: instanceBaseId * 100 +/i selectorType.<br/>
     * +/- depends on selectorType ( + for categories, - for localities )<br/>
     *
     * @param instanceBaseId
     * @param selectorType
     * @return unique instanceId
     */
    private int buildInstanceId(int instanceBaseId, int selectorType) {
        if (selectorType == SELECTOR_TYPE_CATEGORIES) {
            return selectorType * 100 + instanceBaseId;
        } else {
            return -(selectorType * 100 + instanceBaseId);
        }
    }
}
