/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.dao.user;

import cz.poptavka.sample.dao.GenericDao;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.service.user.ClientSearchCriteria;

import java.util.List;


/**
 *
 * @author Excalibur
 */
public interface ClientDao extends GenericDao<Client> {

    List<Client> searchByCriteria(ClientSearchCriteria clientSearchCritera);
}
