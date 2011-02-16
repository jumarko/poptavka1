package cz.poptavka.sample.service.user;

import cz.poptavka.sample.dao.client.ClientDao;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.service.GenericService;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 18.12.10
 */
public interface ClientService extends GenericService<Client, ClientDao> {

    /**
     * Tries to find client with given id and return it.
     * Results of this method are cached.
     * <p>
     * This method, probably, has no added value, it is used mainly for demonstrating purposes!
     * @see cz.poptavka.sample.service.client.ClientServiceTest
     *
     * @param id
     * @return
     */
    Client getCachedClientById(long id);

    List<Client> searchByCriteria(ClientSearchCriteria clientSearchCritera);
}
