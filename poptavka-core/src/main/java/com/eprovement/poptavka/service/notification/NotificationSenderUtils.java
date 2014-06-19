package com.eprovement.poptavka.service.notification;

import com.eprovement.poptavka.domain.settings.Notification;
import org.apache.commons.collections.MapUtils;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Map;

/**
 * Utility class containing methods useful for {@link NotificationSender}-s.
 */
final class NotificationSenderUtils {

    private NotificationSenderUtils() {
        throw new AssertionError("Utility class - do not instantiate!");
    }

    private static final PropertyPlaceholderHelper PLACEHOLDER_HELPER = new PropertyPlaceholderHelper("${", "}");

    static String expandMessageSubject(Notification notificationEntity, Map<String, String> messageVariables) {
        if (MapUtils.isEmpty(messageVariables)) {
            return notificationEntity.getName();
        }
        return PLACEHOLDER_HELPER.replacePlaceholders(notificationEntity.getName(),
                MapUtils.toProperties(messageVariables));
    }

    static String expandMessageBody(Notification notificationEntity, Map<String, String> messageVariables) {
        if (MapUtils.isEmpty(messageVariables)) {
            return notificationEntity.getMessageTemplate();
        }
        return PLACEHOLDER_HELPER.replacePlaceholders(notificationEntity.getMessageTemplate(),
                MapUtils.toProperties(messageVariables));
    }

}
