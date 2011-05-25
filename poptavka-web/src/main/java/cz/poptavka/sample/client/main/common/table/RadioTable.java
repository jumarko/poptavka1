package cz.poptavka.sample.client.main.common.table;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

import cz.poptavka.sample.client.resources.StyleResource;

public class RadioTable extends HeaderTable {

    private static final Logger LOGGER = Logger.getLogger(RadioTable.class
        .getName());

    private static final int COLUMN_RADIO = 1;
    private static final int COLUMN_DESCRIPTION = 2;

    private int defaultChecked = -1;
    private int selectedRow = -1;
    private boolean popupEnabled = false;

    public RadioTable(ArrayList<String> titles, boolean clickable, int defaultChecked) {
        super(titles, clickable);
        this.constantsCount = 2;
        this.defaultChecked = defaultChecked;
        addSpecialColumn(COLUMN_RADIO, "");
    }

    @Override
    public void getClickedRow(ClickEvent event) {
        Cell clickedCell = getCellForEvent(event);
        if (clickedCell != null) {
            selectedRow = clickedCell.getRowIndex();
            Element body = (Element) getBodyElement().getChildNodes().getItem(selectedRow).getChild(COLUMN_RADIO);
            Element radio = (Element) body.getFirstChildElement();
            radio.setPropertyBoolean("checked", true);
        }
    }

    public void setData(ArrayList<ArrayList<String>> data) {
        LOGGER.fine("Set Data ->");

        for (int i = 0; i < data.size(); i++) {
            setRow(data.get(i), i);
        }
    }

    public void setRow(ArrayList<String> item, int index) {
        int rowIndex = getRowCount();
        LOGGER.fine("Insert row #" + rowIndex);
        int columnsToFill = item.size();

        for (int i = 0; i < columnsToFill; i++) {
            if (i == COLUMN_RADIO) {
                LOGGER.fine("Creating radioButton");
                Element radio = DOM.createInputRadio("selection");
                Element row = (Element) getBodyElement().getChildNodes().getItem(rowIndex);
                Element td = DOM.createTD();
                DOM.appendChild(row, td);
                td.appendChild(radio);
                if (index == this.defaultChecked) {
                    radio.setAttribute("checked", "true");
                }
            } else {
                LOGGER.fine("Filling string");
                setHTML(rowIndex, i, item.get(i));
            }
            getCellFormatter().addStyleName(rowIndex, COLUMN_ID, StyleResource.INSTANCE.table().hiddenField());
        }
    }

    public int getSelectedValue() {
        String content = getHTML(selectedRow, COLUMN_ID);
        return Integer.parseInt(content);
    }

    public void setPopupEnabled(boolean enabled) {
        this.popupEnabled = enabled;
    }

    public boolean getPopupEnabled() {
        return popupEnabled;
    }

}
