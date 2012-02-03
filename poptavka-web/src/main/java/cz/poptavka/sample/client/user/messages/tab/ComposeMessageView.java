package cz.poptavka.sample.client.user.messages.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


import com.mvp4g.client.view.ReverseViewInterface;
import cz.poptavka.sample.client.user.messages.tab.ComposeMessagePresenter.IComposeMessage;
import cz.poptavka.sample.shared.domain.message.MessageDetail;

/**
 * IMPORTANT NOTE: This view is ReverseView. Because of eventBus calls from dataGrid table and these event calls are
 * defined in view, not in presenter.
 *
 * @author beho
 *
 */
public class ComposeMessageView extends Composite
        implements ReverseViewInterface<ComposeMessagePresenter>, IComposeMessage {
//extends ReverseCompositeView<IComposeMessagePresenter> implements IComposeMessage {

    private static ComposeMessageViewUiBinder uiBinder = GWT.create(ComposeMessageViewUiBinder.class);

    interface ComposeMessageViewUiBinder extends UiBinder<Widget, ComposeMessageView> {
    }
    //attrribute preventing repeated loading of demand detail, when clicked on the same demand
    private long lastSelectedMail = -1;
    //table handling buttons
    @UiField
    Button sendBtn, discardBtn;
    @UiField
    TextBox recipient, subject;
    @UiField
    TextArea body;
    @UiField
    HTMLPanel wrapper;
    //presenter
    private ComposeMessagePresenter presenter;
    //detailWrapperPanel

    @Override
    public ComposeMessagePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(ComposeMessagePresenter presenter) {
        this.presenter = presenter;
    }

    public MessageDetail getMessage() {
        MessageDetail detail = new MessageDetail();
        detail.setSenderId(Long.parseLong(recipient.getValue()));
        detail.setSubject(subject.getText());
        detail.setBody(body.getText());
        return detail;
    }

    @Override
    public TextBox getRecipientTextBox() {
        return recipient;
    }

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public Button getSendBtn() {
        return sendBtn;
    }

    @Override
    public Button getDiscardBtn() {
        return discardBtn;
    }

    @Override
    public HTMLPanel getWrapperPanel() {
        return wrapper;
    }
}
