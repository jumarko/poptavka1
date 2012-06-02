package com.eprovement.poptavka.domain.message;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.util.strings.ToStringUtils;

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
