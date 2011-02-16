/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.service.user;

import com.googlecode.ehcache.annotations.Cacheable;
import cz.poptavka.sample.dao.client.ClientDao;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.service.GenericServiceImpl;

import java.util.List;

/**
 * @author Excalibur
 * @author Juraj Martinka
 */
public class ClientServiceImpl extends GenericServiceImpl<Client, ClientDao> implements ClientService {

    @Cacheable(cacheName = "cache5min")
    public Client getCachedClientById(long id) {
        return searchById(id);
    }

    @Override
    public List<Client> searchByCriteria(ClientSearchCriteria clientSearchCritera) {
        return this.getDao().searchByCriteria(clientSearchCritera);
    }

}
