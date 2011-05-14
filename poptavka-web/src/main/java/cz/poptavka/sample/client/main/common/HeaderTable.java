package cz.poptavka.sample.client.main.common;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlexTable;

import cz.poptavka.sample.client.resources.StyleResource;

public abstract class HeaderTable<E> extends FlexTable {

    private static final Logger LOGGER = Logger.getLogger(HeaderTable.class
            .getName());

    protected static final int COLUMN_ID = 0;

    private boolean clickable = false;

    /**
     * Creates no-clickable table without cellspacing
     */
    public HeaderTable() {
        super();
        this.setCellSpacing(0);
        LOGGER.fine("TABLE -- no-arg constructor");
    }

    /**
     * Can create clickable or no-clickable header.
     *
     * @param titles - list of table caption
     * @param boolean - to set clickability of header
     */
    public HeaderTable(ArrayList<String> titles, boolean clickable) {
        this();
        LOGGER.fine("TABLE -- normal constructor");
        initTableHead(titles);
        this.clickable = clickable;
    }

    /**
     * Can create clickable or no-clickable header.
     *
     * @param titles - list of table caption
     * @param boolean - to set clickability of header
     */
    public HeaderTable(ArrayList<String> titles, boolean clickable, String className) {
        this();
        LOGGER.fine("TABLE -- normal constructor");
        initTableHead(titles);
        this.clickable = clickable;
        setStylePrimaryName(className);
    }

    private void initTableHead(ArrayList<String> titles) {
        Element thead = DOM.createTHead();
        DOM.insertChild(this.getElement(), thead, 0);
        Element tr = DOM.createTR();
        DOM.appendChild(thead, tr);
        /** first column is ID column, invisible by default **/
        Element idColumn = DOM.createTH();
        idColumn.setClassName(StyleResource.INSTANCE.table().hiddenField());
        DOM.appendChild(thead, idColumn);
        /** visible data **/
        for (int column = 0; column < titles.size(); column++) {
            Element th = DOM.createTD();
            DOM.setInnerText(th, titles.get(column));
            DOM.appendChild(tr, th);
        }
    }

    public abstract void setData(ArrayList<E> data);

    public abstract void getClickedRow(ClickEvent event);
}
