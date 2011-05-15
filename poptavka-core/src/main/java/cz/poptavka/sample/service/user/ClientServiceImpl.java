/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.service.user;

import com.googlecode.ehcache.annotations.Cacheable;
import cz.poptavka.sample.dao.user.ClientDao;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.service.GeneralService;

/**
 * @author Excalibur
 * @author Juraj Martinka
 */
public class ClientServiceImpl extends BusinessUserRoleServiceImpl<Client, ClientDao> implements ClientService {


    public ClientServiceImpl(GeneralService generalService) {
        super(generalService);
    }

    @Cacheable(cacheName = "cache5min")
    public Client getCachedClientById(long id) {
        return searchById(id);
    }

}
