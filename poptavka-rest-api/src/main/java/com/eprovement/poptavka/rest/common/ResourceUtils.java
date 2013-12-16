/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.common;

import com.eprovement.poptavka.domain.common.DomainObject;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.Validate;

public final class ResourceUtils {

    // TODO jumar: is there any better way how to handle rest api prefix which is configured in web.xml ??
    private static final String REST_API_PREFIX = "/api";

    private ResourceUtils() {
        // utility class -- DO NOT INSTANTIATE
    }

    /**
     * return collection of links containing link "self" which references collection located at {@code baseResourceUri}
     * @param baseResourceUri base collection's uri starting with "/"
     * @return link "self" to collection
     * @see #generateSelfLinks
     */
    public static Map<String, String> generateCollectionLinks(String baseResourceUri) {
        final HashMap<String, String> links = new HashMap<>();
        links.put("self", generateCollectionLink(baseResourceUri));
        return links;
    }

    public static String generateCollectionLink(String baseResourceUri) {
        return REST_API_PREFIX + baseResourceUri;
    }

    /**
     * Generate links containing link named "self" which references to {@code domainObject}
     * E.g. if baseResourceUri is "/demands/
     *
     * @param baseResourceUri base Uri under which the object with id {@code objectId} can be found starting with "/"
     * @param domainObject object for which the link should be generated
     * @return links containing self link to object
     */
    public static Map<String, String> generateSelfLinks(String baseResourceUri, DomainObject domainObject) {
        Validate.notNull(domainObject);
        final Map<String, String> links = new HashMap<>();
        links.put("self", generateSelfLink(baseResourceUri, domainObject));
        return links;
    }

    public static String generateSelfLink(String baseResourceUri, DomainObject domainObject) {
        Validate.notNull(domainObject);
        return generateSelfLink(baseResourceUri, Long.toString(domainObject.getId()));
    }

    public static String generateSelfLink(String baseResourceUri, String id) {
        Validate.notEmpty(baseResourceUri, " baseResourceUri could not be empty!");
        Validate.notNull(id, "id cannot be null!");
        return REST_API_PREFIX + baseResourceUri + "/" + id;
    }
}
