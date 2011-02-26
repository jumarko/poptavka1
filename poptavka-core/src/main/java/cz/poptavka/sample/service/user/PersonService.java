package cz.poptavka.sample.service.user;

import cz.poptavka.sample.dao.user.PersonDao;
import cz.poptavka.sample.domain.user.Person;
import cz.poptavka.sample.service.GenericService;

/**
 * @author Juraj Martinka
 *         Date: 26.2.11
 */
public interface PersonService extends GenericService<Person, PersonDao> {
}
