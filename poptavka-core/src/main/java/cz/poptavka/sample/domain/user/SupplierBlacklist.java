package cz.poptavka.sample.domain.user;

import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.util.strings.ToStringUtils;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * List of blacklisted suppliers for given (one) client.
 * These suppliers should be automatically excluded from set of potential suppliers for any client's demand.
 *
 * @see Client
 * @see Supplier
 *
 * @author Juraj Martinka
 *         Date: 11.4.11
 */
@Entity
public class SupplierBlacklist extends DomainObject {

    @OneToOne
    private Client client;

    /**
     * Many suppliers can be blacklisted for one client as well as
     * many different clients can blacklist the same supplier.
     */
    @ManyToMany
    private List<Supplier> suppliers;


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SupplierBlacklist");
        sb.append("{client.id=").append(ToStringUtils.printId(client));
        sb.append(", suppliers.size=").append(suppliers != null ? suppliers.size() : 0);
        sb.append('}');
        return sb.toString();
    }
}
