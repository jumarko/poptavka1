package cz.poptavka.sample.client.resources;

import com.google.gwt.resources.client.CssResource;

public interface TableStyle extends CssResource {

    @ClassName("click-table")
    String clickTable();

    @ClassName("header")
    String header();

    @ClassName("even")
    String evenRow();

    @ClassName("selected-row")
    String selectedRow();

    @ClassName("selected-offer")
    String selectedOffer();

    @ClassName("hidden-field")
    String hiddenField();

    @ClassName("column-visible")
    String columnVisible();

    @ClassName("column-hidden")
    String columnHidden();

    @ClassName("bold")
    String bold();

}
