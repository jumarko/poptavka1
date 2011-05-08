package cz.poptavka.sample.dao.user;

import com.google.common.base.Preconditions;
import cz.poptavka.sample.dao.GenericHibernateDao;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.service.user.ClientSearchCriteria;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;

import java.util.List;


/**
 *
 * @author Juraj Martinka
 */
public class ClientDaoImpl extends GenericHibernateDao<Client> implements ClientDao {

    @Override
    public List<Client> searchByCriteria(ClientSearchCriteria clientSearchCritera) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(clientSearchCritera.getName())
                    || StringUtils.isNotEmpty(clientSearchCritera.getSurName()),
                "At least one search criterion must be specified!");

        // query by example
        final Criteria personCriteria = getHibernateSession().createCriteria(Client.class)
                .createCriteria("businessUserData");
        personCriteria.add(Example.create(
                new BusinessUserData.Builder()
                        .personFirstName(clientSearchCritera.getName())
                        .personLastName(clientSearchCritera.getSurName())
                        .build()));
        return personCriteria.list();
    }
}
