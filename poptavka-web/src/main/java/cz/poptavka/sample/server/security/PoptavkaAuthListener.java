package cz.poptavka.sample.server.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;

public class PoptavkaAuthListener implements ApplicationListener<AbstractAuthenticationEvent> {

    private static final Log LOGGER = LogFactory.getLog(PoptavkaAuthListener.class);

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Authentication event ");
        builder.append(event.getClass().getSimpleName());
        builder.append(": ");
        builder.append(event.getAuthentication().getName());
        builder.append("; details: ");
        builder.append(event.getAuthentication().getDetails());

        if (event instanceof AbstractAuthenticationFailureEvent) {
            builder.append("; exception: ");
            builder.append(((AbstractAuthenticationFailureEvent) event).getException().getMessage());
        }

        LOGGER.warn(builder.toString());

    }

}
