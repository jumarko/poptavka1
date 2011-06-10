package cz.poptavka.sample.client.home.demands.demand;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;

public class DemandView extends Composite implements
        DemandPresenter.DemandViewInterface {

    private static DemandViewUiBinder uiBinder = GWT.create(DemandViewUiBinder.class);

    interface DemandViewUiBinder extends UiBinder<Widget, DemandView> {
    }

    public DemandView() {
        initWidget(uiBinder.createAndBindUi(this));
        linkAttachment.setVisible(false);

    }
    @UiField
    Hyperlink linkAttachment, linkLogin, linkRegisterClient, linkRegisterSupplier;

    @UiField
    FlexTable infoTable;
//    @UiField
//    Label label1;
//    @UiField
//    Label label2;
    @UiField
    TextArea textArea;

    LocalizableMessages bundle = (LocalizableMessages) GWT.create(LocalizableMessages.class);

    public DemandView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
        linkAttachment.setVisible(false);
    }

    @Override
    public void setAttachmentToken(String token) {
        linkAttachment.setTargetHistoryToken(token);
    }

    @Override
    public void setLoginToken(String token) {
        linkLogin.setTargetHistoryToken(token);
    }

    @Override
    public void setRegisterToken(String token) {
        linkRegisterClient.setTargetHistoryToken(token);
    }

    public void setDemand(FullDemandDetail demand) {
        infoTable.clear();
        textArea.setText("");
        textArea.setWidth("400px");
        textArea.setHeight("250px");
        linkAttachment.setVisible(false);

        int row = 0;

        if (demand.getDescription() != null) {
            textArea.setText(demand.getDescription());
        }

        if (demand.getPrice() != null) {
            infoTable.setWidget(row, 0, new Label(bundle.title()));
            infoTable.setWidget(row++, 1, new Label(demand.getTitle().toString()));
        }

        if (demand.getPrice() != null) {
            infoTable.setWidget(row, 0, new Label(bundle.price()));
            infoTable.setWidget(row++, 1, new Label(demand.getPrice().toPlainString()));
        }

        if (demand.getEndDate() != null) {
            infoTable.setWidget(row, 0, new Label(bundle.endDate()));
            infoTable.setWidget(row++, 1, new Label(demand.getEndDate().toString()));
        }

        if (demand.getValidToDate() != null) {
            infoTable.setWidget(row, 0, new Label(bundle.validTo()));
            infoTable.setWidget(row++, 1, new Label(demand.getValidToDate().toString()));
        }

        if (demand.getDemandType() != null) {
            infoTable.setWidget(row, 0, new Label(bundle.type()));
            infoTable.setWidget(row++, 1, new Label(demand.getDemandType()));
        }

        if (demand.getCategories() != null) {
            infoTable.setWidget(row, 0, new Label(bundle.category()));
            infoTable.setWidget(row++, 1, new Label(demand.getCategories().toString()
                    .substring(1, demand.getCategories().toString().length() - 1)));
        }

        if (demand.getLocalities() != null) {
            infoTable.setWidget(row, 0, new Label(bundle.locality()));
            infoTable.setWidget(row++, 1, new Label(demand.getLocalities().toString()
                    .substring(1, demand.getLocalities().toString().length() - 1)));
        }

        infoTable.setWidget(row, 0, new Label(bundle.offers()));
        infoTable.setWidget(row++, 1, new Label(Integer.toString(demand.getMaxOffers())));

        if (demand.getPrice() != null) {
            infoTable.setWidget(row++, 0, new Label(bundle.attachment()));
            infoTable.setWidget(row, 1, new Label(demand.getTitle().toString()));
            linkAttachment.setVisible(true);
        }
    }
}