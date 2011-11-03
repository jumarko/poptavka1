package cz.poptavka.sample.shared.domain;

import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents full detail of Client. Serves for creating new
 * Client or for call of detail, that supports editing.
 *
 * @author Beho
 *
 */
public class ClientDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private Long id;
    private int overalRating = -1;
    private UserDetail userDetail = null;
    private String verification = null;
    private List<Long> supplierBlackListIds = null;
    private List<Long> demandsIds = null;

    /** for serialization. **/
    public ClientDetail() {
    }

    public ClientDetail(ClientDetail role) {
        this.updateWholeClient(role);
    }

    /**
     * Method created FullDemandDetail from provided Demand domain object.
     * @param client
     * @return DemandDetail
     */
    public static ClientDetail createClientDetail(Client client) {
        ClientDetail detail = new ClientDetail();
        detail.setId(client.getId());
        if (client.getOveralRating() != null) {
            detail.setOveralRating(client.getOveralRating());
        }
        if (client.getVerification() != null) {
            detail.setVerification(client.getVerification().name());
        }
        detail.setUserDetail(UserDetail.createUserDetail(client.getBusinessUser()));
        if (client.getSupplierBlacklist() != null) {
            List<Long> supplierBlackListIds = new ArrayList<Long>();
            for (Supplier supplier : client.getSupplierBlacklist().getSuppliers()) {
                supplierBlackListIds.add(supplier.getId());
            }
            detail.setSupplierBlackListIds(supplierBlackListIds);
        }

        List<Long> demandsIds = new ArrayList<Long>();
        for (Demand demand : client.getDemands()) {
            demandsIds.add(demand.getId());
        }
        detail.setDemandsIds(demandsIds);

        return detail;
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeClient(ClientDetail detail) {
        id = detail.getId();
        overalRating = detail.getOveralRating();
        verification = detail.getVerification();
        userDetail = detail.getUserDetail();
        supplierBlackListIds = detail.getSupplierBlackListIds();
        demandsIds = detail.getDemandsIds();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public List<Long> getDemandsIds() {
        return demandsIds;
    }

    public void setDemandsIds(List<Long> demandsIds) {
        this.demandsIds = demandsIds;
    }

    public int getOveralRating() {
        return overalRating;
    }

    public void setOveralRating(int overalRating) {
        this.overalRating = overalRating;
    }

    public List<Long> getSupplierBlackListIds() {
        return supplierBlackListIds;
    }

    public void setSupplierBlackListIds(List<Long> supplierBlackListIds) {
        this.supplierBlackListIds = supplierBlackListIds;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    @Override
    public String toString() {
//        StringBuilder str = new StringBuilder();
//        str.append("\nGlobal Client Detail Info:");
//        str.append("\n    ClientId=");
//        str.append(id + "\n     overalRating=");
//        str.append(Integer.valueOf(overalRating) + "\n    UserDetail=");
//        str.append(userDetail.toString() + "\n    SupplierBlackListIds=");
//        str.append(supplierBlackListIds.toString() + "\n    DemandsIds=");
//        str.append(demandsIds.toString());
//        return str.toString();
        return "Client.toString() not implemented";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClientDetail other = (ClientDetail) obj;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
