/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.ui;

import com.eprovement.poptavka.client.common.session.Storage;
import com.github.gwtbootstrap.client.ui.base.HasPlaceholder;
import com.github.gwtbootstrap.client.ui.base.PlaceholderHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Creates custom date box that extends classic gwt DateBox but has
 * bootstrap features like placeholder and date format.
 *
 * @author Martin Slavkovsky
 */
public class WSDateBox extends DateBox implements HasPlaceholder {

    private PlaceholderHelper placeholderHelper = GWT.create(PlaceholderHelper.class);

    public WSDateBox() {
        super.setFormat(new DefaultFormat(Storage.get().getDateTimeFormat()));
    }

    @Override
    public void setPlaceholder(String placeholder) {
        placeholderHelper.setPlaceholer(getElement(), placeholder);
    }

    @Override
    public String getPlaceholder() {
        return placeholderHelper.getPlaceholder(getElement());
    }
}
