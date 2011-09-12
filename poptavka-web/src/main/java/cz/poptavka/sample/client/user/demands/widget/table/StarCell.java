package cz.poptavka.sample.client.user.demands.widget.table;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.shared.domain.message.TableDisplay;

public class StarCell extends AbstractCell<Object> {

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context,
            Object value, SafeHtmlBuilder sb) {
        TableDisplay obj = (TableDisplay) value;
        sb.appendHtmlConstant("<table>");
        sb.appendHtmlConstant("<tr><td>");

        if (obj.isStarred()) {
            sb.appendHtmlConstant(AbstractImagePrototype.create(Storage.RSCS.images().starGold()).getHTML());
        } else {
            sb.appendHtmlConstant(AbstractImagePrototype.create(Storage.RSCS.images().starSilver()).getHTML());
        }
        sb.appendHtmlConstant("</td></tr></table>");
    }

}
