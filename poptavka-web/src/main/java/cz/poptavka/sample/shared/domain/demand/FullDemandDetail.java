package cz.poptavka.sample.shared.domain.demand;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.type.DemandDetailType;

/**
 * Represents full detail of demand. Serves for creating new demand or for call of detail, that supports editing.
 *
 * @author Beho
 *
 */
public class FullDemandDetail extends BaseDemandDetail implements Serializable, DemandDetail {
    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;

    private static final DemandDetailType TYPE = DemandDetailType.FULL;

    public enum DemandField {
        TITLE, DESCRIPTION, PRICE, FINISH_DATE, VALID_TO_DATE, MAX_OFFERS, MIN_RATING, DEMAND_TYPE
    }

    private ArrayList<String> localities;
    private ArrayList<String> categories;
    private long clientId;
    private int maxOffers;
    private int minRating;

    private String demandType;
    private String demandStatus;

    /** for serialization. **/
    public FullDemandDetail() {
        super();
    }

    /**
     * Method created FullDemandDetail from provided Demand domain object.
     * @param demand
     * @return DemandDetail
     */
    public static DemandDetail createDemandDetail(Demand demand) {
        FullDemandDetail detail = (FullDemandDetail) BaseDemandDetail.fillDemandDetail(new FullDemandDetail(), demand);
        detail.setMaxOffers(demand.getMaxSuppliers() == null ? 0 : demand.getMaxSuppliers());
        detail.setMinRating(demand.getMinRating() == null ? 0 : demand.getMinRating());
        //categories
        ArrayList<String> catList = new ArrayList<String>();
        for (Category cat : demand.getCategories()) {
            catList.add(cat.getName());
        }
        detail.setCategories(catList);
        //localities
        ArrayList<String> locList = new ArrayList<String>();
        for (Locality loc : demand.getLocalities()) {
            locList.add(loc.getName());
        }
        detail.setLocalities(locList);
        detail.setDemandStatus(demand.getStatus().getValue());
        detail.setDemandType(demand.getType().getType().getValue());
        detail.setClientId(demand.getClient().getId());
        return detail;
    }

    public void setBasicInfo(HashMap<DemandField, Object> map) {
        this.setTitle((String) map.get(DemandField.TITLE));
        this.setDescription((String) map.get(DemandField.DESCRIPTION));
        this.setPrice((String) map.get(DemandField.PRICE));
        this.setEndDate((Date) map.get(DemandField.FINISH_DATE));
        this.setValidToDate((Date) map.get(DemandField.VALID_TO_DATE));
    }

    public void setAdvInfo(HashMap<DemandField, Object> map) {
        this.maxOffers = (Integer) map.get(DemandField.MAX_OFFERS);
        this.minRating = (Integer) map.get(DemandField.MIN_RATING);
        this.demandType = (String) map.get(DemandField.DEMAND_TYPE);
    }

    //---------------------------- GETTERS AND SETTERS --------------------

    public ArrayList<String> getLocalities() {
        return localities;
    }

    public void setLocalities(ArrayList<String> localities) {
        this.localities = localities;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public int getMaxOffers() {
        return maxOffers;
    }

    public void setMaxOffers(int maxOffers) {
        this.maxOffers = maxOffers;
    }

    public int getMinRating() {
        return minRating;
    }

    public void setMinRating(int minRating) {
        this.minRating = minRating;
    }

    public String getDemandType() {
        return demandType;
    }

    public void setDemandType(String demandType) {
        this.demandType = demandType;
    }

    @Override
    public String toString() {

        return super.toString()
                + "* FullDemandDetail:"
                + "\n     localities="
                + localities + "\n     categories="
                + categories + "\n     clientId="
                + clientId + "\n     maxOffers="
                + maxOffers + "\n     minRating="
                + minRating + "\n     demandType="
                + demandType + "\n     demandStatus="
                + demandStatus + "\n"
                + "TEST na TYPE: " + getType();
    }

    public String getDemandStatus() {
        return demandStatus;
    }

    public void setDemandStatus(String demandStatus) {
        this.demandStatus = demandStatus;
    }
}
