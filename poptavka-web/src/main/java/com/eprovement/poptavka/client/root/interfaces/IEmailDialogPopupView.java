/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.mvp4g.client.view.LazyView;

/**
 *
 * @author ivlcek
 */
public interface IEmailDialogPopupView extends LazyView {

    public interface IEmailDialogPopupPresenter {
    }

    HasClickHandlers getSendButton();
    TextArea getTextArea();
    ListBox getSubjectListBox();
    TextBox getReEmailTextBox();
    TextBox getEmailTextBox();
    SimplePanel getPanel();
}
