package cz.poptavka.sample.server.service;

import net.sf.gilead.gwt.PersistentRemoteService;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;


/**
 * Expose Spring services to GWT app.
 * From http://pgt.de/2009/07/17/non-invasive-gwt-and-spring-integration-reloaded/
 */
public abstract class AutoinjectingRemoteService extends PersistentRemoteService  {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(
                this.getServletContext());
        AutowireCapableBeanFactory beanFactory = ctx.getAutowireCapableBeanFactory();
        beanFactory.autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true);

    }
}
