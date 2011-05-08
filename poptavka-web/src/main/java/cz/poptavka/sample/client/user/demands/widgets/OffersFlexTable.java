package cz.poptavka.sample.client.user.demands.widgets;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;

public class OffersFlexTable extends FlexTable {

    private static final Logger LOGGER = Logger.getLogger(OffersFlexTable.class.getName());

    private static final int COULMN_SIGN = 0;
    private static final int COLUMN_DEMAND = 1;
    private static final int COLUMN_PRICE = 2;
    private static final int COLUMN_RATING = 3;
    private static final int COLUMN_DATE = 4;
    private static final int COLUMN_ID = 5;

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    private ArrayList<DemandDetail> demands = new ArrayList<DemandDetail>();
    private ArrayList<Boolean> demandsPos = new ArrayList<Boolean>();

    public OffersFlexTable() {
        this.setHTML(0, COULMN_SIGN, "&nbsp;");
        this.setHTML(0, COLUMN_DEMAND, "<strong>Detail</strong>");
        this.setHTML(0, COLUMN_PRICE, "<strong>Cena</strong>");
        this.setWidget(0, COLUMN_RATING, new Image(StyleResource.INSTANCE.images().star()));
        this.setHTML(0, COLUMN_DATE, "<strong>" + MSGS.endDate() + "</strong>");
        LOGGER.fine("OFFERSFLEXTABLE " + "Constructor");
    }

    public void setData(ArrayList<DemandDetail> demands) {
        if (this.demands.hashCode() != demands.hashCode()) {
            this.demands = demands;

            for (DemandDetail demand : this.demands) {
                setDemandRow(demand);
                demandsPos.add(false);
            }
        }
    }

    public void setDemandRow(DemandDetail demand) {
        int nextRow = this.getRowCount();
        this.setHTML(nextRow, COULMN_SIGN, "&nbsp;+&nbsp;");
        this.setHTML(nextRow, COLUMN_DEMAND, demand.getTitle());
        this.setHTML(nextRow, COLUMN_PRICE,
                (demand.getEndDate() == null ? MSGS.emptyField() : demand.getEndDate().toString()));
        this.setHTML(nextRow, COLUMN_RATING, demand.getMinRating() + "%");
        this.setHTML(nextRow, COLUMN_DATE,
                (demand.getEndDate() != null ? demand.getEndDate().toString() : MSGS.emptyField()));
        this.setHTML(nextRow, COLUMN_ID, demand.getId() + "");
        this.getCellFormatter().addStyleName(nextRow, COLUMN_ID, StyleResource.INSTANCE.cssBase().closedWidget());
    }

    public int[] getClickedRow(ClickEvent event) {
        Cell cell = this.getCellForEvent(event);
        Element elem = cell.getElement().getParentElement();
        int clicked = cell.getRowIndex();
        int demandId = -1;
        int isMessage = 0;
        if (clicked > 0) {
            String tempString = elem.getChild(elem.getChildCount() - 1).getChildNodes().getItem(0).getNodeValue();
            if (tempString.contains("o:")) {
                isMessage = 1;
            } else {
                demandId =
                    Integer.parseInt(elem.getChild(elem.getChildCount() - 1).getChildNodes().getItem(0).getNodeValue());
            }
        }
        return new int[] {clicked, demandId, isMessage};
    }

    public void displayOffers(int insertAfter, ArrayList<OfferDetail> offerList) {
        if (demandsPos.get(insertAfter - 1)) {
            hideOffers(insertAfter, offerList.size());

        } else {
            if (offerList.size() == 0) {
                return;
            }
            demandsPos.remove(insertAfter - 1);
            demandsPos.add(insertAfter - 1, true);
            ++insertAfter;
            LOGGER.fine("row to insert" + insertAfter);
            this.insertRow(insertAfter);
            VerticalPanel master = new VerticalPanel();
            setWidget(insertAfter, COLUMN_DEMAND, master);
            for (OfferDetail offer : offerList) {
                OfferDetailView detail = new OfferDetailView();
                detail.message.setText("offer message");
                //detail.message.setText(offer.getLatestMessage());
                detail.company.setText(offer.getSupplierName());
                master.add(detail);
                this.setHTML(insertAfter, COLUMN_PRICE,
                        (offer.getPrice() == null ? MSGS.emptyField() :  offer.getPrice() + " Kc"));
                this.setHTML(insertAfter, COLUMN_DATE,
                        (offer.getFinishDate() == null ? MSGS.emptyField() : offer.getFinishDate().toString()));
                //this.setHTML(insertAfter, COLUMN_ID, "" + offer.getMessageId());
                this.setHTML(insertAfter, COLUMN_ID, "o:" + 0);
                this.getCellFormatter().addStyleName(insertAfter,
                        COLUMN_ID, StyleResource.INSTANCE.cssBase().closedWidget());
            }
        }
    }

    private void hideOffers(int clickedDemand, int rowCountToRemove) {
//        for (int i = 0; i < rowCountToRemove; i++) {
        LOGGER.fine("row to remove " + clickedDemand);
        removeRow(clickedDemand + 1);
//        }
        demandsPos.remove(clickedDemand - 1);
        demandsPos.add(clickedDemand - 1, false);
    }



}
