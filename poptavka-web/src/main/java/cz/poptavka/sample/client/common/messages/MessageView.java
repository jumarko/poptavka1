package cz.poptavka.sample.client.common.messages;


import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import cz.poptavka.sample.domain.mail.Message;

public class MessageView extends Composite
        implements MessagePresenter.MessageViewInterface {

    private static final Logger LOGGER = Logger.getLogger(MessageView.class.getName());

    private static MessagesUiBinder uiBinder = GWT
            .create(MessagesUiBinder.class);

    interface MessagesUiBinder extends UiBinder<Widget, MessageView> {
    }

    @UiField
    Image logo;
    @UiField
    Label from;
    @UiField
    Label shortText;
    @UiField
    Label date;

    @UiField
    Button buttonReply;
    @UiField
    Button buttonReplyToAll;
    @UiField
    Button buttonForward;

    @UiField
    Button buttonSend;
    @UiField
    Button buttonDiscard;

    @UiField
    MyHorizontalPanel panelHeader;
    @UiField
    DisclosurePanel panelBody;
    @UiField
    HorizontalPanel panelFooter1;
    @UiField
    HorizontalPanel panelFooter2;
    @UiField
    DisclosurePanel panelDetail;
    @UiField
    VerticalPanel messageText;

    //EXISTING MESSAGES
    public MessageView(Message message) {
        LOGGER.info("Creating MessageView: " + message.getBody());
        initWidget(uiBinder.createAndBindUi(this));
        this.from.setText(message.getSender().getEmail());
        this.date.setText(message.getSent().toString());
        messageText.add(this.getArea(message.getBody()));
        if (message.getBody().length() > 50) {
            this.shortText.setText(message.getBody().substring(0, 50) + "...");
        } else {
            this.shortText.setText(message.getBody());
        }
    }

    // NEW MESSAGE
    public MessageView() {
        initWidget(uiBinder.createAndBindUi(this));
        messageText.add(this.getAreaWithToolbox());
    }

    public Grid getArea(String text) {

        RichTextArea area = new RichTextArea();
        area.ensureDebugId("cwRichText-area");
        area.setSize("100%", "14em");
        area.setText(text);

        Grid grid = new Grid(1, 1);
        grid.setStyleName("cw-RichText");
        grid.setWidget(0, 0, area);
        return grid;
    }

    public Grid getAreaWithToolbox() {
        // Create the text area and toolbar
        RichTextArea area = new RichTextArea();
        area.ensureDebugId("cwRichText-area");
        area.setSize("100%", "14em");
        RichTextToolbar toolbar = new RichTextToolbar(area);
        toolbar.ensureDebugId("cwRichText-toolbar");
        toolbar.setWidth("100%");

        // Add the components to a panel
        Grid grid = new Grid(2, 1);
        grid.setStyleName("cw-RichText");
        grid.setWidget(0, 0, toolbar);
        grid.setWidget(1, 0, area);
        return grid;
    }

    @Override
    public DisclosurePanel getPanelBody() {
        return panelBody;
    }

    /**
     * Displays message body. Just GUI. Not working if through eventBus and presenter.
     * @param event
     */
    @UiHandler("panelHeader")
    public void onClickHeaderPanel(ClickEvent click) {
        if (panelBody.isOpen()) {
            panelBody.setOpen(false);
            panelFooter2.setVisible(false);
            panelDetail.setVisible(false);
        } else {
            panelBody.setOpen(true);
            panelFooter2.setVisible(true);
            panelDetail.setVisible(true);
        }
    }

    @Override
    public HasClickHandlers getReplyMessageBtn() {
        return buttonReply;
    }
    @Override
    public HasClickHandlers getReplyToAllMessageBtn() {
        return buttonReplyToAll;
    }
    @Override
    public HasClickHandlers getForwardMessageBtn() {
        return buttonForward;
    }

    @Override
    public HasClickHandlers getSendMessageBtn() {
        return buttonSend;
    }
    @Override
    public HasClickHandlers getDeleteMessageBtn() {
        return buttonDiscard;
    }
    /**
     * Returns this view instance.
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}
