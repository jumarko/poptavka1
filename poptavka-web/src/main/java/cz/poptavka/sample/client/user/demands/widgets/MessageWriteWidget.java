package cz.poptavka.sample.client.user.demands.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class MessageWriteWidget extends Composite {

    private static MessageWriteWidgetUiBinder uiBinder = GWT
            .create(MessageWriteWidgetUiBinder.class);

    interface MessageWriteWidgetUiBinder extends UiBinder<Widget, MessageWriteWidget> {
    }

    interface SelectionStyle extends CssResource {
        String panel();

        @ClassName("header-area")
        String headerArea();

        String content();

        @ClassName("header-area-clicked")
        String headerAreaClicked();
    }

    @UiField SelectionStyle uiBinderStyle;

    @UiField Button toggleBtn;
    @UiField Button replyBtn;
    @UiField LongBox priceBox;
    @UiField DateBox dateBox;

    private boolean contentOpen = false;

    public MessageWriteWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("toggleBtn")
    public void doClick(ClickEvent event) {
        if (contentOpen) {
            Document.get().getElementById("replyWidget").getStyle().setDisplay(Display.NONE);
            toggleBtn.getElement().getParentElement().setClassName(uiBinderStyle.headerArea());
            contentOpen = !contentOpen;
        } else {
            Document.get().getElementById("replyWidget").getStyle().setDisplay(Display.BLOCK);
            toggleBtn.getElement().getParentElement().setClassName(uiBinderStyle.headerAreaClicked());
            contentOpen = !contentOpen;
        }
    }

    public Button getReplyButton() {
        return replyBtn;
    }

    // TODO implement when message
    public Object getContent() {
        return null;
    }
}
