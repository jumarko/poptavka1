package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.dao.user.ClientDao;
import com.eprovement.poptavka.domain.user.Client;

/**
 * @author Juraj Martinka
 *         Date: 18.12.10
 */
public interface ClientService extends BusinessUserRoleService<Client, ClientDao> {

    /**
     * Tries to find client with given id and return it.
     * Results of this method are cached.
     * <p>
     * This method, probably, has no added value, it is used mainly for demonstrating purposes!
     * @see com.eprovement.poptavka.service.client.ClientServiceTest
     *
     * @param id
     * @return
     */
    Client getCachedClientById(long id);
}
