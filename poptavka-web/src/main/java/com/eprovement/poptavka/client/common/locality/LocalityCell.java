/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.locality;

import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 *
 * @author Martin
 */
public class LocalityCell extends AbstractCell<LocalityDetail> {

    //DisplayCountOfWhat
    public static final int DISPLAY_COUNT_OF_DEMANDS = 0;
    public static final int DISPLAY_COUNT_OF_SUPPLIERS = 1;
    public static final int DISPLAY_COUNT_DISABLED = -1;
    //Display string Constants
    public static final String LEFT_BRACE = "(";
    public static final String RIGHT_BRACE = ")";
    public static final String SPACE = " ";
    //
    private int displayCountOfWhat = -1;

    /**
     * The html of the image used for contacts.
     */
    public LocalityCell(int displayCountOfWhat) {
        this.displayCountOfWhat = displayCountOfWhat;
    }

    @Override
    public void render(Cell.Context context, LocalityDetail value, SafeHtmlBuilder sb) {
        if (value != null) {
            StringBuilder text = new StringBuilder();

            text.append(value.getName().replaceAll("-a-", " a ").replaceAll("-", ", "));
            switch (displayCountOfWhat) {
                case DISPLAY_COUNT_OF_DEMANDS:
                    text.append(SPACE);
                    text.append(LEFT_BRACE);
                    text.append(value.getDemandsCount());
                    text.append(RIGHT_BRACE);
                    break;
                case DISPLAY_COUNT_OF_SUPPLIERS:
                    text.append(SPACE);
                    text.append(LEFT_BRACE);
                    text.append(value.getSuppliersCount());
                    text.append(RIGHT_BRACE);
                    break;
                default:
                    break;
            }

            sb.appendEscaped(text.toString());
        }
    }
}