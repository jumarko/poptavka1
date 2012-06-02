package com.eprovement.poptavka.client.main.common.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.resources.StyleResource;

/**
 * Base class structure for dynamic table widgets. Every child class in case of increasing constants count, HAVE TO
 * increase attribute constantsCount
 *
 * @author Beho
 *
 */
public abstract class HeaderTable extends FlexTable {

    private static final Logger LOGGER = Logger.getLogger(HeaderTable.class.getName());

    /** Constants. **/
    protected static final int COLUMN_ID = 0;
    protected int constantsCount = 1;

    private ArrayList<String> columnTitles;
    private HashSet<Integer> hiddenColumns = new HashSet<Integer>();
    private boolean clickable = false;

    private StyleResource style = GWT.create(StyleResource.class);


    /**
     * Can create clickable or no-clickable header.
     *
     * @param titles - list of table caption
     * @param clickable - to set clickability of header
     */
    public HeaderTable(ArrayList<String> titles, boolean clickable) {
        super();
        style.INSTANCE.table().ensureInjected();
        this.setCellSpacing(0);
        this.clickable = clickable;
        columnTitles = titles;
        initTableHead(titles);
    }

    /**
     * Can create clickable or no-clickable header.
     *
     * @param titles - list of table caption
     * @param clickable - to set clickability of header
     * @param className
     */
    public HeaderTable(ArrayList<String> titles, boolean clickable, String className) {
        this(titles, clickable);
        initTableHead(titles);
        this.clickable = clickable;
        setStylePrimaryName(className);
    }

    /** init table header, according to clickable it will be clickable or not. **/
    private void initTableHead(ArrayList<String> titles) {
        Element thead = DOM.createTHead();
        DOM.insertChild(this.getElement(), thead, 0);
        Element tr = DOM.createTR();

        /** first column is ID column, invisible by default **/
        Element idColumn = DOM.createTH();
        idColumn.addClassName(style.table().hiddenField());
        idColumn.setInnerText("ID");
        getColumnFormatter().setStylePrimaryName(COLUMN_ID, StyleResource.INSTANCE.table().hiddenField());
        DOM.appendChild(tr, idColumn);
        DOM.appendChild(thead, tr);

        /** visible data **/
        Element last = null;
        for (int column = 0; column < titles.size(); column++) {
            Element th = DOM.createTH();
            DOM.setInnerText(th, titles.get(column));
            DOM.appendChild(tr, th);
            last = th;
        }

        /** clickability **/
        if (clickable) {
            DOM.sinkEvents(tr, Event.ONCONTEXTMENU | Event.ONCLICK);
            DOM.setEventListener(tr, new HeaderClickListener());
        }
    }

    /** Header RIGHT-CLICK popup menu. **/
    private Widget initColumnController() {
        MenuBar items = new MenuBar(true);
        String visibleColumn = style.table().columnVisible();
        String hiddenColumn = style.table().columnHidden();
        for (int i = 0; i < columnTitles.size(); i++) {
            MenuItem item = new MenuItem(columnTitles.get(i), new HeaderRightCommand(i));
            if (hiddenColumns.contains(new Integer(i + constantsCount))) {
                item.setStylePrimaryName(hiddenColumn);
            } else {
                item.setStylePrimaryName(visibleColumn);
            }
            items.addItem(item);
        }
        items.getElement().setAttribute("id", "menu-bar");
        return items;
    }

    private void toggleColumnVisibility(int index) {
        NodeList<com.google.gwt.dom.client.Element> list = this.getElement().getElementsByTagName("thead");
        Element castedElement = (Element) list.getItem(0).getFirstChild().getChild(index);
        String applyStyle = style.table().hiddenField();

        if (hiddenColumns.contains(index)) {
            castedElement.removeClassName(applyStyle);
            for (int i = 0; i < getRowCount(); i++) {
                getCellFormatter().removeStyleName(i, index, applyStyle);
            }
            hiddenColumns.remove(index);
        } else {
            castedElement.addClassName(applyStyle);
            for (int i = 0; i < getRowCount(); i++) {
                LOGGER.fine("STATUS: "  + "row: " + i + ", column: " + index + ", style: " + applyStyle);
                getCellFormatter().addStyleName(i, index, applyStyle);
            }
            hiddenColumns.add(index);
        }
        DOM.getElementById("menu-bar").removeFromParent();
    }

    protected void addSpecialColumn(int indexToInsert, String title) {
        LOGGER.fine("Inserting column");
        com.google.gwt.dom.client.Element thead = this.getElement().getElementsByTagName("thead").getItem(0);
        Element tr = (Element) thead.getFirstChildElement();
        Element newChild = DOM.createTH();
        DOM.insertChild(tr, newChild, indexToInsert);
    }

    public abstract void getClickedRow(ClickEvent event);

    private class HeaderClickListener implements EventListener {
        @Override
        public void onBrowserEvent(Event event) {
            LOGGER.fine("ON_BROWSER_EVENT");
            event.stopPropagation();
            event.preventDefault();
            if (event.getTypeInt() == Event.ONCONTEXTMENU) {
                LOGGER.fine("RIGHT-CLICKED");
                PopupPanel menuPopup = new PopupPanel(true);
                menuPopup.setStylePrimaryName("no_style");
                menuPopup.getElement().getStyle().setPadding(5, Unit.PX);
                menuPopup.add(initColumnController());
                menuPopup.setPopupPosition(event.getClientX(), event.getClientY());
                menuPopup.show();
            }
            if (event.getTypeInt() == Event.ONCLICK) {
                // TODO implement sorting
                // Sort popup
            }
        }
    }

    private class HeaderRightCommand implements Command {
        private int handlingColumn;

        public HeaderRightCommand(int handlingColumn) {
            this.handlingColumn = handlingColumn + constantsCount;
        }

        @Override
        public void execute() {
            toggleColumnVisibility(handlingColumn);
        }
    }

}
