package cz.poptavka.sample.client.user.demands.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;

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
    private static final int COLUMN_TYPE = 6;

    public static final int RESULT_ACTION = 0;
    public static final int ACTION_DEMAND = 1;
    public static final int ACTION_TOGGLE = 2;
    public static final int ACTION_OFFER = 3;
    public static final int ACTION_SORT = 4;

    public static final int RESULT_TYPE = 10;
    public static final int TYPE_DEMAND = 11;
    public static final int TYPE_OFFER = 12;

    public static final int RESULT_ID = 20;


    private int selectedId = 0;
    private int selectedRow = -1;
    private int selectedOffer = -1;

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);


    private ArrayList<DemandDetail> demands = new ArrayList<DemandDetail>();
    private ArrayList<ArrayList<OfferDetail>> offers = new ArrayList<ArrayList<OfferDetail>>();

    public OffersFlexTable() {
        /** TODO to make thead **/
        initTable();
        this.setHTML(0, COULMN_SIGN, "&nbsp;&nbsp;&nbsp;");
        this.setHTML(0, COLUMN_DEMAND, "<strong>" + MSGS.demand() + "</strong>");
        this.setHTML(0, COLUMN_PRICE, "<strong>" + MSGS.price() + "</strong>");
        this.setWidget(0, COLUMN_RATING, new Image(StyleResource.INSTANCE.images().star()));
        this.setHTML(0, COLUMN_DATE, "<strong>" + MSGS.endDate() + "</strong>");
        /* style. */
        StyleResource.INSTANCE.table().ensureInjected();
        getRowFormatter().addStyleName(0, StyleResource.INSTANCE.table().header());
        setStyleName(StyleResource.INSTANCE.table().clickTable());
        setCellSpacing(0);
    }

    private void initTable() {
        Element thead = DOM.createTHead();
        DOM.insertChild(this.getElement(), thead, 0);
        //update this condition
        for (int row = 0; row < 1; row++) {
            Element tr = DOM.createTR();
            DOM.appendChild(thead, tr);
            for (int col = 0; col < 1; col++) {
                Element th = DOM.createTH();
                DOM.appendChild(tr, th);
                // NB!!!! camelback on attributes for IE or despair
                //DOM.setElementAttribute(th, "colSpan", String.valueOf(headerModel.getHeaderSpan(row, col)));
                DOM.setElementAttribute(th, "align", "left");
                DOM.setElementAttribute(th, "border", "1");
                //set header text
            //    DOM.setInnerText(th, headerModel.getHeaderText(row, col));
            }
        }
    }


    public void setData(ArrayList<DemandDetail> demands) {
        if (this.demands.hashCode() != demands.hashCode()) {
            this.demands = demands;
            for (DemandDetail demand : this.demands) {
                setDemandRow(demand);
            }
        }
    }

    public void setDemandRow(DemandDetail demand) {
        int nextRow = this.getRowCount();
        ToggleButton button = new ToggleButton(new Image(StyleResource.INSTANCE.images().showMiddle()),
                new Image(StyleResource.INSTANCE.images().showDown()));
        this.setWidget(nextRow, COULMN_SIGN, button);
        this.setHTML(nextRow, COLUMN_DEMAND, demand.getTitle());
        this.setHTML(nextRow, COLUMN_PRICE,
                (demand.getEndDate() == null ? MSGS.emptyField() : demand.getEndDate().toString()));
        this.setHTML(nextRow, COLUMN_RATING, demand.getMinRating() + "%");
        this.setHTML(nextRow, COLUMN_DATE,
                (demand.getEndDate() != null ? demand.getEndDate().toString() : MSGS.emptyField()));
        this.setHTML(nextRow, COLUMN_ID, demand.getId() + "");
        this.getCellFormatter().addStyleName(nextRow, COLUMN_ID, StyleResource.INSTANCE.layout().closedWidget());
        this.setHTML(nextRow, COLUMN_TYPE, TYPE_DEMAND + "");
        this.getCellFormatter().addStyleName(nextRow, COLUMN_TYPE, StyleResource.INSTANCE.layout().closedWidget());
        if ((nextRow % 2) == 0) {
            getRowFormatter().addStyleName(nextRow, StyleResource.INSTANCE.table().evenRow());
        }
    }

    public HashMap<Integer, Integer> getClickedRow(ClickEvent event) {
        Cell clickedCell = this.getCellForEvent(event);
        int clickedRow = clickedCell.getRowIndex();
        selectedId = Integer.parseInt(getHTML(clickedRow, COLUMN_ID));
        int type = Integer.parseInt(getHTML(clickedRow, COLUMN_TYPE));

        HashMap<Integer, Integer> resultMap = new HashMap<Integer, Integer>();

        if (clickedRow == 0) {
            //sorting according to criteria
            resultMap.put(RESULT_ACTION, ACTION_SORT);
        } else {
            if (clickedCell.getCellIndex() == 0) {
                ToggleButton button = (ToggleButton) getWidget(clickedRow, COULMN_SIGN);
                if (button.isDown()) {
                    findOffersAndInsert(clickedRow, selectedId);
                } else {
                    hideOffers(clickedRow);
                }
                resultMap.put(RESULT_ACTION, ACTION_TOGGLE);
            } else {
                //display demand detail
                resultMap.put(RESULT_ACTION, ACTION_DEMAND);
            }
            if (type == TYPE_OFFER) {
                //display last message
                resultMap.put(RESULT_ACTION, ACTION_OFFER);
            }
        }
        selectRowType(type, resultMap.get(RESULT_ACTION), clickedRow);
        resultMap.put(RESULT_TYPE, type);
        resultMap.put(RESULT_ID, selectedId);
        return resultMap;
    }

    private void selectRowType(int type, int action, int newRow) {
        if (type == TYPE_OFFER) {
            if (selectedOffer != -1) {
                getRowFormatter().removeStyleName(selectedOffer, StyleResource.INSTANCE.table().selectedOffer());
                getRowFormatter().addStyleName(newRow, StyleResource.INSTANCE.table().selectedOffer());
            } else {
                getRowFormatter().addStyleName(newRow, StyleResource.INSTANCE.table().selectedOffer());
            }
            selectedOffer = newRow;
        }
        if (type == TYPE_DEMAND && action == ACTION_DEMAND) {
            if (selectedRow != -1) {
                getRowFormatter().removeStyleName(selectedRow, StyleResource.INSTANCE.table().selectedRow());
                getRowFormatter().addStyleName(newRow, StyleResource.INSTANCE.table().selectedRow());
            } else {
                getRowFormatter().addStyleName(newRow, StyleResource.INSTANCE.table().selectedRow());
            }
            selectedRow = newRow;
        }
    }

    private void findOffersAndInsert(int clickedRow, int demandId) {
        for (ArrayList<OfferDetail> offerItem : offers) {
            if (offerItem.size() != 0) {
                if (offerItem.get(0).getDemandId() == demandId) {
                    displayOffers(clickedRow, offerItem);
                }
            }
        }
    }

    public void displayOffers(int insertAfter, ArrayList<OfferDetail> offerList) {
        for (OfferDetail offer : offerList) {
            ++insertAfter;
            insertOfferRow(insertAfter, offer);
        }
    }

    private void insertOfferRow(int row, OfferDetail offer) {
        insertRow(row);
        OfferDetailView detail = new OfferDetailView();

        detail.message.setText("offer message");
        //detail.message.setText(offer.getLatestMessage());

        detail.company.setText(offer.getSupplierName());
        this.setWidget(row, COLUMN_DEMAND, detail);
        this.setHTML(row, COLUMN_PRICE,
              (offer.getPrice() == null ? MSGS.emptyField() :  offer.getPrice() + " Kč"));
        this.setHTML(row, COLUMN_DATE,
              (offer.getFinishDate() == null ? MSGS.emptyField() : offer.getFinishDate().toString()));

        //this.setHTML(row, COLUMN_ID, "" + offer.getMessageId());
        this.setHTML(row, COLUMN_ID, "" + 0);

        this.getCellFormatter().addStyleName(row, COLUMN_ID, StyleResource.INSTANCE.layout().closedWidget());
        this.setHTML(row, COLUMN_TYPE, "" + TYPE_OFFER);
        this.getCellFormatter().addStyleName(row, COLUMN_TYPE, StyleResource.INSTANCE.layout().closedWidget());
    }

    private void hideOffers(int clickedDemand) {
        boolean work = true;
        ++clickedDemand;
        while ((getRowCount() > clickedDemand) && work) {
            int type = Integer.parseInt(getHTML(clickedDemand, COLUMN_TYPE));
            if (type == TYPE_OFFER) {
                removeRow(clickedDemand);
            } else {
                work = false;
            }
        }
    }

    public void setOffers(ArrayList<ArrayList<OfferDetail>> offersList) {
        this.offers = offersList;
    }

    public int getSelectedId() {
        return selectedId;
    }


}
