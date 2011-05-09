/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.service.user;

import com.google.common.base.Preconditions;
import com.googlecode.ehcache.annotations.Cacheable;
import cz.poptavka.sample.dao.user.ClientDao;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.GenericServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Excalibur
 * @author Juraj Martinka
 */
public class ClientServiceImpl extends GenericServiceImpl<Client, ClientDao> implements ClientService {

    private GeneralService generalService;


    public ClientServiceImpl(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Cacheable(cacheName = "cache5min")
    public Client getCachedClientById(long id) {
        return searchById(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Client> searchByCriteria(ClientSearchCriteria clientSearchCritera) {
        return this.getDao().searchByCriteria(clientSearchCritera);
    }

    @Override
    @Transactional
    public Client create(Client client) {
        Preconditions.checkArgument(client != null, "Client must have BusinessUser assigned!");
        if (isNewBusinessUser(client)) {
            final BusinessUser savedBusinessUserEntity = generalService.save(client.getBusinessUser());
            client.setBusinessUser(savedBusinessUserEntity);
        }
        return super.create(client);
    }

    private boolean isNewBusinessUser(Client client) {
        return client.getBusinessUser().getId() == null;
    }


    @Override
    public Client update(Client client) {
        return getDao().update(client);
    }
}
