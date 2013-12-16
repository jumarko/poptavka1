/*
 * Copyright (C) 2012, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.DemandCategory;
import com.eprovement.poptavka.domain.demand.DemandLocality;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.SupplierCategory;
import com.eprovement.poptavka.domain.user.SupplierLocality;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.UserField;
import com.eprovement.poptavka.shared.domain.FullClientDetail.ClientField;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail.OfferField;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.eprovement.poptavka.shared.domain.message.MessageDetail.MessageField;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail.UserMessageField;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail.SupplierField;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Contains all used mapping in search process. In client we can select which
 * attributes should be searched or sorted. We can also define in what class
 * is particular attribute present. But we don't know nothing on client side about
 * RPC implementation - which method, which domain class used in genericdao.Search.
 * Therefore we are not able to provide path for attributes which depends on search class.
 *
 * To do the process of sorting and searching automatically , this class provide
 * mapping of various search classes and domain objects used. Lets say our RPC method
 * searches in <b>UserMessages</b>. And we want sorting client rating attribute. We know
 * that the attribute is present in <b>Client</b> class. Therefore this class has mapping:
 * If search class is <b>UserMessage</b> and we want to sort some <b>client</b>'s attribute,
 * use path <b>message.demand.client.</b> to access those attributes.
 *
 * @author Martin Slavkovsky
 */
public final class DomainObjectsMaping {

    private ArrayList<MapingPair> mapingPairs = new ArrayList<MapingPair>();
    //Singleton instance
    private static DomainObjectsMaping instacne;

    public static DomainObjectsMaping getInstance() {
        if (instacne == null) {
            return new DomainObjectsMaping();
        }
        return instacne;
    }

    /**
     * Initialize mapping.
     */
    private DomainObjectsMaping() {
        //Demand
        mapingPairs.add(new MapingPair(Demand.class, DemandField.SEARCH_CLASS, ""));
        mapingPairs.add(new MapingPair(DemandCategory.class, DemandField.SEARCH_CLASS, "demand."));
        mapingPairs.add(new MapingPair(DemandLocality.class, DemandField.SEARCH_CLASS, "demand."));
        //Supplier
        mapingPairs.add(new MapingPair(Supplier.class, SupplierField.SEARCH_CLASS, ""));
        mapingPairs.add(new MapingPair(SupplierCategory.class, SupplierField.SEARCH_CLASS, "supplier."));
        mapingPairs.add(new MapingPair(SupplierLocality.class, SupplierField.SEARCH_CLASS, "supplier."));
        //Offer
        mapingPairs.add(new MapingPair(Offer.class, DemandField.SEARCH_CLASS, "demand."));
        //UserMessage
        mapingPairs.add(new MapingPair(UserMessage.class, DemandField.SEARCH_CLASS, "message.demand."));
        mapingPairs.add(new MapingPair(UserMessage.class, ClientField.SEARCH_CLASS, "message.demand.client."));
        mapingPairs.add(new MapingPair(UserMessage.class, SupplierField.SEARCH_CLASS, "message.offer.supplier."));
        mapingPairs.add(new MapingPair(UserMessage.class, OfferField.SEARCH_CLASS, "message.offer."));
        mapingPairs.add(new MapingPair(UserMessage.class, MessageField.SEARCH_CLASS, "message."));
        mapingPairs.add(new MapingPair(UserMessage.class, UserMessageField.SEARCH_CLASS, ""));
        mapingPairs.add(new MapingPair(UserMessage.class, UserField.SEARCH_CLASS, "businessUser.businessUserData."));
    }

    /**
     *
     * @param searchClass - class used in com.google.genericdao.Search object.
     * @param searchClassAttributes = class whose attributes we want to search.
     * @return
     */
    public String getPath(Class<?> searchClass, String searchClassAttributes) {
        int idx = mapingPairs.indexOf(new MapingPair(searchClass, searchClassAttributes, null));
        if (idx != -1) {
            return mapingPairs.get(idx).getPath();
        }
        return "";
    }
}

/**
 * Represents maping pair.
 * @author Martin Slavkovsky
 */
class MapingPair {

    private Class<?> searchClass;
    private String searchClassAttributes;
    private String path;

    public MapingPair(Class<?> searchClass, String searchClassAttributes, String path) {
        this.searchClass = searchClass;
        this.searchClassAttributes = searchClassAttributes;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.searchClass);
        hash = 97 * hash + Objects.hashCode(this.searchClassAttributes);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MapingPair other = (MapingPair) obj;
        if (!Objects.equals(this.searchClass, other.searchClass)) {
            return false;
        }
        if (!Objects.equals(this.searchClassAttributes, other.searchClassAttributes)) {
            return false;
        }
        return true;
    }
}
