package cz.poptavka.sample.domain.mail;

import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.util.strings.ToStringUtils;

import javax.persistence.OneToOne;

/**
 * Class represents inbox for storing messages between given client and supplier.
 * One instance of this class is unique for client and supplier.
 *
 * @author Juraj Martinka
 *         Date: 29.1.11
 */
public class Inbox extends DomainObject {

    @OneToOne
    private Client client;

    @OneToOne
    private Supplier supplier;


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Inbox");
        sb.append("{client.id=").append(ToStringUtils.printId(client));
        sb.append(", supplier.id=").append(supplier);
        sb.append('}');
        return sb.toString();
    }
}
