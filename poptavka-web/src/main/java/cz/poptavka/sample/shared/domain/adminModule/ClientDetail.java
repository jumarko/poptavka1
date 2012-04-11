package cz.poptavka.sample.shared.domain.adminModule;

import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.domain.user.Verification;
import cz.poptavka.sample.shared.domain.UserDetail;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents full detail of domain object <b>Client</b> used in <i>Administration Module</i>.
 * Contains 2 static methods:  1. creating detail object
 *                             2. updating domain object
 *
 * @author Martin Slavkovsky
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
     * Method created <b>ClientDetail</b> from provided Demand domain object.
     * @param domain - given domain object
     * @return ClientDetail - created detail object
     */
    public static ClientDetail createClientDetail(Client domain) {
        ClientDetail detail = new ClientDetail();
        detail.setId(domain.getId());
        if (domain.getOveralRating() != null) {
            detail.setOveralRating(domain.getOveralRating());
        }
        if (domain.getVerification() != null) {
            detail.setVerification(domain.getVerification().name());
        }
        detail.setUserDetail(UserDetail.createUserDetail(domain.getBusinessUser()));
        if (domain.getSupplierBlacklist() != null) {
            List<Long> supplierBlackListIds = new ArrayList<Long>();
            for (Supplier supplier : domain.getSupplierBlacklist().getSuppliers()) {
                supplierBlackListIds.add(supplier.getId());
            }
            detail.setSupplierBlackListIds(supplierBlackListIds);
        }

        List<Long> demandsIds = new ArrayList<Long>();
        for (Demand demand : domain.getDemands()) {
            demandsIds.add(demand.getId());
        }
        detail.setDemandsIds(demandsIds);

        return detail;
    }

    /**
     * Method created domain object <b>Client</b> from provided <b>ClientDetail</b> object.
     * @param domain - domain object to be updated
     * @param detail - detail object which provides updated data
     * @return Client - updated given domain object
     */
    public static Client updateClient(Client client, ClientDetail clientDetail) {
        if (client.getOveralRating() != clientDetail.getOveralRating()) {
            client.setOveralRating(clientDetail.getOveralRating());
        }
        client.setVerification(Verification.valueOf(clientDetail.getVerification()));
        //TODO Martin - how to update businessUserData, supplierBlackList, demandsIds???
        return client;
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
        return "\nGlobal Client Detail Info:"
                + "\n    ClientId=" + Long.toString(id)
                + "\n    OveralRating=" + Integer.valueOf(overalRating)
                + "\n    UserDetail=" + userDetail.toString()
                + "\n    SupplierBlackListIds=" + supplierBlackListIds.toString()
                + "\n    DemandsIds=" + demandsIds.toString();
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
