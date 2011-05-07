package cz.poptavka.sample.service.mail;

import cz.poptavka.sample.dao.mail.MessageDao;
import cz.poptavka.sample.domain.mail.Message;
import cz.poptavka.sample.service.GenericService;

/**
 * @author Juraj Martinka
 *         Date: 3.5.11
 */
public interface MessageService extends GenericService<Message, MessageDao> {
}
