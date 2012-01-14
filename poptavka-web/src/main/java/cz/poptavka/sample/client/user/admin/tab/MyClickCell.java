/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.dom.client.Element;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.EditTextCell;


/**
 *
 * @author Mato
 */
class MyClickCell extends EditTextCell {

    @Override
    public boolean isEditing(Context context, Element parent, String value) {
        return false;
    }
}
